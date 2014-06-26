package perfsonarserver.appConnect;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.ParseException;
import java.util.Date;
import java.util.StringTokenizer;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;

/**
 * Communicator class for handling incoming POST-requests. Reads the JSON object
 * in the POST-body and sends the processed query to the DB. The response is in
 * turn processed into a JSON object and sent back to the PerfsonarUIApp via
 * POST-response.
 * 
 * @author Fabian Misiak, Reinhard Winkler, Mirco Bohlmann
 * 
 */
public class Communicator extends Thread
{
	private static Date date = new Date();
	/** Filename, for incoming JSON object */
	private static final String FILENAME = "./json_requests/" + date.getTime() + ".js";
	/** The connected app */
	private Socket connectedClient = null;
	/** Output stream to app */
	private DataOutputStream out = null;
	/** BR for reading the request */
	private BufferedReader in = null;
	/** Parses JSON objects into transfer objects and vice versa */
	private JsonHandler jsonHandler = null;
	/** For database requests */
	private DatabaseRequest dbConnect = null;

	/**
	 * Constructor, initializes the Communicator
	 * 
	 * @param socket
	 *            Socket with which the app is connected
	 */
	public Communicator(Socket socket)
	{
		this.connectedClient = socket;
		this.jsonHandler = new JsonHandler();
		// this.da = DataAccess.getInstance();
		this.dbConnect = new DatabaseRequest();
	}

	/** Thread-method, handles POST request */
	public void run()
	{
		String currentLine = null, headerLine = null;
		String httpMethod = null, httpQueryString = null;
		StringTokenizer tokenizer = null;
		int contentLength = 0;

		try
		{
			System.out.println("[Communicator] Connection with client " + this.connectedClient.getInetAddress() + ":" + this.connectedClient.getPort() + " established.");

			in = new BufferedReader(new InputStreamReader(connectedClient.getInputStream()));
			out = new DataOutputStream(connectedClient.getOutputStream());

			// Read request and extract HTTP method and query
			if ((currentLine = in.readLine()) != null)
			{
				headerLine = currentLine;
				tokenizer = new StringTokenizer(headerLine);

				httpMethod = tokenizer.nextToken();
				httpQueryString = tokenizer.nextToken();

				System.out.println("Header Line: " + headerLine);
				System.out.println("HTTP Method: " + httpMethod);
				System.out.println("HTTP Query: " + httpQueryString);

				// only handle POST requests
				if (httpMethod.equals("POST"))
				{
					System.out.println("--- POST Request received ---");

					do
					{
						currentLine = in.readLine();
						System.out.println(currentLine);

						if (currentLine != null)
						{
							// Checking for correct content-type and extracting
							// content-length
							if (currentLine.indexOf("Content-Type: application/json") != -1)
							{
								while (true)
								{
									if ((currentLine = in.readLine()) != null)
									{
										if (currentLine.indexOf("Content-Length:") != -1)
										{
											contentLength = Integer.valueOf(currentLine.split(" ")[1]);
											System.out.println("Content Length = " + contentLength);
											break;
										} // if (currentLine.indexOf...)
									}
								} // while (true)

								// Discard "unimportant" query lines and emtpy
								// line between POST header and the request body
								if ((currentLine = in.readLine()) != null)
								{
									while (!(currentLine.compareToIgnoreCase("") == 0))
									{
										currentLine = in.readLine();
									}
								}

								// Process the data in the POST body
								writeJsonRequestFile(contentLength);

								// Parse JSON file created in
								// processPOST(contentLength)
								// and fill a TO for DB-request
								RequestTO request = new RequestTO();
								request = jsonHandler.parseJSON(request, FILENAME);

								// DB-request, response is stored in JSON array
								JsonArray values = new JsonArray();
								try
								{
									values = dbConnect.dbRequest(request);
								}
								catch (ParseException e)
								{
									System.out.println("Content not parseable!");
									e.printStackTrace();
									values = new JsonArray();
									values.add("Parse error!");
									sendResponse(400, jsonHandler.createResponseJSON(request, values));
								}

								// Create the JSON object for the POST response
								// and send POST response
								sendResponse(200, jsonHandler.createResponseJSON(request, values));
							} // if (currentLine.indexOf("Content-Type...
						}
					}
					while (in.ready());
				} // if (httpMethod.equals("POST")
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		// Close the connection
		finalize();
	}

	/**
	 * Writes the content of the POST body into a file to be processed by
	 * {@link JsonHandler}. The additional step of storing the request in a file
	 * makes troubleshooting easier, since one can check the requests for errors
	 * mor easily
	 * 
	 * @param contentLength
	 *            The length of the POST body
	 */
	private void writeJsonRequestFile(int contentLength)
	{
		System.out.println("\nprocessPOST\n");
		char[] cbuf = new char[contentLength]; // char buffer
		PrintWriter fout = null; // file output

		try
		{
			fout = new PrintWriter(FILENAME);
			in.read(cbuf); // read chars
			fout.print(cbuf); // write char buffer into file
			fout.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Sends the POST response to connected app with the required header
	 * information and tells the client the connection can be closed.
	 * 
	 * @param status
	 *            HTTP status
	 * @param response
	 *            JSON object for response
	 */
	private void sendResponse(int status, JsonObject response)
	{
		System.out.println("sendResponse\n");
		String statusLine = null;
		String serverDetails = "Server: PerfsonarUIApp-Server" + "\r\n";
		String contentTypeLine = "Content-Type: application/json" + "\r\n";
		String contentLengthLine = "Content-Length: " + response.toString().length() + "\r\n";
		String responseLine = response.toString();

		if (status == 200)
			statusLine = "HTTP/1.1 200 OK" + "\r\n";
		else
			statusLine = "HTTP/1.1 404 Not Found" + "\r\n";

		try
		{
			out.writeBytes(statusLine);
			out.writeBytes(serverDetails);
			out.writeBytes(contentTypeLine);
			out.writeBytes(contentLengthLine);
			out.writeBytes("Connection: close\r\n");
			out.writeBytes("\r\n");
			out.writeBytes(responseLine);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

	}

	/** Close connection after request has been handled */
	public void finalize()
	{
		try
		{
			out.close();
			in.close();
			connectedClient.close();
			System.out.println("[Communicator] Disconnected from app.");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}