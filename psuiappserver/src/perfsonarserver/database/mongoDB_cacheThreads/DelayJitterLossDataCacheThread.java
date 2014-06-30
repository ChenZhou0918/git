package perfsonarserver.database.mongoDB_cacheThreads;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import perfsonarserver.database.mongoDBImpl.*;
import perfsonarserver.database.mongoDB_cacheTO.DataCacheDB;
import perfsonarserver.database.mongoDB_DatabaseTO.DelayJitterLossDataDB;
import perfsonarserver.database.exception.FetchNothingException;
import perfsonarserver.fetchDataAndProcess.IServerRequest;
import perfsonarserver.fetchDataAndProcess.PerfsonarRequest;
import perfsonarserver.fetchDataAndProcess.exception.FetchDJLDataException;
import perfsonarserver.fetchDataAndProcess.exception.FetchFailException;
import perfsonarserver.fetchDataAndProcess.transferObjects.DelayJitterLossData;

/**
 * Requests DelayJitterLoss data from server and inserts them into database.
 * 
 * @author Benjamin Konrad, Sascha Degener
 */
public class DelayJitterLossDataCacheThread extends Thread
{
	/** instance of database */
	private DatabaseDJL db;
	/** service name */
	private String serviceName;
	/** service url */
	private String serviceURL;
	/** source interface */
	private String srcInterface;
	/** destination interface */
	private String destInterface;
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
	public DelayJitterLossDataCacheThread(String serviceName, String serviceURL, String srcInterface, String destInterface, long aktStart, long aktEnd, DJLCache cache)
	{
		this.serviceURL = serviceURL;
		this.serviceName = serviceName;
		this.srcInterface = srcInterface;
		this.destInterface = destInterface;
		this.aktStart = aktStart;
		this.aktEnd = aktEnd;
		this.db = DatabaseDJL.getInstance();
		this.cache = cache;
	}

	/**
	 * This thread requests all data for this chunk from the server and inserts
	 * it into the database.
	 */
	public void run()
	{
		System.out.println("Update DJLData Cache"+ serviceName + " " + srcInterface + "-" + destInterface + " " + aktStart + "-" + aktEnd + " " + DataAccess.convertDateToString(System.currentTimeMillis()));
		IServerRequest request = new PerfsonarRequest();
		// List for fast insertion into database
		List<DBObject> ll = new LinkedList<DBObject>();

		BasicDBObject query = new BasicDBObject(DataCacheDB.SERVICENAME_COL, serviceName);
		query.append(DataCacheDB.SRCINTERFACE_COL, srcInterface);
		query.append(DataCacheDB.DESTINTERFACE_COL, destInterface);
		query.append(DataCacheDB.STARTTIME_COL, aktStart);
		
		List<DelayJitterLossData> list;
		try {
			list = request.getDelayJitterLossData(serviceURL, srcInterface, destInterface, aktStart, aktEnd);
			Iterator<DelayJitterLossData> it = list.iterator();
	

		// Convert data into database format
		while (it.hasNext())
		{
			DelayJitterLossData djld = it.next();
			DelayJitterLossDataDB djlddb = new DelayJitterLossDataDB(djld.getTime(), djld.getMaxDelay(),djld.getMedDelay(), djld.getMinDelay(), djld.getMaxIpdvJitter(),djld.getMedIpdvJitter(),djld.getMinIpdvJitter(), djld.getLoss(), serviceName, srcInterface, destInterface);
			DBObject dbObj = djlddb.toDBObject();
			ll.add(dbObj);
		}
		// Insert data into database
		db.insert(DataAccess.DATA_COLLECTION, ll);

		
		} catch (FetchDJLDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Set the updating in cache to false		
		cache.DataLock.lock();	
		try
		{
			db.update(DJLCache.DATACACHE_COLLECTION, query, new BasicDBObject("$set", new BasicDBObject(DataCacheDB.UPDATING_COL, false)));
		}
		finally
		{
			cache.DataLock.unlock();
		}	
		System.out.println("Ende Update DJLData Cache"+ serviceName + " " + srcInterface + "-" + destInterface + " " + aktStart + "-" + aktEnd + " " + DataAccess.convertDateToString(System.currentTimeMillis()));

	}
}
