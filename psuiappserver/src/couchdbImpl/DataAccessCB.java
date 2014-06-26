package perfsonarserver.database.couchdbImpl;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;

import com.cedarsoftware.util.io.JsonWriter;
import com.couchbase.client.CouchbaseClient;


import perfsonarserver.appConnect.RequestTO;
import perfsonarserver.database.cassandraDAO.ThroughputDataDBDAOImpl;
import perfsonarserver.database.cassandraDAO.ThroughputInterfaceDBDAOImpl;
import perfsonarserver.database.cassandraDAO.ThroughputServiceDBDAOImpl;
import perfsonarserver.database.cassandraDAO.UtilizationDataDBDAOImpl;
import perfsonarserver.database.cassandraDAO.UtilizationServiceDBDAOImpl;
import perfsonarserver.database.cassandraImpl.IDataAccess;
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

public class DataAccessCB implements IDataAccess {
	
	/** Instance of this class for the singleton design pattern. */
	private static DataAccessCB instance;
	
	/** constant for the date format in the SimpleDateFormat */
	private static final String DATE_FORMAT = "yyyy-MM-dd HH-mm-ss-SSS";
	
	
	private CouchbaseClient cbl;
	private DatabaseDJLCB djlCB;
	private DatabaseThroughputCB tCB;
	private DatabaseUtilizationCB uCB;
	
	
	/** Key-Collections for searching */
	private Collection<String> djlServiceKeyCol   = new ArrayList<String>();
	private Collection<String> djlInterfaceKeyCol = new ArrayList<String>();
	private Collection<String> djlDataKeyCol      = new ArrayList<String>();
	
	private Collection<String> throughputServiceKeyCol   = new ArrayList<String>();
	private Collection<String> throughputInterfaceKeyCol = new ArrayList<String>();
	private Collection<String> throughputDataKeyCol      = new ArrayList<String>();
	
	private Collection<String> utilizationServiceKeyCol   = new ArrayList<String>(); 
	private Collection<String> utilizationInterfaceKeyCol = new ArrayList<String>();
	private Collection<String> utilizationDataKeyCol   = new ArrayList<String>();
    
	/** JSON-Keys for request and response objects */
	private static final String JS_FEATURE = "FeatureName";
	private static final String JS_SERVICE = "Service";
	private static final String JS_SOURCE = "SourceInterface";
	private static final String JS_DEST = "DestinationInterface";
	private static final String JS_START = "StartTime";
	private static final String JS_END = "EndTime";
	private static final String JS_CACHE = "GetNonCachedData";
	private static final String JS_INFO = "Info";
	private static final String JS_INTERFACES = "Interfaces";
	private static final String JS_VALUES = "MeasuredValues";

	/**
	 * Initializes this class and creates the singleton object cacheMaxDays(30),
	 * cacheSectorSizeHours(4) and cacheDeleteDays(2) to set by default
	 * 
	 * @return the singleton instance
	 *
	 */
	public static DataAccessCB getInstance() 
	{
		if (instance == null)
			// 1 is for test use
			instance = new DataAccessCB(1, 1, 24, 1, 10, 10, 10);
		return instance;
	}
	
	private DataAccessCB(int cacheMaxDays, int cacheDJLSectorSizeHours, int cacheThroughSectorSizeHours, int cacheUtilSectorSizeHours, int cacheDJLDeleteDays, int cacheThroughDeleteDays, int cacheUtilDeleteDays) 
	{	
		//this.djlCB = DatabaseDJLCB.getInstance();
		this.tCB   = DatabaseThroughputCB.getInstance();
		this.uCB   = DatabaseUtilizationCB.getInstance();
		// cbl = djlCB.client;
//		try {
//			testCouch();
//		} catch (InterruptedException e) {
//			
//			e.printStackTrace();
//		} catch (ExecutionException e) {
//			
//			e.printStackTrace();
//		}
	
	
		// fill db with services and interface pairs.
//		try {
//			initDJLMetaData();
//			//initDJLData();
//			
//			//testCouch();
//		} catch (InterruptedException e) {
//			
//			e.printStackTrace();
//		} catch (ExecutionException e) {
//			
//			e.printStackTrace();
//		}	
		
		//cbl = tCB.client;
//		try {
//			initThroughMetaData();
//		   // initThroughData();
//		} catch (FindNothingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (FetchNothingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (Throwable e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		cbl = uCB.client;
		
		try {
			initUtilMetaData();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		   // initUtilData();
		
		System.out.println("finish");

	}
	/**************************************************************************************************************************/
	/*                                                Test Couchbase                                                          */
	/**************************************************************************************************************************/
	
	public void testCouch() throws InterruptedException, ExecutionException{

		// Store a Document
		DelayJitterLossDataDB djldata1 = new DelayJitterLossDataDB();
		/* DJL Data*/
		String timestamp = "5000"; 
		String maxDelay = "40.23";
		String minDelay = "0.5"; 
		String maxJitter = "24.42"; 
	    String minJitter = "1.2"; 
	    String loss = "7"; 
	    String service = "GEANT"; 
	    String srcInterface = "SOFIA_bla"; 
	    String destInterface = "Stodo_blub";
		String json3 = "";
		djldata1 = new DelayJitterLossDataDB(timestamp, maxDelay, minDelay, maxJitter, minJitter, loss);
		djldata1.setServiceName(service);
		djldata1.setSrcInterface(srcInterface);
		djldata1.setDestInterface(destInterface);
		try {
			 json3 = JsonWriter.objectToJson(djldata1);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		cbl.set("DelayJitterLossData", json3).get();
	    // Retreive the Document and print it
	    System.out.println(cbl.get("DelayJitterLossData"));
	    // Shutting down properly
	    cbl.shutdown();
		
	}
	
	
	/**************************************************************************************************************************/
	/*                                             Delay Jitter Loss                                                          */
	/**************************************************************************************************************************/
	/**
	 * initialize the Services and Interfaces of DJL Services is read from file
	 * Interfaces is initialized in a concurrent fashion
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	public void initDJLMetaData() throws InterruptedException, ExecutionException
	{
			System.out.println("Begin to initiate DJLMetaData " + convertDateToString(System.currentTimeMillis())); 
			System.out.println("Fetch DelayJitterLossService...");
			try {
				getDelayJitterLossService(null);
			} catch (FindNothingException e1) {
				
				e1.printStackTrace();
			} catch (FetchNothingException e1) {
				
				e1.printStackTrace();
			}
			
		System.out.println("Finish init DJLMetaData " + convertDateToString(System.currentTimeMillis()));
	}
	
	/**
	 * searching for the DelayJitterLossService in the db and returns a list of
	 * them if no data inside cache or data too old delayJitterLossServiceCheck
	 * will load data.
	 * 
	 * @param to
	 *            the request object
	 * @return a list of all services
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 * @throws FetchFailException 
	 */
	@Override
	public void getDelayJitterLossService(RequestTO to) throws InterruptedException, ExecutionException{
		
		IServerRequest request = new PerfsonarRequest();
		DelayJitterLossServiceDB dbDJLServiceObj = new DelayJitterLossServiceDB();
		Map<String, String> servicePairs = new HashMap<String,String>();
		String djlServiceGEANTtoJson = "";
		String djlServiceXWINtoJson = "";
		String djlServiceLHCOPNtoJson = "";
		String serviceName = "";
		int zaehler = 0;
		
		try {
			servicePairs = request.getDelayJitterLossServices();
		} catch (FetchFailException e) {
			e.printStackTrace();
		}
		
		Iterator it = servicePairs.entrySet().iterator();
		
		while(it.hasNext())
		{
			Map.Entry<String,String> pairs = (Map.Entry)it.next();
			System.out.println("Found Service: "+pairs.getKey());
			dbDJLServiceObj = new DelayJitterLossServiceDB(pairs.getKey(), pairs.getValue());
			dbDJLServiceObj.setDelayJitterLossServiceID("service_"+(++zaehler));
			djlServiceKeyCol.add(dbDJLServiceObj.getDelayJitterLossServiceID());
			serviceName = dbDJLServiceObj.getServiceName();
				//Differentiate between the fetched Services,convert them into JSON Objects
			    //and save them inside the Database
				if(serviceName.equalsIgnoreCase("GEANT")){
					try {
						djlServiceGEANTtoJson = JsonWriter.objectToJson(dbDJLServiceObj);
					} catch (IOException e) {
						
						e.printStackTrace();
					}
					cbl.set(dbDJLServiceObj.getDelayJitterLossServiceID(), djlServiceGEANTtoJson).get();
				}else if(serviceName.equalsIgnoreCase("X-WiN")){
							try {
									djlServiceXWINtoJson = JsonWriter.objectToJson(dbDJLServiceObj);
								} catch (IOException e) {
						
										e.printStackTrace();
								}
								cbl.set(dbDJLServiceObj.getDelayJitterLossServiceID(), djlServiceXWINtoJson).get();
					
					   } else if(serviceName.equalsIgnoreCase("LHCOPN")){
						   			try {
						   					djlServiceLHCOPNtoJson = JsonWriter.objectToJson(dbDJLServiceObj);
						   				} catch (IOException e) {
						
						   						e.printStackTrace();
						   				}
						   				cbl.set(dbDJLServiceObj.getDelayJitterLossServiceID(), djlServiceLHCOPNtoJson).get();	
				}
			System.out.println(cbl.get("service_1"));
		}
		
		//Get DelayJitterLossInterfaces
		getDelayJitterLossInterface(djlServiceKeyCol);		
	}
	
	/**
	 * Get DelayJitterLossInterfaces
	 */
	public void getDelayJitterLossInterface(Collection<String> serviceKeys
			) throws FindNothingException, FetchNothingException {
		
		Collection<String> djlServicKeys = serviceKeys;
		String json2 = "";
		List<DelayJitterLossServiceDB> searchDJLservices = new LinkedList<DelayJitterLossServiceDB>();
		//Saving the interfaces for the different Services
		List<DelayJitterLossInterfacePair> interfacePairsXWIN = new LinkedList<DelayJitterLossInterfacePair>();
		List<DelayJitterLossInterfacePair> interfacePairsGEANT = new LinkedList<DelayJitterLossInterfacePair>();
		List<DelayJitterLossInterfacePair> interfacePairsLHCOPN = new LinkedList<DelayJitterLossInterfacePair>();
		IServerRequest request = new PerfsonarRequest();
		GregorianCalendar grec = new GregorianCalendar();
		String urlXWIN = "";
	 	String urlGEANT = "";
	 	String urlLHCOPN = "";
	 	//Counts the InterfacesID´s
		int counterInt  = 0;
		//Debug: Holds the number of fetched Interfaces down
		int countXWIN   = 0;
		int countGEANT  = 0;
		int countLHCOPN = 0;
		
		
		long aktTime = grec.getTimeInMillis();
		
		//search for Delay Jitter Loss Services
		Map<String, Object> dl = cbl.getBulk(djlServicKeys);
		
		//searchResult = cbl.get("DelayJitterLossInterface");
		if(dl.isEmpty())
			throw new FindNothingException("fail to find any Delay Jitter Loss Services");
		
		//pull the URLs out of the DelayJitterLossService Data in order to request the Interfaces
		System.out.println("Searching for DelayJitterLossInterfaces...");
		
		Iterator it = dl.entrySet().iterator();
		List<String> serviceList = new LinkedList<String>();
		while(it.hasNext())
		{
			Map.Entry<String,Object> pairs = (Map.Entry)it.next();
			
			serviceList.add((String)pairs.getValue());
			
			System.out.println("aus der map: "+serviceList);
		}
		
		//Convert the List Type from String to DelayJitterLossServiceDB
		searchDJLservices = convertServiceMap(serviceList);
		
		
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
		
		List<DelayJitterLossInterfaceDB> allInterfaces = new LinkedList<DelayJitterLossInterfaceDB>();
		
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
					djlInterfaceKeyCol.add(dbDJLInterfaceObj.getDelayJitterLossInterfaceID());
					allInterfaces.add(dbDJLInterfaceObj);
					try {
						 json2 = JsonWriter.objectToJson(dbDJLInterfaceObj);
					} catch (IOException e) {
						
						e.printStackTrace();
					}
					cbl.set(dbDJLInterfaceObj.getDelayJitterLossInterfaceID(), json2);
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
					djlInterfaceKeyCol.add(dbDJLInterfaceObj.getDelayJitterLossInterfaceID());
					allInterfaces.add(dbDJLInterfaceObj);
					try {
						 json2 = JsonWriter.objectToJson(dbDJLInterfaceObj);
					} catch (IOException e) {
					
						e.printStackTrace();
					}
					cbl.set(dbDJLInterfaceObj.getDelayJitterLossInterfaceID(), json2);
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
					djlInterfaceKeyCol.add(dbDJLInterfaceObj.getDelayJitterLossInterfaceID());
					allInterfaces.add(dbDJLInterfaceObj);
					try {
						 json2 = JsonWriter.objectToJson(dbDJLInterfaceObj);
					} catch (IOException e) {
						
						e.printStackTrace();
					}
					cbl.set(dbDJLInterfaceObj.getDelayJitterLossInterfaceID(), json2);
					countGEANT++;	
							}
						}
			System.out.println("GEANT Interfaces have been successfully saved inside the Database!");
			}
		
		System.out.println("All successfully fetched DelayJitterLoss interfaces are now inside the database!" );
		initDJLData(allInterfaces);
		}
		

	/**
 	 * initialize the Data of DJL Data is initialized in a concurrent fashion
 	*/
 	public void initDJLData(List<DelayJitterLossInterfaceDB> allDJLInterfaces)
 	{

 		System.out.println("init DJLData: "); 
 		//Intialize the DelayJitterLossDataDB DAO Object with the perfSONAR Keyspace
 		RequestTO requestTO = new RequestTO();
 		
 		List<DelayJitterLossServiceDB>     services = new LinkedList<DelayJitterLossServiceDB>();
 		List<DelayJitterLossInterfaceDB> interfaces = allDJLInterfaces;
 		List<DelayJitterLossDataDB> saveDataList    = new LinkedList<DelayJitterLossDataDB>();
 		List<DelayJitterLossDataDB> readSavedData   = new LinkedList<DelayJitterLossDataDB>();
 		List<DelayJitterLossData> fetchedDJLdata    = new LinkedList<DelayJitterLossData>();
 		
 		// get all services		
 		//search for Delay Jitter Loss Services
 		Map<String, Object> serviceMap = cbl.getBulk(djlServiceKeyCol);
 				
 		if(serviceMap.isEmpty())
 				throw new FindNothingException("fail to find any Delay Jitter Loss Services");
 				
 		//Convert the Map into a List		
 		Iterator it = serviceMap.entrySet().iterator();
 		List<String> serviceList = new LinkedList<String>();
 		while(it.hasNext())
 		{
 			Map.Entry<String,Object> servicePairs = (Map.Entry)it.next();
 			serviceList.add((String)servicePairs.getValue());
 					
 			System.out.println("aus der map: "+serviceList);
 		}
 				
 		//Convert the List Type from String to DelayJitterLossServiceDB
 		services = convertServiceMap(serviceList);
 		System.out.println("Searching for DelayJitterLoss Services done");
 		
 		
		// init data
 		try {
		
 			fetchedDJLdata = getDelayJitterLossData(requestTO, services, interfaces);
		
 		} catch (FindNothingException e) {
 			
 			e.printStackTrace();
 		} catch (FetchNothingException e) {
 			
 			e.printStackTrace();
 		} catch (ParseException e) {
 			
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
 		String jsonDJLdata = "";
 		int counterInt = 0;
 		for(DelayJitterLossDataDB djldata : saveDataList){
 			System.out.println(djldata);	
 			djldata.setDelayJitterLossDataID("data_"+(++counterInt));
			djlDataKeyCol.add(djldata.getDelayJitterLossDataID());
			
			try {
				 jsonDJLdata = JsonWriter.objectToJson(djldata);
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			cbl.set(djldata.getDelayJitterLossDataID(), jsonDJLdata);
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
	/*                                                  Throughput                                                            */
 	/**************************************************************************************************************************/

	public void initThroughMetaData() throws FindNothingException, FetchNothingException, InterruptedException, Throwable{
		
		IServerRequest request = new PerfsonarRequest();
		System.out.println("Begin initiate ThroughputMetaData " + convertDateToString(System.currentTimeMillis()));
		List<ThroughputServiceDB> serviceList = new LinkedList<ThroughputServiceDB>();
		List<ThroughputInterfaceDB> interfaceList = new LinkedList<ThroughputInterfaceDB>();
		serviceList = getThroughputService(null);
		//DEBUG
		for(ThroughputServiceDB theTServices : serviceList){
			System.out.println(theTServices.getServiceName());
		}
		
		interfaceList = getThroughputInterface(request, throughputServiceKeyCol);
		//DEBUG
		for(ThroughputInterfaceDB theInterfaces : interfaceList){
			System.out.println(theInterfaces.getServiceName());
		}
		
		System.out.println("Ende init ThroughputMetaData " + convertDateToString(System.currentTimeMillis()));
		
		initThroughData(interfaceList);
	}	
 	
	
	@Override
	public List<ThroughputServiceDB> getThroughputService(RequestTO to)
			throws FindNothingException, FetchNothingException, InterruptedException, ExecutionException {
				
		IServerRequest request = new PerfsonarRequest();
		ThroughputServiceDB dbThroughServiceObj = new ThroughputServiceDB();
		Map<String, String> servicePairs = new HashMap<String,String>();
		List<ThroughputServiceDB> throughputServiceList = new LinkedList<ThroughputServiceDB>();
		String tServiceGEANTtoJson = "";
		String serviceName = "";
		int zaehler = 0;
		
		try {
			servicePairs = request.getThroughputServices();
		} catch (FetchFailException e) {
			e.printStackTrace();
		}
		
		Iterator it = servicePairs.entrySet().iterator();
		
		while(it.hasNext())
		{
			Map.Entry<String,String> pairs = (Map.Entry)it.next();
			System.out.println("Found Service: "+pairs.getKey());
			dbThroughServiceObj = new ThroughputServiceDB(pairs.getKey(), pairs.getValue());
			dbThroughServiceObj.setThroughputID("throughputService_"+(++zaehler));
			throughputServiceKeyCol.add(dbThroughServiceObj.getThroughputID());
			serviceName = dbThroughServiceObj.getServiceName();
				//Differentiate between the fetched Services,convert them into JSON Objects
			    //and save them inside the Database
			throughputServiceList.add(dbThroughServiceObj);
				if(serviceName.equalsIgnoreCase("GEANT SQL-MA")){
					try {
						tServiceGEANTtoJson = JsonWriter.objectToJson(dbThroughServiceObj);
					} catch (IOException e) {
						
						e.printStackTrace();
					}
					cbl.set(dbThroughServiceObj.getThroughputID(), tServiceGEANTtoJson).get();
				}
			System.out.println(cbl.get("service_1"));
		}		
		return throughputServiceList;
	}
	
	@Override
	public List<ThroughputInterfaceDB> getThroughputInterface(
			IServerRequest request, Collection<String> serviceKeys) throws FindNothingException,
			FetchNothingException {
		
		request = new PerfsonarRequest();
		Collection<String> tServicKeys = serviceKeys;
		String json2 = "";
		List<ThroughputServiceDB> searchTservices = new LinkedList<ThroughputServiceDB>();
		//Saving the interfaces for the different Services
		List<ThroughputInterfacePair> interfacePairsGEANT = new LinkedList<ThroughputInterfacePair>();
	
		GregorianCalendar grec = new GregorianCalendar();
	 	String urlGEANT = "";
	 
	 	//Counts the InterfacesID´s
		int counterInt  = 0;
		//Debug: Holds the number of fetched Interfaces down
		int countGEANT  = 0;
		long aktTime = grec.getTimeInMillis();
		
		//search for Delay Jitter Loss Services
		Map<String, Object> dl = cbl.getBulk(tServicKeys);
		
		//searchResult = cbl.get("DelayJitterLossInterface");
		if(dl.isEmpty())
			throw new FindNothingException("fail to find any Throughput Services");
		
		//pull the URLs out of the DelayJitterLossService Data in order to request the Interfaces
		System.out.println("Searching for ThroughputInterfaces...");
		
		Iterator it = dl.entrySet().iterator();
		List<String> serviceList = new LinkedList<String>();
		while(it.hasNext())
		{
			Map.Entry<String,Object> pairs = (Map.Entry)it.next();
			
			serviceList.add((String)pairs.getValue());
			
			System.out.println("aus der map: "+serviceList);
		}
		
		//Convert the List Type from String to DelayJitterLossServiceDB
		searchTservices = convertThroughServiceMap(serviceList);
		
		for(ThroughputServiceDB serviceURLs : searchTservices){
			
			//Request for Throughput Interfaces with Servicename GEANT SQL-MA
			if(serviceURLs!=null && serviceURLs.getServiceName().equals("GEANT SQL-MA")) {	
			    urlGEANT = serviceURLs.getServiceURL();
			    System.out.println("url: "+urlGEANT);
			    try {
			    	interfacePairsGEANT = request.getThroughputInterfacePairs(urlGEANT, aktTime - (1 * 60 * 60 * 1000), aktTime);
					 
				} catch (FetchFailException e) {
					e.printStackTrace();
				}
			  }
		}
		List<ThroughputInterfaceDB> allInterfaces = new LinkedList<ThroughputInterfaceDB>();
		System.out.println("Searching Done!");
		
		//Saving DelayJitterLoss interfaces inside the database
		if(interfacePairsGEANT != null ){
			System.out.println("Save ThroughputInterfaces inside Database...");
			System.out.println("Saving GEANT SQL-MA Interfaces...");
			//save the DelayJitterLossInterfaces into the Database (specifically into the DelayJitterLossInterface ColumnFamily)
			for (ThroughputInterfacePair fetchedGEANTInterfaces : interfacePairsGEANT)
			{
				//Debug: if-clause to keep the amount of fetched data minimal
				if(countGEANT<5) {
					ThroughputInterfaceDB dbTInterfaceObj = new ThroughputInterfaceDB("GEANT SQL-MA",
							fetchedGEANTInterfaces.getSrcAddress(), fetchedGEANTInterfaces.getDestAddress(),
							fetchedGEANTInterfaces.getMid()
							);
					dbTInterfaceObj.setThroughputID("throughputInterface_"+(++counterInt));
					throughputInterfaceKeyCol.add(dbTInterfaceObj.getThroughputID());
					allInterfaces.add(dbTInterfaceObj);
					try {
						 json2 = JsonWriter.objectToJson(dbTInterfaceObj);
					} catch (IOException e) {
						
						e.printStackTrace();
					}
					cbl.set(dbTInterfaceObj.getThroughputID(), json2);
					countGEANT++;	
				}
			}
		}
		System.out.println("GEANT SQL-MA Interfaces have been successfully saved inside the Database!");
			
		return allInterfaces;
	}
	
	public void initThroughData(List<ThroughputInterfaceDB> interfaceList){
		
		System.out.println("init ThroughputData: "); 
 		RequestTO requestTO = new RequestTO();
 		
 		List<ThroughputServiceDB>     services = new LinkedList<ThroughputServiceDB>();
 		List<ThroughputInterfaceDB> interfaces = new LinkedList<ThroughputInterfaceDB>();
 		List<ThroughputDataDB> 	  saveDataList = new LinkedList<ThroughputDataDB>();
 		List<ThroughputDataDB>   readSavedData = new LinkedList<ThroughputDataDB>();
 		List<ThroughputData>      fetchedTdata = new LinkedList<ThroughputData>();
 		List<ThroughputInterfacePair>    tInPa = new LinkedList<ThroughputInterfacePair>();
		
 		// get all services		
 	 	//search for Throughput Services
 	    Map<String, Object> serviceMap = cbl.getBulk(throughputServiceKeyCol);
 	 				
 	 	if(serviceMap.isEmpty())
 	 			throw new FindNothingException("fail to find any Throughput Services");
 	 				
 	 	//Convert the Map into a List		
 	 	Iterator it = serviceMap.entrySet().iterator();
 	 	List<String> serviceList = new LinkedList<String>();
 	 	while(it.hasNext())
 	 	{
 	 		Map.Entry<String,Object> servicePairs = (Map.Entry)it.next();
 	 		serviceList.add((String)servicePairs.getValue());
 	 					
 	 		System.out.println("aus der map: "+serviceList);
 	 	}
 	 				
 	 	//Convert the List Type from String to DelayJitterLossServiceDB
 	 	services =  convertThroughServiceMap(serviceList);
 	 	System.out.println("Searching for DelayJitterLoss Services done");
 	 	
 	 	// init data
 	 	try {
 			
 	 		fetchedTdata = getThrougputGetData(requestTO, services, interfaces);
 			
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
	
	
	
	@Override
	public List<ThroughputData> getThrougputGetData(RequestTO to, 
			List<ThroughputServiceDB> services, List<ThroughputInterfaceDB> interfaces)
			throws ParseException, FetchNothingException, FindNothingException {
		
		
		//To Request Services from perfSONAR
		IServerRequest request = new PerfsonarRequest();
		List<ThroughputServiceDB> serviceList = services;
		List<ThroughputInterfaceDB> interfaceList = interfaces;
		List<ThroughputData> fetchUdata = new LinkedList<ThroughputData>();
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
				fetchUdata = request.getThroughputData(interfaceList, serviceList,start.getTimeInMillis(), end.getTimeInMillis());		 		
			}
			catch (FetchFailException e) {
					e.printStackTrace();
			}			
			return fetchUdata;
	}
	
 	/**************************************************************************************************************************/
	/*                                                  Utilization                                                           */
 	/**************************************************************************************************************************/
 	
	public void initUtilMetaData() throws InterruptedException, Exception
	{
		System.out.println("Begin to initiate UtilMetaData " + convertDateToString(System.currentTimeMillis())); 
		System.out.println("Fetch UtilizationService...");
		try {
			//Get Utilization Services
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
	public List<UtilizationServiceTO> getUtilizationService(RequestTO to) throws InterruptedException, ExecutionException {
		
		//To Request Services from perfSONAR
		IServerRequest request = new PerfsonarRequest();
		Map<String, String> servicePairs = new HashMap<String,String>();	
		UtilizationServiceDB dbUServiceObj = new UtilizationServiceDB();
		List<UtilizationServiceDB> utilServiceList = new LinkedList<UtilizationServiceDB>();
		String uServiceGPtoJson = "";
		String uServiceAJtoJson = "";
		String uServiceI2toJson = "";
		//Counts the serviceID´s
		int counterSer = 0;
		int zaehler = 0;
		String service = "";
		
		try {
			//Fetch Services from pefSONAR and saves them into the map
			servicePairs = request.getUtilizationServices();
		} catch (FetchFailException e) {
			e.printStackTrace();
		}
		
		Iterator it = servicePairs.entrySet().iterator();
		
		while(it.hasNext())
		{
			Map.Entry<String,String> pairs = (Map.Entry)it.next();
			System.out.println("Found Service: "+pairs.getKey());
			dbUServiceObj = new UtilizationServiceDB(pairs.getKey(), pairs.getValue());
			dbUServiceObj.setUtilServiceID("utilizationService_"+(++zaehler));
			utilizationServiceKeyCol.add(dbUServiceObj.getUtilServiceID());
			utilServiceList.add(dbUServiceObj);
			service = dbUServiceObj.getServiceName();
			
			//Differentiate between the fetched Services,convert them into JSON Objects
		    //and save them inside the Database
			if(service.equalsIgnoreCase("GEANT PRODUCTION")){
				try {
					uServiceGPtoJson = JsonWriter.objectToJson(dbUServiceObj);
				} catch (IOException e) {
					
					e.printStackTrace();
				}
				cbl.set(dbUServiceObj.getUtilServiceID(), uServiceGPtoJson).get();
			}else if(service.equalsIgnoreCase("APAN-JP")){
						try {
								uServiceAJtoJson = JsonWriter.objectToJson(dbUServiceObj);
							} catch (IOException e) {
					
									e.printStackTrace();
							}
							cbl.set(dbUServiceObj.getUtilServiceID(), uServiceAJtoJson).get();
				
				   } else if(service.equalsIgnoreCase("INTERNET2")){
					   			try {
					   					uServiceI2toJson = JsonWriter.objectToJson(dbUServiceObj);
					   				} catch (IOException e) {
					
					   						e.printStackTrace();
					   				}
					   				cbl.set(dbUServiceObj.getUtilServiceID(), uServiceI2toJson).get();
				   	}
			}
		System.out.println(cbl.get("utilizationService_1"));
		//Get DelayJitterLossInterfaces
		getUtilizationInterface(to, utilServiceList);
		return null;
	}
	
	
	@Override
	public void getUtilizationInterface(RequestTO to, List<UtilizationServiceDB> utilServiceList) {
		
		List<UtilizationServiceDB> utilServices = utilServiceList;
		String json2 = "";
		List<UtilizationServiceDB> searchUservices = new LinkedList<UtilizationServiceDB>();
		//Saving the interfaces for the different Services
		List<UtilizationInterface> interfacePairsAPAN = new LinkedList<UtilizationInterface>();
		List<UtilizationInterface> interfacePairsGEANT = new LinkedList<UtilizationInterface>();
		List<UtilizationInterface> interfacePairsINTERNET2 = new LinkedList<UtilizationInterface>();
		IServerRequest request = new PerfsonarRequest();
		GregorianCalendar grec = new GregorianCalendar();
		String urlAPAN = "";
	 	String urlGEANT = "";
	 	String urlINTERNET2 = "";
	 	//Counts the InterfacesID´s
		int counterInt  = 0;
		//Debug: Holds the number of fetched Interfaces down
		int countAPAN   = 0;
		int countGEANT  = 0;
		int countINTERNET2 = 0;
		
		long aktTime = grec.getTimeInMillis();
	
		for(UtilizationServiceDB serviceURLs : utilServices){
			
			//Request for Throughput Interfaces with Servicename APAN-JP
			if(serviceURLs!=null && serviceURLs.getServiceName().equals("APAN-JP")) {	
			    urlAPAN = serviceURLs.getServiceURL();
			    System.out.println("url: "+urlAPAN);
			    try {
			    	interfacePairsAPAN = request.getUtilizationInterfaces(urlAPAN);
					 
				} catch (FetchFailException e) {
					e.printStackTrace();
				}
			    
			 //Request for Throughput Interfaces with Servicename GEANT PRODUCTION
		     } else if(serviceURLs!=null && serviceURLs.getServiceName().equals("GEANT PRODUCTION")) {
		    	 		urlGEANT = serviceURLs.getServiceURL();
		    	 		System.out.println("url: "+urlGEANT);
		    	 		try {
		    	 				interfacePairsGEANT = request.getUtilizationInterfaces(urlGEANT);
		    	 			} catch (FetchFailException e) {
		    	 					e.printStackTrace();
		    	 			}
		    	 		
		    	 	//Request for DelayJitterLoss Interfaces with Servicename LHCOPN	
		     		}
//		     else if(serviceURLs!=null && serviceURLs.getServiceName().equals("INTERNET2")){
//		     					urlINTERNET2 = serviceURLs.getServiceURL();
//		     					System.out.println("url: "+urlINTERNET2);
//		     					try {
//		     						interfacePairsINTERNET2 = request.getUtilizationInterfaces(urlINTERNET2);
//		     					} catch (FetchFailException e) {			    	 						
//		    	 						e.printStackTrace();
//		     					}
//		     				}	
		}
		List<UtilizationInterfaceDB> allInterfaces = new LinkedList<UtilizationInterfaceDB>();
		
		System.out.println("Searching Done!");
		//Saving DelayJitterLoss interfaces inside the database
		if(interfacePairsAPAN != null || interfacePairsGEANT != null ){
			System.out.println("Save ThroughputInterfaces inside Database...");
			System.out.println("Saving X-WiN Interfaces...");
			//save the DelayJitterLossInterfaces into the Database (specifically into the DelayJitterLossInterface ColumnFamily)
			for (UtilizationInterface fetchedAPANInterfaces : interfacePairsAPAN)
			{
				//Debug: if-clause to keep the amount of fetched data minimal
				if(countAPAN<5 && fetchedAPANInterfaces.getDirection()!=null) {
					UtilizationInterfaceDB dbUInterfaceObj = new UtilizationInterfaceDB(
							fetchedAPANInterfaces.getName(), fetchedAPANInterfaces.getCapacity(),
							fetchedAPANInterfaces.getIpType(), fetchedAPANInterfaces.getDirection()
							);
					dbUInterfaceObj.setutilInterfaceID("utilizationInterface_"+(++counterInt));
					dbUInterfaceObj.setServiceName("APAN-JP");
					utilizationInterfaceKeyCol.add(dbUInterfaceObj.getutilInterfaceID());
					allInterfaces.add(dbUInterfaceObj);
					try {
						 json2 = JsonWriter.objectToJson(dbUInterfaceObj);
					} catch (IOException e) {
						
						e.printStackTrace();
					}
					cbl.set(dbUInterfaceObj.getutilInterfaceID(), json2);
					countAPAN++;	
				}
			}
			System.out.println("APAN-JP Interfaces have been successfully saved inside the Database!");
		}
			System.out.println("Saving GEANT PRODUCTION Interfaces...");
			for (UtilizationInterface fetchedGEANTInterfaces : interfacePairsGEANT)
			{
				//Debug: if-clause to keep the amount of fetched data minimal
				if(countGEANT<5 && fetchedGEANTInterfaces.getDirection()!=null) {
					UtilizationInterfaceDB dbUInterfaceObj = new UtilizationInterfaceDB(
							fetchedGEANTInterfaces.getName(), fetchedGEANTInterfaces.getCapacity(),
							fetchedGEANTInterfaces.getIpType(), fetchedGEANTInterfaces.getDirection()
							);
						
					dbUInterfaceObj.setutilInterfaceID("utilizationInterface_"+(++counterInt));
					dbUInterfaceObj.setServiceName("GEANT PRODUCTION");
					utilizationInterfaceKeyCol.add(dbUInterfaceObj.getutilInterfaceID());
					allInterfaces.add(dbUInterfaceObj);
					try {
						 json2 = JsonWriter.objectToJson(dbUInterfaceObj);
					} catch (IOException e) {
					
						e.printStackTrace();
					}
					cbl.set(dbUInterfaceObj.getutilInterfaceID(), json2);
					countGEANT++;	
				}
			}
			System.out.println("GEANT PRODUCTION Interfaces have been successfully saved inside the Database!");	
			System.out.println("All successfully fetched DelayJitterLoss interfaces are now inside the database!" );
			initUtilData(allInterfaces, utilServices);
	}
		
	public void initUtilData(List<UtilizationInterfaceDB> interfaceList, List<UtilizationServiceDB> serviceList){
		
		System.out.println("init ThroughputData: "); 
 		RequestTO requestTO = new RequestTO();
 		
 		List<UtilizationInterfaceDB> interfaces = interfaceList;
 		List<UtilizationServiceDB>     services = serviceList;	
 		List<UtilizationDataDB> saveDataList    = new LinkedList<UtilizationDataDB>();
 		List<UtilizationDataDB> readSavedData   = new LinkedList<UtilizationDataDB>();
 		List<UtilizationData> fetchedUdata      = new LinkedList<UtilizationData>();
		
 		// init data
 		try {
		
 			fetchedUdata = getUtilizationDataCB(requestTO, services, interfaces);
		
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
	 			
	 			
	 			dataDB.setutilDataID("utilizationDataid_"+(++intcounter));
	 			saveDataList.add(dataDB);
			 
	 		}
	 		String jsonUdata = "";
	 		int counterInt = 0;
	 		for(UtilizationDataDB udata : saveDataList){
 			System.out.println(udata);	
 			udata.setutilDataID("utilizationData_"+(++counterInt));
			utilizationDataKeyCol.add(udata.getutilDataID());
			
			try {
				 jsonUdata = JsonWriter.objectToJson(udata);
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			cbl.set(udata.getutilDataID(), jsonUdata);
	 		}
		 
	}
	
	@Override
	public List<UtilizationData> getUtilizationDataCB(RequestTO to, 
			List<UtilizationServiceDB> services, List<UtilizationInterfaceDB> interfaces)
			throws ParseException, FetchNothingException, FindNothingException {

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
	
		
	
			
				

	@Override
	public List<DashboardDelayGetDataTO> getDashboardDelayGetData(RequestTO to) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DashboardJitterGetDataTO> getDashboardJitterGetData(RequestTO to) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DashboardLossGetDataTO> getDashboardLossGetData(RequestTO to) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DashboardUDGetDataTO> getDashboardUDGetData(RequestTO to) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DelayJitterLossDataDB> getDelayJitterLossData(RequestTO to)
			throws ParseException, FindNothingException, FetchNothingException {
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
	public List<PathSegmentsGetDataTO> getPathSegmentsGetData(RequestTO to) {
		// TODO Auto-generated method stub
		return null;
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
	
	/**
	 * List converter List<String> -> List<DelayJitterLossServiceDB>
	 * 
	 * @param services
	 * 
	 * @return List<DelayJitterLossServiceDB> servicesDJLObj
	 * 
	 * */
	public List<DelayJitterLossServiceDB> convertServiceMap(List<String> services){
		List<String> servicesString = services;
		List<DelayJitterLossServiceDB> servicesDJLObj = new LinkedList<DelayJitterLossServiceDB>();
		String url = "";
		for(String readString : servicesString){
			 url = readString.substring( 191,198);
			 System.out.println();
			 if(url.contains("X-WiN")){
				 String urlXWIN = url.substring(0, 5);
				 System.out.println(urlXWIN);
				 servicesDJLObj.add(new DelayJitterLossServiceDB( urlXWIN, readString.substring( 87,150)));
			 }else if(url.contains("LHCOPN")){
				 String urlLHCOPN = url.substring(1,6);
				 System.out.println(urlLHCOPN);
				 servicesDJLObj.add(new DelayJitterLossServiceDB( urlLHCOPN, readString.substring( 87,150)));
			 	}else if(url.contains("GEANT")){
			 		String urlGEANT = url.substring(0, 5);
			 		System.out.println(urlGEANT);
			 		servicesDJLObj.add(new DelayJitterLossServiceDB( urlGEANT,readString.substring( 87,150)));
			 	}	
		}
		return servicesDJLObj;
	}
	

	public List<ThroughputServiceDB> convertThroughServiceMap(List<String> services){
		List<String> servicesString = services;
		List<ThroughputServiceDB> servicesTObj = new LinkedList<ThroughputServiceDB>();
		String serviceName = "";
		for(String readString : servicesString){
			 serviceName = readString.substring(220, 232);
			 System.out.println(serviceName);
			 if(serviceName.contains("GEANT SQL-MA")){
				 String urlGEANT = readString.substring(82,169);
				 System.out.println(urlGEANT);
				 servicesTObj.add(new ThroughputServiceDB( serviceName, urlGEANT));
			 }
		}
		return servicesTObj;
	}
	


	@Override
	public List<UtilizationData> getUtilizationData(RequestTO to,
			List<UtilizationServiceDB> services,
			List<UtilizationInterfaceDB> interfaces) throws ParseException,
			FindNothingException, FetchNothingException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ThroughputInterfaceDB> getThroughputInterface(
			IServerRequest request) throws FindNothingException,
			FetchNothingException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ThroughputServiceDB> getThroughputService(RequestTO to,
			Collection<String> throughputServiceKeyCol)
			throws FindNothingException, FetchNothingException,
			InterruptedException, ExecutionException {
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
	public void getUtilizationInterface(RequestTO to) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getUtilizationInterface(RequestTO to,
			Collection<String> utilKeys) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<UtilizationDataTO> getUtilizationData(RequestTO to)
			throws ParseException, FetchNothingException, FindNothingException {
		// TODO Auto-generated method stub
		return null;
	}
}
