package perfsonarserver.database.mongoDB_cacheThreads;

import perfsonarserver.database.mongoDBImpl.*;
import perfsonarserver.database.exception.FetchNothingException;
import perfsonarserver.database.exception.FindNothingException;

public class UtilDataInitCacheThread extends Thread
{
	private String serviceName;
	private long startTime;
	private long endTime;
	private String interfaceName;
	private UtilCache cache;
	
	public UtilDataInitCacheThread(String serviceName, long startTime, long endTime, String interfaceName, UtilCache cache)
	{
		this.serviceName = serviceName;
		this.startTime = startTime;
		this.endTime = endTime;
		this.interfaceName = interfaceName;
		this.cache = cache;
	}
	
	public void run()
	{
		try
		{
			System.out.println("Init UtilizationData: " + serviceName + " " + interfaceName + " " + startTime + "-" + endTime + " " + DataAccess.convertDateToString(System.currentTimeMillis()));
			cache.checkDataCache(serviceName, startTime, endTime, interfaceName, "NOT_NEED", true);
			System.out.println("Ende Init UtilizationData: " + serviceName + " " + interfaceName + startTime + "-" + endTime + " " + DataAccess.convertDateToString(System.currentTimeMillis()));
		}
		catch (FindNothingException | FetchNothingException e)
		{
			e.printStackTrace();
		}
	}
}
