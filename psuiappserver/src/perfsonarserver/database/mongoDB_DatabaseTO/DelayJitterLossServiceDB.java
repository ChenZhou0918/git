package perfsonarserver.database.mongoDB_DatabaseTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import perfsonarserver.database.mongoDB_responseTO.*;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * Transfer object to easily convert java-objects into database-objects.
 * 
 * @author Benjamin Konrad, Sascha Degener
 */
public class DelayJitterLossServiceDB extends DelayJitterLossServiceTO
{
	/** constant for col "serviceName" inside database */
	public static final String SERVICENAME_COL = "serviceName";
	/** constant for col "serviceURL" inside database */
	public static final String SERVICEURL_COL = "serviceURL";

	/** service url */
	private String serviceURL;

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
	}

	/**
	 * Converts this java-object into a database-object
	 * 
	 * @return BasicDBObject database-object
	 */
	public DBObject toDBObject()
	{
		BasicDBObject dbObjDataTO = new BasicDBObject();
		{
			dbObjDataTO.append(SERVICENAME_COL, this.getServiceName());
			dbObjDataTO.append(SERVICEURL_COL, this.getServiceURL());
		}
		return dbObjDataTO;
	}

	/**
	 * Converts a database-object into a java-object
	 * 
	 * @param dbObj
	 *            database-object to be converted
	 * @return {@link DelayJitterLossServiceTO}
	 */
	public static DelayJitterLossServiceTO toClass(DBObject dbObj)
	{
		DelayJitterLossServiceTO djlDataTO = new DelayJitterLossServiceTO();
		{
			djlDataTO.setServiceName((String) dbObj.get(SERVICENAME_COL));
		}
		return djlDataTO;
	}
	
	
	public  Map<String, String> fromType() {
		Map<String, String> djlServiceDB = new HashMap<String, String>();
		{
			djlServiceDB.put(SERVICENAME_COL, this.getServiceName());
			djlServiceDB.put(SERVICEURL_COL, this.getServiceName());
		}
		return djlServiceDB;		
	}

	/**
	 * Converts a database-object into a java-object
	 * 
	 * @param dbObj
	 *            database-object to be converted
	 * @return {@link DelayJitterLossServiceDB}
	 */
	public static DelayJitterLossServiceDB toClassDB(DBObject dbObj)
	{
		DelayJitterLossServiceDB djlDataDB = new DelayJitterLossServiceDB();
		{
			djlDataDB.setServiceName((String) dbObj.get(SERVICENAME_COL));
			djlDataDB.setServiceURL((String) dbObj.get(SERVICEURL_COL));
		}
		return djlDataDB;
	}

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
}