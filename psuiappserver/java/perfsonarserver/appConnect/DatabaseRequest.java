package perfsonarserver.appConnect;

import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

import perfsonarserver.database.cassandraImpl.DataAccessCas;
import perfsonarserver.database.cassandraImpl.IDataAccess;
import perfsonarserver.database.database_to.DelayJitterLossDataDB;
import perfsonarserver.database.database_to.ThroughputServiceDB;
import perfsonarserver.database.exception.FetchNothingException;
import perfsonarserver.database.exception.FindNothingException;
import perfsonarserver.database.response_to.DashboardDelayGetDataTO;
import perfsonarserver.database.response_to.DashboardJitterGetDataTO;
import perfsonarserver.database.response_to.DashboardLossGetDataTO;
import perfsonarserver.database.response_to.DashboardUDGetDataTO;
import perfsonarserver.database.response_to.DelayJitterLossDataTO;
import perfsonarserver.database.response_to.DelayJitterLossInterfaceTO;
import perfsonarserver.database.response_to.PathSegmentsGetDataTO;
import perfsonarserver.database.response_to.ThroughputGetDataTO;
import perfsonarserver.database.response_to.ThroughputInterfaceTO;
import perfsonarserver.database.response_to.ThroughputServiceTO;
import perfsonarserver.database.response_to.UtilizationDataTO;
import perfsonarserver.database.response_to.UtilizationInterfaceTO;
import perfsonarserver.database.response_to.UtilizationServiceTO;
import perfsonarserver.fetchData.exception.FetchFailException;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;

public class DatabaseRequest
{

	public DatabaseRequest()
	{
		super();
	}

	/**
	 * This method passes the request to the database and puts the returned
	 * values into a JSON-Array depending on the request type. The JSON-Array is
	 * then returned to the caller for further processing in
	 * {@link Communicator} and {@link JsonHandler}.
	 * 
	 * NOTE: Currently only the cases for Delay Jitter Loss are implemented!
	 * 
	 * @param request
	 *            The transfer object for database request
	 * @return JsonArray with the values of the database response
	 * @throws ParseException
	 *             Thrown by database if some content cannot be parsed
	 * @throws FetchFailException 
	 */
	public JsonArray dbRequest(RequestTO request) throws ParseException, FetchFailException
	{
		/** Instance of database access singleton */
		DataAccessCas da = DataAccessCas.getInstance();
		IDataAccess dataAccess = da;
		System.out.println("\nDatabaseRequest\n\n");

		// List to track duplicate entries in source interface lists, so they
		// can be
		// removed
		List<String> duplicates = new LinkedList<String>();

		// Switch depending on request type (FeatureName)
		switch (request.getFeatureName())
		{
		case "DashboardDelay":
			List<DashboardDelayGetDataTO> dd_list = new LinkedList<DashboardDelayGetDataTO>();
			dd_list = dataAccess.getDashboardDelayGetData(request);

			JsonArray dd_valuesArray = new JsonArray();
			//
			// for (DashboardDelayGetDataTO item : dd_list) {
			// JsonObject currentValue = new JsonObject();
			//
			//
			//
			// dd_valuesArray.add(currentValue);
			// }
			dd_valuesArray.add("Not implemented yet!");

			return dd_valuesArray;

		case "DashboardJitter":
			List<DashboardJitterGetDataTO> dj_list = new LinkedList<DashboardJitterGetDataTO>();
			dj_list = dataAccess.getDashboardJitterGetData(request);

			JsonArray dj_valuesArray = new JsonArray();
			//
			// for (DashboardDelayGetDataTO item : dj_list) {
			// JsonObject currentValue = new JsonObject();
			//
			//
			//
			// dj_valuesArray.add(currentValue);
			// }
			dj_valuesArray.add("Not implemented yet!");

			return dj_valuesArray;
		case "DashboardLoss":
			List<DashboardLossGetDataTO> dl_list = new LinkedList<DashboardLossGetDataTO>();
			dl_list = dataAccess.getDashboardLossGetData(request);

			JsonArray dl_valuesArray = new JsonArray();
			//
			// for (DashboardDelayGetDataTO item : dl_list) {
			// JsonObject currentValue = new JsonObject();
			//
			//
			//
			// dl_valuesArray.add(currentValue);
			// }
			dl_valuesArray.add("Not implemented yet!");

			return dl_valuesArray;

		case "DashboardUtilizationData":
			List<DashboardUDGetDataTO> dud_list = new LinkedList<DashboardUDGetDataTO>();
			dud_list = dataAccess.getDashboardUDGetData(request);

			JsonArray dud_valuesArray = new JsonArray();
			//
			// for (DashboardDelayGetDataTO item : dud_list) {
			// JsonObject currentValue = new JsonObject();
			//
			//
			//
			// dud_valuesArray.add(currentValue);
			// }
			dud_valuesArray.add("Not implemented yet!");

			return dud_valuesArray;

		case "DelayJitterLossGetData":
			List<DelayJitterLossDataTO> djl_list = new LinkedList<DelayJitterLossDataTO>();
			try
			{
				// Database request
			//	djl_list = dataAccess.getDelayJitterLossData(request);

				// if the Cache is empty, return an JSON-Array with the
				// information as a String
			}
			catch (FindNothingException | FetchNothingException e)
			{
				if (!request.getGetNonCached())
				{
					JsonArray empty = new JsonArray();
					empty.add("cache empty!");

					return empty;

				}
				e.printStackTrace();
			}

			JsonArray djl_valuesArray = new JsonArray();

			// Extracts data out of each list element and puts it
			// into a JSON-Object, which then is inserted into a JSON-Array.
			// If no data is available, an according message is inserted.
			if (djl_list.isEmpty())
			{
				djl_valuesArray.add("Sorry, no data for request available");
			}
			else
			{
				for (DelayJitterLossDataTO item : djl_list)
				{
					JsonObject currentValue = new JsonObject();

//					currentValue.add("date", item.getTimestampString());
//					currentValue.add("maxDelay", item.getMaxDelay());
//					currentValue.add("minDelay", item.getMinDelay());
//					currentValue.add("maxJitter", item.getMaxJitter());
//					currentValue.add("minJitter", item.getMinJitter());
					//currentValue.add("loss", item.getLoss());

					djl_valuesArray.add(currentValue);
				}
			}
			return djl_valuesArray;

		case "DelayJitterLossGetSourceInterfaces":
			List<DelayJitterLossInterfaceTO> djlsi_list = new LinkedList<DelayJitterLossInterfaceTO>();

			// Clear duplicates list
			duplicates.clear();

			djlsi_list = dataAccess.getDelayJitterLossInterface(request);

			// Remove duplicate entries in source interface list. This is
			// necessary, because the archive servers send interface pairs,
			// but to reduce the amount of data to transmit to the app the
			// interface request is splitted into first a request for
			// source interfaces, for each of which then the destination
			// interfaces can be requested
			for (int i = djlsi_list.size() - 1; i >= 0; i--)
			{
				String temp = djlsi_list.get(i).getSrcInterface();
				if (!duplicates.contains(temp))
				{
					duplicates.add(temp);
				}
				else
				{
					djlsi_list.remove(i);
				}
			}

			JsonArray djlsi_valuesArray = new JsonArray();

			// Extracts source interfaces out of the list and puts each of them
			// into a JSON-Object, which then is inserted into a JSON-Array
			for (DelayJitterLossInterfaceTO item : djlsi_list)
			{
				JsonObject currentValue = new JsonObject();

				currentValue.add("SourceInterface", item.getSrcInterface());

				djlsi_valuesArray.add(currentValue);
			}

			return djlsi_valuesArray;

		case "DelayJitterLossGetDestinationInterfaces":
			List<DelayJitterLossInterfaceTO> djldi_list = new LinkedList<DelayJitterLossInterfaceTO>();

			djldi_list = dataAccess.getDelayJitterLossInterface(request);

			JsonArray djldi_valuesArray = new JsonArray();

			// Extracts the destination interface out of each list element
			// with the requested source interface and puts it
			// into a JSON-Object, which then is inserted into a JSON-Array
			for (DelayJitterLossInterfaceTO item : djldi_list)
			{
				JsonObject currentValue = new JsonObject();

				if (item.getSrcInterface().equals(request.getSourceInterface()))
				{
					currentValue.add("DestinationInterface", item.getDestInterface());
					djldi_valuesArray.add(currentValue);
				}
			}

			return djldi_valuesArray;

		case "DelayJitterLossGetService":
			// Since there are currently only three services, the response is
			// hard coded in JsonHandler

			// List<DelayJitterLossServiceTO> djls_list = new
			// LinkedList<DelayJitterLossServiceTO>();
			// djls_list = dataAccess.getDelayJitterLossService(request);

			JsonArray djls_valuesArray = new JsonArray();

			// Pulls each value out of each list element and puts it
			// into a JSON-Object, which then is inserted into a JSON-Array
			// for (DelayJitterLossServiceTO item : djls_list) {
			// JsonObject currentValue = new JsonObject();
			//
			// currentValue.add("serviceName", item.getServiceName());
			//
			// djls_valuesArray.add(currentValue);
			// }
			return djls_valuesArray;

		case "PathSegmentsGetData":
			List<PathSegmentsGetDataTO> ps_list = new LinkedList<PathSegmentsGetDataTO>();
			ps_list = dataAccess.getPathSegmentsGetData(request);

			JsonArray ps_valuesArray = new JsonArray();
			//
			// for (DashboardDelayGetDataTO item : ps_list) {
			// JsonObject currentValue = new JsonObject();
			//
			//
			//
			// ps_valuesArray.add(currentValue);
			// }
			ps_valuesArray.add("Not implemented yet!");

			return ps_valuesArray;

		case "ThroughputGetData":
			List<ThroughputGetDataTO> tgd_list = new LinkedList<ThroughputGetDataTO>();
			tgd_list = dataAccess.getThrougputGetData(request);

			JsonArray tgd_valuesArray = new JsonArray();

			// Extracts data out of each list element and puts it
			// into a JSON-Object, which then is inserted into a JSON-Array.
			// If no data is available, an according message is inserted.
			if (tgd_list.isEmpty())
			{
				tgd_valuesArray.add("Sorry, no data for request available");
			}
			else
			{
				for (ThroughputGetDataTO item : tgd_list)
				{
					JsonObject currentValue = new JsonObject();

					currentValue.add("date", item.getTimestamp());
					currentValue.add("throughput", item.getMeasuredThroughput());

					tgd_valuesArray.add(currentValue);
				}
			}

			return tgd_valuesArray;

		case "ThroughputGetSourceInterfaces":
			List<ThroughputInterfaceTO> tgsi_list = new LinkedList<ThroughputInterfaceTO>();
		//	tgsi_list = dataAccess.getThroughputInterface(request);

			// Remove duplicate entries in source interface list. This is
			// necessary, because the archive servers send interface pairs,
			// but to reduce the amount of data to transmit to the app the
			// interface request is splitted into first a request for
			// source interfaces, for each of which then the destination
			// interfaces can be requested
			for (int i = tgsi_list.size() - 1; i >= 0; i--)
			{
				String temp = tgsi_list.get(i).getSrcInterface();
				if (!duplicates.contains(temp))
				{
					duplicates.add(temp);
				}
				else
				{
					tgsi_list.remove(i);
				}
			}

			JsonArray tgsi_valuesArray = new JsonArray();
			// Extracts source interfaces out of the list and puts each of them
			// into a JSON-Object, which then is inserted into a JSON-Array
			for (ThroughputInterfaceTO item : tgsi_list)
			{
				JsonObject currentValue = new JsonObject();

				currentValue.add("SourceInterface", item.getSrcInterface());

				tgsi_valuesArray.add(currentValue);
			}

			return tgsi_valuesArray;

		case "ThroughputGetDestinationInterfaces":
			List<ThroughputInterfaceTO> tgdi_list = new LinkedList<ThroughputInterfaceTO>();
		//	tgdi_list = dataAccess.getThroughputInterface(request);

			JsonArray tgdi_valuesArray = new JsonArray();

			// Extracts the destination interface out of each list element
			// with the requested source interface and puts it
			// into a JSON-Object, which then is inserted into a JSON-Array
			for (ThroughputInterfaceTO item : tgdi_list)
			{
				JsonObject currentValue = new JsonObject();

				if (item.getSrcInterface().equals(request.getSourceInterface()))
				{
					currentValue.add("DestinationInterface", item.getDestInterface());
					tgdi_valuesArray.add(currentValue);
				}
			}

			return tgdi_valuesArray;

		case "ThroughputGetSevice":
			// Since there are currently only three services, the response is
			// hard coded in JsonHandler
			List<ThroughputServiceDB> ts_list = new LinkedList<ThroughputServiceDB>();
			ts_list = dataAccess.getThroughputService(request);

			JsonArray ts_valuesArray = new JsonArray();
			//
			// for (DashboardDelayGetDataTO item : ts_list) {
			// JsonObject currentValue = new JsonObject();
			//
			//
			//
			// ts_valuesArray.add(currentValue);
			// }
			return ts_valuesArray;

		case "UtilizationGetData":
			List<UtilizationDataTO> ud_list = new LinkedList<UtilizationDataTO>();
			ud_list = dataAccess.getUtilizationData(request);

			JsonArray ud_valuesArray = new JsonArray();
			//
			// for (DashboardDelayGetDataTO item : ud_list) {
			// JsonObject currentValue = new JsonObject();
			//
			//
			//
			// ud_valuesArray.add(currentValue);
			// }
			ud_valuesArray.add("Not implemented yet!");

			return ud_valuesArray;

		case "UtilizationGetInterface":
			List<UtilizationInterfaceTO> ugi_list = new LinkedList<UtilizationInterfaceTO>();
			//ugi_list = dataAccess.getUtilizationInterface(request);

			JsonArray ugi_valuesArray = new JsonArray();

			// Extracts the destination interface out of each list element
			// with the requested source interface and puts it
			// into a JSON-Object, which then is inserted into a JSON-Array
			// for (UtilizationInterfaceTO item : ugi_list) {
			// JsonObject currentValue = new JsonObject();
			// ugi_valuesArray.add(currentValue);
			// }
			ugi_valuesArray.add("Not implemented yet!");

			return ugi_valuesArray;

		case "UtilizationGetService":
			// Since there are currently only three services, the response is
			// hard coded in JsonHandler
			List<UtilizationServiceTO> ugs_list = new LinkedList<UtilizationServiceTO>();
			ugs_list = dataAccess.getUtilizationService(request);

			JsonArray ugs_valuesArray = new JsonArray();
			//
			// for (UtilizationServiceTO item : ugs_list) {
			// JsonObject currentValue = new JsonObject();
			//
			//
			//
			// ugs_valuesArray.add(currentValue);
			// }
			return ugs_valuesArray;

		default:
			// In Case of an Error, return an array with an error message
			System.out.println("ERROR");
			JsonArray er_array = new JsonArray();
			er_array.add("Error!");
			return er_array;

		}

	}

}
