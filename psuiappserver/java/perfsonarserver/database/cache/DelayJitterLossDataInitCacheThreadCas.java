package perfsonarserver.database.cache;

//import perfsonarserver.database.cassandraImpl.DataAccessCas;
//import perfsonarserver.database.cassandraImpl.CacheDJLCas;
//import perfsonarserver.database.exception.FetchNothingException;
//import perfsonarserver.database.exception.FindNothingException;
//
///**
// * Requests all DelayJitterLoss data from server and inserts them into database.
// * 
// * @author Benjamin Konrad, Sascha Degener
// * 
// */
//public class DelayJitterLossDataInitCacheThreadCas extends Thread
//{
//	/** service name */
//	private String serviceName;
//	/** start time */
//	private long startTime;
//	/** end time */
//	private long endTime;
//	/** source interface */
//	private String srcInterface;
//	/** destination interface */
//	private String destInterface;
//	/** cache */
//	private CacheDJLCas cache;
//	
//
//	
//	/**
//	 * Constructor
//	 * 
//	 * @param nr
//	 *            number of thread
//	 * @param serviceName
//	 *            service name
//	 * @param startTime
//	 *            start time
//	 * @param endTime
//	 *            end time
//	 * @param srcInterface
//	 *            source interface
//	 * @param destInterface
//	 *            destination interface
//	 * @param cache
//	 *            cache
//	 */
//	public DelayJitterLossDataInitCacheThreadCas( String serviceName, long startTime, long endTime, String srcInterface, String destInterface, CacheDJLCas cache)
//	{
//		this.serviceName = serviceName;
//		this.startTime = startTime;
//		this.endTime = endTime;
//		this.srcInterface = srcInterface;
//		this.destInterface = destInterface;
//		this.cache = cache;
//	}
//	
//
//	/**
//	 * Requests all chunks for this interface between start time and ent time.
//	 */
//	public void run()
//	{
//		try
//		{
//			System.out.println("Init DJLData: " + serviceName + " " + srcInterface + "-" + destInterface + " " + startTime + "-" + endTime + " " + DataAccessCas.convertDateToString(System.currentTimeMillis()));
//			//cache.checkDataCache(serviceName, startTime, endTime, srcInterface, destInterface, true);
//			System.out.println("Ende Init DJLData: " + serviceName + " " + srcInterface + "-" + destInterface + " " + startTime + "-" + endTime + " " + DataAccessCas.convertDateToString(System.currentTimeMillis()));
//		}
//		catch (FindNothingException | FetchNothingException e)
//		{
//			e.printStackTrace();
//		}
//	}
//}
//
