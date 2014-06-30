package perfsonarserver;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Scanner;
import java.util.TimeZone;

import perfsonarserver.database.mongoDBImpl.DataAccess;
import perfsonarserver.fetchDataAndProcess.PerfsonarRequest;
import perfsonarserver.fetchDataAndProcess.exception.FetchFailException;
import perfsonarserver.fetchDataAndProcess.transferObjects.DelayJitterLossData;
import perfsonarserver.fetchDataAndProcess.transferObjects.DelayJitterLossInterfacePair;
import perfsonarserver.fetchDataAndProcess.transferObjects.ThroughputData;
import perfsonarserver.fetchDataAndProcess.transferObjects.ThroughputInterfacePair;
import perfsonarserver.fetchDataAndProcess.transferObjects.UtilizationData;
import perfsonarserver.fetchDataAndProcess.transferObjects.UtilizationInterface;

public class FetchData
{
	public static void start(Scanner scan)
	{
		try
		{
			PerfsonarRequest perfsonar = new PerfsonarRequest();
//			 fetchDJL(perfsonar);
//			fetchThrough(perfsonar);
		fetchUtil(perfsonar);
		}
		catch (FetchFailException e)
		{
			e.printStackTrace();
		}
	}

	public static void fetchDJL(PerfsonarRequest perfsonar) throws FetchFailException
	{
		System.out.println("test the fetch DJL data");
		GregorianCalendar start = new GregorianCalendar(TimeZone.getTimeZone("GMT+2:00"));
		int from_year = 2014;
		int from_month = Calendar.APRIL;
		int from_day = 20;
		int from_hour = 0;
		int from_minute = 0;
		start.set(from_year, from_month, from_day, from_hour, from_minute);

		GregorianCalendar end = new GregorianCalendar(TimeZone.getTimeZone("GMT+2:00"));
		int till_year = 2014;
		int till_month = Calendar.APRIL;
		int till_day = 20;
		int till_hour = 5;
		int till_minute = 2;
		end.set(till_year, till_month, till_day, till_hour, till_minute);

//		 List<DelayJitterLossData> djlList = perfsonar.getTopFiveDelay("http://62.40.105.148:8090/services/MA/HADES/GEANT",
//				 start.getTimeInMillis(), end.getTimeInMillis());
//		 System.out.println(djlList);
//		 perfsonar.getDelayJitterLossInterfacePairs("http://62.40.105.148:8090/services/MA/HADES/GEANT",
//		 start.getTimeInMillis(), end.getTimeInMillis());
//		 System.out.println(djlList);
//		 djlList =
//		 perfsonar.getDelayJitterLossData("http://62.40.105.148:8090/services/MA/HADES/GEANT",
//		 djlList, start.getTimeInMillis(), end.getTimeInMillis());
//		 System.out.println(djlList);
//		
//		 try (PrintWriter pw = new PrintWriter("DJLData.txt"))
//		 {
//		 for (DelayJitterLossInterfacePair p : djlList)
//		 {
//		 pw.println(p.toString());
//		 }
//		 }
//		 catch (IOException e)
//		 {
//		 e.printStackTrace();
//		 }

		List<DelayJitterLossData> djlData = perfsonar.getDelayJitterLossData("http://pallando.rrze.uni-erlangen.de:8090/services/MA/HADES/DFN", "Aachen_DFN", "Berlin_ADH_DFN", start.getTimeInMillis(), end.getTimeInMillis());
		for (DelayJitterLossData ele : djlData)
		{
			System.out.println(DataAccess.convertDateToString(ele.getTime()));
		}
	}

	public static void fetchThrough(PerfsonarRequest perfsonar) throws FetchFailException
	{
		System.out.println("testing fetch Throughput data");
		GregorianCalendar start = new GregorianCalendar(TimeZone.getTimeZone("GMT+2:00"));
		int from_year = 2014;
		int from_month = Calendar.MAY;
		int from_day = 27;
		int from_hour = 11;
		int from_minute = 13;
		start.set(from_year, from_month, from_day, from_hour, from_minute);

		GregorianCalendar end = new GregorianCalendar(TimeZone.getTimeZone("GMT+2:00"));
		int till_year =2014;
		int till_month = Calendar.MAY;
		int till_day =28;
		int till_hour =11;
		int till_minute =13;
		end.set(till_year, till_month, till_day, till_hour, till_minute);

		 List<ThroughputInterfacePair> tpList =
		 perfsonar.getThroughputInterfacePairs("http://prod-sql-ma.geant.net:8080/geant2-java-sql-ma/services/MeasurementArchiveService",
		 start.getTimeInMillis(), end.getTimeInMillis());
		
//		 List<ThroughputInterfacePair> tpList2 = perfsonar.getThroughputInterfacePairs("http://62.40.106.15:8085/perfSONAR_PS/services/pSB", start.getTimeInMillis(), end.getTimeInMillis());
		 
//		 List<ThroughputInterfacePair> tpList3 =perfsonar.getThroughputInterfacePairs("http://data.psmp2.fra.de.geant.net.:8085/perfSONAR_PS/services/pSB", start.getTimeInMillis(), end.getTimeInMillis());
		 
		 
		 
		 System.out.println(tpList);
//		 System.out.println(tpList2);
//		 System.out.println(tpList3);
//		 tpList =
//		 perfsonar.getThroughputData("http://prod-sql-ma.geant.net:8080/geant2-java-sql-ma/services/MeasurementArchiveService",
//		 tpList, start.getTimeInMillis(), end.getTimeInMillis());
//		
//		 try (PrintWriter pw = new PrintWriter("ThroughData.txt"))
//		 {
//		 for (ThroughputInterfacePair p : tpList)
//		 {
//		 pw.println(p.toString());
//		 }
//		 }
//		 catch (IOException e)
//		 {
//		 e.printStackTrace();
//		 }
		 
//		ThroughputInterfacePair pair = new ThroughputInterfacePair("ipv4", "mp1.fra.de.geant2.net", "ipv4", "62.40.122.10", "meta8761930870552403001");
		
		 for(ThroughputInterfacePair TI: tpList){
//			 
		 List<ThroughputData> dataList = perfsonar.getThroughputData("http://prod-sql-ma.geant.net:8080/geant2-java-sql-ma/services/MeasurementArchiveService",TI, start.getTimeInMillis(), end.getTimeInMillis());
		
			if(dataList==null) System.out.println("fail to fetch");
			else
				{System.out.println("size: "+dataList.size());
				System.out.println(dataList);
				}
		 }
//			List<ThroughputData> dataList = perfsonar.getThroughputData("http://prod-sql-ma.geant.net:8080/geant2-java-sql-ma/services/MeasurementArchiveService", "62.40.121.250", "62.40.122.10", "meta8761930870552403001", start.getTimeInMillis(), end.getTimeInMillis());
//			System.out.println(dataList);
	}

	public static void fetchUtil(PerfsonarRequest perfsonar) throws FetchFailException
	{
		System.out.println("testing fetch Utilizatin data");
		GregorianCalendar start = new GregorianCalendar(TimeZone.getTimeZone("GMT+2:00"));
		int from_year = 2014;
		int from_month = Calendar.APRIL;
		int from_day = 10;
		int from_hour = 0;
		int from_minute = 0;
		start.set(from_year, from_month, from_day, from_hour, from_minute);

		GregorianCalendar end = new GregorianCalendar(TimeZone.getTimeZone("GMT+2:00"));
		int till_year = 2014;
		int till_month = Calendar.APRIL;
		int till_day = 10;
		int till_hour = 5;
		int till_minute = 1;
		end.set(till_year, till_month, till_day, till_hour, till_minute);
		
		
		/******** test top 5*******************/
//	    List<UtilizationData> top=perfsonar.getTopFiveUtilization("http://nms2.jp.apan.net:8080/perfSONAR_PS/services/snmpMA", start.getTimeInMillis(), end.getTimeInMillis());
//	    System.out.println(top);
//		UtilizationData[] top=perfsonar.getTopFiveUtilization("http://nms2.jp.apan.net:8080/perfSONAR_PS/services/snmpMA", "10BASE-T/100BASE-TX/1000BASE-T 0/45 e0/45", "in", start.getTimeInMillis(), end.getTimeInMillis());
		
    
       /*************************************/   
//		List<UtilizationInterface> uList = perfsonar.getUtilizationInterfaces("http://nms2.jp.apan.net:8080/perfSONAR_PS/services/snmpMA");
//	    System.out.println(uList);
//        	
//     	uList = perfsonar.getUtilizationData("http://prod-rrd-ma.geant.net:8080/perfsonar-java-rrd-ma/services/MeasurementArchiveService", uList, start.getTimeInMillis(), end.getTimeInMillis());
//		try (PrintWriter pw = new PrintWriter("TopUtilizationData.txt"))
//		{
//			for (UtilizationInterface p : uList)
//			{
//				pw.println(p.toString());
//			}
//		}
//		catch (IOException e)
//		{
//			e.printStackTrace();
//		}
//		
//		
		List<UtilizationData> uData = perfsonar.getUtilizationDataMDB("http://nms2.jp.apan.net:8080/perfSONAR_PS/services/snmpMA", "10BASE-T/100BASE-TX/1000BASE-T 0/45 e0/45", "in",System.currentTimeMillis()-24*60*60*1000, System.currentTimeMillis());
//		for(UtilizationData ut:uData)
		System.out.println( uData);
	}
}