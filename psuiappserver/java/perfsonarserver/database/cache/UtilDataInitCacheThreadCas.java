package perfsonarserver.database.cache;

//import perfsonarserver.database.cassandraImpl.DataAccessCas;
//import perfsonarserver.database.UtilCache;
//import perfsonarserver.database.cassandraImpl.CacheUtilizationCas;
//import perfsonarserver.database.exception.FetchNothingException;
//import perfsonarserver.database.exception.FindNothingException;
//
//public class UtilDataInitCacheThreadCas extends Thread{
//	
//	private String serviceName;
//	private long startTime;
//	private long endTime;
//	private String interfaceName;
//	private CacheUtilizationCas cache;
//	
//	public UtilDataInitCacheThreadCas(String serviceName, long startTime, long endTime, String interfaceName, CacheUtilizationCas cache)
//	{
//		this.serviceName = serviceName;
//		this.startTime = startTime;
//		this.endTime = endTime;
//		this.interfaceName = interfaceName;
//		this.cache = cache;
//	}
//	
//	public void run()
//	{
//		try
//		{
//			System.out.println("Init UtilizationData: " + serviceName + " " + startTime +" " + endTime + " "+ interfaceName + " "  + DataAccess.convertDateToString(System.currentTimeMillis()));
//			cache.checkDataCache(serviceName, startTime, endTime, interfaceName, "NOT_NEED", true);
//			System.out.println("End Init UtilizationData: " + serviceName + " " + startTime +" " + endTime + " "+ interfaceName + " "  + DataAccess.convertDateToString(System.currentTimeMillis()));
//		}
//		catch (FindNothingException | FetchNothingException e)
//		{
//			e.printStackTrace();
//		}
//	}
//
//}
