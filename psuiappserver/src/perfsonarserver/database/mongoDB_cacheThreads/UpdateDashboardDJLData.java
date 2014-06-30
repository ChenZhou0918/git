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
public class UpdateDashboardDJLData {
public static void main(String[] args) throws FetchFailException{
	


	long start=System.currentTimeMillis()-24*60*60*1000;
	long end=System.currentTimeMillis();

	TopDJLCacheThread topDJLThread;

	/***************** test top 5 DJL 4 Services *******************/
//	topDJLThread= new TopDJLCacheThread("http://62.40.105.148:8090/services/MA/HADES/GEANT", "GEANT_production", start, end);
//	topDJLThread.run();

	topDJLThread= new TopDJLCacheThread("http://pallando.rrze.uni-erlangen.de:8090/services/MA/HADES/DFN","X-WiN",start,end);
	topDJLThread.run();
	
	/********not available services, require further investigation*********/
//	topDJLThread= new TopDJLCacheThread("http://data.psmp2.fra.de.geant.net:8085/perfSONAR_PS/services/pSB", "GN Frankfurt Converged", start, end);	

//	topDJLThread= new TopDJLCacheThread("http://alatar.rrze.uni-erlangen.de:8090/services/MA/HADES/LHCOPN", "LHCOPN", start, end);
//	topDJLThread= new TopDJLCacheThread("http://wizbit.rrze.uni-erlangen.de:8090/services/MA/HADES/GEANT", "GEANT_2013", start, end);
//	topDJLThread.run();
}
}
