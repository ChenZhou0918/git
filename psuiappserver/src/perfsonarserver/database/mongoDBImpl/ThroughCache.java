package perfsonarserver.database.mongoDBImpl;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import perfsonarserver.database.mongoDB_DatabaseTO.ThroughputInterfaceDB;
import perfsonarserver.database.mongoDB_DatabaseTO.ThroughputServiceDB;
import perfsonarserver.database.mongoDB_cacheThreads.*;
import perfsonarserver.fetchData.IServerRequest;
import perfsonarserver.fetchData.PerfsonarRequest;
import perfsonarserver.fetchData.exception.FetchFailException;
import perfsonarserver.fetchData.transferObjects.ThroughputInterfacePair;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class ThroughCache extends DJLCache
{

	public ThroughCache(int cacheSectorSizeHours, int cacheDeleteDays)
	{
		super();
		db = DatabaseThrough.getInstance();
		cacheName="Throughput";
		
		this.cacheSectorSizeHours = cacheSectorSizeHours;
		this.cacheDeleteDays = cacheDeleteDays;
		this.cacheSectorSizeMillis = this.cacheSectorSizeHours * 60 * 60 * 1000;
		this.cacheDeleteMillis = this.cacheDeleteDays * 24 * 60 * 60 * 1000;
		
		System.out.println("ThroughCache is initialized with sectorsize: " + cacheSectorSizeHours + " and deletedays: " + cacheDeleteDays);
	}
	
	@Override
	protected void updateDataCache (ExecutorService exec, String serviceName, String serviceURL, String srcInterface, String destInterface, long aktStart, long aktEnd, DJLCache sel)
	{
		String MID="";
		BasicDBObject query = new BasicDBObject(ThroughputInterfaceDB.SERVICENAME_COL,serviceName);
		query.append(ThroughputInterfaceDB.DESTINTERFACE_COL, destInterface);
		query.append(ThroughputInterfaceDB.SRCINTERFACE_COL, srcInterface);
		
		MID = (String) db.findOne(DataAccess.INTERFACE_COLLECTION,query).get(ThroughputInterfaceDB.MID_COL);
		exec.execute(new ThroughputDataCacheThread(serviceName, serviceURL, srcInterface, destInterface, MID, aktStart, aktEnd, sel));
	}
	
	@Override
	protected void doUpdateInterfaceCache(String serviceName, long aktTime) throws FetchFailException
	{
		IServerRequest request = new PerfsonarRequest();
		String serviceURL = this.getServiceURL(serviceName);
		BasicDBObject query = new BasicDBObject(ThroughputInterfaceDB.SERVICENAME_COL, serviceName);
		
		List<ThroughputInterfacePair> list = request.getThroughputInterfacePairs(serviceURL, aktTime - (24 * 60 * 60 * 1000), aktTime);
		// convert all data into DBObjects
		List<DBObject> ll = new LinkedList<DBObject>();
		for (ThroughputInterfacePair temp : list)
		{
			ThroughputInterfaceDB dbObj = new ThroughputInterfaceDB(serviceName, temp.getSrcAddress(), temp.getDestAddress(), temp.getMid());
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
	
	@Override
	protected List<DBObject> doUpdateServiceCache() throws FetchFailException
	{
		IServerRequest request = new PerfsonarRequest();
		Map<String, String> servicePairs = request.getThroughputServices();

		// convert all services into DBObjects
		List<DBObject> ll = new LinkedList<DBObject>();
		for (String str : servicePairs.keySet())
		{
			DBObject dbObj = new ThroughputServiceDB(str, servicePairs.get(str)).toDBObject();
			ll.add(dbObj);
		}

		return ll;
	}
	
	
}
