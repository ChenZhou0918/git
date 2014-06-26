package perfsonarserver.database.mongoDB_cacheThreads;

import perfsonarserver.database.mongoDBImpl.*;
import perfsonarserver.database.exception.FetchNothingException;
import perfsonarserver.database.exception.FindNothingException;

public class ThroughputInterfaceInitCacheThread extends Thread
{
	private String serviceName;
	private ThroughCache cache;
	
	public ThroughputInterfaceInitCacheThread(String serviceName, ThroughCache cache)
	{
		this.serviceName = serviceName;
		this.cache = cache;
	}
	
	public void run()
	{
		try
		{
			System.out.println("Init ThroughputInterface: " + serviceName + DataAccess.convertDateToString(System.currentTimeMillis()));
			cache.checkInterfaceCache(serviceName);
			System.out.println("Ende Init ThroughputInterface: " + serviceName + DataAccess.convertDateToString(System.currentTimeMillis()));
		}
		catch (FindNothingException | FetchNothingException e)
		{
			e.printStackTrace();
		}
	}
}
