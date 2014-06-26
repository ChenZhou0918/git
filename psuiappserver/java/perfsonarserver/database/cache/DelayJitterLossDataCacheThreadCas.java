package perfsonarserver.database.cache;

import java.util.HashMap;
import java.util.Iterator;

import java.util.List;
import java.util.Map;


import perfsonarserver.database.cassandraImpl.DataAccessCas;
import perfsonarserver.database.cache_to.DataCacheDB;
import perfsonarserver.database.cassandraImpl.DatabaseDJLCas;
import perfsonarserver.database.database_to.DelayJitterLossDataDB;
import perfsonarserver.database.exception.FetchNothingException;
import perfsonarserver.fetchData.IServerRequest;
import perfsonarserver.fetchData.PerfsonarRequest;
import perfsonarserver.fetchData.exception.FetchFailException;
import perfsonarserver.fetchData.transferObjects.DelayJitterLossData;


//public class DelayJitterLossDataCacheThreadCas extends Thread
//{
//	/** instance of database */
//	private DatabaseDJLCas db;
//	/** service name */
//	private String serviceName;
//	/** service url */
//	private String serviceURL;
//	/** source interface */
//	private String srcInterface;
//	/** destination interface */
//	private String destInterface;
//	/** starttime of chunk */
//	private long aktStart;
//	/** endtime of chunk */
//	private long aktEnd;
//	/** cache */
////	private CacheDJLCas cache;
//
//	/**
//	 * Constructor
//	 * 
//	 * @param serviceName
//	 *            service name
//	 * @param serviceURL
//	 *            service url
//	 * @param srcInterface
//	 *            source interface
//	 * @param destInterface
//	 *            destination interface
//	 * @param aktStart
//	 *            starttime of chunk
//	 * @param aktEnd
//	 *            endtime of chunk
//	 * @param cacheSector
//	 *            cache sector to reset db semaphor
//	 */
//	public DelayJitterLossDataCacheThreadCas(String serviceName, String serviceURL, String srcInterface, String destInterface, long aktStart, long aktEnd)
//	{
//		this.serviceURL = serviceURL;
//		this.serviceName = serviceName;
//		this.srcInterface = srcInterface;
//		this.destInterface = destInterface;
//		this.aktStart = aktStart;
//		this.aktEnd = aktEnd;
//		this.db = DatabaseDJLCas.getInstance();
//		
//	}
//
//	/**
//	 * This thread requests all data for this chunk from the server and inserts
//	 * it into the database.
//	 */
//	public void run()
//	{
//		System.out.println("Update DJLData Cache：" + serviceName + " " + srcInterface + "-" + destInterface + " " + aktStart + "-" + aktEnd + " " + DataAccessCas.convertDateToString(System.currentTimeMillis()));
//		IServerRequest request = new PerfsonarRequest();
//		// List for fast insertion into database
////		List<DBObject> ll = new LinkedList<DBObject>();
//		Map<String, Object> query = new HashMap<String,Object>();
//		query.put(DataCacheDB.SERVICENAME_COL, serviceName);
//		query.put(DataCacheDB.SRCINTERFACE_COL, srcInterface);
//		query.put(DataCacheDB.DESTINTERFACE_COL, destInterface);
//		query.put(DataCacheDB.STARTTIME_COL, aktStart);
//		
//		
////
////		BasicDBObject query = new BasicDBObject(DataCacheDB.SERVICENAME_COL, serviceName);
////		query.append(DataCacheDB.SRCINTERFACE_COL, srcInterface);
////		query.append(DataCacheDB.DESTINTERFACE_COL, destInterface);
////		query.append(DataCacheDB.STARTTIME_COL, aktStart);
//		
//		// Request data from server
//		try
//		{
//			List<DelayJitterLossData> list = request.getDelayJitterLossData(serviceURL, srcInterface, destInterface, aktStart, aktEnd);
//			Iterator<DelayJitterLossData> it = list.iterator();
//			Map<String, Object> dbObj = new HashMap<String,Object>();
//			// Convert data into database format
//			while (it.hasNext())
//			{
//				DelayJitterLossData djld = it.next();
//				DelayJitterLossDataDB djlddb = new DelayJitterLossDataDB(djld.getTime(), djld.getMaxDelay(), djld.getMinDelay(), djld.getMaxIpdvJitter(), djld.getMinIpdvJitter(), djld.getLoss(), serviceName, srcInterface, destInterface);
//				  
//				//dbObj = djlddb.processKeyValueMap();
//				DBObject dbObj = djlddb.toDBObject();
//				ll.add(dbObj);
//			}
//			// Insert data into database
//		//	db.insertData(DataAccessCas.djlDataKey, DataAccessCas.dbDJLcolumnfamily, dbObj);
//
//			// Set the updating in cache to false		
//			
//			
//		//		db.updateDJLData(CacheDJLCas.djlDataKey_cache,CacheDJLCas.djlCF_cache , new DataCacheDB());
//				//db.update(CacheDJLCas..DATACACHE_COLLECTION, query, new BasicDBObject("$set", new BasicDBObject(DataCacheDB.UPDATING_COL, false)));
//			
//		}
//		catch (FetchFailException e)
//		{
//			e.printStackTrace();
//
//			
//			
//			{
//		//		db.dropColumns(CacheDJLCas.djlDataKey_cache, CacheDJLCas.djlCF_cache);
//			
//
//			throw new FetchNothingException("fail fetch DJLData: " + serviceName + " " + srcInterface + "-" + destInterface + " " + aktStart + "-" + aktEnd);
//
//			}
//
//		} 
//	
//	}
//}
