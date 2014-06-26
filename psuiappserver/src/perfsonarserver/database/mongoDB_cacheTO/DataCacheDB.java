package perfsonarserver.database.mongoDB_cacheTO;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * Transfer object to easily convert java-objects into database-objects.
 * 
 * @author Benjamin Konrad, Sascha Degener
 */
public class DataCacheDB
{
	/** constant for col "serviceName" inside database */
	public static final String SERVICENAME_COL = "serviceName";
	/** constant for col "srcInterface" inside database */
	public static final String SRCINTERFACE_COL = "srcInterface";
	/** constant for col "destInterface" inside database */
	public static final String DESTINTERFACE_COL = "destInterface";
	/** constant for col "start" inside database */
	public static final String STARTTIME_COL = "starttime";
	/** constant for col "lastUsed" inside database */
	public static final String LASTUSED_COL = "lastUsed";
	/** constant for col "updating" inside database */
	public static final String UPDATING_COL = "updating";

	/** service name */
	private String serviceName;
	/** source interface */
	private String srcInterface;
	/** destination interface */
	private String destInterface;
	/** start time */
	private long starttime;
	/** last access */
	private long lastUsed;
	/** this chunk is being updated */
	private boolean updating;

	/**
	 * Constructor for an empty object
	 */
	public DataCacheDB()
	{
		this.serviceName = "";
		this.srcInterface = "";
		this.destInterface = "";
		this.starttime = 0;
		this.lastUsed = 0;
		this.updating = false;
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
	 * @param start
	 *            starttime of chunk
	 * @param end
	 *            endtime of chunk
	 * @param lastUsed
	 *            last access
	 * @param updating
	 *            chunk is being updated
	 */
	public DataCacheDB(String serviceName, String srcInterface, String destInterface, long start, long lastUsed, boolean updating)
	{
		this.serviceName = serviceName;
		this.srcInterface = srcInterface;
		this.destInterface = destInterface;
		this.starttime = start;
		this.lastUsed = lastUsed;
		this.updating = updating;
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
			dbObjDataTO.append(SRCINTERFACE_COL, this.getSrcInterface());
			dbObjDataTO.append(DESTINTERFACE_COL, this.getDestInterface());
			dbObjDataTO.append(STARTTIME_COL, this.getStart());
			dbObjDataTO.append(LASTUSED_COL, this.getLastUsed());
			dbObjDataTO.append(UPDATING_COL, this.isUpdating());
		}
		return dbObjDataTO;
	}

	/**
	 * Converts a database-object into a java-object
	 * 
	 * @param dbObj
	 *            database-object to be converted
	 * @return {@link DelayJitterLossDataCacheDB}
	 */
	public static DataCacheDB toClass(DBObject dbObj)
	{
		DataCacheDB djldCacheTO = new DataCacheDB();
		{
			djldCacheTO.setServiceName((String) dbObj.get(SERVICENAME_COL));
			djldCacheTO.setSrcInterface((String) dbObj.get(SRCINTERFACE_COL));
			djldCacheTO.setDestInterface((String) dbObj.get(DESTINTERFACE_COL));
			djldCacheTO.setStart((long) dbObj.get(STARTTIME_COL));
			djldCacheTO.setLastUsed((long) dbObj.get(LASTUSED_COL));
			djldCacheTO.setUpdating((boolean) dbObj.get(UPDATING_COL));
		}
		return djldCacheTO;
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
	 * Get source interface
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
	 * Get destination interface
	 * 
	 * @param destInterface
	 *            destination interface
	 */
	public void setDestInterface(String destInterface)
	{
		this.destInterface = destInterface;
	}

	/**
	 * Get starttime of chunk
	 * 
	 * @return starttime of chunk
	 */
	public long getStart()
	{
		return starttime;
	}

	/**
	 * Set starttime of chunk
	 * 
	 * @param start
	 *            starttime of chunk
	 */
	public void setStart(long start)
	{
		this.starttime = start;
	}

	/**
	 * Get last use
	 * 
	 * @return last access
	 */
	public long getLastUsed()
	{
		return lastUsed;
	}

	/**
	 * Set last used
	 * 
	 * @param lastUsed
	 *            last use
	 */
	public void setLastUsed(long lastUsed)
	{
		this.lastUsed = lastUsed;
	}

	/**
	 * is chunk updating?
	 * 
	 * @return chunk updating
	 */
	public boolean isUpdating()
	{
		return updating;
	}

	/**
	 * Set updating
	 * 
	 * @param updating
	 *            chunk updating
	 */
	public void setUpdating(boolean updating)
	{
		this.updating = updating;
	}
	
	public String toString()
	{
		return serviceName + " " + srcInterface + "-" + destInterface + " " + starttime;
	}
}
