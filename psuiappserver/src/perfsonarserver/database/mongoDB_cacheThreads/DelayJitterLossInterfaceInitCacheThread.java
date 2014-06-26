package perfsonarserver.database.mongoDB_cacheThreads;

import perfsonarserver.database.mongoDBImpl.*;
import perfsonarserver.database.exception.FetchNothingException;
import perfsonarserver.database.exception.FindNothingException;

public class DelayJitterLossInterfaceInitCacheThread extends Thread
{
	private String serviceName;
	private DJLCache cache;
	
	
	public DelayJitterLossInterfaceInitCacheThread(String serviceName, DJLCache cache)
	{
		this.serviceName = serviceName;
		this.cache = cache;
	}
	
	
	public void run()
	{
		try
		{
			System.out.println("Init DJLInterface: " + serviceName + DataAccess.convertDateToString(System.currentTimeMillis()));
			cache.checkInterfaceCache(serviceName);
			System.out.println("Ende Init DJLInterface: " + serviceName + DataAccess.convertDateToString(System.currentTimeMillis()));
		}
		catch (FindNothingException | FetchNothingException e)
		{
			e.printStackTrace();
		}
	}
}
