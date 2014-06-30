package perfsonarserver.fetchDataAndProcess;

import java.util.List;

import perfsonarserver.fetchDataAndProcess.transferObjects.DelayJitterLossInterfacePair;

/**
 * Interface for XML to different objects converting.
 * 
 * @author Clemens Schlei, Florian Rueffer
 * 
 */
public interface IConverter
{
	/**
	 * Convert the response XML to java object. Type of object depends on the
	 * implementing class.
	 * 
	 * @param response
	 *            Source XML
	 * @param uri
	 *            URI of the Service.
	 * @param startTime
	 *            Start time of measurement period in milliseconds
	 * @param endTime
	 *            End time of measurement period in milliseconds
	 * @return Object XML as java object
	 */
	public Object convertDJLIPairList(String response, String uri, long startTime, long endTime);

	/**
	 * Convert the response XML to java object. Type of object depends on the
	 * implementing class.
	 * 
	 * @param response
	 *            Source XML
	 * @param uri
	 *            URI of the Service
	 * @param startTime
	 *            Start time of measurement period in milliseconds
	 * @param endTime
	 *            End time of measurement period in milliseconds
	 * @return Object XML as java object
	 */
	public Object convertThroughputIPairList(String response, String uri, long startTime, long endTime);

	/**
	 * Convert the response XML to java object. Type of object depends on the
	 * implementing class.
	 * 
	 * @param response
	 *            Source XML
	 * @param uri
	 *            URI of the Service
	 * @return Object XML as java object
	 */
	public Object convertUtilizationIList(String response, String uri);

	/**
	 * Links java objects to list.
	 * 
	 * @param response
	 *            Source XML
	 * @param list
	 *            List of java objects
	 * @return Object XML as java object
	 */
	public boolean fillDJLData(String response, Object list);

	/**
	 * Links java objects to list.
	 * 
	 * @param response
	 *            Source XML
	 * @param list
	 *            List of java objects
	 * @return Object XML as java object
	 */
	public Object fillThroughputData(String response, Object list);

	/**
	 * Links java objects to list.
	 * 
	 * @param response
	 *            Source XML
	 * @param list
	 *            List of java objects
	 * @param startTime
	 *            Start time of measurement period in milliseconds
	 * @param endTime
	 *            End time of measurement period in milliseconds
	 * @return Object XML as java object
	 */
	public boolean fillUtilizationData(String response, Object list, long startTime, long endTime);
	
	List<DelayJitterLossInterfacePair> getDelayJitterLossInterfacePair();
}
