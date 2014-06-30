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
import perfsonarserver.fetchDataAndProcess.exception.FetchFailException;



/**
 * update db top data
 * 
 * @author Zhou Chen
 * 
 */
public class UpdateDashboardUtilData {
public static void main(String[] args) throws FetchFailException{	
    long start=System.currentTimeMillis()-24*60*60*1000;
	long end=System.currentTimeMillis();	
	TopUtilizationCacheThread topUtilThread;
	/******** test top 5 Utilization  3 Services *******************/
	topUtilThread= new TopUtilizationCacheThread("http://nms2.jp.apan.net:8080/perfSONAR_PS/services/snmpMA","APAN-JP", start,end);
	topUtilThread.run();
//    topUtilThread= new TopUtilizationCacheThread("http://prod-rrd-ma.geant.net:8080/perfsonar-java-rrd-ma/services/MeasurementArchiveService","GEANT-PRODUCTION", start,end);
//	topUtilThread.run();     //no data response
//	topUtilThread= new TopUtilizationCacheThread("http://rrdma.net.internet2.edu:8080/perfSONAR_PS/services/snmpMA","INTERNET2", start,end);
//	topUtilThread.run();  // not available 
	


}
}
