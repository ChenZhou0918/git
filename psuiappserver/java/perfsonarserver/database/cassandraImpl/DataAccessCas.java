package perfsonarserver.database.cassandraImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import perfsonarserver.appConnect.RequestTO;
import perfsonarserver.database.cassandraDAO.DelayJitterLossDataDBDAOImpl;
import perfsonarserver.database.cassandraDAO.DelayJitterLossInterfaceDBDAOImpl;
import perfsonarserver.database.cassandraDAO.DelayJitterLossServiceDBDAOImpl;
import perfsonarserver.database.cassandraDAO.ThroughputDataDBDAOImpl;
import perfsonarserver.database.cassandraDAO.ThroughputInterfaceDBDAOImpl;
import perfsonarserver.database.cassandraDAO.ThroughputServiceDBDAOImpl;
import perfsonarserver.database.cassandraDAO.UtilizationDataDBDAOImpl;
import perfsonarserver.database.cassandraDAO.UtilizationInterfaceDBDAOImpl;
import perfsonarserver.database.cassandraDAO.UtilizationServiceDBDAOImpl;
import perfsonarserver.database.database_to.DelayJitterLossDataDB;
import perfsonarserver.database.database_to.DelayJitterLossInterfaceDB;
import perfsonarserver.database.database_to.DelayJitterLossServiceDB;
import perfsonarserver.database.database_to.ThroughputDataDB;
import perfsonarserver.database.database_to.ThroughputInterfaceDB;
import perfsonarserver.database.database_to.ThroughputServiceDB;
import perfsonarserver.database.database_to.UtilizationDataDB;
import perfsonarserver.database.database_to.UtilizationInterfaceDB;
import perfsonarserver.database.database_to.UtilizationServiceDB;
import perfsonarserver.database.exception.FetchNothingException;
import perfsonarserver.database.exception.FindNothingException;
import perfsonarserver.database.response_to.DashboardDelayGetDataTO;
import perfsonarserver.database.response_to.DashboardJitterGetDataTO;
import perfsonarserver.database.response_to.DashboardLossGetDataTO;
import perfsonarserver.database.response_to.DashboardUDGetDataTO;
import perfsonarserver.database.response_to.DelayJitterLossInterfaceTO;
import perfsonarserver.database.response_to.PathSegmentsGetDataTO;
import perfsonarserver.database.response_to.ThroughputGetDataTO;
import perfsonarserver.database.response_to.ThroughputInterfaceTO;
import perfsonarserver.database.response_to.ThroughputServiceTO;
import perfsonarserver.database.response_to.UtilizationDataTO;
import perfsonarserver.database.response_to.UtilizationInterfaceTO;
import perfsonarserver.database.response_to.UtilizationServiceTO;
import perfsonarserver.fetchData.IServerRequest;
import perfsonarserver.fetchData.PerfsonarRequest;
import perfsonarserver.fetchData.exception.FetchFailException;
import perfsonarserver.fetchData.transferObjects.DelayJitterLossData;
import perfsonarserver.fetchData.transferObjects.DelayJitterLossInterfacePair;
import perfsonarserver.fetchData.transferObjects.ThroughputData;
import perfsonarserver.fetchData.transferObjects.ThroughputInterfacePair;
import perfsonarserver.fetchData.transferObjects.UtilizationData;
import perfsonarserver.fetchData.transferObjects.UtilizationInterface;


/**
 * Finds the requested data or gets them from server and returns them.
 * 
 * Author: Mirco Bohlmann
 * 
 */
public class DataAccessCas implements IDataAccess
{
	/** Instance of this class for the singleton design pattern. */
	private static DataAccessCas instance;
	/** constant for the date format in the SimpleDateFormat */
	private static final String DATE_FORMAT = "yyyy-MM-dd HH-mm-ss-SSS";
	/** DatabaseSingleton instances cassandra*/
	private DatabaseDJLCas      dbDJLcas;
	private DatabaseThroughCas  dbTcas;
	private DatabaseUtilCas     dbUcas;
	
	/** Cassandra Column Family Names*/
	public static String dbDJLcolumnfamily = "DelayJitterLoss";
	public static String dbTcolumnfamily   = "Throughput";
	public static String dbUcolumnfamily   = "Utilization";
	
	/** Test Data*/
	private DelayJitterLossDataDB djldata1;
	private DelayJitterLossDataDB djldata2;
	private DelayJitterLossServiceDB djlservice;
	private DelayJitterLossInterfaceDB djlinterface;
	private long timestamp = 5000; 
	private double maxDelay = 40.23;
	private double minDelay = 0.5; 
    private double maxJitter = 24.42; 
    private double minJitter = 1.2; 
    private int loss = 7; 
    private String service = "GEANT"; 
    
    /** DAO Objects */
    private DelayJitterLossDataDBDAOImpl djld;
    private DelayJitterLossServiceDBDAOImpl djls;
    private DelayJitterLossInterfaceDBDAOImpl djlinf;
    
    private ThroughputServiceDBDAOImpl ts;
    private ThroughputInterfaceDBDAOImpl tinf;
    private ThroughputDataDBDAOImpl td;
    
    private UtilizationServiceDBDAOImpl us;
    private UtilizationInterfaceDBDAOImpl uinf;
    private UtilizationDataDBDAOImpl ud;

	/**
	 * Initializes this class and creates the singleton object 
	 * 
	 * @return the singleton instance
	 *
	 */
	public static DataAccessCas getInstance() 
	{
		if (instance == null)
			// 1 is for test use
			instance = new DataAccessCas(1, 1, 24, 1, 10, 10, 10);
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
	private DataAccessCas(int cacheMaxDays, int cacheDJLSectorSizeHours, int cacheThroughSectorSizeHours, int cacheUtilSectorSizeHours, int cacheDJLDeleteDays, int cacheThroughDeleteDays, int cacheUtilDeleteDays) 
	{	
		//Get the Cassandra DelayJitterLoss Instance
		//this.dbDJLcas  = DatabaseDJLCas.getInstance();
		//Get the Cassandra Throughput Instance
	    //this.dbTcas    = DatabaseThroughCas.getInstance(); 
		//Get the Cassandra Utilization Instance
	    this.dbUcas    = DatabaseUtilCas.getInstance();    
		//testCas();
		
		// Fill db with services and interface pairs.
	//	initDJLMetaData();	
	//	initThroughMetaData();
		initUtilMetaData();
		
		//Fill db with measured DelayJitterLoss Data
//		initDJLData();
		
		//Fill db with measured Throughput Data
	  //  initThroughData();
	    System.out.println("FINISH!!!");
		//Fill db with measured Utilization Data
	    initUtilData();
	}
	/**************************************************************************************************************************/
	/*                                                Test Cassandra                                                          */
	/**************************************************************************************************************************/
	
	public void testCas(){
		
//	this.djldata1 = new DelayJitterLossDataDB(timestamp, maxDelay, minDelay, maxJitter, minJitter, loss);
//	this.djldata2 = new DelayJitterLossDataDB(timestamp, maxDelay, minDelay, maxJitter, minJitter, loss);
	this.djlservice = new DelayJitterLossServiceDB("GEANT", "www.dasendeistnahe.de/GEANT/superSecurityThringis");
	this.djlinterface = new DelayJitterLossInterfaceDB();
	this.djld = new DelayJitterLossDataDBDAOImpl(dbDJLcas.getKeyspace());
	this.djls = new DelayJitterLossServiceDBDAOImpl(dbDJLcas.getKeyspace());
	this.djlinf = new DelayJitterLossInterfaceDBDAOImpl(dbDJLcas.getKeyspace());
	this.djld.save(djldata1);
	System.out.println("insert Data: "+djldata1+ " into DelayJitterLossData");
	this.djld.save(djldata2);
	System.out.println("insert Data: "+djldata2+ " into DelayJitterLossData");
	this.djls.save(djlservice);
	System.out.println("insert Data: "+djlservice+ " into DelayJitterLossService");
	//this.inf.save(djlinterface);
	System.out.println("insert Data: "+djlinterface+ " into DelayJitterLossInterface");
	System.out.println("Fertig");

	}
	/**************************************************************************************************************************/
	/*                                             Delay Jitter Loss                                                          */
	/**************************************************************************************************************************/
	/**
	 * initialize the Services and Interfaces of DJL Services is read from file
	 * Interfaces is initialized in a concurrent fashion
	 */
	public void initDJLMetaData()
	{
			System.out.println("Begin to initiate DJLMetaData " + convertDateToString(System.currentTimeMillis())); 
			System.out.println("Fetch DelayJitterLossService...");
			try {
				//Get DelayJitterLoss Services
				getDelayJitterLossService(null);
			} catch (FindNothingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (FetchNothingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (FetchFailException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		System.out.println("Finish init DJLMetaData " + convertDateToString(System.currentTimeMillis()));
	}
	
	
	/**
	 * searching for the DelayJitterLossService in the db and returns a list of
	 * them 
	 * 
	 * @param to
	 *            the request object
	 * 
	 * @throws FetchFailException 
	 */
	@Override
	public void getDelayJitterLossService(RequestTO to) throws FindNothingException, FetchNothingException, FetchFailException
	{
		//To Request Services from perfSONAR
		IServerRequest request = new PerfsonarRequest();
		//Intialize the DelayJitterLossServiceDB DAO Object with the perfSONAR Keyspace
		this.djls = new DelayJitterLossServiceDBDAOImpl(dbDJLcas.getKeyspace());
		Map<String, String> servicePairs = new HashMap<String,String>();
		
		DelayJitterLossServiceDB dbDJLServiceObj = new DelayJitterLossServiceDB();
		//Counts the serviceID´s
		int counterSer = 0;
		
		try {
			//Fetch Services from pefSONAR and saves them into the map
			servicePairs = request.getDelayJitterLossServices();
		} catch (FetchFailException e) {
			e.printStackTrace();
		}
		Iterator it = servicePairs.entrySet().iterator();
		//Goes through the services, creates DelayJitterLossDB Objects which are then saved inside the Database
		while(it.hasNext())
		{
			Map.Entry<String,String> pairs = (Map.Entry)it.next();
			System.out.println("Found Service: "+pairs.getKey());
			dbDJLServiceObj = new DelayJitterLossServiceDB(pairs.getKey(), pairs.getValue());
			//Add the ID to the services
			dbDJLServiceObj.setDelayJitterLossServiceID("service_"+(++counterSer));
			dbDJLServiceObj.getServiceName();
			this.djls.save(dbDJLServiceObj);
		}
		
		//Get DelayJitterLossInterfaces
		getDelayJitterLossInterface();	
	}
	
	/**
	 * searching for DelayJitterLossInterfaces in the db and returns a list of
	 * them 
	 * 
	 * @throws FetchFailException 
	 */
	 public void getDelayJitterLossInterface() throws FindNothingException, FetchNothingException {
		    
		    //To Request Interfaces from perfSONAR
			IServerRequest request = new PerfsonarRequest();
			//Intialize the DelayJitterLossInterfaceDB DAO Object with the perfSONAR Keyspace
		 	this.djlinf = new DelayJitterLossInterfaceDBDAOImpl(dbDJLcas.getKeyspace());
		 	//Holds the found Services
			List<DelayJitterLossServiceDB> searchDJLservices = new LinkedList<DelayJitterLossServiceDB>();
			//Saving the interfaces for the different Services
			List<DelayJitterLossInterfacePair> interfacePairsXWIN = new LinkedList<DelayJitterLossInterfacePair>();
			List<DelayJitterLossInterfacePair> interfacePairsGEANT = new LinkedList<DelayJitterLossInterfacePair>();
			List<DelayJitterLossInterfacePair> interfacePairsLHCOPN = new LinkedList<DelayJitterLossInterfacePair>();
			GregorianCalendar grec = new GregorianCalendar();
			//Needed to differentiate between the different perfSONAR Services
			String urlXWIN = "";
		 	String urlGEANT = "";
		 	String urlLHCOPN = "";
		 	//Counts the InterfacesID´s
			int counterInt  = 0;
			//Debug: Holds the number of fetched Interfaces down
			int countXWIN   = 0;
			int countGEANT  = 0;
			int countLHCOPN = 0;
			
			
			//Get the current time for a request
			long aktTime = grec.getTimeInMillis();
			
			//search for Delay Jitter Loss Services
			searchDJLservices = djls.find();
			if(searchDJLservices.isEmpty())
				throw new FindNothingException("fail to find any Delay Jitter Loss Services");
			
			//pull the URLs out of the DelayJitterLossService Data in order to request the Interfaces
			System.out.println("Searching for DelayJitterLoss Interfaces...");
			for(DelayJitterLossServiceDB serviceURLs : searchDJLservices){
				
				//Request for DelayJitterLoss Interfaces with Servicename X-WiN
				if(serviceURLs!=null && serviceURLs.getServiceName().equals("X-WiN")) {	
				    urlXWIN = serviceURLs.getServiceURL();
				    System.out.println("url: "+urlXWIN);
				    try {
				    	interfacePairsXWIN = request.getDelayJitterLossInterfacePairs(urlXWIN, aktTime - (1 * 60 * 60 * 1000), aktTime);
						 
					} catch (FetchFailException e) {
						e.printStackTrace();
					}
				    
				 //Request for DelayJitterLoss Interfaces with Servicename GEANT
			     } else if(serviceURLs!=null && serviceURLs.getServiceName().equals("GEANT")) {
			    	 		urlGEANT = serviceURLs.getServiceURL();
			    	 		System.out.println("url: "+urlGEANT);
			    	 		try {
			    	 				interfacePairsGEANT = request.getDelayJitterLossInterfacePairs(urlGEANT, aktTime - (1 * 60 * 60 * 1000), aktTime);
			    	 			} catch (FetchFailException e) {
			    	 					e.printStackTrace();
			    	 			}
			    	 		
			    	 	//Request for DelayJitterLoss Interfaces with Servicename LHCOPN	
			     		}else if(serviceURLs!=null && serviceURLs.getServiceName().equals("LHCOPN")){
			     					urlLHCOPN = serviceURLs.getServiceURL();
			     					System.out.println("url: "+urlLHCOPN);
			     					try {
			     						interfacePairsLHCOPN = request.getDelayJitterLossInterfacePairs(urlLHCOPN, aktTime - (1 * 60 * 60 * 1000), aktTime);
			     					} catch (FetchFailException e) {			    	 						
			    	 						e.printStackTrace();
			     					}
			     				}	
			}
			System.out.println("Searching Done!");
			//Saving DelayJitterLoss interfaces inside the database
			if(interfacePairsXWIN != null || interfacePairsLHCOPN != null && interfacePairsGEANT != null ){
				System.out.println("Save DelayJitterLossInterfaces inside Database...");
				System.out.println("Saving X-WiN Interfaces...");
				//save the DelayJitterLossInterfaces into the Database (specifically into the DelayJitterLossInterface ColumnFamily)
				for (DelayJitterLossInterfacePair fetchedXWINInterfaces : interfacePairsXWIN)
				{
					//Debug: if-clause to keep the amount of fetched data minimal
					if(countXWIN<5) {
						DelayJitterLossInterfaceDB dbDJLInterfaceObj = new DelayJitterLossInterfaceDB(
								"X-WiN", 
								fetchedXWINInterfaces.getSource(), fetchedXWINInterfaces.getDestination()
								);
						dbDJLInterfaceObj.setDelayJitterLossInterfaceID("interface_"+(++counterInt));
						djlinf.save(dbDJLInterfaceObj);
						countXWIN++;	
					}
				}
				System.out.println("X-Win Interfaces have been successfully saved inside the Database!");
			}
				System.out.println("Saving LHCOPN Interfaces...");
				for (DelayJitterLossInterfacePair fetchedLHCOPNInterfaces : interfacePairsLHCOPN)
				{
					//Debug: if-clause to keep the amount of fetched data minimal
					if(countLHCOPN<5) {
						DelayJitterLossInterfaceDB dbDJLInterfaceObj = new DelayJitterLossInterfaceDB(
								"LHCOPN",
								fetchedLHCOPNInterfaces.getSource(), fetchedLHCOPNInterfaces.getDestination()
								);
						dbDJLInterfaceObj.setDelayJitterLossInterfaceID("interface_"+(++counterInt));
						djlinf.save(dbDJLInterfaceObj);
						countLHCOPN++;	
					}
				}
				System.out.println("LHCOPN Interfaces have been successfully saved inside the Database!");	
			
			//At the moment it is not possible to fetch any GEANT interfaces!
			if(interfacePairsGEANT != null ) {
				System.out.println("Saving GEANT Interfaces...");
				for (DelayJitterLossInterfacePair fetchedGEANTInterfaces : interfacePairsGEANT)
				{
					//Debug: if-clause to keep the amount of fetched data minimal
					if(countGEANT<5) {
						DelayJitterLossInterfaceDB dbDJLInterfaceObj = new DelayJitterLossInterfaceDB(
								"GEANT",
								fetchedGEANTInterfaces.getSource(), fetchedGEANTInterfaces.getDestination()
								);
						dbDJLInterfaceObj.setDelayJitterLossInterfaceID("interface_"+(++counterInt));
						djlinf.save(dbDJLInterfaceObj);
						countGEANT++;	
								}
							}
				System.out.println("GEANT Interfaces have been successfully saved inside the Database!");
				}
			
			System.out.println("All successfully fetched DelayJitterLoss interfaces are now inside the database!" );
		}
	
	 
	 	/**
	 	 * initialize the Data of DJL Data is initialized in a concurrent fashion
	 	*/
	 	public void initDJLData()
	 	{

	 		System.out.println("init DJLData: "); 
	 		//Intialize the DelayJitterLossDataDB DAO Object with the perfSONAR Keyspace
	 		RequestTO requestTO = new RequestTO();
	 		this.djld = new DelayJitterLossDataDBDAOImpl(dbDJLcas.getKeyspace());
	 		List<DelayJitterLossServiceDB>     services = new LinkedList<DelayJitterLossServiceDB>();
	 		List<DelayJitterLossInterfaceDB> interfaces = new LinkedList<DelayJitterLossInterfaceDB>();
	 		List<DelayJitterLossDataDB> saveDataList    = new LinkedList<DelayJitterLossDataDB>();
	 		List<DelayJitterLossDataDB> readSavedData   = new LinkedList<DelayJitterLossDataDB>();
	 		List<DelayJitterLossData> fetchedDJLdata    = new LinkedList<DelayJitterLossData>();
	 		
	 		
	 		// get all services		
	 		services = djls.find();
	 		//get all interfaces
	 		interfaces = djlinf.find();

			// init data
	 		try {
			
	 			fetchedDJLdata = getDelayJitterLossData(requestTO, services, interfaces);
			
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
	 		
	 		//Take the servicename out of the Object and delete it from the list.
	 		//It only carries the servicename
	 		String servicename = fetchedDJLdata.get(0).getServiceName();
	 		System.out.println(servicename);
	 		fetchedDJLdata.remove(0);
		
	 		Iterator<DelayJitterLossData> itData = fetchedDJLdata.iterator();
	 		
	 		DelayJitterLossDataDB dataDB;
	 		int intcounter = 0;
	 		DelayJitterLossData itDataValues; 
	 		while(itData.hasNext()){
			
	 			itDataValues =  itData.next();
			 
	 			dataDB = new DelayJitterLossDataDB();
	 			dataDB.setServiceName(servicename);
	 			dataDB.setSrcInterface(itDataValues.getSender());
	 			dataDB.setDestInterface(itDataValues.getReceiver());
	 			dataDB.setMaxDelay(String.valueOf(itDataValues.getMaxDelay()));
	 			dataDB.setMinDelay(String.valueOf(itDataValues.getMinDelay()));
	 			dataDB.setMaxJitter(String.valueOf(itDataValues.getMaxIpdvJitter()));
	 			dataDB.setMinJitter(String.valueOf(itDataValues.getMinIpdvJitter()));
	 			dataDB.setTimestamp(String.valueOf(itDataValues.getTime()));
	 			dataDB.setloss(String.valueOf(itDataValues.getLoss()));
	 			dataDB.setDelayJitterLossDataID("dataid_"+(++intcounter));
	 			saveDataList.add(dataDB);
			 
	 		}
	 		for(DelayJitterLossDataDB djldata : saveDataList){
	 			System.out.println(djldata);	
	 			djld.save(djldata);
	 		}
		 
	 		readSavedData = djld.find();
		
	 		for(DelayJitterLossDataDB readData : readSavedData){
	 			System.out.println(readData);
	 		}
	}	
	 	
	@Override
	public List<DelayJitterLossData> getDelayJitterLossData(RequestTO to, List<DelayJitterLossServiceDB> services,
			List<DelayJitterLossInterfaceDB> interfaces)
			throws ParseException, FindNothingException, FetchNothingException {
			
			//To Request Services from perfSONAR
			IServerRequest request = new PerfsonarRequest();
			List<DelayJitterLossData> fetchDJLdata = new LinkedList<DelayJitterLossData>();
			new GregorianCalendar();
			
			//Set a random start and end date for the fetched data
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
	
				try
				{
					fetchDJLdata = request.getDelayJitterLossData(interfaces, services,start.getTimeInMillis(), end.getTimeInMillis());		 		
				}
				catch (FetchFailException e) {
						e.printStackTrace();
				}			
				return fetchDJLdata;		
	}

	
	/**************************************************************************************************************************/
	/*                                               Throughput                                                               */
	/**************************************************************************************************************************/
	public void initThroughMetaData()
	{
		IServerRequest request = new PerfsonarRequest();
		System.out.println("Begin initiate ThroughputMetaData " + convertDateToString(System.currentTimeMillis()));
		List<ThroughputServiceDB> serviceList = new LinkedList<ThroughputServiceDB>();
		List<ThroughputInterfaceDB> interfaceList = new LinkedList<ThroughputInterfaceDB>();
		serviceList = getThroughputService(null);
		//DEBUG
		for(ThroughputServiceDB theTServices : serviceList){
			System.out.println(theTServices.getServiceName());
		}
		
		interfaceList = getThroughputInterface(request);
		//DEBUG
		for(ThroughputInterfaceDB theInterfaces : interfaceList){
			System.out.println(theInterfaces.getServiceName());
		}
		
		System.out.println("Ende init ThroughputMetaData " + convertDateToString(System.currentTimeMillis()));
	}
	
	@Override
	public List<ThroughputServiceDB> getThroughputService(RequestTO to)
			throws FindNothingException, FetchNothingException {
		
		//To Request Services from perfSONAR
		IServerRequest request = new PerfsonarRequest();
		//Intialize the ThroughputServiceDB DAO Object with the perfSONAR Keyspace
		this.ts = new ThroughputServiceDBDAOImpl(dbTcas.getKeyspace());
		Map<String, String> servicePairs = new HashMap<String,String>();
		
		ThroughputServiceDB dbTServiceObj = new ThroughputServiceDB();
		List<ThroughputServiceDB> throughputServiceList = new LinkedList<ThroughputServiceDB>();
		//Counts the serviceID´s
		int counterSer = 0;
		
		try {
			//Fetch Services from pefSONAR and saves them into the map
			servicePairs = request.getThroughputServices();
		} catch (FetchFailException e) {
			e.printStackTrace();
		}
		
		Iterator it = servicePairs.entrySet().iterator();
		//Goes through the services, creates DelayJitterLossDB Objects which are then saved inside the Database
		while(it.hasNext())
		{
			Map.Entry<String,String> pairs = (Map.Entry)it.next();
			System.out.println("Found Service: "+pairs.getKey());
			dbTServiceObj = new ThroughputServiceDB(pairs.getKey(), pairs.getValue());
			//Add the ID to the services
			dbTServiceObj.setThroughputID("service_"+(++counterSer));
			dbTServiceObj.getServiceName();
			throughputServiceList.add(dbTServiceObj);
			this.ts.save(dbTServiceObj);
		}
		return throughputServiceList;
	}
	
	@Override
	public List<ThroughputInterfaceDB> getThroughputInterface(IServerRequest request) 
			throws FindNothingException, FetchNothingException
	{
		IServerRequest inferfaceRequest = request;
		inferfaceRequest = new PerfsonarRequest();
		
		//Intialize the ThroughputInterfaceDB DAO Object with the perfSONAR Keyspace
		this.tinf = new ThroughputInterfaceDBDAOImpl(dbTcas.getKeyspace());
	 	//Holds the found Services
		List<ThroughputServiceDB> searchTservices = new LinkedList<ThroughputServiceDB>();
		//Saving the interfaces for the different Services
		List<ThroughputInterfacePair> interfacePairsGEANT = new LinkedList<ThroughputInterfacePair>();
		List<ThroughputInterfaceDB> throughputInterfaceList = new LinkedList<ThroughputInterfaceDB>();
		String urlGEANT = "";
		GregorianCalendar grec = new GregorianCalendar();
		//Counts the InterfacesID´s
		int counterInt  = 0;
		//Debug: Holds the number of fetched Interfaces down
		int countGEANT  = 0;
		

		//Get the current time for a request
		long aktTime = grec.getTimeInMillis();
		
		//search for Throughput Services
		searchTservices = ts.find();
		if(searchTservices.isEmpty())
			throw new FindNothingException("fail to find any Throughput Services");
		
		
		//pull the URLs out of the DelayJitterLossService Data in order to request the Interfaces
		System.out.println("Searching for Throughput Interfaces...");
		for(ThroughputServiceDB serviceURLs : searchTservices){
			 //Request for DelayJitterLoss Interfaces with Servicename GEANT
			if(serviceURLs!=null && serviceURLs.getServiceName().equals("GEANT SQL-MA")) {
	    	 		urlGEANT = serviceURLs.getServiceURL();
	    	 		System.out.println("url: "+urlGEANT);
	    	 		try {
	    	 				interfacePairsGEANT = inferfaceRequest.getThroughputInterfacePairs(urlGEANT, aktTime - (1 * 60 * 60 * 1000), aktTime);
	    	 			} catch (FetchFailException e) {
	    	 					e.printStackTrace();
	    	 			}
			}	
		}
		System.out.println("Searching Done!");
		
		//Saving Throughput interfaces inside the database
		if(interfacePairsGEANT != null ) {
			System.out.println("Saving GEANT Interfaces...");
			for (ThroughputInterfacePair fetchedGEANTInterfaces : interfacePairsGEANT)
			{
				//Debug: if-clause to keep the amount of fetched data minimal
				if(countGEANT<20) {
					ThroughputInterfaceDB dbTInterfaceObj = new ThroughputInterfaceDB(
							"GEANT",
							fetchedGEANTInterfaces.getSrcAddress(), fetchedGEANTInterfaces.getDestAddress(),
							fetchedGEANTInterfaces.getMid()
							);
					dbTInterfaceObj.setThroughputID("interface_"+(++counterInt));
					throughputInterfaceList.add(dbTInterfaceObj);
					tinf.save(dbTInterfaceObj);
					countGEANT++;	
							}
						}
			System.out.println("GEANT Interfaces have been successfully saved inside the Database!");
			}
		
		System.out.println("All successfully fetched Throughput interfaces are now inside the database!" );
		
		return throughputInterfaceList;
	}
	
	
	
	
	public void initThroughData()
	{
		System.out.println("init ThroughputData: "); 
 		RequestTO requestTO = new RequestTO();
 		//Intialize the ThroughputDataDB DAO Object with the perfSONAR Keyspace
 		this.td = new ThroughputDataDBDAOImpl(dbTcas.getKeyspace());
 		List<ThroughputServiceDB>     services = new LinkedList<ThroughputServiceDB>();
 		List<ThroughputInterfaceDB> interfaces = new LinkedList<ThroughputInterfaceDB>();
 		List<ThroughputDataDB> saveDataList    = new LinkedList<ThroughputDataDB>();
 		List<ThroughputDataDB> readSavedData   = new LinkedList<ThroughputDataDB>();
 		List<ThroughputData> fetchedTdata    = new LinkedList<ThroughputData>();
 		List<ThroughputInterfacePair> tInPa = new LinkedList<ThroughputInterfacePair>();
 		// get all services		
 		services = ts.find();
 		//get all interfaces
 		interfaces = tinf.find();
 		
 		
 		
 	// init data
 		try {
		
 			fetchedTdata = getThroughputData(requestTO, services, interfaces);
		
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
	}
	
	/*public void initDJLData()
	 	{

	 		System.out.println("init DJLData: "); 
	 		//Intialize the DelayJitterLossDataDB DAO Object with the perfSONAR Keyspace
	 		RequestTO requestTO = new RequestTO();
	 		this.djld = new DelayJitterLossDataDBDAOImpl(dbDJLcas.getKeyspace());
	 		List<DelayJitterLossServiceDB>     services = new LinkedList<DelayJitterLossServiceDB>();
	 		List<DelayJitterLossInterfaceDB> interfaces = new LinkedList<DelayJitterLossInterfaceDB>();
	 		List<DelayJitterLossDataDB> saveDataList    = new LinkedList<DelayJitterLossDataDB>();
	 		List<DelayJitterLossDataDB> readSavedData   = new LinkedList<DelayJitterLossDataDB>();
	 		List<DelayJitterLossData> fetchedDJLdata    = new LinkedList<DelayJitterLossData>();
	 		
	 		
	 		// get all services		
	 		services = djls.find();
	 		//get all interfaces
	 		interfaces = djlinf.find();

			// init data
	 		try {
			
	 			fetchedDJLdata = getDelayJitterLossData(requestTO, services, interfaces);
			
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
	 		
	 		//Take the servicename out of the Object and delete it from the list.
	 		//It only carries the servicename
	 		String servicename = fetchedDJLdata.get(0).getServiceName();
	 		System.out.println(servicename);
	 		fetchedDJLdata.remove(0);
		
	 		Iterator<DelayJitterLossData> itData = fetchedDJLdata.iterator();
	 		
	 		DelayJitterLossDataDB dataDB;
	 		int intcounter = 0;
	 		DelayJitterLossData itDataValues; 
	 		while(itData.hasNext()){
			
	 			itDataValues =  itData.next();
			 
	 			dataDB = new DelayJitterLossDataDB();
	 			dataDB.setServiceName(servicename);
	 			dataDB.setSrcInterface(itDataValues.getSender());
	 			dataDB.setDestInterface(itDataValues.getReceiver());
	 			dataDB.setMaxDelay(String.valueOf(itDataValues.getMaxDelay()));
	 			dataDB.setMinDelay(String.valueOf(itDataValues.getMinDelay()));
	 			dataDB.setMaxJitter(String.valueOf(itDataValues.getMaxIpdvJitter()));
	 			dataDB.setMinJitter(String.valueOf(itDataValues.getMinIpdvJitter()));
	 			dataDB.setTimestamp(String.valueOf(itDataValues.getTime()));
	 			dataDB.setloss(String.valueOf(itDataValues.getLoss()));
	 			dataDB.setDelayJitterLossDataID("dataid_"+(++intcounter));
	 			saveDataList.add(dataDB);
			 
	 		}
	 		for(DelayJitterLossDataDB djldata : saveDataList){
	 			System.out.println(djldata);	
	 			djld.save(djldata);
	 		}
		 
	 		readSavedData = djld.find();
		
	 		for(DelayJitterLossDataDB readData : readSavedData){
	 			System.out.println(readData);
	 		}
	}	
	 	
	*/
	
	public List<ThroughputData> getThroughputData(RequestTO to, List<ThroughputServiceDB> services,
			List<ThroughputInterfaceDB> interfaces) throws ParseException, FindNothingException, FetchNothingException{
				
		//To Request Services from perfSONAR
		IServerRequest request = new PerfsonarRequest();
		List<ThroughputServiceDB> serviceList = services;
		List<ThroughputInterfaceDB> interfaceList = interfaces;
		List<ThroughputData> fetchDJLdata = new LinkedList<ThroughputData>();
		new GregorianCalendar();
		
		//Set a random start and end date for the fetched data
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
		int till_minute = 10;
		end.set(till_year, till_month, till_day, till_hour, till_minute);
		
		String urlData = "";
		
		urlData = serviceList.get(0).getServiceURL();
		

			try
			{
				fetchDJLdata = request.getThroughputData(interfaceList, serviceList,start.getTimeInMillis(), end.getTimeInMillis());		 		
			}
			catch (FetchFailException e) {
					e.printStackTrace();
			}			
			return fetchDJLdata;
		
	}

	/**************************************************************************************************************************/
	/*                                               Utilization                                                              */
	/**************************************************************************************************************************/
	public void initUtilMetaData()
	{
		System.out.println("Begin to initiate UtilMetaData " + convertDateToString(System.currentTimeMillis())); 
		System.out.println("Fetch UtilizationService...");
		try {
			//Get DelayJitterLoss Services
			getUtilizationService(null);
		} catch (FindNothingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (FetchNothingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	System.out.println("Finish init UtilMetaData " + convertDateToString(System.currentTimeMillis()));	
	}
	
	@Override
	public List<UtilizationServiceTO> getUtilizationService(RequestTO to) {
		//To Request Services from perfSONAR
		IServerRequest request = new PerfsonarRequest();
		//Intialize the Utilization ServiceDB DAO Object with the perfSONAR Keyspace
		this.us = new UtilizationServiceDBDAOImpl(dbUcas.getKeyspace());
		Map<String, String> servicePairs = new HashMap<String,String>();
				
		UtilizationServiceDB dbUServiceObj = new UtilizationServiceDB();
		//Counts the serviceID´s
		int counterSer = 0;
		
		try {
			//Fetch Services from pefSONAR and saves them into the map
			servicePairs = request.getUtilizationServices();
		} catch (FetchFailException e) {
			e.printStackTrace();
		}
		Iterator it = servicePairs.entrySet().iterator();
		//Goes through the services, creates DelayJitterLossDB Objects which are then saved inside the Database
		while(it.hasNext())
		{
			Map.Entry<String,String> pairs = (Map.Entry)it.next();
			System.out.println("Found Service: "+pairs.getKey());
			dbUServiceObj = new UtilizationServiceDB(pairs.getKey(), pairs.getValue());
			//Add the ID to the services
			dbUServiceObj.setUtilServiceID("service_"+(++counterSer));
			dbUServiceObj.getServiceName();
			this.us.save(dbUServiceObj);
		}
		
		//Get DelayJitterLossInterfaces
		getUtilizationInterface(to);
		return null;	
		
	}
	
	@Override
	public void getUtilizationInterface(RequestTO to) {
		
		//To Request Interfaces from perfSONAR
		IServerRequest request = new PerfsonarRequest();
		//Intialize the UtilizationInterfaceDB DAO Object with the perfSONAR Keyspace
	 	this.uinf = new UtilizationInterfaceDBDAOImpl(dbUcas.getKeyspace());
	 	//Holds the found Services
		List<UtilizationServiceDB> searchUservices = new LinkedList<UtilizationServiceDB>();
		//Saving the interfaces for the different Services
		List<UtilizationInterface> interfacePairsAPAN = new LinkedList<UtilizationInterface>();
		List<UtilizationInterface> interfacePairsGEANT = new LinkedList<UtilizationInterface>();
		List<UtilizationInterface> interfacePairsINTERNET2 = new LinkedList<UtilizationInterface>();
		GregorianCalendar grec = new GregorianCalendar();
		//Needed to differentiate between the different perfSONAR Services
		String urlAPAN = "";
	 	String urlGEANT = "";
	 	String urlINTERNET2 = "";
	 	//Counts the InterfacesID´s
		int counterInt  = 0;
		//Debug: Holds the number of fetched Interfaces down
		int countAPAN      = 0;
		int countGEANT     = 0;
		int countINTERNET2 = 0;
		
		//Get the current time for a request
		long aktTime = grec.getTimeInMillis();
		
		//search for Delay Jitter Loss Services
		searchUservices = us.find();
		if(searchUservices.isEmpty())
			throw new FindNothingException("fail to find any Utilization Services");
		
		//pull the URLs out of the DelayJitterLossService Data in order to request the Interfaces
		//doesn´t fetch any relevant parameters
		System.out.println("Searching for Utilization Interfaces...");
		for(UtilizationServiceDB serviceURLs : searchUservices){
			
			//Request for Utilization Interfaces with Servicename APAN-JP
			if(serviceURLs!=null && serviceURLs.getServiceName().equals("APAN-JP")) {	
			    urlAPAN = serviceURLs.getServiceURL();
			    System.out.println("url: "+urlAPAN);
			    try {
			    	interfacePairsAPAN = request.getUtilizationInterfaces(urlAPAN);
					 
				} catch (FetchFailException e) {
					e.printStackTrace();
				}
			    
			 //Request for Utilization Interfaces with Servicename GEANT PRODUCTION7
			 //Currently the only useful intferface
		     } else if(serviceURLs!=null && serviceURLs.getServiceName().equals("GEANT PRODUCTION")) {
		    	 		urlGEANT = serviceURLs.getServiceURL();
		    	 		System.out.println("url: "+urlGEANT);
		    	 		try {
		    	 				interfacePairsGEANT = request.getUtilizationInterfaces(urlGEANT);
		    	 			} catch (FetchFailException e) {
		    	 					e.printStackTrace();
		    	 			}
		    	 		
		    	 	//Request for Utilization Interfaces with Servicename INTERNET2	
		    	    //Currently throws an Error at XML2Java Class
//		     		}else if(serviceURLs!=null && serviceURLs.getServiceName().equals("INTERNET2")){
//		     					urlINTERNET2 = serviceURLs.getServiceURL();
//		     					System.out.println("url: "+urlINTERNET2);
//		     					try {
//		     						interfacePairsINTERNET2 = request.getUtilizationInterfaces(urlINTERNET2);
//		     					} catch (FetchFailException e) {			    	 						
//		    	 						e.printStackTrace();
//		     					}
//		     				}	
		}
			System.out.println("Searching Done!");
			//Saving Utilization interfaces inside the database
			if(interfacePairsAPAN != null || interfacePairsGEANT != null ){
				System.out.println("Save Utilization interfaces inside Database...");
				System.out.println("Saving APAN-JP Interfaces...");
				//save the DelayJitterLossInterfaces into the Database (specifically into the DelayJitterLossInterface ColumnFamily)
				for (UtilizationInterface fetchedAPANInterfaces : interfacePairsAPAN)
				{
					//Debug: if-clause to keep the amount of fetched data minimal
					if( countAPAN<250 && fetchedAPANInterfaces.getDirection()!=null) {
						UtilizationInterfaceDB dbUInterfaceObj = new UtilizationInterfaceDB(
								
								fetchedAPANInterfaces.getName(), fetchedAPANInterfaces.getCapacity(),
								fetchedAPANInterfaces.getIpType(), fetchedAPANInterfaces.getDirection()
								);
						dbUInterfaceObj.setServiceName("APAN-JP");
						dbUInterfaceObj.setutilInterfaceID("interface_"+(++counterInt));
						uinf.save(dbUInterfaceObj);
						countAPAN++;	
					}
				}
				System.out.println("APAN-JP Interfaces have been successfully saved inside the Database!");
			
				System.out.println("Saving GEANT PRODUCTION Interfaces...");
				for (UtilizationInterface fetchedGEANTInterfaces : interfacePairsGEANT)
				{
					//Debug: if-clause to keep the amount of fetched data minimal
					if(countGEANT<5 && fetchedGEANTInterfaces.getDirection()!=null) {
						UtilizationInterfaceDB dbUInterfaceObj = new UtilizationInterfaceDB(
								fetchedGEANTInterfaces.getName(), fetchedGEANTInterfaces.getCapacity(),
								fetchedGEANTInterfaces.getIpType(), fetchedGEANTInterfaces.getDescription()
								);
						dbUInterfaceObj.setServiceName("GEANT PRODUCTION");
						dbUInterfaceObj.setutilInterfaceID("interface_"+(++counterInt));
						uinf.save(dbUInterfaceObj);
						countGEANT++;	
					}
				}
				System.out.println("GEANT PRODUCTION Interfaces have been successfully saved inside the Database!");	
			
			
			System.out.println("All successfully fetched Utilization interfaces are now inside the database!" );
		}
		}
		
	}

		public void initUtilData()
		{
			System.out.println("init Util Data: "); 
	 		//Intialize the DelayJitterLossDataDB DAO Object with the perfSONAR Keyspace
	 		RequestTO requestTO = new RequestTO();
	 		this.ud = new UtilizationDataDBDAOImpl(dbUcas.getKeyspace());
	 		List<UtilizationServiceDB>     services = new LinkedList<UtilizationServiceDB>();
	 		List<UtilizationInterfaceDB> interfaces = new LinkedList<UtilizationInterfaceDB>();
	 		List<UtilizationDataDB> saveDataList    = new LinkedList<UtilizationDataDB>();
	 		List<UtilizationDataDB> readSavedData   = new LinkedList<UtilizationDataDB>();
	 		List<UtilizationData> fetchedUdata    = new LinkedList<UtilizationData>();
	 		
	 		// get all services		
	 		services = us.find();
	 		//get all interfaces
	 		interfaces = uinf.find();
	 		
			// init data
	 		try {
			
	 			fetchedUdata = getUtilizationData(requestTO, services, interfaces);
			
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
	 		
		
					
	 		//Take the servicename out of the Object and delete it from the list.
	 		//It only carries the servicename
	 		String servicename = fetchedUdata.get(0).getserviceName();
	 		System.out.println(servicename);
	 		fetchedUdata.remove(0);
		
	 		Iterator<UtilizationData> itData = fetchedUdata.iterator();
	 		
	 		UtilizationDataDB dataDB;
	 		int intcounter = 0;
	 		UtilizationData itDataValues; 
	 		while(itData.hasNext()){
			
	 			itDataValues =  itData.next();
			 
	 			dataDB = new UtilizationDataDB();
	 			dataDB.setServiceName(servicename);
	 			dataDB.setMeasuredUtil(String.valueOf(itDataValues.getValue()));
	 			dataDB.setValueUnits(itDataValues.getValueUnits());
	 			dataDB.setTimestamp(String.valueOf(itDataValues.getTime()));
	 			
	 			
	 			dataDB.setutilDataID("dataid_"+(++intcounter));
	 			saveDataList.add(dataDB);
			 
	 		}
	 		for(UtilizationDataDB udata : saveDataList){
	 			System.out.println(udata);	
	 			ud.save(udata);
	 		}
		 
	 		readSavedData = ud.find();
		
	 		for(UtilizationDataDB readData : readSavedData){
	 			System.out.println(readData);
	 		}
	}	
	 	
	@Override
	public List<UtilizationData> getUtilizationData(RequestTO to, List<UtilizationServiceDB> services,
			List<UtilizationInterfaceDB> interfaces)
			throws ParseException, FindNothingException, FetchNothingException {
			
			List<UtilizationInterfaceDB> interfaceList = interfaces;
			List<UtilizationServiceDB> serviceList = services;
			//To Request Services from perfSONAR
			IServerRequest request = new PerfsonarRequest();
			List<UtilizationData> fetchUdata = new LinkedList<UtilizationData>();
			new GregorianCalendar();
			
			//Set a random start and end date for the fetched data
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
			int till_minute = 5;
			end.set(till_year, till_month, till_day, till_hour, till_minute);
	
				try
				{
					fetchUdata = request.getUtilizationData(serviceList, interfaceList, start.getTimeInMillis(), end.getTimeInMillis());		 		
				}
				catch (FetchFailException e) {
						e.printStackTrace();
				}			
				return fetchUdata;		
	}


	/**************************************************************************************************************************/
	/*                                                 Convert Methods                                                        */
	/**************************************************************************************************************************/
		
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
	

	/**************************************************************************************************************************/
	/*                                              NOT NEEDED RIGHT NOW                                                      */
	/**************************************************************************************************************************/

	@Override
	public List<PathSegmentsGetDataTO> getPathSegmentsGetData(RequestTO to) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<ThroughputGetDataTO> getThrougputGetData(RequestTO to)
			throws ParseException, FetchNothingException, FindNothingException {
		// TODO Auto-generated method stub
		return null;
	}
	

	@Override
	public List<DashboardDelayGetDataTO> getDashboardDelayGetData(RequestTO to)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DashboardJitterGetDataTO> getDashboardJitterGetData(RequestTO to)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DashboardLossGetDataTO> getDashboardLossGetData(RequestTO to)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DashboardUDGetDataTO> getDashboardUDGetData(RequestTO to)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UtilizationDataTO> getUtilizationData(RequestTO to)
			throws ParseException, FetchNothingException, FindNothingException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DelayJitterLossInterfaceTO> getDelayJitterLossInterface(
			RequestTO to) throws FindNothingException, FetchNothingException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DelayJitterLossDataDB> getDelayJitterLossData(RequestTO to)
			throws ParseException, FindNothingException, FetchNothingException {
		// TODO Auto-generated method stub
		return null;
	}

}