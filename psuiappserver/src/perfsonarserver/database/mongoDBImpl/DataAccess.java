package perfsonarserver.database.mongoDBImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import perfsonarserver.appConnect.RequestTO;
import perfsonarserver.database.mongoDB_DatabaseTO.DelayJitterLossInterfaceDB;
import perfsonarserver.database.mongoDB_DatabaseTO.DelayJitterLossServiceDB;
import perfsonarserver.database.mongoDB_DatabaseTO.ThroughputDataDB;
import perfsonarserver.database.mongoDB_DatabaseTO.ThroughputInterfaceDB;
import perfsonarserver.database.mongoDB_DatabaseTO.ThroughputServiceDB;
import perfsonarserver.database.mongoDB_DatabaseTO.UtilizationDataDB;
import perfsonarserver.database.mongoDB_DatabaseTO.UtilizationInterfaceDB;
import perfsonarserver.database.mongoDB_DatabaseTO.UtilizationServiceDB;
import perfsonarserver.database.mongoDB_cacheThreads.*;
import perfsonarserver.database.mongoDB_DatabaseTO.*;
import perfsonarserver.database.exception.FetchNothingException;
import perfsonarserver.database.exception.FindNothingException;
import perfsonarserver.database.mongoDB_responseTO.*;
import perfsonarserver.fetchData.IServerRequest;
import perfsonarserver.fetchData.PerfsonarRequest;
import perfsonarserver.fetchData.exception.FetchDJLDataException;
import perfsonarserver.fetchData.exception.FetchFailException;
import perfsonarserver.fetchData.transferObjects.DelayJitterLossData;
import perfsonarserver.fetchData.transferObjects.UtilizationData;



/**
 * Finds the requested data or gets them from server and returns them.
 * 
 * @author Benjamin Konrad, Sascha Degener,Zhou Chen
 * 
 */
public class DataAccess implements IDataAccessMongoDB
{
	/** Instance of this class for the singleton design pattern. */
	private static DataAccess instance;
	/** constant for the date format in the SimpleDateFormat */
	private static final String DATE_FORMAT = "yyyy-MM-dd HH-mm-ss-SSS";
	/** constant for one hour in milliseconds */
	private static final long HOUR_IN_MILLIS = 60 * 60 * 1000;

	
	public static final String TOPDATA_COLLECTION = "TopData";
	public static final String TOPDELAYDATA_COLLECTION = "TopDelayData";
	public static final String TOPJITTERDATA_COLLECTION = "TopJitterData";
	public static final String TOPTHROUGHPUTDATA_COLLECTION = "TopThroughputData";
	public static final String TOPLOSSINTERFACES_COLLECTION = "TopLossInterfaces";
	
	/** constant for collection name "DelayJitterLossData" inside database */
	public static final String DATA_COLLECTION = "Data";
	/** constant for collection name "DelayJitterLossInterface" inside database */
	public static final String INTERFACE_COLLECTION = "Interface";
	/** constant for collection name "DelayJitterLossService" inside database */
	public static final String SERVICE_COLLECTION = "Service";


	/** size in days of the Initialization */
	private int cacheMaxDays;
	/** size in hours of one sector in the cache */
	private int cacheDJLSectorSizeHours;
	private int cacheThroughSectorSizeHours;
	private int cacheUtilSectorSizeHours;
	/** size in Days of delete */
	private int cacheDJLDeleteDays;
	private int cacheThroughDeleteDays;
	private int cacheUtilDeleteDays;

	/** DatabaseSingleton instance mongoDB */
	private DatabaseDJL db;
	private DatabaseThrough dbt;
	private DatabaseUtil dbu;
	
	
	/** Cache instance */
	private DJLCache cache;
	private ThroughCache cachet;
	private UtilCache cacheu;
	


	/**
	 * Initializes this class and creates the singleton object cacheMaxDays(30),
	 * cacheSectorSizeHours(4) and cacheDeleteDays(2) to set by default
	 * 
	 * @return the singleton instance
	 *
	 */
	public static DataAccess getInstance() 
	{
		if (instance == null)
			// 1 is for test use
			instance = new DataAccess(1, 1, 24, 1, 10, 10, 10);
		return instance;
	}

	/**
	 * Constructor initializes this class and creates the singleton object
	 * 
	 * @param cacheMaxDays
	 *            Maximum to be precached
	 * @param cacheSectorSizeHours
	 *            Size of a cache sector (hours) 1-24
	 * @param cacheDeleteDays
	 *            Days until cache sector should be dropped
	 * @throws FetchFailException 
	 */
	private DataAccess(int cacheMaxDays, int cacheDJLSectorSizeHours, int cacheThroughSectorSizeHours, int cacheUtilSectorSizeHours, int cacheDJLDeleteDays, int cacheThroughDeleteDays, int cacheUtilDeleteDays) 
	{	
		//MongoDB Instanzen
		this.db = DatabaseDJL.getInstance();
		this.dbt = DatabaseThrough.getInstance();
		this.dbu = DatabaseUtil.getInstance();
		
		//Cassandra Instanzen
//				this.dbdjlc  = DatabaseDJLCas.getInstance();     //Cassandra Instance DJL aufrufen
				//this.dbtc    = DatabaseThroughCas.getInstance(); //Cassandra Instance Throughput aufrufen
				//this.dbuc    = DatabaseUtilCas.getInstance();    //Cassandra Instance Utilization aufrufen
		
		
		this.cacheMaxDays = cacheMaxDays;
		
		this.cacheDJLSectorSizeHours = cacheDJLSectorSizeHours;
		this.cacheThroughSectorSizeHours = cacheThroughSectorSizeHours;
		this.cacheUtilSectorSizeHours = cacheUtilSectorSizeHours;

		this.cacheDJLDeleteDays = cacheDJLDeleteDays;
		this.cacheThroughDeleteDays = cacheThroughDeleteDays;
		this.cacheUtilDeleteDays = cacheUtilDeleteDays;
		
//		
//		this.cacheDJLcas = new CacheDJLCas(this.cacheDJLSectorSizeHours, this.cacheDJLDeleteDays);
//		testCas();
		this.cache = new DJLCache(this.cacheDJLSectorSizeHours, this.cacheDJLDeleteDays);
		this.cachet = new ThroughCache(this.cacheThroughSectorSizeHours, this.cacheThroughDeleteDays);
		this.cacheu = new UtilCache(this.cacheUtilSectorSizeHours, this.cacheUtilDeleteDays);
		
		System.out.println("Data  initialized with CacheMaxDay: " + cacheMaxDays);

		// fill db with services and interface pairs.
//		
//		initDJLMetaData();	
//		initThroughMetaData();
//		initUtilMetaData();
	
//		initDJLData();
//		initThroughData();
//		intiUtilData();
	}


	@Override
	public List<DashboardDelayGetDataTO> getDashboardDelayGetData(RequestTO to) throws ParseException, FetchFailException
	{		List<DashboardDelayGetDataTO> ll = new LinkedList<DashboardDelayGetDataTO>();
	      to.setService(to.getServiceDelayJitterLoss());
	// build query
	BasicDBObject search = new BasicDBObject();
	search.append(DelayJitterLossDataDB.SERVICE_COL, to.getServiceDelayJitterLoss());
	long start;
	long end;

	//if request is with start,end parameter, append them to query object
	if(to.getStartDate()!=null&&to.getEndDate()!=null)
	{
	 start = convertDateToLong(to.getStartDate().trim());
	 end = convertDateToLong(to.getEndDate().trim());
	search.append(DelayJitterLossDataDB.TIMESTAMP_COL, new BasicDBObject("$gte", start).append("$lte", end));
	}

	// query the data
	List<DBObject> lDBObj = db.find(TOPDELAYDATA_COLLECTION, search,DelayJitterLossDataDB.MAXDELAY_COL,-1);
	//if not existed, update the top five data by running threads
	if (lDBObj.size()==0&&to.getGetNonCached())
	{
	
		TopDJLCacheThread topDJLThread;  
		if(to.getStartDate()!=null&&to.getEndDate()!=null)
		{   
			 start = convertDateToLong(to.getStartDate().trim());
			 end = convertDateToLong(to.getEndDate().trim());
			 topDJLThread= new TopDJLCacheThread(convertToServiceUrl(to), to.getServiceDelayJitterLoss(), start, end);		
		}
	//if request has no start, end parameter, the period is 24 hours by default
		else
		{
	    topDJLThread= new TopDJLCacheThread(convertToServiceUrl(to),to.getServiceDelayJitterLoss(), System.currentTimeMillis()-24*60*60*1000,System.currentTimeMillis());
		}
	    topDJLThread.run();
	    lDBObj = db.find(TOPDELAYDATA_COLLECTION, search,DelayJitterLossDataDB.MAXDELAY_COL,-1);
	}
	if (lDBObj == null)
		throw new FindNothingException("Fail to find any related delay Data: " + to.toString());
//throw new FindNothingException("Fail to find any  TopUtilizationData: " + to.toString());
	
	// transform the DBObject into DJLDataTO(responseTO)
	for (DBObject dbObj : lDBObj)
	{
		ll.add(DelayJitterLossDataDB.toDashDelayClass(dbObj));
	}	return ll;// TODO Auto-generated method stub
	}
	
	@Override
	public List<DashboardJitterGetDataTO> getDashboardJitterGetData(RequestTO to) throws ParseException, FetchFailException
	{
		List<DashboardJitterGetDataTO> ll = new LinkedList<DashboardJitterGetDataTO>();
		to.setService(to.getServiceDelayJitterLoss());
		// build query
		BasicDBObject search = new BasicDBObject();
		search.append(DelayJitterLossDataDB.SERVICE_COL, to.getServiceDelayJitterLoss());

		
		if(to.getStartDate()!=null&&to.getEndDate()!=null)
		{
		long start = convertDateToLong(to.getStartDate().trim());
		long end = convertDateToLong(to.getEndDate().trim());
		search.append(DelayJitterLossDataDB.TIMESTAMP_COL, new BasicDBObject("$gte", start).append("$lte", end));
		}

		// query the data
		List<DBObject> lDBObj = db.find(TOPJITTERDATA_COLLECTION, search,DelayJitterLossDataDB.MAXJITTER_COL,-1);
		if (lDBObj.size()==0&&to.getGetNonCached())
		{
			TopDJLCacheThread topDJLThread;  
			if(to.getStartDate()!=null&&to.getEndDate()!=null)
			{   
				long start = convertDateToLong(to.getStartDate().trim());
				long end = convertDateToLong(to.getEndDate().trim());
				topDJLThread= new TopDJLCacheThread(convertToServiceUrl(to), to.getServiceDelayJitterLoss(), start, end);
				
			}
			
			else
			{
		    topDJLThread= new TopDJLCacheThread(convertToServiceUrl(to),to.getServiceDelayJitterLoss(), System.currentTimeMillis()-24*60*60*1000,System.currentTimeMillis());
			}
		    topDJLThread.run();
		    lDBObj = db.find(TOPJITTERDATA_COLLECTION, search,DelayJitterLossDataDB.MAXJITTER_COL,-1);
		}
		if (lDBObj == null)
			throw new FindNothingException("Fail to find any related DJLData: " + to.toString());
		
		
	//throw new FindNothingException("Fail to find any  TopUtilizationData: " + to.toString());
		//if not existed, update the top five data in db

		// transform the DBObject into DJLDataTO
		for (DBObject dbObj : lDBObj)
		{
			ll.add(DelayJitterLossDataDB.toDashJitterClass(dbObj));
		}	return ll;// TODO Auto-generated method stub
		}
	

	
	@Override
	public List<DashboardLossGetDataTO> getDashboardLossGetData(RequestTO to) throws ParseException, FetchFailException
	{
		List<DashboardLossGetDataTO> ll = new LinkedList<DashboardLossGetDataTO>();
		to.setService(to.getServiceDelayJitterLoss());
		// build query
		BasicDBObject search = new BasicDBObject();
		search.append("serviceName", to.getServiceDelayJitterLoss());

		
		if(to.getStartDate()!=null&&to.getEndDate()!=null)
		{
		long start = convertDateToLong(to.getStartDate().trim());
		long end = convertDateToLong(to.getEndDate().trim());
		search.append(DelayJitterLossDataDB.TIMESTAMP_COL, new BasicDBObject("$gte", start).append("$lte", end));
		}

		// query the data
		List<DBObject> lDBObj = db.find(TOPLOSSINTERFACES_COLLECTION, search,DelayJitterLossInterfaceDB.LOSSNUMBER_COL,-1);
		if (lDBObj.size()==0&&to.getGetNonCached())
		{
			TopDJLCacheThread topDJLThread;  
			if(to.getStartDate()!=null&&to.getEndDate()!=null)
			{   
				long start = convertDateToLong(to.getStartDate().trim());
				long end = convertDateToLong(to.getEndDate().trim());
				topDJLThread= new TopDJLCacheThread(convertToServiceUrl(to), to.getServiceDelayJitterLoss(), start, end);
				
			}
			
			else
			{
		    topDJLThread= new TopDJLCacheThread(convertToServiceUrl(to),to.getServiceDelayJitterLoss(), System.currentTimeMillis()-24*60*60*1000,System.currentTimeMillis());
			}
		    topDJLThread.run();
		    lDBObj = db.find(TOPLOSSINTERFACES_COLLECTION, search,DelayJitterLossInterfaceDB.LOSSNUMBER_COL,-1);
		}
		if (lDBObj == null)
			throw new FindNothingException("Fail to find any related Loss Data: " + to.toString());
		

		// transform the DBObject into DJLDataTO
		for (DBObject dbObj : lDBObj)
		{
			ll.add(DelayJitterLossInterfaceDB.toDashClass(dbObj));
		}	return ll;// TODO Auto-generated method stub
	}
	
	
	
	
	

	@Override
	public List<DashboardUDGetDataTO> getDashboardUDGetData(RequestTO to) throws ParseException
	{
		to.setService(to.getServiceUtilization());
		List<DashboardUDGetDataTO> ll = new LinkedList<DashboardUDGetDataTO>();
		System.out.println("name"+to.getService()+"  address"+convertToServiceUrl(to));
		
		// build query
		BasicDBObject search = new BasicDBObject();
		search.append(UtilizationDataDB.SERVICENAME_COL, to.getServiceUtilization());
		
		if(to.getStartDate()!=null&&to.getEndDate()!=null)
		{
		long start = convertDateToLong(to.getStartDate().trim())/1000;
		long end = convertDateToLong(to.getEndDate().trim())/1000;
		search.append(DelayJitterLossDataDB.TIMESTAMP_COL, new BasicDBObject("$gte", start).append("$lte", end));
		}

		// query the data
		List<DBObject> lDBObj = dbu.find(TOPDATA_COLLECTION, search,UtilizationDataDB.MEASUREDUTIL_COL,-1);
		if (lDBObj.size()==0&&to.getGetNonCached())
		{
			
			TopUtilizationCacheThread topUtilThread;  
			if(to.getStartDate()!=null&&to.getEndDate()!=null)
			{   
				long startTime = convertDateToLong(to.getStartDate().trim());
				long endTime = convertDateToLong(to.getEndDate().trim());
				topUtilThread= new TopUtilizationCacheThread(convertToServiceUrl(to),  to.getServiceUtilization(), startTime, endTime);
			}
			
			else
			{
		    topUtilThread= new TopUtilizationCacheThread(convertToServiceUrl(to), to.getServiceUtilization(), System.currentTimeMillis()-24*60*60*1000,System.currentTimeMillis());
			}
		    topUtilThread.run();
		    lDBObj = dbu.find(TOPDATA_COLLECTION, search,UtilizationDataDB.MEASUREDUTIL_COL,-1);
		}
		
		if (lDBObj == null)
			throw new FindNothingException("Fail to find any related DJLData: " + to.toString());
//	throw new FindNothingException("Fail to find any  TopUtilizationData: " + to.toString());
		//if not existed, update the top five data in db
	
		// transform the DBObject into DJLDataTO
		for (DBObject dbObj : lDBObj)
		{
			ll.add(UtilizationDataDB.toDashClass(dbObj));
		}	
		
		return ll;
		
		
	// TODO Auto-generated method stub
	}
	
	
	
	
	public List<DashboardThroughputGetDataTO> getDashboardThroughputGetData(RequestTO to) throws ParseException, FetchFailException
	{		List<DashboardThroughputGetDataTO> ll = new LinkedList<DashboardThroughputGetDataTO>();
	      to.setService(to.getServiceThroughput());
	// build query
	BasicDBObject search = new BasicDBObject();
	System.out.println(to.getServiceThroughput());
	search.append(ThroughputDataDB.SERVICENAME_COL,to.getServiceThroughput());

	
	if(to.getStartDate()!=null&&to.getEndDate()!=null)
	{
	long start = convertDateToLong(to.getStartDate().trim());
	long end = convertDateToLong(to.getEndDate().trim());
	search.append(ThroughputDataDB.TIMESTAMP_COL, new BasicDBObject("$gte", start).append("$lte", end));
	}

	// query the data
	List<DBObject> lDBObj = dbt.find(TOPTHROUGHPUTDATA_COLLECTION, search,ThroughputDataDB.MEASUREDTHROUGHPUT_COL,-1);

	if (lDBObj.size()==0&&to.getGetNonCached())
	{
		TopThroughputCacheThread topThroughputThread;  
		if(to.getStartDate()!=null&&to.getEndDate()!=null)
		{   
			long start = convertDateToLong(to.getStartDate().trim());
			long end = convertDateToLong(to.getEndDate().trim());
			topThroughputThread= new TopThroughputCacheThread(convertToServiceUrl(to), to.getServiceThroughput(), start, end);
			
		}
		
		else
		{
	    topThroughputThread= new TopThroughputCacheThread(convertToServiceUrl(to),to.getServiceThroughput(), System.currentTimeMillis()-24*60*60*1000,System.currentTimeMillis());
		}
	    topThroughputThread.run();
	    lDBObj = dbt.find(TOPTHROUGHPUTDATA_COLLECTION, search,ThroughputDataDB.MEASUREDTHROUGHPUT_COL,-1);
	}
	
	if (lDBObj == null)
		throw new FindNothingException("Fail to find any related top through Data: " + to.toString());

	//if not existed, update the top five data in db

	// transform the DBObject into ThroughputDataTO
	for (DBObject dbObj : lDBObj)
	{
		ll.add(ThroughputDataDB.toDashClass(dbObj));
	}	return ll;// TODO Auto-generated method stub
	}
	
	/**********************************************************************************************************************/
	/**
	 * searching for the DelayJitterLossData data in the db and returns a list
	 * of them if no data inside cache and getData is true
	 * delayJitterLossDataCheckCache will load data.
	 * 
	 * @param to
	 *            the request
	 * @param getData
	 *            cache requested data if its not cached yet.
	 * @return list of all requested data.
	 * @throws ParseException
	 *             Wrong date format
	 * @throws CacheEmptyException
	 *             if the requested data is not cached.
	 */

	@Override
	public List<DelayJitterLossDataTO> getDelayJitterLossData(RequestTO to) throws ParseException, FindNothingException, FetchNothingException
	{
		List<DelayJitterLossDataTO> ll = new LinkedList<DelayJitterLossDataTO>();
		long start,end;
		 if(to.getStartDate()!=null&&to.getEndDate()!=null)
		    { 
		     start = convertDateToLong(to.getStartDate().trim());
			 end = convertDateToLong(to.getEndDate().trim());
		    }
		    // for dashboard get data, the time period is default 24 hours
		    else
		    {
		     long currentTime=System.currentTimeMillis()/1000;//precision:minutes
		  	    start=(currentTime-24*60*60)*1000;
		  	    end=currentTime*1000;
		    }

		// build query
		BasicDBObject search = new BasicDBObject();
		{
			search.append(DelayJitterLossDataDB.TIMESTAMP_COL, new BasicDBObject("$gte", start).append("$lte", end));
			search.append(DelayJitterLossDataDB.SERVICE_COL, to.getService());
			search.append(DelayJitterLossDataDB.DESTINTERFACE_COL, to.getDestinationInterface());
			search.append(DelayJitterLossDataDB.SRCINTERFACE_COL, to.getSourceInterface());
		}

		// checks if data is cached
		cache.checkDataCache(to.getService(), start, end, to.getSourceInterface(), to.getDestinationInterface(), to.getGetNonCached());

		// query the data
		List<DBObject> lDBObj = db.find(DATA_COLLECTION, search,DelayJitterLossDataDB.TIMESTAMP_COL,1);
		if (lDBObj == null)
			throw new FindNothingException("Fail to find any related DJLData: " + to.toString());

		// transform the DBObject into DJLDataTO
		for (DBObject dbObj : lDBObj)
		{
			ll.add(DelayJitterLossDataDB.toClass(dbObj));
		}

		return ll;
	}

	/**
	 * searching for the DelayJitterLossInterfaces in the db and returns a list
	 * of them if no data inside cache or data too old
	 * delayJitterLossInterfaceCheck will load data.
	 * 
	 * @param to
	 *            the request object
	 * @return a list of all interfaces
	 */
	@Override
	public List<DelayJitterLossInterfaceTO> getDelayJitterLossInterface(RequestTO to) throws FindNothingException, FetchNothingException
	{
		List<DelayJitterLossInterfaceTO> ll = new LinkedList<DelayJitterLossInterfaceTO>();
		String service = to.getService();
		String orderby;

		// check if interface is cached
		cache.checkInterfaceCache(service);

		// query the interface
		BasicDBObject search = new BasicDBObject(DelayJitterLossInterfaceDB.SERVICENAME_COL, service);
		if(to.getSourceInterface()==null)
		{
			orderby=DelayJitterLossInterfaceDB.SRCINTERFACE_COL;
		}
		else orderby=DelayJitterLossInterfaceDB.DESTINTERFACE_COL;
		List<DBObject> lDBObj = db.find(INTERFACE_COLLECTION, search,orderby,1);
		if (lDBObj == null)
			throw new FindNothingException("Fail to find any related DJLInterfaces: " + service);

		// transform from DBObject to TO
		for (DBObject dbObj : lDBObj)
		{
			ll.add(DelayJitterLossInterfaceDB.toClass(dbObj));
		}

		return ll;
	}

	/**
	 * searching for the DelayJitterLossService in the db and returns a list of
	 * them if no data inside cache or data too old delayJitterLossServiceCheck
	 * will load data.
	 * 
	 * @param to
	 *            the request object
	 * @return a list of all services
	 */
	@Override
	public List<DelayJitterLossServiceTO> getDelayJitterLossService(RequestTO to) throws FindNothingException, FetchNothingException
	{
		List<DelayJitterLossServiceTO> ll = new LinkedList<DelayJitterLossServiceTO>();
		// check
		cache.checkServiceCache();
		// query
		List<DBObject> lDBObj = db.find(SERVICE_COLLECTION);
		if (lDBObj == null)
			throw new FindNothingException("Fail to find any DJLServices");
		// transform
		for (DBObject dbObj : lDBObj)
		{
			ll.add(DelayJitterLossServiceDB.toClass(dbObj));
		}
		return ll;
	}

	@Override
	public List<ThroughputGetDataTO> getThrougputGetData(RequestTO to) throws ParseException, FetchNothingException, FindNothingException
	{
		List<ThroughputGetDataTO> ll = new LinkedList<ThroughputGetDataTO>();
	
		String serviceName = to.getService();
		String srcInterface = to.getSourceInterface();
		String destInterface = to.getDestinationInterface();
		long start,end;
		 if(to.getStartDate()!=null&&to.getEndDate()!=null)
		    { 
		     start = convertDateToLong(to.getStartDate().trim());
			 end = convertDateToLong(to.getEndDate().trim());
		    }
		    // for dashboard get data, the time period is default 24 hours
		    else
		    {
		    	  long currentTime=System.currentTimeMillis()/1000;//precision:minutes
		  	    start=(currentTime-24*60*60)*1000;
		  	    end=currentTime*1000;
		    }
	
		// check
		cachet.checkDataCache(serviceName, start, end, srcInterface, destInterface, to.getGetNonCached());
	
		// query
		BasicDBObject query = new BasicDBObject(ThroughputDataDB.SERVICENAME_COL, serviceName);
		query.append(ThroughputDataDB.SRCINTERFACE_COL, srcInterface);
		query.append(ThroughputDataDB.DESTINTERFACE_COL, destInterface);
		query.append(ThroughputDataDB.TIMESTAMP_COL, new BasicDBObject("$gte", start).append("$lte", end));
	
		List<DBObject> list = dbt.find(DATA_COLLECTION, query,ThroughputDataDB.TIMESTAMP_COL,1);
	
		// transform
		if (list == null)
			throw new FindNothingException("Fail to find any ThroughputData");
	
		for (DBObject dbObj : list)
		{
			ll.add(ThroughputDataDB.toClass(dbObj));
		}
	
		return ll;
	}

	@Override
	public List<ThroughputInterfaceTO> getThroughputInterface(RequestTO to) throws FindNothingException, FetchNothingException
	{
		List<ThroughputInterfaceTO> ll = new LinkedList<ThroughputInterfaceTO>();
		String serviceName = to.getService();

		// check the cache
		cachet.checkInterfaceCache(serviceName);

		// query
		BasicDBObject query = new BasicDBObject(ThroughputInterfaceDB.SERVICENAME_COL, serviceName);
		List<DBObject> list = dbt.find(INTERFACE_COLLECTION, query,ThroughputInterfaceDB.SRCINTERFACE_COL,1);

		// transform
		if (list == null)
			throw new FindNothingException("Fail to find and Throughput Interface");

		for (DBObject dbObj : list)
		{
			ll.add(ThroughputInterfaceDB.toClass(dbObj));
		}

		return ll;
	}

	@Override
	public List<ThroughputServiceTO> getThroughputService(RequestTO to) throws FindNothingException, FetchNothingException
	{
		List<ThroughputServiceTO> ll = new LinkedList<ThroughputServiceTO>();

		// check
		cachet.checkServiceCache();

		// query
		List<DBObject> list = dbt.find(SERVICE_COLLECTION);

		// transform
		if (list == null)
			throw new FindNothingException("fail to find any Throughput service");

		for (DBObject dbObj : list)
		{
			ll.add(ThroughputServiceDB.toClass(dbObj));
		}

		return ll;
	}

	@Override
	public List<UtilizationDataTO> getUtilizationData(RequestTO to) throws ParseException, FetchNothingException, FindNothingException
	{
		List<UtilizationDataTO> ll = new LinkedList<UtilizationDataTO>();
		
		String serviceName = to.getService();
		String interfaceName = to.getSourceInterface();
	    long start,end;
	    if(to.getStartDate()!=null&&to.getEndDate()!=null)
	    { 
	     start = convertDateToLong(to.getStartDate().trim());
		 end = convertDateToLong(to.getEndDate().trim());
	    }
	    // for dashboard get data, the time period is default 24 hours
	    else
	    {
	    long currentTime=System.currentTimeMillis()/1000;//precision:minutes
	    start=(currentTime-24*60*60)*1000;
	    end=currentTime*1000;
	    }
		//check
		cacheu.checkDataCache(serviceName, start, end, interfaceName, "NOT_NEED", to.getGetNonCached());
		
		//query
		BasicDBObject query = new BasicDBObject();
		{
		query.append(UtilizationDataDB.TIMESTAMP_COL, new BasicDBObject("$gte", start/1000).append("$lte", end/1000));
		query.append(UtilizationDataDB.INTERFACENAME_COL, interfaceName);
		query.append(UtilizationDataDB.SERVICENAME_COL,serviceName);
		}
		List<DBObject> list = dbu.find(DATA_COLLECTION, query,UtilizationDataDB.TIMESTAMP_COL,1);
		
		
		//tranform
		if(list == null) {
			throw new FindNothingException("Fail to find any UtilizationData");
		}
		System.out.println("database size: "+list.size());
		for(DBObject dbObj : list)
		{
			ll.add(UtilizationDataDB.toClass(dbObj));
		}
		
		return ll;
	}

	@Override
	public List<UtilizationInterfaceTO> getUtilizationInterface(RequestTO to) throws FindNothingException, FetchNothingException
	{
		List<UtilizationInterfaceTO> ll = new LinkedList<UtilizationInterfaceTO>();
		String serviceName = to.getService();

		// check the cache
		cacheu.checkInterfaceCache(serviceName);

		// query
		BasicDBObject query = new BasicDBObject(UtilizationInterfaceDB.SERVICENAME_COL, serviceName);
		List<DBObject> list = dbu.find(INTERFACE_COLLECTION, query,UtilizationInterfaceDB.INTERFACENAME_COL,1);

		// transform
		if (list == null)
			throw new FindNothingException("Fail to find any Utilization Interface");

		for (DBObject dbObj : list)
		{
			ll.add(UtilizationInterfaceDB.toClass(dbObj));
		}

		return ll;
	}

	@Override
	public List<UtilizationServiceTO> getUtilizationService(RequestTO to) throws FindNothingException, FetchNothingException
	{
		List<UtilizationServiceTO> ll = new LinkedList<UtilizationServiceTO>();

		// check
		cacheu.checkServiceCache();

		// query
		List<DBObject> list = dbu.find(SERVICE_COLLECTION);

		// transform
		if (list == null)
			throw new FindNothingException("fail to find any Utilization service");

		for (DBObject dbObj : list)
		{
			ll.add(UtilizationServiceDB.toClass(dbObj));
		}

		return ll;
	}

	@Override
	public List<PathSegmentsGetDataTO> getPathSegmentsGetData(RequestTO to)
	{
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * initialize the Services and Interfaces of DJL Services is read from file
	 * Interfaces is initialized in a concurrent fashion
	 */
	public void initDJLMetaData()
	{
		System.out.println("init DJLMetaData " + convertDateToString(System.currentTimeMillis()));
		List<DelayJitterLossServiceTO> serviceList = new LinkedList<DelayJitterLossServiceTO>();
		serviceList = getDelayJitterLossService(null);
		ExecutorService exe = Executors.newCachedThreadPool();

		for (DelayJitterLossServiceTO service : serviceList)
		{
			exe.execute(new DelayJitterLossInterfaceInitCacheThread(service.getServiceName(), cache));
		}

		exe.shutdown();
		try
		{
			exe.awaitTermination(1, TimeUnit.MINUTES);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		System.out.println("Ende init DJLMetaData " + convertDateToString(System.currentTimeMillis()));
	}

	public void initThroughMetaData()
	{
		System.out.println("init ThroughputMetaData " + convertDateToString(System.currentTimeMillis()));
		List<ThroughputServiceTO> serviceList = new LinkedList<ThroughputServiceTO>();
		serviceList = getThroughputService(null);
		ExecutorService exe = Executors.newCachedThreadPool();

		for (ThroughputServiceTO service : serviceList)
		{
			exe.execute(new ThroughputInterfaceInitCacheThread(service.getServiceName(), cachet));
		}

		exe.shutdown();
		try
		{
			exe.awaitTermination(1, TimeUnit.MINUTES);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		System.out.println("Ende init ThroughputMetaData " + convertDateToString(System.currentTimeMillis()));
	}
	
	public void initUtilMetaData()
	{
		System.out.println("init UtilizationMetaData " + convertDateToString(System.currentTimeMillis()));
		List<UtilizationServiceTO> serviceList = new LinkedList<UtilizationServiceTO>();
		serviceList = getUtilizationService(null);
		ExecutorService exe = Executors.newCachedThreadPool();

		for (UtilizationServiceTO service : serviceList)
		{
			exe.execute(new UtilInterfaceInitCacheThread(service.getServiceName(), cacheu));
		}

		exe.shutdown();
		try
		{
			exe.awaitTermination(1, TimeUnit.MINUTES);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		System.out.println("Ende init UtilizationMetaData " + convertDateToString(System.currentTimeMillis()));
	}

	/**
	 * initialize the Data of DJL Data is initialized in a concurrent fashion
	 */
	public void initDJLData()
	{
		long endTime = System.currentTimeMillis() - cacheDJLSectorSizeHours * 60 * 60 * 1000;
		long startTime = endTime - (HOUR_IN_MILLIS * 24 * cacheMaxDays);
		System.out.println("init DJLData: " + cacheMaxDays + " " + startTime + "-" + endTime + " " + convertDateToString(System.currentTimeMillis()));

		// get all services
		List<DelayJitterLossServiceTO> services = this.getDelayJitterLossService(null);
		RequestTO request = new RequestTO();
		ExecutorService exe = Executors.newFixedThreadPool(1);

		for (DelayJitterLossServiceTO service : services)
		{
			// get all interfaces
			request.setService(service.getServiceName());
			List<DelayJitterLossInterfaceTO> interfacePairs = getDelayJitterLossInterface(request);
			System.out.println("init DJLData from " + service.getServiceName() + "    " + interfacePairs.size() + " interface pairs need init");

			// init data
			for (DelayJitterLossInterfaceTO interfacePair : interfacePairs)
			{
				exe.execute(new DelayJitterLossDataInitCacheThread(service.getServiceName(), startTime, endTime, interfacePair.getSrcInterface(), interfacePair.getDestInterface(), cache));
			}
		}

		exe.shutdown();
		try
		{
			exe.awaitTermination(30, TimeUnit.MINUTES);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}

		System.out.println("Ende init DJLData " + cacheMaxDays + " " + startTime + "-" + endTime + " " + convertDateToString(System.currentTimeMillis()));
	}

	public void initThroughData()
	{
		long endTime = System.currentTimeMillis() - cacheThroughSectorSizeHours * 60 * 60 * 1000;
		long startTime = endTime - (HOUR_IN_MILLIS * 24 * cacheMaxDays * 3);
		System.out.println("init Throughput Data: " + cacheMaxDays + " " + startTime + "-" + endTime + " " + convertDateToString(System.currentTimeMillis()));

		// get all services
		List<ThroughputServiceTO> services = this.getThroughputService(null);
		RequestTO request = new RequestTO();
		ExecutorService exe = Executors.newFixedThreadPool(1);

		for (ThroughputServiceTO service : services)
		{
			// get all interfaces
			request.setService(service.getServiceName());
			List<ThroughputInterfaceTO> interfacePairs = getThroughputInterface(request);
			System.out.println("init ThroughputData from " + service.getServiceName() + "    " + interfacePairs.size() + " interface pairs need init");

			// init data
			for (ThroughputInterfaceTO interfacePair : interfacePairs)
			{
				exe.execute(new ThroughputDataInitCacheThread(service.getServiceName(), startTime, endTime, interfacePair.getSrcInterface(), interfacePair.getDestInterface(), cachet));
			}
		}

		exe.shutdown();
		try
		{
			exe.awaitTermination(30, TimeUnit.MINUTES);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}

		System.out.println("Ende init Throughput Data " + cacheMaxDays + " " + startTime + "-" + endTime + " " + convertDateToString(System.currentTimeMillis()));
	}
	
	public void initUtilData()
	{
		//TODO
	}

	/**
	 * initialize the services, interfaces, and data of DJL All of them is
	 * initialize in a serial fashion this method is slower than the
	 * initDJLData()
	 * @throws FetchDJLDataException 
	 */
	@Deprecated
	public void initCache() throws FetchDJLDataException
	{
		IServerRequest req = new PerfsonarRequest();
		System.out.println("init Cache\t" + convertDateToString(System.currentTimeMillis()));
		long endTime = System.currentTimeMillis() - cacheDJLSectorSizeHours * 60 * 60 * 1000;
		long startTime = endTime - (HOUR_IN_MILLIS * 24 * cacheMaxDays);
		System.out.println("StartTime\t" + convertDateToString(startTime));
		System.out.println("EndTime\t" + convertDateToString(endTime));

		// get all services
		System.out.println("init Services");
		List<DelayJitterLossServiceTO> services = this.getDelayJitterLossService(null);
		System.out.println(services.size() + " Services initiated");

		// get all interface pairs
		RequestTO request = new RequestTO();
		for (DelayJitterLossServiceTO service : services)
		{
			System.out.println("init Interfaces from: " + service.getServiceName());
			request.setService(service.getServiceName());
			List<DelayJitterLossInterfaceTO> interfacePairs = getDelayJitterLossInterface(request);
			System.out.println(interfacePairs.size() + " Interfaces initiated");
			int count = 1;

			// get all DJL data
			for (DelayJitterLossInterfaceTO interfacePair : interfacePairs)
			{
				System.out.println(convertDateToString(System.currentTimeMillis()) + " - " + count++ + ": " + interfacePair.getSrcInterface() + "\t" + interfacePair.getDestInterface());
				List<DelayJitterLossData> liste;

				liste = req.getDelayJitterLossData(cache.getServiceURL(service.getServiceName()), interfacePair.getSrcInterface(), interfacePair.getDestInterface(), startTime, endTime);
				Iterator<DelayJitterLossData> it = liste.iterator();
				List<DBObject> ll2 = new LinkedList<DBObject>();
				// converts data into database format.
				while (it.hasNext())
				{
					DelayJitterLossData djld = it.next();
					DelayJitterLossDataDB djlddb = new DelayJitterLossDataDB(djld.getTime(), djld.getMaxDelay(), djld.getMinDelay(), djld.getMaxIpdvJitter(), djld.getMinIpdvJitter(), djld.getLoss(), service.getServiceName(), interfacePair.getSrcInterface(), interfacePair.getDestInterface());
					DBObject dbObj = djlddb.toDBObject();
					ll2.add(dbObj);
				}
				// insert data into database
				db.insert(DataAccess.DATA_COLLECTION, ll2);
				System.out.println(liste.size() + " DJLs initiated");

			}
		}
		System.out.println("Ende init Cache\t" + convertDateToString(System.currentTimeMillis()));
	}

	/**
	 * ISO Date converter yyyy-MM-ddTHH-mm-ss-SSS
	 * 
	 * @param timestamp
	 *            "2013-03-01T12-20-00-000"
	 * @return
	 * @throws ParseException
	 */
	public static long convertDateToLong(String timestamp) throws ParseException
	{
		SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
		return formatter.parse(timestamp).getTime();
	}

	/**
	 * ISO Date converter yyyy-MM-ddTHH-mm-ss-SSS
	 * 
	 * @param timestamp
	 *            The number of milliseconds since January 1, 1970, 00:00:00 GMT
	 * @return yyyy-MM-ddTHH-mm-ss-SSS
	 */
	public static String convertDateToString(long timestamp)
	{
		SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
		return formatter.format(new Date(timestamp));
	}

	/**
	 * convert the service name to url
	 */
	private String convertToServiceUrl(RequestTO request) {
		// TODO Auto-generated method stub
		String service=request.getService();
		switch (service){
		
		case "APAN-JP" :
		   return "http://nms2.jp.apan.net:8080/perfSONAR_PS/services/snmpMA";
		   
		case "GEANT-PRODUCTION":
			return "http://prod-rrd-ma.geant.net:8080/perfsonar-java-rrd-ma/services/MeasurementArchiveService";
			
		case "INTERNET2":
			return "http://rrdma.net.internet2.edu:8080/perfSONAR_PS/services/snmpMA";
			
		case "X-WiN":
			return "http://pallando.rrze.uni-erlangen.de:8090/services/MA/HADES/DFN";
			
		case "GEANT_production" :
			return "http://62.40.105.148:8090/services/MA/HADES/GEANT";
			
		case "LHCOPN":
			return "http://alatar.rrze.uni-erlangen.de:8090/services/MA/HADES/LHCOPN";
			
		case "GN Frankfurt Converged":
			return "http://data.psmp2.fra.de.geant.net:8085/perfSONAR_PS/services/pSB";
		
		case"GEANT-Diagnostic" :
			return "http://prod-sql-ma.geant.net:8080/geant2-java-sql-ma/services/MeasurementArchiveService";
			
		default:return null;}
	
	}
}