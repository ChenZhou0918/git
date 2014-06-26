package perfsonarserver.database.mongoDB_cacheThreads;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import perfsonarserver.database.mongoDBImpl.DJLCache;
import perfsonarserver.database.mongoDBImpl.ThroughCache;
import perfsonarserver.database.mongoDBImpl.UtilCache;
//import perfsonarserver.fetchData.PerfsonarRequest;
//import perfsonarserver.fetchData.exception.FetchFailException;
//import perfsonarserver.fetchData.transferObjects.UtilizationData;
import perfsonarserver.fetchData.exception.FetchFailException;



/**
 * update db top data
 * 
 * @author Zhou Chen
 * 
 */
public class UpdateDashboardThroughputData {
public static void main(String[] args) throws FetchFailException{
	
	long start=System.currentTimeMillis()-24*60*60*1000;
	long end=System.currentTimeMillis();
	TopThroughputCacheThread topThroughputThread;
	
	/***************** test top throughput Services *******************/
	topThroughputThread= new TopThroughputCacheThread("http://prod-sql-ma.geant.net:8080/geant2-java-sql-ma/services/MeasurementArchiveService","GEANT-Diagnostic",
			start, end);
	topThroughputThread.run();


}
}