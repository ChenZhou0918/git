package perfsonarserver;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Scanner;
import java.util.TimeZone;

import perfsonarserver.fetchData.PerfsonarRequest;
import perfsonarserver.fetchData.exception.FetchFailException;
import perfsonarserver.fetchData.transferObjects.DelayJitterLossData;
import perfsonarserver.fetchData.transferObjects.ThroughputData;
import perfsonarserver.fetchData.transferObjects.UtilizationData;

public class FetchData
{
	public static void start(Scanner scan)
	{
		try
		{
			PerfsonarRequest perfsonar = new PerfsonarRequest();
			 fetchDJL(perfsonar);
			fetchThrough(perfsonar);
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
		int from_year = 2013;
		int from_month = Calendar.MAY;
		int from_day = 1;
		int from_hour = 0;
		int from_minute = 0;
		start.set(from_year, from_month, from_day, from_hour, from_minute);

		GregorianCalendar end = new GregorianCalendar(TimeZone.getTimeZone("GMT+2:00"));
		int till_year = 2013;
		int till_month = Calendar.MAY;
		int till_day = 1;
		int till_hour = 0;
		int till_minute = 2;
		end.set(till_year, till_month, till_day, till_hour, till_minute);

		// List<DelayJitterLossInterfacePair> djlList =
		// perfsonar.getDelayJitterLossInterfacePairs("http://pallando.rrze.uni-erlangen.de:8090/services/MA/HADES/DFN",
		// start.getTimeInMillis(), end.getTimeInMillis());
		// djlList =
		// perfsonar.getDelayJitterLossData("http://pallando.rrze.uni-erlangen.de:8090/services/MA/HADES/DFN",
		// djlList, start.getTimeInMillis(), end.getTimeInMillis());
		//
		// try (PrintWriter pw = new PrintWriter("DJLData.txt"))
		// {
		// for (DelayJitterLossInterfacePair p : djlList)
		// {
		// pw.println(p.toString());
		// }
		// }
		// catch (IOException e)
		// {
		// e.printStackTrace();
		// }

//		List<DelayJitterLossData> djlData = perfsonar.getDelayJitterLossData("http://alatar.rrze.uni-erlangen.de:8090/services/MA/HADES/LHCOPN", "ES-PIC-HADES", "DE-KIT-HADES", start.getTimeInMillis(), end.getTimeInMillis());
//		for (DelayJitterLossData ele : djlData)
//		{
//			System.out.println(ele);
//		}
	}

	public static void fetchThrough(PerfsonarRequest perfsonar) throws FetchFailException
	{
		System.out.println("testing fetch Throughput data");
		GregorianCalendar start = new GregorianCalendar(TimeZone.getTimeZone("GMT+2:00"));
		int from_year = 2013;
		int from_month = Calendar.OCTOBER;
		int from_day = 1;
		int from_hour = 0;
		int from_minute = 0;
		start.set(from_year, from_month, from_day, from_hour, from_minute);

		GregorianCalendar end = new GregorianCalendar(TimeZone.getTimeZone("GMT+2:00"));
		int till_year = 2013;
		int till_month = Calendar.OCTOBER;
		int till_day = 3;
		int till_hour = 0;
		int till_minute = 0;
		end.set(till_year, till_month, till_day, till_hour, till_minute);

		// List<ThroughputInterfacePair> tpList =
		// perfsonar.getThroughputInterfacePairs("http://prod-sql-ma.geant.net:8080/geant2-java-sql-ma/services/MeasurementArchiveService",
		// start.getTimeInMillis(), end.getTimeInMillis());
		// tpList =
		// perfsonar.getThroughputData("http://prod-sql-ma.geant.net:8080/geant2-java-sql-ma/services/MeasurementArchiveService",
		// tpList, start.getTimeInMillis(), end.getTimeInMillis());
		//
		// try (PrintWriter pw = new PrintWriter("ThroughData.txt"))
		// {
		// for (ThroughputInterfacePair p : tpList)
		// {
		// pw.println(p.toString());
		// }
		// }
		// catch (IOException e)
		// {
		// e.printStackTrace();
		// }

		//ThroughputInterfacePair pair = new ThroughputInterfacePair("", "62.40.121.250", "", "62.40.122.10", "meta8761930870552403001");
		List<ThroughputData> dataList = perfsonar.getThroughputData("http://prod-sql-ma.geant.net:8080/geant2-java-sql-ma/services/MeasurementArchiveService", "62.40.121.250", "62.40.122.10", "meta8761930870552403001", start.getTimeInMillis(), end.getTimeInMillis());
		for (ThroughputData p : dataList)
		{
			System.out.println(p);
		}

	}

	public static void fetchUtil(PerfsonarRequest perfsonar) throws FetchFailException
	{
		System.out.println("testing fetch Utilizatin data");
		GregorianCalendar start = new GregorianCalendar(TimeZone.getTimeZone("GMT+2:00"));
		int from_year = 2013;
		int from_month = Calendar.MAY;
		int from_day = 1;
		int from_hour = 0;
		int from_minute = 0;
		start.set(from_year, from_month, from_day, from_hour, from_minute);

		GregorianCalendar end = new GregorianCalendar(TimeZone.getTimeZone("GMT+2:00"));
		int till_year = 2013;
		int till_month = Calendar.MAY;
		int till_day = 1;
		int till_hour = 1;
		int till_minute = 1;
		end.set(till_year, till_month, till_day, till_hour, till_minute);

//		List<UtilizationInterface> uList = perfsonar.getUtilizationInterfaces("http://prod-rrd-ma.geant.net:8080/perfsonar-java-rrd-ma/services/MeasurementArchiveService");
//		uList = perfsonar.getUtilizationData("http://prod-rrd-ma.geant.net:8080/perfsonar-java-rrd-ma/services/MeasurementArchiveService", uList, start.getTimeInMillis(), end.getTimeInMillis());
//		try (PrintWriter pw = new PrintWriter("UtilizationData.txt"))
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
		
		//List<UtilizationData> uData = perfsonar.getUtilizationData("http://nms2.jp.apan.net:8080/perfSONAR_PS/services/snmpMA", "10BASE-T/100BASE-TX/1000BASE-T 0/45 e0/45", "in", start.getTimeInMillis(), end.getTimeInMillis());
	//	System.out.println(uData);
	}
}