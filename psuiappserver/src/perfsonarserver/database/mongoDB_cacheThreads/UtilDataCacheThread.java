package perfsonarserver.database.mongoDB_cacheThreads;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import perfsonarserver.database.mongoDBImpl.*;
import perfsonarserver.database.mongoDB_DatabaseTO.UtilizationDataDB;
import perfsonarserver.database.mongoDB_cacheTO.DataCacheDB;
import perfsonarserver.database.exception.FetchNothingException;
import perfsonarserver.fetchDataAndProcess.IServerRequest;
import perfsonarserver.fetchDataAndProcess.PerfsonarRequest;
import perfsonarserver.fetchDataAndProcess.exception.FetchFailException;
import perfsonarserver.fetchDataAndProcess.transferObjects.UtilizationData;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class UtilDataCacheThread extends Thread
{
	/** instance of database */
	private DatabaseUtil db;
	/** service url */
	private String serviceURL;
	private String serviceName;
	/** source interface */
	private String interfaceName;
	private String direction;
	/** starttime of chunk */
	private long aktStart;
	/** endtime of chunk */
	private long aktEnd;
	/** cache */
	private DJLCache cache;

	/**
	 * Constructor
	 * 
	 * @param serviceName
	 *            service name
	 * @param serviceURL
	 *            service url
	 * @param srcInterface
	 *            source interface
	 * @param destInterface
	 *            destination interface
	 * @param aktStart
	 *            starttime of chunk
	 * @param aktEnd
	 *            endtime of chunk
	 * @param cacheSector
	 *            cache sector to reset db semaphor
	 */
	public UtilDataCacheThread(String serviceURL, String serviceName, String interfaceName, String direction, long aktStart, long aktEnd, DJLCache cache)
	{
		this.serviceURL = serviceURL;
		this.serviceName = serviceName;
		this.interfaceName = interfaceName;
		this.direction = direction;
		this.aktStart = aktStart;
		this.aktEnd = aktEnd;
		this.db = DatabaseUtil.getInstance();
		this.cache = cache;
	}

	/**
	 * This thread requests all data for this chunk from the server and inserts
	 * it into the database.
	 */
	public void run()
	{
		System.out.println("Update UtilData Cache" + serviceName + " " + interfaceName + " " + aktStart + "-" + aktEnd + " " + DataAccess.convertDateToString(System.currentTimeMillis()));
		IServerRequest request = new PerfsonarRequest();
		// List for fast insertion into database
		List<DBObject> ll = new LinkedList<DBObject>();

		BasicDBObject query = new BasicDBObject(DataCacheDB.SERVICENAME_COL, serviceName);
		query.append(DataCacheDB.SRCINTERFACE_COL, interfaceName);
		query.append(DataCacheDB.STARTTIME_COL, aktStart);
		
		// Request data from server
		try
		{
			List<UtilizationData> list = request.getUtilizationDataMDB(serviceURL, interfaceName, direction, aktStart,aktEnd);
			Iterator<UtilizationData> it = list.iterator();

			// Convert data into database format
			while (it.hasNext())
			{
				UtilizationData dt = it.next();
				UtilizationDataDB dtdb = new UtilizationDataDB(dt.getTime(), Double.valueOf(dt.getValue()), direction, interfaceName, serviceName);
				DBObject dbObj = dtdb.toDBObject();
				ll.add(dbObj);
			}
			// Insert data into database
			db.insert(DataAccess.DATA_COLLECTION, ll);

			// Set the updating in cache to false		
			cache.DataLock.lock();
			try
			{
				db.update(UtilCache.DATACACHE_COLLECTION, query, new BasicDBObject("$set", new BasicDBObject(DataCacheDB.UPDATING_COL, false)));
			}
			finally
			{
				cache.DataLock.unlock();
			}
		}
		catch (FetchFailException e)
		{
			e.printStackTrace();

			cache.DataLock.lock();
			try
			{
				db.remove(DJLCache.DATACACHE_COLLECTION, query);
			}
			finally
			{
				cache.DataLock.unlock();
			}

			throw new FetchNothingException("fail fetch UtilData: " + serviceName + " " + interfaceName + " " + aktStart + "-" + aktEnd);

		}	
		System.out.println("Ende Update UtilData Cache" + serviceName + " " + interfaceName + " " + aktStart + "-" + aktEnd + " " + DataAccess.convertDateToString(System.currentTimeMillis()));

	}
}
