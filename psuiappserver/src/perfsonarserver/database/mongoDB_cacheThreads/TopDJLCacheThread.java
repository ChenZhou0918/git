package perfsonarserver.database.mongoDB_cacheThreads;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

	import perfsonarserver.database.mongoDBImpl.*;
import perfsonarserver.database.mongoDB_DatabaseTO.DelayJitterLossInterfaceDB;
import perfsonarserver.database.mongoDB_cacheTO.DataCacheDB;
import perfsonarserver.database.mongoDB_DatabaseTO.DelayJitterLossDataDB;
import perfsonarserver.database.exception.FetchNothingException;
import perfsonarserver.fetchData.IServerRequest;
import perfsonarserver.fetchData.PerfsonarRequest;
import perfsonarserver.fetchData.exception.FetchFailException;
import perfsonarserver.fetchData.transferObjects.DelayJitterLossData;


import perfsonarserver.fetchData.transferObjects.DelayJitterLossInterfacePair;

	import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;


/**
 * thread to update top DJL data
 * 
 * @author Zhou Chen
 * 
 */
public class TopDJLCacheThread implements Runnable {
		/** instance of database */
		private DatabaseDJL db;
		/** service url */
		private String serviceURL;
		private String serviceName;
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
		public TopDJLCacheThread(String serviceURL, String serviceName, long aktStart, long aktEnd)
		{
			this.serviceURL = serviceURL;
			this.serviceName = serviceName;
			this.aktStart = aktStart;
			this.aktEnd = aktEnd;
			this.db = DatabaseDJL.getInstance();
			
		}

		/**
		 * This thread requests all data for this chunk from the server and inserts
		 * it into the database.
		 */
		public void run()
		{
			System.out.println("Update DJlData Cache" + serviceName + " "  + " " + aktStart + "-" + aktEnd + " " + DataAccess.convertDateToString(System.currentTimeMillis()));
			IServerRequest request = new PerfsonarRequest();
			// List for fast insertion into database
			List<DBObject> TopDelayList = new LinkedList<DBObject>();
			List<DBObject> TopJitterList = new LinkedList<DBObject>();
			List<DBObject> TopLossInterfaces=new LinkedList<DBObject>();
			
         
			// Request data from server
			/********************insert top Loss interfaces**********************/
			try
			{
			List<DelayJitterLossInterfacePair> list2;
				list2 = request.getTopLossInterfaces(serviceURL,aktStart,aktEnd);
				Iterator<DelayJitterLossInterfacePair> it2= list2.iterator();
	
			
			// Convert data into database format
			while (it2.hasNext())
			{
				DelayJitterLossInterfacePair dt = it2.next();
				DelayJitterLossInterfaceDB dtdb = new DelayJitterLossInterfaceDB(serviceName,dt.getSource(),dt.getDestination(),dt.getLossNumber());
				DBObject dbObj = dtdb.toDBObject();
				TopLossInterfaces.add(dbObj);
			}
			// Insert data into database
			System.out.println("inserted Loss Interfaces      "+TopLossInterfaces );
			
			BasicDBObject search2=new BasicDBObject(DelayJitterLossInterfaceDB.SERVICENAME_COL,serviceName);
	
			//remove old top five data
			List<DBObject> searchList2=db.find(DataAccess.TOPLOSSINTERFACES_COLLECTION, search2);
			System.out.println("searched size: "+searchList2.size());
			for(DBObject currentObject:searchList2)
			{db.remove(DataAccess.TOPLOSSINTERFACES_COLLECTION,currentObject);}
			
			db.insert(DataAccess.TOPLOSSINTERFACES_COLLECTION, TopLossInterfaces );
//			
			/***********************************insert top 5 Delay data********************************************/
		
				List<DelayJitterLossData> list = request.getTopFiveDelay(serviceURL,aktStart,aktEnd);
				Iterator<DelayJitterLossData> it = list.iterator();
				
				// Convert data into database format
				while (it.hasNext())
				{
					DelayJitterLossData dt = it.next();
					DelayJitterLossDataDB dtdb = new DelayJitterLossDataDB(dt.getTime(),dt.getMaxDelayPercentage(),dt.getMinDelay(),dt.getMaxIpdvJitter(),
							dt.getMinIpdvJitter(),dt.getLoss(),serviceName,dt.getSender(),dt.getReceiver());
					DBObject dbObj = dtdb.toDBObject();
					TopDelayList .add(dbObj);
				}
				// Insert data into database
				System.out.println("inserted delay List      "+TopDelayList );
////				
				BasicDBObject search=new BasicDBObject(DelayJitterLossDataDB.SERVICE_COL,serviceName);
				
				//remove old top five data
				List<DBObject> searchList=db.find(DataAccess.TOPDELAYDATA_COLLECTION, search);
				
				for(DBObject currentObject:searchList)
				{db.remove(DataAccess.TOPDELAYDATA_COLLECTION,currentObject);}
				
				db.insert(DataAccess.TOPDELAYDATA_COLLECTION, TopDelayList );

				
				/***********************************insert top 5 jitter data********************************************/
				
//			List<DelayJitterLossData> 
			list= request.getTopFiveJitter(serviceURL,aktStart,aktEnd);
//			Iterator<DelayJitterLossData>  
			it = list.iterator();
		
				
				// Convert data into database format
				while (it.hasNext())
				{
					DelayJitterLossData dt = it.next();
					DelayJitterLossDataDB dtdb = new DelayJitterLossDataDB(dt.getTime(),dt.getMaxDelay(),dt.getMinDelay(),dt.getMaxJitterPercentage(),
							dt.getMinIpdvJitter(),dt.getLoss(),serviceName,dt.getSender(),dt.getReceiver());
					DBObject dbObj = dtdb.toDBObject();
					 TopJitterList.add(dbObj);
					 
				}
				// Insert data into database
				System.out.println("inserted jitter List      "+ TopJitterList);
				
				
				//remove old top five data
//				List<DBObject> 
				searchList=db.find(DataAccess.TOPJITTERDATA_COLLECTION, search);
				System.out.println(searchList.size());
	
				for(DBObject currentObject:searchList)
				{db.remove(DataAccess.TOPJITTERDATA_COLLECTION,currentObject);}
				
				db.insert(DataAccess.TOPJITTERDATA_COLLECTION,  TopJitterList);
				
			
				
			}
			catch (FetchFailException e)
			{
				e.printStackTrace();


				throw new FetchNothingException("fail fetch DJLData: " + serviceName + " "  + " " + aktStart + "-" + aktEnd);

			}	
			System.out.println("Ende Update TopDJLData Cache" + serviceName + " " + " " + aktStart + "-" + aktEnd + " " + DataAccess.convertDateToString(System.currentTimeMillis()));

		}
	}


