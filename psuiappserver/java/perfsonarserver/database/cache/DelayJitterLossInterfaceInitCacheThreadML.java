//package perfsonarserver.database.cache;
//
//import perfsonarserver.database.MarkLogicImpl.*;
//import perfsonarserver.database.cassandraImpl.DataAccessCas;
//import perfsonarserver.database.exception.FetchNothingException;
//import perfsonarserver.database.exception.FindNothingException;
//
//public class DelayJitterLossInterfaceInitCacheThreadML extends Thread
//{
//	private String serviceName;
//	private CacheDJLML cache;
//	
//	
//	public DelayJitterLossInterfaceInitCacheThreadML(String serviceName, CacheDJLML cache)
//	{
//		this.serviceName = serviceName;
//		this.cache = cache;
//	}
//	
//	
//	public void run()
//	{
//		try
//		{
//			System.out.println("Init DJLInterface: " + serviceName + DataAccess.convertDateToString(System.currentTimeMillis()));
//			//cache.checkInterfaceCache(serviceName);
//			System.out.println("Ende Init DJLInterface: " + serviceName + DataAccess.convertDateToString(System.currentTimeMillis()));
//		}
//		catch (FindNothingException | FetchNothingException e)
//		{
//			e.printStackTrace();
//		}
//	}
//}
