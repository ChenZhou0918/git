package perfsonarserver.database.cache_to;

import java.util.HashMap;
import java.util.Map;



/**
 * Transfer object to easily convert java-objects into database-objects.
 * 
 * @author Benjamin Konrad, Sascha Degener
 */
public class ServiceCacheDB
{
	/** constant for col "deleteAt" inside database */
	public static final String DELETEAT_COL = "deleteAt";

	/** time of deletion for this chunk */
	private long deleteAt;

	/**
	 * Constructor for empty object
	 */
	public ServiceCacheDB()
	{
		this(0);
	}

	/**
	 * Constructor
	 * 
	 * @param deleteAt
	 *            time of deletion for this chunk
	 */
	public ServiceCacheDB(long deleteAt)
	{
		this.deleteAt = deleteAt;
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
//			dbObjDataTO.append(DELETEAT_COL, this.getDeleteAt());
//		}
//		return dbObjDataTO;
//	}
	
	public  Map<String, Long> fromType() {
		Map<String, Long> djlServiceDB =  new HashMap<String,Long>();
		{
			djlServiceDB.put(DELETEAT_COL, this.getDeleteAt());
		}
		return djlServiceDB;		
	}

	/**
	 * Converts a database-object into a java-object
	 * 
	 * @param dbObj
	 *            database-object to be converted
	 * @return {@link DelayJitterLossServiceCacheDB}
	 */
//	public static ServiceCacheDB toClass(DBObject dbObj)
//	{
//		ServiceCacheDB djldCacheTO = new ServiceCacheDB();
//		{
//			djldCacheTO.setDeleteAt((long) dbObj.get(DELETEAT_COL));
//		}
//		return djldCacheTO;
//	}
	
	

	/**
	 * Get time of deletion for this chunk
	 * 
	 * @return time of deletion for this chunk
	 */
	public long getDeleteAt()
	{
		return deleteAt;
	}

	/**
	 * Set time of deletion for this chunk
	 * 
	 * @param deleteAt
	 *            time of deletion for this chunk
	 */
	public void setDeleteAt(long deleteAt)
	{
		this.deleteAt = deleteAt;
	}
}
