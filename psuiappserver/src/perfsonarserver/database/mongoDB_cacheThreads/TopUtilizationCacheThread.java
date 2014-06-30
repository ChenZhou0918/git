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



/**
 * thread to update top Util data
 * 
 * @author Zhou Chen
 * 
 */
public class TopUtilizationCacheThread implements Runnable{
	/** instance of database */
	private DatabaseUtil db;
	/** service url */
	private String serviceURL;
	private String serviceName;
	/** starttime of chunk */
	private long aktStart;
	/** endtime of chunk */
	private long aktEnd;
	/** cache */
	private UtilCache cache;

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
	public TopUtilizationCacheThread(String serviceURL, String serviceName, long aktStart, long aktEnd)
	{
		this.serviceURL = serviceURL;
		this.serviceName = serviceName;
		this.aktStart = aktStart;
		this.aktEnd = aktEnd;
		this.db = DatabaseUtil.getInstance();
	}

	/**
	 * This thread requests all data for this chunk from the server and inserts
	 * it into the database.
	 */
	public void run()
	{
		System.out.println("Update UtilData Cache" + serviceName + " "  + " " + aktStart + "-" + aktEnd + " " + DataAccess.convertDateToString(System.currentTimeMillis()));
		IServerRequest request = new PerfsonarRequest();
		// List for fast insertion into database
		List<DBObject> ll = new LinkedList<DBObject>();

		
		// Request data from server
		try
		{
			List<UtilizationData> list = request.getTopFiveUtilization(serviceURL,aktStart,aktEnd);
			System.out.println("Top 5 List      "+list);
			Iterator<UtilizationData> it = list.iterator();
			
			// Convert data into database format
			while (it.hasNext())
			{
				UtilizationData dt = it.next();
				UtilizationDataDB dtdb = new UtilizationDataDB(dt.getTime(),dt.getUtilPercentage(), dt.getdirection(), dt.getInterface(), serviceName,
						dt.HostName(),dt.getDescription(),dt.getCapacity());
				DBObject dbObj = dtdb.toDBObject();
				ll.add(dbObj);
			}
			// Insert data into database
			System.out.println("inserted List      "+ll);
			
			BasicDBObject search=new BasicDBObject(UtilizationDataDB.SERVICENAME_COL,serviceName);
			
			//remove old top five data
			List<DBObject> searchList=db.find(DataAccess.TOPDATA_COLLECTION, search);
			System.out.println("search list size:  "+searchList.size());
			
			for(DBObject currentObject:searchList)
			{db.remove(DataAccess.TOPDATA_COLLECTION,currentObject);}
			
			for(DBObject dbob:ll)
			{
			db.insert(DataAccess.TOPDATA_COLLECTION,dbob);
			}
		}
		catch (FetchFailException e)
		{
			e.printStackTrace();


			throw new FetchNothingException("fail fetch TopUtilData: " + serviceName + " "  + " " + aktStart + "-" + aktEnd);

		}	
		System.out.println("Ende Update TopUtilData Cache" + serviceName + " " + " " + aktStart + "-" + aktEnd + " " + DataAccess.convertDateToString(System.currentTimeMillis()));

	}
}
