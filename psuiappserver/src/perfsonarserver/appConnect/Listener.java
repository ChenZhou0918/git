package perfsonarserver.appConnect;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Listener, waits for incoming connections on the specified port. Starts a
 * worker thread of type {@link Communicator} to handle the request.
 * 
 * @author Fabian Misiak, Reinhard Winkler, Mirco Bohlmann
 * 
 */
public class Listener extends Thread
{
	/** The port, on which the listener waits for connections */
	private static final int PORT =80;

	public void run()
	{
		try
		{
			System.out.println("[Listener] Waiting for connections on port " + PORT + "...");
			ServerSocket server = new ServerSocket(PORT);
			while (true)
			{
				Socket socket = server.accept();
				Communicator receiver = new Communicator(socket);
				receiver.start();
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
//
// /** Innere Klasse, der Worker-Thread des Servers */
// class ListenerStart extends Thread {
// /** Der Port, auf dem der Server auf Verbindungen wartet */
// private static final int PORT = 7777;
//
// public ListenerStart() throws IOException {
//
// }
//
// /** Die Methode run() wird beim Start des Threads ausgef�hrt */
//
//
// }
