package perfsonarserver.database.mongoDBImpl;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import perfsonarserver.database.mongoDB_DatabaseTO.UtilizationInterfaceDB;
import perfsonarserver.database.mongoDB_DatabaseTO.UtilizationServiceDB;
import perfsonarserver.database.mongoDB_cacheThreads.*;
import perfsonarserver.fetchData.IServerRequest;
import perfsonarserver.fetchData.PerfsonarRequest;
import perfsonarserver.fetchData.exception.FetchFailException;
import perfsonarserver.fetchData.transferObjects.UtilizationInterface;

public class UtilCache extends DJLCache
{
	public UtilCache(int cacheSectorSizeHours, int cacheDeleteDays)
	{
		super();
		db = DatabaseUtil.getInstance();
		cacheName = "Utilization";

		this.cacheSectorSizeHours = cacheSectorSizeHours;
		this.cacheDeleteDays = cacheDeleteDays;
		this.cacheSectorSizeMillis = this.cacheSectorSizeHours * 60 * 60 * 1000;
		this.cacheDeleteMillis = this.cacheDeleteDays * 24 * 60 * 60 * 1000;

		System.out.println("UtilCache is initialized with sectorsize: " + cacheSectorSizeHours + " and deletedays: " + cacheDeleteDays);
	}

	@Override
	protected void updateDataCache(ExecutorService exec, String serviceName, String serviceURL, String srcInterface, String destInterface, long aktStart, long aktEnd, DJLCache sel)
	{
		exec.execute(new UtilDataCacheThread(serviceURL, serviceName, srcInterface, "in", aktStart, aktEnd, this));
		exec.execute(new UtilDataCacheThread(serviceURL, serviceName, srcInterface, "out", aktStart, aktEnd, this));
	}

	@Override
	protected void doUpdateInterfaceCache(String serviceName, long aktTime) throws FetchFailException
	{
		IServerRequest request = new PerfsonarRequest();
		String serviceURL = this.getServiceURL(serviceName);
		BasicDBObject query = new BasicDBObject(UtilizationInterfaceDB.SERVICENAME_COL, serviceName);

		List<UtilizationInterface> list = request.getUtilizationInterfaces(serviceURL);
		List<DBObject> ll = new LinkedList<DBObject>();
		for (UtilizationInterface temp : list)
		{
			//each interface contain two pair, one with direction in, one with direction out
			//in the interface db we only want to contain one not two
			if (temp.getDirection().equals("in"))
			{
				UtilizationInterfaceDB dbObj = new UtilizationInterfaceDB(temp.getName(), serviceName,temp.getHostname());
				ll.add(dbObj.toDBObject());
			}
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
		Map<String, String> servicePairs = request.getUtilizationServices();

		// convert all services into DBObjects
		List<DBObject> ll = new LinkedList<DBObject>();
		for (String str : servicePairs.keySet())
		{
			DBObject dbObj = new UtilizationServiceDB(str, servicePairs.get(str)).toDBObject();
			ll.add(dbObj);
		}

		return ll;
	}
}
