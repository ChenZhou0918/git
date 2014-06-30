package perfsonarserver.database.mongoDB_DatabaseTO;

import java.util.HashMap;
import java.util.Map;

import perfsonarserver.database.mongoDB_responseTO.*;
import perfsonarserver.fetchDataAndProcess.transferObjects.DelayJitterLossInterfacePair;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * Transfer object to easily convert java-objects into database-objects.
 * 
 * @author Benjamin Konrad, Sascha Degener,ZHOU CHEN
 */
public class DelayJitterLossInterfaceDB extends DelayJitterLossInterfaceTO
{
	/** constant for col "srcInterface" inside database */
	public static final String SRCINTERFACE_COL = "srcInterface";
	/** constant for col "destInterface" inside database */
	public static final String DESTINTERFACE_COL = "destInterface";
	/** constant for col "serviceName" inside database */
	public static final String SERVICENAME_COL = "serviceName";
	public static final String LOSSNUMBER_COL = "lossNumber";
	/** service name */
	private String serviceName;
	private int lossNumber;

	/**
	 * Constructor for an empty object
	 */
	public DelayJitterLossInterfaceDB()
	{
		super();
		this.serviceName = "";
	}

	/**
	 * Constructor
	 * 
	 * @param serviceName
	 *            service name
	 * @param srcInterface
	 *            source interface
	 * @param destInterface
	 *            destination interface
	 */
	public DelayJitterLossInterfaceDB(String serviceName, String srcInterface, String destInterface)
	{
		super(srcInterface, destInterface);
		this.serviceName = serviceName;
	}
	
	/**
	 * constructor for dashboard : Top loss interfaces*/
	public DelayJitterLossInterfaceDB(String serviceName, String srcInterface, String destInterface,int lossNumber)
	{
		super(srcInterface, destInterface);
		this.serviceName = serviceName;
		this.lossNumber=lossNumber;
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
			dbObjDataTO.append(SRCINTERFACE_COL, this.getSrcInterface());
			dbObjDataTO.append(DESTINTERFACE_COL, this.getDestInterface());
			dbObjDataTO.append(SERVICENAME_COL, this.getServiceName());
			dbObjDataTO.append(LOSSNUMBER_COL, this.getLossNumber());
		}
		return dbObjDataTO;
	}

	/**
	 * Converts a database-object into a java-object(for Dashboard Loss ResponseTO)
	 * 
	 * @param dbObj
	 *            database-object to be converted
	 * @return {@link DelayJitterLossInterfaceTO}
	 */
	public static DashboardLossGetDataTO toDashClass(DBObject dbObj)
	{
		DashboardLossGetDataTO djlDataTO = new DashboardLossGetDataTO();
		{
			djlDataTO.setSrcInterface(((String) dbObj.get(SRCINTERFACE_COL)));
			djlDataTO.setDestInterface((String) dbObj.get(DESTINTERFACE_COL));
			djlDataTO.setLossNumber((int)dbObj.get(LOSSNUMBER_COL));
			djlDataTO.setServiceName((String)dbObj.get(SERVICENAME_COL));
		}
		return djlDataTO;
	}
	
	public static DelayJitterLossInterfaceTO toClass(DBObject dbObj)
	{
		DelayJitterLossInterfaceTO djlDataTO = new DelayJitterLossInterfaceTO();
		{
			djlDataTO.setSrcInterface((String) dbObj.get(SRCINTERFACE_COL));
			djlDataTO.setDestInterface((String) dbObj.get(DESTINTERFACE_COL));
		}
		return djlDataTO;
	}
	
	
	
	/** For Cassandra Implementation: Put Objects into Map */
	public Map<String, String> processKeyValueMap() {
		HashMap<String, String> returnMap = new HashMap<String, String>();
		 {
			returnMap.put("Servicename", this.getServiceName());
			returnMap.put("Source", this.getSrcInterface());
			returnMap.put("Destination", this.getDestInterface());

		}
		return returnMap;
	
	}	

	/**
	 * Get service name
	 * 
	 * @return service name
	 */
	public String getServiceName()
	{
		return serviceName;
	}

	/**
	 * Set service name
	 * 
	 * @param serviceName
	 *            service name
	 */
	public void setServiceName(String serviceName)
	{
		this.serviceName = serviceName;
	}



public int getLossNumber()
{
	return lossNumber;
}

public void setLossNumber(int lossNumber)
{
	this.lossNumber=lossNumber;
}

}



