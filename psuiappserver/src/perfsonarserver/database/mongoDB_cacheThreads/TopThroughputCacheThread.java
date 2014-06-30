package perfsonarserver.database.mongoDB_cacheThreads;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import perfsonarserver.database.exception.FetchNothingException;
import perfsonarserver.database.mongoDBImpl.DJLCache;
import perfsonarserver.database.mongoDBImpl.DataAccess;
import perfsonarserver.database.mongoDBImpl.DatabaseThrough;
import perfsonarserver.database.mongoDBImpl.ThroughCache;
import perfsonarserver.database.mongoDB_DatabaseTO.DelayJitterLossInterfaceDB;
import perfsonarserver.database.mongoDB_DatabaseTO.ThroughputDataDB;
import perfsonarserver.database.mongoDB_cacheTO.DataCacheDB;
import perfsonarserver.fetchDataAndProcess.IServerRequest;
import perfsonarserver.fetchDataAndProcess.PerfsonarRequest;
import perfsonarserver.fetchDataAndProcess.exception.FetchFailException;
import perfsonarserver.fetchDataAndProcess.transferObjects.ThroughputData;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class TopThroughputCacheThread implements Runnable
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
	public TopThroughputCacheThread(String serviceURL,String serviceName,  long aktStart, long aktEnd)
	{
		this.serviceURL = serviceURL;
		this.serviceName = serviceName;
		this.aktStart = aktStart;
		this.aktEnd = aktEnd;
		this.db = DatabaseThrough.getInstance();
	}

	public void run()
	{
		System.out.println("Update TopThroughData Cache" + serviceName + " "  + aktStart + "-" + aktEnd + " " + DataAccess.convertDateToString(System.currentTimeMillis()));
		IServerRequest request = new PerfsonarRequest();
		// List for fast insertion into database
		List<DBObject> ll = new LinkedList<DBObject>();

		
		// Request data from server
		try
		{
			List<ThroughputData> list = request.getTopFiveThroughput(serviceURL,aktStart, aktEnd);
			Iterator<ThroughputData> it = list.iterator();

			// Convert data into database format
			while (it.hasNext())
			{
				ThroughputData dt = it.next();
				ThroughputDataDB dtdb = new ThroughputDataDB(dt.getTimeValue(), dt.getInterval(), dt.getValue(), serviceName, dt.getSrcInterface(),dt.getDestInterface());
				DBObject dbObj = dtdb.toDBObject();
				ll.add(dbObj);
			}
			// Insert data into database
			
			//remove old top 5 data
			BasicDBObject search=new BasicDBObject(ThroughputDataDB.SERVICENAME_COL,serviceName);
			List<DBObject> searchList=db.find(DataAccess.TOPTHROUGHPUTDATA_COLLECTION, search);
			System.out.println("searched size: "+searchList.size());
			for(DBObject currentObject:searchList)
			{db.remove(DataAccess.TOPTHROUGHPUTDATA_COLLECTION,currentObject);}
			
			
			
			db.insert(DataAccess.TOPTHROUGHPUTDATA_COLLECTION, ll);

			
		}
		catch (FetchFailException e)
		{
			e.printStackTrace();

			throw new FetchNothingException("fail fetch TOPThroughData: " + serviceName + " " + srcInterface + "-" + destInterface + " " + aktStart + "-" + aktEnd);

		}	
		System.out.println("Ende Update TOPThroughData Cache"+ serviceName + " " + srcInterface + "-" + destInterface + " " + aktStart + "-" + aktEnd + " " + DataAccess.convertDateToString(System.currentTimeMillis()));

	}
}


