package perfsonarserver.database.database_to;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.prettyprint.cassandra.model.HColumnImpl;
import perfsonarserver.database.response_to.DelayJitterLossServiceTO;



/**
 * Transfer object to easily convert java-objects into database-objects.
 * 
 * @author Benjamin Konrad, Sascha Degener
 */
public class DelayJitterLossServiceDB extends DelayJitterLossServiceTO implements Serializable
{
	/** constant for col "serviceName" inside database */
//	public static final String SERVICENAME_COL = "";
//	/** constant for col "serviceURL" inside database */
//	public static final String SERVICEURL_COL = "";

	/**
	 * 
	 */
	
	/**
	 * 
	 */
	//private static final long serialVersionUID = 1726913991543065889L;
	/** service url */
	private String serviceURL;
	private String serviceID;
	private String serviceName;
	
	/**
	 * Constructor for an empty object
	 */
	public DelayJitterLossServiceDB()
	{
		super();
		serviceURL = "";
	}

	/**
	 * Constructor
	 * 
	 * @param serviceName
	 *            service name
	 * @param serviceURL
	 *            service url
	 */
	public DelayJitterLossServiceDB(String serviceName, String serviceURL)
	{
		super(serviceName);
		this.serviceURL = serviceURL;
		this.serviceName = serviceName;
	}

	/**
	 * Converts this java-object into a database-object
	 * 
	 * @return BasicDBObject database-object
	 */
//	public DBObject toDBObject()
//	{
//		BasicDBObject dbObjDataTO = new BasicDBObject();
//		{
//			dbObjDataTO.append(SERVICENAME_COL, this.getServiceName());
//			dbObjDataTO.append(SERVICEURL_COL, this.getServiceURL());
//		}
//		return dbObjDataTO;
//	}

	/**
	 * Converts a database-object into a java-object
	 * 
	 * @param dbObj
	 *            database-object to be converted
	 * @return {@link DelayJitterLossServiceTO}
	 */
//	public static DelayJitterLossServiceTO toClass(DBObject dbObj)
//	{
//		DelayJitterLossServiceTO djlDataTO = new DelayJitterLossServiceTO();
//		{
//			djlDataTO.setServiceName((String) dbObj.get(SERVICENAME_COL));
//		}
//		return djlDataTO;
//	}
	
	public static DelayJitterLossServiceDB setValues(Map.Entry<String, String> values) {
		DelayJitterLossServiceTO djlServiceTO = new DelayJitterLossServiceTO();
		DelayJitterLossServiceDB djlServiceDB = new DelayJitterLossServiceDB();
		{
			if(values.getKey().equalsIgnoreCase("SERVICENAME_COL"))
				djlServiceTO.setServiceName(values.getValue());
			else if(values.getKey().equalsIgnoreCase("SERVICEURL_COL"))
				djlServiceDB.setServiceURL(values.getValue());
		}
		
		return djlServiceDB;
	}
	
//	public  Map<String, String> fromType() {
//		Map<String, String> djlServiceDB = new HashMap<String, String>();
//		{
//			djlServiceDB.put(SERVICENAME_COL, this.getServiceName());
//			djlServiceDB.put(SERVICEURL_COL, this.getServiceName());
//		}
//		return djlServiceDB;		
//	}

	/**
	 * Converts a database-object into a java-object
	 * 
	 * @param dbObj
	 *            database-object to be converted
	 * @return {@link DelayJitterLossServiceDB}
	 */
//	public static DelayJitterLossServiceDB toClassDB(DBObject dbObj)
//	{
//		DelayJitterLossServiceDB djlDataDB = new DelayJitterLossServiceDB();
//		{
//			djlDataDB.setServiceName((String) dbObj.get(SERVICENAME_COL));
//			djlDataDB.setServiceURL((String) dbObj.get(SERVICEURL_COL));
//		}
//		return djlDataDB;
//	}

	/**
	 * Get service url
	 * 
	 * @return service url
	 */
	public String getServiceURL()
	{
		return serviceURL;
	}

	/**
	 * Set service url
	 * 
	 * @param serviceURL
	 *            service url
	 */
	public void setServiceURL(String serviceURL)
	{
		this.serviceURL = serviceURL;
	}
	
	public String getDelayJitterLossServiceID(){
		return serviceID;
	}
	
	public void setDelayJitterLossServiceID(String serviceID){
		this.serviceID = serviceID;
	}
	
	/**
	 * Get for service name
	 * 
	 * @return serviceName name of the service
	 */
	public String getServiceName()
	{
		return serviceName;
	}

	/**
	 * Set for service name
	 * 
	 * @param serviceName
	 *            name of the service
	 */
	public void setServiceName(String serviceName)
	{
		this.serviceName = serviceName;
	}
	
	/**
	 * To string for debugging purpose
	 */
	
	public String toString()
	{
		return "DelayJitterLossServiceDB [serviceID=" + serviceID+ ", serviceName=" + serviceName + ", serviceURL=" + serviceURL+"]";
	}
	 
}