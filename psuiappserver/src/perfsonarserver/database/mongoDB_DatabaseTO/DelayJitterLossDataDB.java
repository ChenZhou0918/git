package perfsonarserver.database.mongoDB_DatabaseTO;

import perfsonarserver.database.mongoDB_responseTO.DashboardDelayGetDataTO;
import perfsonarserver.database.mongoDB_responseTO.DashboardJitterGetDataTO;
import perfsonarserver.database.mongoDB_responseTO.DelayJitterLossDataTO;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * Transfer object to easily convert java-objects into database-objects.
 * 
 * @author Benjamin Konrad, Sascha Degener
 */
public class DelayJitterLossDataDB extends DelayJitterLossDataTO
{
	/** constant for col "timestamp" inside database */
	public static final String TIMESTAMP_COL = "timestamp";
	/** constant for col "maxDelay" inside database */
	public static final String MAXDELAY_COL = "maxDelay";
	/** constant for col "minDelay" inside database */
	public static final String MINDELAY_COL = "minDelay";
	/** constant for col "maxJitter" inside database */
	public static final String MAXJITTER_COL = "maxJitter";
	/** constant for col "minJitter" inside database */
	public static final String MINJITTER_COL = "minJitter";
	/** constant for col "loss" inside database */
	public static final String LOSS_COL = "loss";
	/** constant for col "service" inside database */
	public static final String SERVICE_COL = "service";
	/** constant for col "srcInterface" inside database */
	public static final String SRCINTERFACE_COL = "srcInterface";
	/** constant for col "destInterface" inside database */
	public static final String DESTINTERFACE_COL = "destInterface";

	/** service name */
	private String serviceName;
	/** source interface */
	private String srcInterface;
	/** destination interface */
	private String destInterface;

	/**
	 * Constructor for an empty object
	 */
	public DelayJitterLossDataDB()
	{
		super();
		this.serviceName = "";
		this.srcInterface = "";
		this.destInterface = "";
	}

	/**
	 * Constructor
	 * 
	 * @param timestamp
	 *            time of measurement
	 * @param maxDelay
	 *            maximal delay
	 * @param minDelay
	 *            minimal delay
	 * @param maxJitter
	 *            maximal jitter
	 * @param minJitter
	 *            minimal jitter
	 * @param loss
	 *            loss
	 * @param service
	 *            service name
	 * @param srcInterface
	 *            source interface
	 * @param destInterface
	 *            destination interface
	 */
	public DelayJitterLossDataDB(long timestamp, double maxDelay, double minDelay, double maxJitter, double minJitter, int loss, String service, String srcInterface, String destInterface)
	{
		super(timestamp, maxDelay, minDelay, maxJitter, minJitter, loss);
		this.serviceName = service;
		this.srcInterface = srcInterface;
		this.destInterface = destInterface;
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
			dbObjDataTO.append(TIMESTAMP_COL, this.getTimestamp());
			dbObjDataTO.append(MAXDELAY_COL, this.getMaxDelay());
			dbObjDataTO.append(MINDELAY_COL, this.getMinDelay());
			dbObjDataTO.append(MAXJITTER_COL, this.getMaxJitter());
			dbObjDataTO.append(MINJITTER_COL, this.getMinJitter());
			dbObjDataTO.append(LOSS_COL, this.getLoss());
			dbObjDataTO.append(SERVICE_COL, this.getServiceName());
			dbObjDataTO.append(SRCINTERFACE_COL, this.getSrcInterface());
			dbObjDataTO.append(DESTINTERFACE_COL, this.getDestInterface());
		}
		return dbObjDataTO;
	}

	/**
	 * Converts a database-object into a java-object
	 * 
	 * @param dbObj
	 *            database-object to be converted
	 * @return {@link DelayJitterLossDataTO}
	 */
	public static DelayJitterLossDataTO toClass(DBObject dbObj)
	{
		DelayJitterLossDataTO djlDataTO = new DelayJitterLossDataTO();
		{
			djlDataTO.setTimestamp((long) dbObj.get(TIMESTAMP_COL));
			djlDataTO.setMaxDelay((double) dbObj.get(MAXDELAY_COL));
			djlDataTO.setMinDelay((double) dbObj.get(MINDELAY_COL));
			djlDataTO.setMaxJitter((double) dbObj.get(MAXJITTER_COL));
			djlDataTO.setMinJitter((double) dbObj.get(MINJITTER_COL));
			djlDataTO.setLoss((int) dbObj.get(LOSS_COL));
		}
		return djlDataTO;
	}
	
	
	// for dashboard responseTO
	public static DashboardDelayGetDataTO toDashDelayClass(DBObject dbObj)
	{
	      DashboardDelayGetDataTO DataTO = new DashboardDelayGetDataTO();
		{
			DataTO.setTimestamp((long) dbObj.get(TIMESTAMP_COL));
			DataTO.setService((String) dbObj.get(SERVICE_COL));
			DataTO.setSrcInterface((String) dbObj.get(SRCINTERFACE_COL));
			DataTO.setDestInterface((String) dbObj.get(DESTINTERFACE_COL));
			DataTO.setMaxDelay((double)dbObj.get(MAXDELAY_COL));
		}
		return DataTO;
	}
	public static DashboardJitterGetDataTO toDashJitterClass(DBObject dbObj)
	{
	      DashboardJitterGetDataTO DataTO = new DashboardJitterGetDataTO();
		{
			DataTO.setTimestamp((long) dbObj.get(TIMESTAMP_COL));
			DataTO.setService((String) dbObj.get(SERVICE_COL));
			DataTO.setSrcInterface((String) dbObj.get(SRCINTERFACE_COL));
			DataTO.setDestInterface((String) dbObj.get(DESTINTERFACE_COL));
			DataTO.setMaxJitter((double)dbObj.get(MAXJITTER_COL));
		}
		return DataTO;
	}
	public static DelayJitterLossDataDB toDBClass(DBObject dbObj)
	{
		DelayJitterLossDataDB djlDataDB = new DelayJitterLossDataDB();
		{
			djlDataDB.setTimestamp((long) dbObj.get(TIMESTAMP_COL));
			djlDataDB.setMaxDelay((double) dbObj.get(MAXDELAY_COL));
			djlDataDB.setMinDelay((double) dbObj.get(MINDELAY_COL));
			djlDataDB.setMaxJitter((double) dbObj.get(MAXJITTER_COL));
			djlDataDB.setMinJitter((double) dbObj.get(MINJITTER_COL));
			djlDataDB.setLoss((int) dbObj.get(LOSS_COL));
			djlDataDB.setDestInterface((String) dbObj.get(DESTINTERFACE_COL));
			djlDataDB.setServiceName((String) dbObj.get(SERVICE_COL));
			djlDataDB.setSrcInterface((String) dbObj.get(SRCINTERFACE_COL));
		}
		return djlDataDB;
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

	/**
	 * Get source interface
	 * 
	 * @return source interface
	 */
	public String getSrcInterface()
	{
		return srcInterface;
	}

	/**
	 * Set source interface
	 * 
	 * @param srcInterface
	 *            source interface
	 */
	public void setSrcInterface(String srcInterface)
	{
		this.srcInterface = srcInterface;
	}

	/**
	 * Get destination interface
	 * 
	 * @return destination interface
	 */
	public String getDestInterface()
	{
		return destInterface;
	}

	/**
	 * Set destination interface
	 * 
	 * @param destInterface
	 *            destination interface
	 */
	public void setDestInterface(String destInterface)
	{
		this.destInterface = destInterface;
	}

}