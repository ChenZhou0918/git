package perfsonarserver.database.mongoDBImpl;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import perfsonarserver.database.mongoDB_DatabaseTO.DelayJitterLossInterfaceDB;
import perfsonarserver.database.mongoDB_DatabaseTO.DelayJitterLossServiceDB;
import perfsonarserver.database.mongoDB_cacheThreads.*;
import perfsonarserver.database.mongoDB_cacheTO.*;
import perfsonarserver.database.mongoDB_DatabaseTO.*;
import perfsonarserver.database.exception.FetchNothingException;
import perfsonarserver.database.exception.FindNothingException;
import perfsonarserver.fetchData.IServerRequest;
import perfsonarserver.fetchData.PerfsonarRequest;
import perfsonarserver.fetchData.exception.FetchFailException;
import perfsonarserver.fetchData.transferObjects.DelayJitterLossInterfacePair;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * Main class for handling cache with chunks
 * 
 * @author Benjamin Konrad, Sascha Degener, wwd
 * 
 */
public class DJLCache
{
	/** constant for collection name "DelayJitterLossDataCache" inside database */
	public static final String DATACACHE_COLLECTION = "DataCache";
	/**
	 * constant for collection name "DelayJitterLossInterfaceCache" inside
	 * database
	 */
	public static final String INTERFACECACHE_COLLECTION = "InterfaceCache";
	/**
	 * constant for collection name "DelayJitterLossServiceCache" inside
	 * database
	 */
	public static final String SERVICECACHE_COLLECTION = "ServiceCache";

	/** sector size hours inside cache */
	protected int cacheSectorSizeHours;
	/** sector size milliseconds */
	protected long cacheSectorSizeMillis;
	/** data that has not being accessed for this time will be deleted */
	protected int cacheDeleteDays;
	/** if no access data will be dropped after this time */
	protected long cacheDeleteMillis;

	/** instance of database */
	protected DatabaseDriver db;

	/**
	 * name of the cache
	 */
	protected String cacheName;

	/** Lock for the DJL data cache collection */
	public Lock DataLock = new ReentrantLock();
	/** Lock for the DJL interface cache collection */
	public Lock InterfaceLock = new ReentrantLock();
	/** Lock for the DJL Service cache collection */
	public Lock ServiceLock = new ReentrantLock();

	/**
	 * the default constructor
	 */
	public DJLCache()
	{
	}

	/**
	 * Constructor
	 * 
	 * @param cacheMaxDays
	 *            maximum days to be precached
	 * @param cacheSectorSizeHours
	 *            size of a cache sector (hours) 1-24
	 * @param cacheDeleteDays
	 *            days until cache sector should be dropped (no access)
	 */
	public DJLCache(int cacheSectorSizeHours, int cacheDeleteDays)
	{
		db = DatabaseDJL.getInstance();
		cacheName = "DJL";

		this.cacheSectorSizeHours  = cacheSectorSizeHours;
		this.cacheDeleteDays       = cacheDeleteDays;
		this.cacheSectorSizeMillis = this.cacheSectorSizeHours * 60 * 60 * 1000;
		this.cacheDeleteMillis     = this.cacheDeleteDays * 24 * 60 * 60 * 1000;

		System.out.println("DJLCache is initialized with sectorsize: " + cacheSectorSizeHours + " and deletedays: " + cacheDeleteDays);
	}

	/**
	 * Locks chunk -> only one thread can write to this sector -> mutex
	 * 
	 * @param search
	 *            which sector to lock?
	 * @param cacheSector
	 *            this will be set if not locked
	 * @param updateCache
	 *            should cache being updated?
	 * @return cacheObject
	 */
	private DBObject searchDataCache(BasicDBObject search, DataCacheDB cacheSector, boolean updateCache, long lastUsed)
	{
		DataLock.lock();
		try
		{
			DBObject found = db.findOne(DATACACHE_COLLECTION, search);
			if (found == null && updateCache)
			{
				db.insert(DATACACHE_COLLECTION, cacheSector.toDBObject());
			}
			else if (found != null)
			{
				db.update(DATACACHE_COLLECTION, search, new BasicDBObject("$set", new BasicDBObject(DataCacheDB.LASTUSED_COL, lastUsed)));
			}
			return found;
		}
		finally
		{
			DataLock.unlock();
		}
	}

	/**
	 * Checks whether chunks are precached or not, insert data into database if
	 * not cached and "updateCache" = true
	 * 
	 * @param serviceName
	 *            service name
	 * @param start
	 *            start time in milliseconds
	 * @param end
	 *            end time in milliseconds
	 * @param srcInterface
	 *            source interface
	 * @param destInterface
	 *            destination interface
	 * @param updateCache
	 *            request data if not cached?
	 * @return all chunks are precached?
	 */
	public void checkDataCache(String serviceName, long start, long end, String srcInterface, String destInterface, boolean updateCache)
	{
		System.out.println(cacheName + "Check Data Cache: " + serviceName + " " + srcInterface + "-" + destInterface + " " + start + "-" + end + " " + DataAccess.convertDateToString(System.currentTimeMillis()));
		GregorianCalendar grec = new GregorianCalendar();
		long lastUsed = grec.getTimeInMillis();

		long aktStart = calcCacheChunkStart(start);
		end = calcCacheChunkEnd(end);
		long aktEnd = aktStart + cacheSectorSizeMillis - 1;

		ExecutorService exec = Executors.newCachedThreadPool();
		String serviceURL = getServiceURL(serviceName);

		while (aktEnd <= end)
		{
			// build database search object
			BasicDBObject search = new BasicDBObject().append(DataCacheDB.STARTTIME_COL, aktStart);
			search.append(DataCacheDB.SERVICENAME_COL, serviceName);
			search.append(DataCacheDB.SRCINTERFACE_COL, srcInterface);
			search.append(DataCacheDB.DESTINTERFACE_COL, destInterface);

			// build new cache sector
			DataCacheDB cacheSector = new DataCacheDB(serviceName, srcInterface, destInterface, aktStart, lastUsed, true);

			// search for cache data and lock if none found
			DBObject found = searchDataCache(search, cacheSector, updateCache, lastUsed);
			if (found == null)
			{
				// no data inside cache
				if (updateCache)
					// get data from server and insert into database
					updateDataCache(exec, serviceName, serviceURL, srcInterface, destInterface, aktStart, aktEnd, this);
				else
					throw new FindNothingException(cacheName + "Data is not fetched, and you dont want it: " + serviceName + " " + srcInterface + "-" + destInterface + " " + start + "-" + end);
			}
			else
			{
				// Data inside cache
				if (updateCache)
					
				{
					// wait until other thread finished insertig new data
					int tryTimes = 0;
					while (DataCacheDB.toClass(found).isUpdating())
					{
						try
						{
							// active waiting could be improved by observer
							// pattern
							Thread.sleep(1000);
						}
						catch (InterruptedException e)
						{
							e.printStackTrace();
						}

						found = db.findOne(DATACACHE_COLLECTION, search);
						if (found == null)
							throw new FetchNothingException(cacheName + "fail in waiting other fetch data, the cach sector is deleted: " + serviceName + " " + srcInterface + "-" + destInterface + " " + start + "-" + end);

						tryTimes++;
						if (tryTimes > 15)
							throw new FetchNothingException(cacheName + "fail in waiting other fetch data, max wait time reached: " + serviceName + " " + srcInterface + "-" + destInterface + " " + start + "-" + end);
					}

				}
				else
				{
					if (DataCacheDB.toClass(found).isUpdating())
					{
						// chunk is being updated but no updated wanted -> too
						// many time to wait -> not cached
						throw new FindNothingException(cacheName + "Data is updating, but you dont want it: " + serviceName + " " + srcInterface + "-" + destInterface + " " + start + "-" + end);
					}
				}
			}
			aktStart += cacheSectorSizeMillis;
			aktEnd += cacheSectorSizeMillis;
		}

		// wait until all threads ended
		exec.shutdown();
		try
		{
			exec.awaitTermination(1, TimeUnit.MINUTES);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}

		System.out.println(cacheName + "Ende Check Data Cache: " + serviceName + " " + srcInterface + "-" + destInterface + " " + start + "-" + end + " " + DataAccess.convertDateToString(System.currentTimeMillis()));
	}

	protected void updateDataCache(ExecutorService exec, String serviceName, String serviceURL, String srcInterface, String destInterface, long aktStart, long aktEnd, DJLCache self)
	{
		exec.execute(new DelayJitterLossDataCacheThread(serviceName, serviceURL, srcInterface, destInterface, aktStart, aktEnd, this));
	}

	private DBObject searchInterfaceCache(DBObject query, InterfaceCacheDB newItem)
	{
		InterfaceLock.lock();
		try
		{
			DBObject findOne = db.findOne(INTERFACECACHE_COLLECTION, query);

			if (findOne == null)
				db.upsert(INTERFACECACHE_COLLECTION, query, newItem.toDBObject());

			return findOne;
		}
		finally
		{
			InterfaceLock.unlock();
		}
	}

	/**
	 * Checks Interface cache and uses method to update (ONLY if no data
	 * available or data too old!) must be improved to chunks too because server
	 * only gives interface lists of time intervals)
	 * 
	 * @param serviceName
	 *            service name
	 */
	public void checkInterfaceCache(String serviceName)
	{
		System.out.println(cacheName + "Check Interface Cache" + serviceName + " " + DataAccess.convertDateToString(System.currentTimeMillis()));

		GregorianCalendar grec = new GregorianCalendar();
		long aktTime = grec.getTimeInMillis();
//		long aktTime = System.currentTimeMillis();

		BasicDBObject query = new BasicDBObject(InterfaceCacheDB.SERVICENAME_COL, serviceName);
		query.append(InterfaceCacheDB.DELETEAT_COL, new BasicDBObject("$gt", aktTime));

		InterfaceCacheDB newItem = new InterfaceCacheDB(serviceName, aktTime + cacheDeleteMillis, true);

		DBObject findOne = searchInterfaceCache(query, newItem);

		if (findOne == null)
		{	
			System.out.println("leer");
			// the data is either not cached or empty
			// update data
			updateInterfaceCache(serviceName, aktTime);
		}
		else
		{
			int tryTimes = 0;
			while (InterfaceCacheDB.toClass(findOne).getUpdating())
			{
				
				if(tryTimes>15) updateInterfaceCache(serviceName, aktTime);
				System.out.println("nicht leer");
				// the data is cached but is updating
				// waiting the update complete
				try
				{
					Thread.sleep(1000);
					
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}

				findOne = db.findOne(INTERFACECACHE_COLLECTION, query);
				if (findOne == null)
					throw new FetchNothingException(cacheName + "fail in waiting other fetch Interface, the cach sector is deleted: " + serviceName);

				tryTimes++;
				if (tryTimes > 20)
					throw new FetchNothingException(cacheName + "fail in waiting other fetch Interface, max wait time reached: " + serviceName);

			}
		}

		System.out.println(cacheName + "Ende Check Interface Cache: " + serviceName + " " + DataAccess.convertDateToString(System.currentTimeMillis()));
	}

	/**
	 * Drops all DelayJitterLoss Interfaces and gets the new interfaces (between
	 * (now - 1h) and now) Should be improved by a not that static thing.
	 * eventually we can get all interfaces from server?
	 * 
	 * @param serviceName
	 *            service name
	 * @param aktTime
	 *            now in milliseconds
	 */
	private void updateInterfaceCache(String serviceName, long aktTime)
	{
		System.out.println(cacheName + "Update Interface Cache: " + serviceName + " " + DataAccess.convertDateToString(System.currentTimeMillis()));

		BasicDBObject cacheQuery = new BasicDBObject(InterfaceCacheDB.SERVICENAME_COL, serviceName);

		// get all interfaces between (now - 1h) and now
		try
		{
			doUpdateInterfaceCache(serviceName, aktTime);

			// change the updating status
			InterfaceLock.lock();
			try
			{
				db.update(INTERFACECACHE_COLLECTION, cacheQuery, new BasicDBObject("$set", new BasicDBObject(InterfaceCacheDB.UPDATING_COL, false)));
			}
			finally
			{
				InterfaceLock.unlock();
			}
		}
		catch (FetchFailException e)
		{
			e.printStackTrace();
			InterfaceLock.lock();
			try
			{
				db.remove(INTERFACECACHE_COLLECTION, cacheQuery);
			}
			finally
			{
				InterfaceLock.unlock();
			}

			throw new FetchNothingException(cacheName + "fail fetch interface :" + serviceName);
		}

		System.out.println(cacheName + "Ende Update Interface Cache: " + serviceName + " " + DataAccess.convertDateToString(System.currentTimeMillis()));

	}
	
	protected void doUpdateInterfaceCache(String serviceName, long aktTime) throws FetchFailException
	{
		IServerRequest request = new PerfsonarRequest();
		String serviceURL = this.getServiceURL(serviceName);
		BasicDBObject query = new BasicDBObject(DelayJitterLossInterfaceDB.SERVICENAME_COL, serviceName);
		
		List<DelayJitterLossInterfacePair> list = request.getDelayJitterLossInterfacePairs(serviceURL, aktTime - (1 * 60 * 60 * 1000), aktTime);
		// convert all data into DBObjects
		List<DBObject> ll = new LinkedList<DBObject>();
		for (DelayJitterLossInterfacePair temp : list)
		{
			DelayJitterLossInterfaceDB dbObj = new DelayJitterLossInterfaceDB(serviceName, temp.getSource(), temp.getDestination());
			ll.add(dbObj.toDBObject());
		}

		if (db.findOne(DataAccess.INTERFACE_COLLECTION, query) == null)
		{
			// just insert
			db.insert(DataAccess.INTERFACE_COLLECTION, ll);
		}
		else
		{
			// first remove, than insert
			db.remove(DataAccess.INTERFACE_COLLECTION, query);
			db.insert(DataAccess.INTERFACE_COLLECTION, ll);
		}
	}

	/**
	 * Gets all services from server (by now text file) if no services inside
	 * database or data too old.
	 */
	public void checkServiceCache()
	{
		System.out.println(cacheName + "Check Service Cache " + DataAccess.convertDateToString(System.currentTimeMillis()));
		GregorianCalendar grec = new GregorianCalendar();
		long aktTime = grec.getTimeInMillis();

		ServiceLock.lock();
		try
		{
			DBObject findOne = db.findOne(SERVICECACHE_COLLECTION);
			if (findOne == null)
			{
				// if no services inside db -> get services
				updateServiceCache(aktTime);
			}
			else
			{
				if (ServiceCacheDB.toClass(findOne).getDeleteAt() < aktTime)
				{
					// if services are too old -> get services
					updateServiceCache(aktTime);
				}
			}
		}
		finally
		{
			ServiceLock.unlock();
		}
		System.out.println(cacheName + "Ende Check Service Cache " + DataAccess.convertDateToString(System.currentTimeMillis()));
	}

	/**
	 * Drops all services and inserts the new
	 * 
	 * @param aktTime
	 *            actual time in milliseconds
	 */
	private void updateServiceCache(long aktTime)
	{
		System.out.println(cacheName + "Update Service Cache " + DataAccess.convertDateToString(System.currentTimeMillis()));
		// drop old data
		db.dropCollection(SERVICECACHE_COLLECTION);
		db.dropCollection(DataAccess.SERVICE_COLLECTION);

		List<DBObject> ll = new LinkedList<DBObject>();
		try
		{
			ll = doUpdateServiceCache();
		}
		catch (FetchFailException e)
		{
			e.printStackTrace();
			throw new FetchNothingException("Fail to fetch the DJLService");
		}

		// insert services into database
		ServiceCacheDB temp = new ServiceCacheDB(aktTime + cacheDeleteMillis);
		db.insert(DataAccess.SERVICE_COLLECTION, ll);
		db.insert(SERVICECACHE_COLLECTION, temp.toDBObject());
		System.out.println(cacheName + "Ende Update Service Cache " + DataAccess.convertDateToString(System.currentTimeMillis()));
	}

	protected List<DBObject> doUpdateServiceCache() throws FetchFailException
	{
		IServerRequest request = new PerfsonarRequest();
		Map<String, String> servicePairs = request.getDelayJitterLossServices();

		// convert all services into DBObjects
		List<DBObject> ll = new LinkedList<DBObject>();
		for (String str : servicePairs.keySet())
		{
			DBObject dbObj = new DelayJitterLossServiceDB(str, servicePairs.get(str)).toDBObject();
			ll.add(dbObj);
		}

		return ll;
	}

	/**
	 * Delete the outdated DJLData Delete only one chunk each time, should be
	 * used in a iterative fashion Thread safe, the remove of the cache and data
	 * is atomic
	 * 
	 * @return true if there is outdated data need to be deleted
	 */
	public boolean deleteOldData()
	{
		System.out.println(cacheName + "Delete outdated Data " + DataAccess.convertDateToString(System.currentTimeMillis()));
		BasicDBObject query = new BasicDBObject();
		query.append(DataCacheDB.LASTUSED_COL, new BasicDBObject("$lte", (System.currentTimeMillis() - cacheDeleteMillis)));

		// delete only one cache chunk every time
		// the deletion of the data and cache is atomic
		DBObject result = new BasicDBObject();
		DataCacheDB resultClass = new DataCacheDB();

		DataLock.lock();
		try
		{
			result = db.findOne(DATACACHE_COLLECTION, query);
			if (result == null)
			{
				System.out.println(cacheName + "Ende delete outdated Data, no data is outdated");
				return false;
			}

			// remove the records from the cache
			db.remove(DATACACHE_COLLECTION, query);

			// remove the records from the data
			// build the query
			resultClass = DataCacheDB.toClass(result);
			BasicDBObject chunkQuery = new BasicDBObject(DelayJitterLossDataDB.SERVICE_COL, resultClass.getServiceName());
			chunkQuery.append(DelayJitterLossDataDB.SRCINTERFACE_COL, resultClass.getSrcInterface());
			chunkQuery.append(DelayJitterLossDataDB.DESTINTERFACE_COL, resultClass.getDestInterface());
			chunkQuery.append(DelayJitterLossDataDB.TIMESTAMP_COL, new BasicDBObject("$gte", resultClass.getStart()).append("$lte", resultClass.getStart() + cacheSectorSizeMillis));

			// for the debugg use, show how many records is deleted
			System.out.println(db.find(DataAccess.DATA_COLLECTION, chunkQuery).size() + " DJLData is deleted");
			// remove
			db.remove(DataAccess.DATA_COLLECTION, chunkQuery);
		}
		finally
		{
			DataLock.unlock();
		}

		System.out.println(cacheName + "Ende delete outdated Data, delete chunk: " + resultClass + " " + DataAccess.convertDateToString(System.currentTimeMillis()));
		return true;
	}

	/**
	 * Searches the url (inside db) to a given service
	 * 
	 * @param serviceName
	 *            service name
	 * @return service url
	 */
	public String getServiceURL(String serviceName)
	{

		BasicDBObject search = new BasicDBObject();
		{
			search.append(DelayJitterLossServiceDB.SERVICENAME_COL, serviceName);
		}

		DBObject lDBObj = db.findOne(DataAccess.SERVICE_COLLECTION, search);
		serviceName = DelayJitterLossServiceDB.toClassDB(lDBObj).getServiceURL();
		return serviceName;
	}

	/**
	 * Calculating the beginning of the first chunk
	 * 
	 * @param start
	 *            start of requested data (milliseconds)
	 * @return beginning of first chunk (milliseconds)
	 */
	private long calcCacheChunkStart(long start)
	{
		GregorianCalendar grec = new GregorianCalendar();
		grec.clear();
		grec.setTimeInMillis(start);
		int hour = 24;
		// the beginning of all chunk must be less or equal to the beginning
		// hour
		while (hour > grec.get(Calendar.HOUR_OF_DAY))
		{
			hour -= cacheSectorSizeHours;
		}
		grec.set(grec.get(Calendar.YEAR), grec.get(Calendar.MONTH), grec.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		// Year month and day should not have been changed
		// chunk length between 1 and 24 hours only integral numbers. No minutes
		// and no seconds
		long aktStart = grec.getTimeInMillis() + hour * 60 * 60 * 1000;
		return aktStart;
	}

	/**
	 * Calculating the end of the last chunk for this request
	 * 
	 * @param end
	 *            end of requested data (milliseconds)
	 * @return end of last chunk (milliseconds)
	 */
	private long calcCacheChunkEnd(long end)
	{
		GregorianCalendar grec = new GregorianCalendar();
		grec.clear();
		grec.setTimeInMillis(end);
		int hour = 0;
		// the end of all chunks must be greater or equal to the end hour
		while (hour < grec.get(Calendar.HOUR_OF_DAY))
		{
			hour += cacheSectorSizeHours;
		}
		grec.set(grec.get(Calendar.YEAR), grec.get(Calendar.MONTH), grec.get(Calendar.DAY_OF_MONTH), 0, 59, 59);
		// Year month and day should not have been changed
		// chunk length between 1 and 24 hours only integral numbers. And
		// maximum minutes and seconds.
		end = grec.getTimeInMillis() + hour * 60 * 60 * 1000 + 999;
		if (end > System.currentTimeMillis())
		{
			// we cant cache the future ;)
			end -= cacheSectorSizeMillis;
		}
		return end;
	}
}