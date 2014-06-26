package perfsonarserver.database.cache;


//import perfsonarserver.database.cassandraImpl.DataAccessCas;
//import perfsonarserver.database.cassandraImpl.CacheDJLCas;
//import perfsonarserver.database.exception.FetchNothingException;
//import perfsonarserver.database.exception.FindNothingException;
//
//public class DelayJitterLossInterfaceInitCacheThreadCas extends Thread
//{
//	private String serviceName;
//	private CacheDJLCas cache;
//	
//	public DelayJitterLossInterfaceInitCacheThreadCas(String serviceName, CacheDJLCas cache)
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
//			System.out.println("Init DJLInterface: " + serviceName + DataAccessCas.convertDateToString(System.currentTimeMillis()));
//			cache.checkInterfaceCache(serviceName);
//			System.out.println("Ende Init DJLInterface: " + serviceName + DataAccessCas.convertDateToString(System.currentTimeMillis()));
//		}
//		catch (FindNothingException | FetchNothingException e)
//		{
//			e.printStackTrace();
//		}
//	}
//}
//
