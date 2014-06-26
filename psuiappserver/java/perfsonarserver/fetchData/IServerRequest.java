package perfsonarserver.fetchData;

import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import perfsonarserver.database.database_to.DelayJitterLossDataDB;
import perfsonarserver.database.database_to.DelayJitterLossInterfaceDB;
import perfsonarserver.database.database_to.DelayJitterLossServiceDB;
import perfsonarserver.database.database_to.ThroughputInterfaceDB;
import perfsonarserver.database.database_to.ThroughputServiceDB;
import perfsonarserver.database.database_to.UtilizationInterfaceDB;
import perfsonarserver.database.database_to.UtilizationServiceDB;
import perfsonarserver.fetchData.exception.FetchFailException;
import perfsonarserver.fetchData.transferObjects.*;

/**
 * Interface for server requests.
 * 
 * @author Clemens Schlei, Florian Rueffer
 * 
 */
public interface IServerRequest
{

	/**
	 * Fills the interfaces in the list with data.
	 * 
	 * @param serverURL
	 *            Server URL of service
	 * @param list
	 *            List of DelayJitterLossInterfacePairs
	 * @param startTime
	 *            Start time of measurement period in milliseconds
	 * @param endTime
	 *            End time of measurement period in milliseconds
	 * 
	 * @return List with Interfacepairs which can contain data in its data list
	 */
	public List<DelayJitterLossInterfacePair> getDelayJitterLossData(String serverURL, List<DelayJitterLossInterfacePair> list, long startTime, long endTime) throws FetchFailException;

	/**
	 * Gets data for an interface pair in a specific period.
	 * 
	 * @param serverURL
	 *            Server URL of service
	 * @param source
	 *            Source
	 * @param dest
	 *            Destination
	 * @param startTime
	 *            Start time of measurement period in milliseconds
	 * @param endTime
	 *            End time of measurement period in milliseconds
	 * @return List with DelayJitterLossData Objects
	 */
	public List<DelayJitterLossData> getDelayJitterLossData(List<DelayJitterLossInterfaceDB> list,List<DelayJitterLossServiceDB> serviceList, long startTime, long endTime) throws FetchFailException;

	/**
	 * Provides a List with interface names and their connections to each.
	 * 
	 * @param serverURL
	 *            Server URL of service
	 * @param startTime
	 *            Start time of measurement period in milliseconds
	 * @param endTime
	 *            End time of measurement period in milliseconds
	 * @return List with DelayJitterLossInterfacePair Objects
	 */
	public List<DelayJitterLossInterfacePair> getDelayJitterLossInterfacePairs(String serverURL, long startTime, long endTime) throws FetchFailException;

	/**
	 * Provides available services for delay, jitter, loss.
	 * 
	 * @return Map of available services and their uri
	 */
	public Map<String, String> getDelayJitterLossServices() throws FetchFailException;

	/**
	 * Gets data for a list of interface pairs in a specific period.
	 * 
	 * @param serverURL
	 *            Server URL of service
	 * @param list
	 *            List of ThroughputInterfacePairs
	 * @param startTime
	 *            Start time of measurement period in milliseconds
	 * @param endTime
	 *            End time of measurement period in milliseconds
	 * @return List of ThroughputInterfacePair Objects
	 */
	public List<ThroughputInterfacePair> getThroughputData(String serverURL, List<ThroughputInterfacePair> list, long startTime, long endTime) throws FetchFailException;

	/**
	 * Gets data for an interface pair in a specific period.
	 * 
	 * @param serverURL
	 *            Server URL of service
	 * @param pair
	 *            ThroughputInterfacePair Object
	 * @param startTime
	 *            Start time of measurement period in milliseconds
	 * @param endTime
	 *            End time of measurement period in milliseconds
	 * @return List of ThroughputData Objects
	 */
	public List<ThroughputData> getThroughputData(String serverURL, String source, String dest,String MID, long startTime, long endTime) throws FetchFailException;

	/**
	 * Provides a List with interface names and their connections to each.
	 * 
	 * @param serverURL
	 *            Server URL of service
	 * @param startTime
	 *            Start time of measurement period in milliseconds
	 * @param endTime
	 *            End time of measurement period in milliseconds
	 * @return List of ThroughputInterfacePair Objects
	 */
	public List<ThroughputInterfacePair> getThroughputInterfacePairs(String serverURL, long startTime, long endTime) throws FetchFailException;

	/**
	 * Provides available services for throughput.
	 * 
	 * @return Map of available services and their uri
	 */
	public Map<String, String> getThroughputServices() throws FetchFailException;


	

	/**
	 * Gets data for an interface in a specific period.
	 * 
	 * @param serverURL
	 *            Server URL of service
	 * @param ipAddress
	 *            IP address
	 * @param ipType
	 *            IP type
	 * @param hostname
	 *            Hostname
	 * @param start
	 *            Start time of measurement period in milliseconds
	 * @param end
	 *            End time of measurement period in milliseconds
	 * @return List of UtilizationData Objects
	 */
	public List<UtilizationData> getUtilizationData(List<UtilizationServiceDB> services,
			List<UtilizationInterfaceDB> interfaces, long start, long end) throws FetchFailException;
	
	/**
	 * Provides a List with interfaces.
	 * 
	 * @param serverURL
	 *            Server URL of service
	 * @return List of UtilizationInterface Objects
	 */
	public List<UtilizationInterface> getUtilizationInterfaces(String serverURL) throws FetchFailException;

	/**
	 * Provides available services for utilization.
	 * 
	 * @return Map of available services and their uri
	 */
	public Map<String, String> getUtilizationServices() throws FetchFailException;
	
	public List<ThroughputData> getThroughputData(List<ThroughputInterfaceDB> interfaces,
			List<ThroughputServiceDB> services, long startTime, long endTime) throws FetchFailException;

}
