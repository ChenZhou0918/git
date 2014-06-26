package perfsonarserver.database.mongoDB_DatabaseTO;

//import me.prettyprint.cassandra.model.HColumnImpl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import perfsonarserver.database.mongoDB_responseTO.*;

public class UtilizationServiceDB extends UtilizationServiceTO
{
	/** constant for col "serviceName" inside database */
	public static final String SERVICENAME_COL = "serviceName";
	/** constant for col "serviceURL" inside database */
	public static final String SERVICEURL_COL = "serviceURL";
	
	private String serviceURL ="";
	
	public UtilizationServiceDB()
	{
		super();
	}
	
	public UtilizationServiceDB(String serviceName, String serviceURL)
	{
		super(serviceName);
		this.serviceURL = serviceURL;
	}

	public DBObject toDBObject()
	{
		BasicDBObject dbObjDataTO = new BasicDBObject();
		{
			dbObjDataTO.append(SERVICENAME_COL, this.getServiceName());
			dbObjDataTO.append(SERVICEURL_COL, this.getServiceURL());
		}
		return dbObjDataTO;
	}
	
	public static UtilizationServiceTO toClass(DBObject dbObj)
	{
		UtilizationServiceTO djlDataTO = new UtilizationServiceTO();
		{
			djlDataTO.setServiceName((String) dbObj.get(SERVICENAME_COL));
		}
		return djlDataTO;
	}
	
//	public static UtilizationServiceTO toType(HColumnImpl<String, String> dbType) {
//		UtilizationServiceTO throughputServiceTO = new UtilizationServiceTO();
//		{
//			throughputServiceTO.setServiceName(dbType.getName());
//		}
//		
//		return throughputServiceTO;
//	}
//	
	/**
	 * @return the serviceURL
	 */
	public String getServiceURL()
	{
		return serviceURL;
	}

	/**
	 * @param serviceURL the serviceURL to set
	 */
	public void setServiceURL(String serviceURL)
	{
		this.serviceURL = serviceURL;
	}
}
