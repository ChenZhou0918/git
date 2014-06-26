package perfsonarserver.database.cache_to;

import java.util.List;
//import me.prettyprint.cassandra.model.HColumnImpl;
import perfsonarserver.database.response_to.DelayJitterLossServiceTO;


/**
 * Transfer object to easily convert java-objects into database-objects.
 * 
 * @author Benjamin Konrad, Sascha Degener
 */
public class InterfaceCacheDB
{
	/** constant for col "serviceName" inside database */
	public static final String SERVICENAME_COL = "serviceName";
	/** constant for col "deleteAt" inside database */
	public static final String DELETEAT_COL = "deleteAt";
	/** constant for col "updating" inside database */
	public static final String UPDATING_COL = "updating";

	/** serviceName */
	private String serviceName;
	/** time of deletion for this chunk */
	private long deleteAt;
	/** is updating*/
	private boolean updating;

	/**
	 * Constructor for an empty object
	 */
	public InterfaceCacheDB()
	{
		super();
		this.serviceName = "";
		this.deleteAt = 0;
		updating = false;
	}

	/**
	 * Constructor
	 * 
	 * @param serviceName
	 *            service name
	 * @param deleteAt
	 *            time of deletion for this chunk
	 */
	public InterfaceCacheDB(String serviceName, long deleteAt, boolean updating)
	{
		this.serviceName = serviceName;
		this.deleteAt = deleteAt;
		this.updating = updating;
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
//			dbObjDataTO.append(DELETEAT_COL, this.getDeleteAt());
//			dbObjDataTO.append(UPDATING_COL, this.getUpdating());
//		}
//		return dbObjDataTO;
//	}

	/**
	 * Converts a database-object into a java-object
	 * 
	 * @param dbObj
	 *            database-object to be converted
	 * @return {@link DelayJitterLossInterfaceCacheDB}
	 */
//	public static InterfaceCacheDB toClass(DBObject dbObj)
//	{
//		InterfaceCacheDB djldCacheTO = new InterfaceCacheDB();
//		{
//			djldCacheTO.setServiceName((String) dbObj.get(SERVICENAME_COL));
//			djldCacheTO.setDeleteAt((long) dbObj.get(DELETEAT_COL));
//			djldCacheTO.setUpdating((boolean) dbObj.get(UPDATING_COL));
//		}
//		return djldCacheTO;
//	}
	
//	public static InterfaceCacheDB toType(List<HColumnImpl<String, String>> dbType) {
//		InterfaceCacheDB djlInterfaceTO = new InterfaceCacheDB();
//		{	
//			for(HColumnImpl<String, String> temp : dbType) {
//				if(temp.getName().equalsIgnoreCase(SERVICENAME_COL)){
//					djlInterfaceTO.setServiceName(temp.getValue());
//				} else if(temp.getName().equalsIgnoreCase(DELETEAT_COL)){
//					djlInterfaceTO.setDeleteAt(temp.getValueBytes().getLong());
//				} else if(temp.getName().equalsIgnoreCase(UPDATING_COL))
//			djlInterfaceTO.setUpdating(temp.getValueBytes().isReadOnly());
//			
//			}
//		}
//		
//		return djlInterfaceTO;
//	}
/*public static DataCacheDB toType(List<HColumnImpl<String, String>> dbObj)
	{
		DataCacheDB djlCacheTO = new DataCacheDB();
		{
			for(HColumnImpl<String, String> temp : dbObj) {			
				if(temp.getName().equalsIgnoreCase(SERVICENAME_COL)) {
					djlCacheTO.setServiceName(temp.getValue());
				}	
				else if(temp.getName().equalsIgnoreCase(SRCINTERFACE_COL)) {
					djlCacheTO.setSrcInterface(temp.getValue());
				}	
				else if(temp.getName().equalsIgnoreCase(DESTINTERFACE_COL)) {
					djlCacheTO.setSrcInterface(temp.getValue());
				} 
				else if(temp.getName().equalsIgnoreCase(STARTTIME_COL)) {
					djlCacheTO.setStart(temp.getValueBytes().getLong());
				}
				else if(temp.getName().equalsIgnoreCase(LASTUSED_COL)) {
					djlCacheTO.setLastUsed(temp.getValueBytes().getLong());
				}
				else if(temp.getName().equalsIgnoreCase(UPDATING_COL)) {
					djlCacheTO.setUpdating(temp.getValueBytes().isReadOnly());
				}
				
			} 
		}
		return djlCacheTO;
	}*/
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
	
	public boolean getUpdating()
	{
		return updating;
	}
	
	public void setUpdating(boolean updating)
	{
		this.updating = updating;
	}
	
	public String toString()
	{
		return serviceName + " " + deleteAt;
	}
}
