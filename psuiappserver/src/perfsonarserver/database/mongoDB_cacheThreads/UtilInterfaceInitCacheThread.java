package perfsonarserver.database.mongoDB_cacheThreads;

import perfsonarserver.database.mongoDBImpl.*;
import perfsonarserver.database.exception.FetchNothingException;
import perfsonarserver.database.exception.FindNothingException;

public class UtilInterfaceInitCacheThread extends Thread
{
	private String serviceName;
	private UtilCache cache;
	
	public UtilInterfaceInitCacheThread(String serviceName, UtilCache cache)
	{
		this.serviceName = serviceName;
		this.cache = cache;
	}
	
	public void run()
	{
		try
		{
			System.out.println("Init UtilizationInterface: " + serviceName + DataAccess.convertDateToString(System.currentTimeMillis()));
			cache.checkInterfaceCache(serviceName);
			System.out.println("Ende Init UtilizationInterface: " + serviceName + DataAccess.convertDateToString(System.currentTimeMillis()));
		}
		catch (FindNothingException | FetchNothingException e)
		{
			e.printStackTrace();
		}
	}
}
