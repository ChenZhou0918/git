package perfsonarserver.database.mongoDB_cacheThreads;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import perfsonarserver.database.mongoDBImpl.*;
import perfsonarserver.database.mongoDB_DatabaseTO.ThroughputDataDB;
import perfsonarserver.database.mongoDB_cacheTO.*;
import perfsonarserver.database.exception.FetchNothingException;
import perfsonarserver.fetchDataAndProcess.IServerRequest;
import perfsonarserver.fetchDataAndProcess.PerfsonarRequest;
import perfsonarserver.fetchDataAndProcess.exception.FetchFailException;
import perfsonarserver.fetchDataAndProcess.transferObjects.ThroughputData;
import perfsonarserver.fetchDataAndProcess.transferObjects.ThroughputInterfacePair;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class ThroughputDataCacheThread extends Thread
{
	/** instance of database */
	private DatabaseThrough db;
	/** service name */
	private String serviceName;
	/** service url */
	private String serviceURL;
	/** source interface */
	private String srcInterface;
	/** destination interface */
	private String destInterface;
	private String MID;
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
	public ThroughputDataCacheThread(String serviceName, String serviceURL, String srcInterface, String destInterface, String MID, long aktStart, long aktEnd, DJLCache cache)
	{
		this.serviceURL = serviceURL;
		this.serviceName = serviceName;
		this.srcInterface = srcInterface;
		this.destInterface = destInterface;
		this.MID = MID;
		this.aktStart = aktStart;
		this.aktEnd = aktEnd;
		this.db = DatabaseThrough.getInstance();
		this.cache = cache;
	}

	/**
	 * This thread requests all data for this chunk from the server and inserts
	 * it into the database.
	 */
	public void run()
	{
		System.out.println("Update ThroughData Cache" + serviceName + " " + srcInterface + "-" + destInterface + " " + aktStart + "-" + aktEnd + " " + DataAccess.convertDateToString(System.currentTimeMillis()));
		IServerRequest request = new PerfsonarRequest();
		// List for fast insertion into database
		List<DBObject> ll = new LinkedList<DBObject>();

		BasicDBObject query = new BasicDBObject(DataCacheDB.SERVICENAME_COL, serviceName);
		query.append(DataCacheDB.SRCINTERFACE_COL, srcInterface);
		query.append(DataCacheDB.DESTINTERFACE_COL, destInterface);
		query.append(DataCacheDB.STARTTIME_COL, aktStart);
		
		// Request data from server
		try
		{ThroughputInterfacePair ti=new ThroughputInterfacePair();
		ti.setDestAddress(destInterface);
		ti.setSrcAddress(srcInterface);
		ti.setMid(MID);
		
			List<ThroughputData> list = request.getThroughputData(serviceURL, ti, aktStart, aktEnd);
			Iterator<ThroughputData> it = list.iterator();

			// Convert data into database format
			while (it.hasNext())
			{
				ThroughputData dt = it.next();
				ThroughputDataDB dtdb = new ThroughputDataDB(dt.getTimeValue(), dt.getInterval(), dt.getValue(), serviceName, srcInterface, destInterface);
				DBObject dbObj = dtdb.toDBObject();
				ll.add(dbObj);
			}
			// Insert data into database
			db.insert(DataAccess.DATA_COLLECTION, ll);

			// Set the updating in cache to false		
			cache.DataLock.lock();
			try
			{
				db.update(ThroughCache.DATACACHE_COLLECTION, query, new BasicDBObject("$set", new BasicDBObject(DataCacheDB.UPDATING_COL, false)));
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

			throw new FetchNothingException("fail fetch ThroughData: " + serviceName + " " + srcInterface + "-" + destInterface + " " + aktStart + "-" + aktEnd);

		}	
		System.out.println("Ende Update ThroughData Cache"+ serviceName + " " + srcInterface + "-" + destInterface + " " + aktStart + "-" + aktEnd + " " + DataAccess.convertDateToString(System.currentTimeMillis()));

	}
}

