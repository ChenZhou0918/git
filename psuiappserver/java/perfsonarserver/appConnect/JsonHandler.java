package perfsonarserver.appConnect;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;

public class JsonHandler
{
	/** JSON-Keys for request and response objects */
	private static final String JS_FEATURE = "FeatureName";
	private static final String JS_SERVICE = "Service";
	private static final String JS_SOURCE = "SourceInterface";
	private static final String JS_DEST = "DestinationInterface";
	private static final String JS_START = "StartTime";
	private static final String JS_END = "EndTime";
	private static final String JS_CACHE = "GetNonCachedData";
	private static final String JS_INFO = "Info";
	private static final String JS_INTERFACES = "Interfaces";
	private static final String JS_VALUES = "MeasuredValues";

	public JsonHandler()
	{
		super();
	}

	/**
	 * Parses the JSON file created in {@link processPOST} and prepares the
	 * transfer object for database request
	 * 
	 * @param request
	 *            The transfer object for database request
	 * @param filename
	 *            The JSON file to be parsed
	 * @return The prepared transfer object
	 */
	public RequestTO parseJSON(RequestTO request, String filename)
	{
		System.out.println("parseJSON()\n");
		try
		{
			JsonObject json = JsonObject.readFrom(new InputStreamReader(new FileInputStream(filename)));

			request.setFeatureName(json.get(JS_FEATURE).asString());

			if (json.get(JS_SERVICE).isNull())
			{
				request.setService(null);
			}
			else
			{
				request.setService(json.get(JS_SERVICE).asString());
			}

			if (json.get(JS_SOURCE).isNull())
			{
				request.setSourceInterface(null);
			}
			else
			{
				request.setSourceInterface(json.get(JS_SOURCE).asString());
			}

			if (json.get(JS_DEST).isNull())
			{
				request.setDestinationInterface(null);
			}
			else
			{
				request.setDestinationInterface(json.get(JS_DEST).asString());
			}

			if (json.get(JS_START).isNull())
			{
				request.setStartDate(null);
			}
			else
			{
				request.setStartDate(json.get(JS_START).asString());
			}

			if (json.get(JS_END).isNull())
			{
				request.setEndDate(null);
			}
			else
			{
				request.setEndDate(json.get(JS_END).asString());
			}

			if (json.get(JS_CACHE).isNull())
			{
				request.setGetNonCached(null);
			}
			else
			{
				request.setGetNonCached(json.get(JS_CACHE).asBoolean());
			}

			// Output to console, for debugging
			System.out.println("JSON to String");
			System.out.println(json.toString());
			System.out.println("\nRequestTO to String");
			System.out.println(request.toString() + "\n");

		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return request;
	}

	/**
	 * Creates the JSON object to be sent to app in POST response. Different
	 * JSON objects depending on the request type will be created
	 * 
	 * @param request
	 *            The request transfer object, for distinction of cases which
	 *            JSON object to create and to fill the info segment of said
	 *            object, if existent
	 * @param values
	 *            The JSON array wich will be included into the response object
	 * @return JsonObject response
	 */
	public JsonObject createResponseJSON(RequestTO request, JsonArray values)
	{
		/** JSON object for response */
		JsonObject response = new JsonObject();
		/** Temporary JSON objects to build response JSON object */
		JsonObject info = new JsonObject();
		JsonObject geant = new JsonObject();
		JsonObject lhc = new JsonObject();
		JsonObject xwin = new JsonObject();

		// Check for FeatureName to build response accordingly
		if (request.getFeatureName().indexOf("GetService") != -1)
		{
			// Hard coded, since there are currently only 3 services
			// and no more will probably be available in the near future
			geant.add(JS_SERVICE, "GEANT");
			lhc.add(JS_SERVICE, "LHCOPN");
			xwin.add(JS_SERVICE, "X-WiN");
			values.add(geant);
			values.add(lhc);
			values.add(xwin);
			response.add("Services", values);
		}
		else if (request.getFeatureName().indexOf("GetInterface") != -1)
		{
			info.add(JS_SERVICE, request.getService());
			response.add(JS_INFO, info);
			response.add(JS_INTERFACES, values);
		}
		else if (request.getFeatureName().indexOf("GetSourceInterfaces") != -1)
		{
			info.add(JS_SERVICE, request.getService());
			response.add(JS_INFO, info);
			response.add(JS_INTERFACES, values);
		}
		else if (request.getFeatureName().indexOf("GetDestinationInterfaces") != -1)
		{
			info.add(JS_SERVICE, request.getService());
			info.add(JS_SOURCE, request.getSourceInterface());
			response.add(JS_INFO, info);
			response.add(JS_INTERFACES, values);
		}
		else if (request.getFeatureName().indexOf("GetData") != -1)
		{
			info.add(JS_SERVICE, request.getService());
			info.add(JS_SOURCE, request.getSourceInterface());
			info.add(JS_DEST, request.getDestinationInterface());
			info.add(JS_START, request.getStartDate());
			info.add(JS_END, request.getEndDate());
			info.add(JS_CACHE, request.getGetNonCached());

			response.add(JS_INFO, info);
			response.add(JS_VALUES, values);
		}

		return response;
	}
}
