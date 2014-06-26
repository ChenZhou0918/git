package perfsonarserver.database.cache;

//import perfsonarserver.database.cassandraImpl.DataAccessCas;
//import perfsonarserver.database.cassandraImpl.CacheThroughputCas;
//import perfsonarserver.database.exception.FetchNothingException;
//import perfsonarserver.database.exception.FindNothingException;
//
//public class ThroughputInterfaceInitCacheThreadCas extends Thread{
//	
//	private String serviceName;
//	private CacheThroughputCas cache;
//	
//	public ThroughputInterfaceInitCacheThreadCas(String serviceName, CacheThroughputCas cache)
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
//			System.out.println("Init ThroughputInterface: " + serviceName + DataAccessCas.convertDateToString(System.currentTimeMillis()));
//		//	cache.checkInterfaceCache(serviceName);
//			System.out.println("Ende Init ThroughputInterface: " + serviceName + DataAccessCas.convertDateToString(System.currentTimeMillis()));
//		}
//		catch (FindNothingException | FetchNothingException e)
//		{
//			e.printStackTrace();
//		}
//	}
//
//}
