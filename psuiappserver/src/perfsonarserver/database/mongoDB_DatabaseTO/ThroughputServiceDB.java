package perfsonarserver.database.mongoDB_DatabaseTO;

//import me.prettyprint.cassandra.model.HColumnImpl;
import perfsonarserver.database.mongoDB_responseTO.*;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * Transfer object to easily convert java-objects into database-objects.
 * 
 * @author Benjamin Konrad, Sascha Degener
 */
public class ThroughputServiceDB extends ThroughputServiceTO
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
	public ThroughputServiceDB()
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
	public ThroughputServiceDB(String serviceName, String serviceURL)
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
	 * @return {@link ThroughputServiceTO}
	 */
	public static ThroughputServiceTO toClass(DBObject dbObj)
	{
		ThroughputServiceTO djlDataTO = new ThroughputServiceTO();
		{
			djlDataTO.setServiceName((String) dbObj.get(SERVICENAME_COL));
		}
		return djlDataTO;
	}

	/**
	 * Converts a database-object into a java-object
	 * 
	 * @param dbObj
	 *            database-object to be converted
	 * @return {@link ThroughputServiceDB}
	 */
	public static ThroughputServiceDB toClassDB(DBObject dbObj)
	{
		ThroughputServiceDB djlDataDB = new ThroughputServiceDB();
		{
			djlDataDB.setServiceName((String) dbObj.get(SERVICENAME_COL));
			djlDataDB.setServiceURL((String) dbObj.get(SERVICEURL_COL));
		}
		return djlDataDB;
	}
	
//	public static ThroughputServiceTO toType(HColumnImpl<String, String> dbType) {
//		ThroughputServiceTO throughputServiceTO = new ThroughputServiceTO();
//		{
//			throughputServiceTO.setServiceName(dbType.getName());
//		}
//		
//		return throughputServiceTO;
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
}
