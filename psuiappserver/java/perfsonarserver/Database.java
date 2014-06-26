package perfsonarserver;


import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import perfsonarserver.appConnect.RequestTO;
import perfsonarserver.database.cassandraImpl.DataAccessCas;
import perfsonarserver.database.couchdbImpl.DataAccessCB;
import perfsonarserver.database.database_to.DelayJitterLossDataDB;
import perfsonarserver.database.exception.FetchNothingException;
import perfsonarserver.database.exception.FindNothingException;
import perfsonarserver.database.response_to.DelayJitterLossDataTO;

public class Database
{
	private static RequestTO request;
	//MongoDB Instance
	//private static DataAccess da = DataAccess.getInstance();
	//Cassandra Instance
	private static DataAccessCas da = DataAccessCas.getInstance();
	//Couchbase Instance
	//private static DataAccessCB da = DataAccessCB.getInstance();
	public Database()
	{

	}

	public static void start(Scanner scan)
	{
		request = new RequestTO();
		{
			request.setSourceInterface("Augsburg_DFN");
			request.setDestinationInterface("Aachen_DFN");
			request.setStartDate("2013-03-31 23-00-00-000");
			request.setEndDate("2013-03-31 23-59-00-000");
			request.setFeatureName("");
			request.setGetNonCached(true);
			request.setService("X-WiN");
		}
		Database start = new Database();
		List<DelayJitterLossDataDB> list;
		try {
			list = da.getDelayJitterLossData(request);
		} catch (FindNothingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FetchNothingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("O: oldTest");
		System.out.println("M: MultiThread");
		System.out.println("E: Exit");
		System.out.print("Chose: ");
		switch (scan.nextLine())
		{
		case "O":
		case "o":
		//	start.oldTest();
			break;
		case "M":
		case "m":
			start.multiThread();
			break;
		case "E":
		case "e":
			break;
		default:
			System.out.println("Falsche Eingabe!");
		}
		System.out.println();
	}

//	private void oldTest()
//	{
//		boolean getList = true;
//		long start = System.currentTimeMillis();
//		long end = 0;
//	//	DataAccessCas dataAccess = da;
//		System.out.println("Build Request");
//		try
//		{
//			System.out.println("Requesting Data");
//
//			File file = new File("output.txt");
//			PrintWriter bw = new PrintWriter(file);
//			if (getList)
//			{
//			//	List<DelayJitterLossDataTO> list = dataAccess.getDelayJitterLossData(request);
//				System.out.println("Listsize: " + list.size());
//				bw.println("Listsize: " + list.size());
//				for (DelayJitterLossDataTO data : list)
//				{
//					System.out.println(data);
//					bw.println(data);
//				}
//			}
//			else
//			{
//				da.getDelayJitterLossData(request);
//			}
//
//			end = System.currentTimeMillis();
//			int time = (int) ((end - start) / 1000);
//			System.out.println("Time: " + time);
//			bw.println("Time: " + time);
//			bw.flush();
//			bw.close();
//		}
//		catch (ParseException | FileNotFoundException e)
//		{
//			e.printStackTrace();
//			System.err.println(e);
//		}
//	}

	private void multiThread()
	{
		List<MyThread> tList = new LinkedList<MyThread>();
		for (int i = 0; i < 5; i++)
		{
			tList.add(new MyThread(request));
		}
		for (MyThread t : tList)
		{
			t.start();
		}

		
	}

	private class MyThread extends Thread
	{
		RequestTO request;

		public MyThread(RequestTO request)
		{
			this.request = request;
		}

		public void run()
		{
			try
			{
				List<DelayJitterLossDataDB> list = da.getDelayJitterLossData(request);
				System.out.println(list.size());
			}
			catch (ParseException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}