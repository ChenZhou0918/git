package perfsonarserver.fetchDataAndProcess;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import perfsonarserver.database.database_to.DelayJitterLossDataDB;
import perfsonarserver.database.database_to.DelayJitterLossInterfaceDB;
import perfsonarserver.database.database_to.DelayJitterLossServiceDB;
import perfsonarserver.database.database_to.ThroughputInterfaceDB;
import perfsonarserver.database.database_to.ThroughputServiceDB;
import perfsonarserver.database.database_to.UtilizationInterfaceDB;
import perfsonarserver.database.database_to.UtilizationServiceDB;
import perfsonarserver.fetchDataAndProcess.IServerRequest;
import perfsonarserver.fetchDataAndProcess.exception.FetchDJLDataException;
import perfsonarserver.fetchDataAndProcess.exception.FetchDJLInterfaceException;
import perfsonarserver.fetchDataAndProcess.exception.FetchDJLServiceException;
import perfsonarserver.fetchDataAndProcess.exception.FetchFailException;
import perfsonarserver.fetchDataAndProcess.exception.FetchThroughDataExcpetion;
import perfsonarserver.fetchDataAndProcess.exception.FetchThroughInterfaceException;
import perfsonarserver.fetchDataAndProcess.exception.FetchThroughServiceException;
import perfsonarserver.fetchDataAndProcess.exception.FetchUtilDataException;
import perfsonarserver.fetchDataAndProcess.exception.FetchUtilInterfaceException;
import perfsonarserver.fetchDataAndProcess.exception.FetchUtilServiceException;
import perfsonarserver.fetchDataAndProcess.minheap.DelayMinHeap;
import perfsonarserver.fetchDataAndProcess.minheap.JitterMinHeap;
import perfsonarserver.fetchDataAndProcess.minheap.ThroughputMinHeap;
import perfsonarserver.fetchDataAndProcess.minheap.UtilMinHeap;
import perfsonarserver.fetchDataAndProcess.transferObjects.DelayJitterLossData;
import perfsonarserver.fetchDataAndProcess.transferObjects.DelayJitterLossInterfacePair;
import perfsonarserver.fetchDataAndProcess.transferObjects.ThroughputData;
import perfsonarserver.fetchDataAndProcess.transferObjects.ThroughputInterfacePair;
import perfsonarserver.fetchDataAndProcess.transferObjects.UtilizationData;
import perfsonarserver.fetchDataAndProcess.transferObjects.UtilizationInterface;

/**
 * This class perfomrs requestss into the perfSONAR network.
 * 
 * @author Clemens Schlei, Florian Rueffer, Zhou Chen
 * 
 */
public class PerfsonarRequest implements IServerRequest
{

	private final String REQUEST_METHOD = "POST";
	private final String SOAP_ACTION_VALUE = "";
	private final String SOAP_ACTION = "SOAPAction";
	private final String ACCEPT_PARAM = "text/xml multipart/* application/soap";
	private final String ACCEPT = "Accept";
	private final String CONTENT_CHARSET = "text/xml; charset=utf-8";
	private final String CONTENT_TYPE = "Content-Type";
	private final String DJL_S_File = "djlServices.txt";
	private final String UTILIZATION_S_File = "utServices.txt";
	private final String THROUGHPUT_S_File = "thServices.txt";
	private final String SPLIT = ";;";
	
	

	public PerfsonarRequest()
	{
		super();
	}
	


	/**************************************************************************************************************************/
	/*                                             Delay Jitter Loss                                                          */
	/**************************************************************************************************************************/


	@Override/**
	 * fetch DJL data from a single interface
	 */
	public List<DelayJitterLossData> getDelayJitterLossData(String serviceURL,
			String srcInterface, String destInterface, long aktStart,
			long aktEnd) throws FetchDJLDataException {
		
		List<DelayJitterLossInterfacePair> list = new ArrayList<>();
		list.add(new DelayJitterLossInterfacePair( srcInterface, destInterface,serviceURL));
		try {
			getDJLData(list, aktStart, aktEnd, serviceURL);
		} catch (FetchDJLDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (!list.isEmpty())
			return list.get(0).getData();
		else
			
		throw new FetchDJLDataException();
	}
	
	/**
	 * fetch DJL data from a list of interface
	 */
	public List<DelayJitterLossInterfacePair> getDelayJitterLoss(String serverURL, List<DelayJitterLossInterfacePair> uIList, long startTime, long endTime) throws FetchFailException
	{
		if (!uIList.isEmpty())
		{
			getDJLData(uIList, startTime, endTime, serverURL);
			return uIList;
		}
		else
			throw new FetchDJLDataException();
	}

	public List<DelayJitterLossData> getDelayJitterLossData(List<DelayJitterLossInterfaceDB> interfaceList,
			List<DelayJitterLossServiceDB> serviceList, long startDate, long endDate) throws FetchFailException
	{
		
		//TODO Differenzieren zwischen GEANT, LHCOPN und X-WiN
		
		List<DelayJitterLossInterfacePair> djlIPGEANT = new ArrayList<DelayJitterLossInterfacePair>();
		List<DelayJitterLossInterfacePair> djlIPLHCOPN = new ArrayList<DelayJitterLossInterfacePair>();
		List<DelayJitterLossInterfacePair> djlIPXWIN = new ArrayList<DelayJitterLossInterfacePair>();
		List<DelayJitterLossInterfaceDB> iList = interfaceList;
		List<DelayJitterLossServiceDB>   sList = serviceList;
		String urlGEANT = "";
		String urlLHCOPN = "";
		String urlXWIN = "";
		
		//Read the urls out of each DelayJitterLoss Service in order to use them
		//for the specific interfaces
		for(DelayJitterLossServiceDB services : sList){
			if( services!=null && services.getServiceName().equalsIgnoreCase("GEANT")  ){
					urlGEANT = services.getServiceURL();
			} else if(services!=null && services.getServiceName().equalsIgnoreCase("LHCOPN") ){
						urlLHCOPN = services.getServiceURL();
				   } else if( services!=null && services.getServiceName().equalsIgnoreCase("X-WiN") ){
					   			urlXWIN = services.getServiceURL();
				   		  }	
		System.out.println("GEANT: " +urlGEANT+ " \nLHCOPN: " +urlLHCOPN+ "\nX-WIN: "+urlXWIN);
		}
		
		//Read the sources and destinations out of each DelayJitterloss Interface, combine them with
		//the urls from above and save everything inside a List for further treatment
		for(DelayJitterLossInterfaceDB interfaces : iList) {
			if( interfaces!=null && interfaces.getServiceName().equalsIgnoreCase("GEANT")  ){
				djlIPGEANT.add(new DelayJitterLossInterfacePair(interfaces.getSrcInterface(), interfaces.getDestInterface(), urlGEANT));
			} else if( interfaces!=null && interfaces.getServiceName().equalsIgnoreCase("LHCOPN") ){
						djlIPLHCOPN.add(new DelayJitterLossInterfacePair(interfaces.getSrcInterface(), interfaces.getDestInterface(), urlLHCOPN));
			   	   } else if( interfaces!=null && interfaces.getServiceName().equalsIgnoreCase("X-WiN") ){
				   			djlIPXWIN.add(new DelayJitterLossInterfacePair(interfaces.getSrcInterface(), interfaces.getDestInterface(), urlXWIN));
			   	      	  }
		}
		
		//Pre-set the features precedence, groupsize, interval and packetsize for each created list.
		//These features are needed for the XML Request later
		for(DelayJitterLossInterfacePair dataGEANT : djlIPGEANT){
			dataGEANT.setPrecedence("0x0");
			dataGEANT.setGroupsize(9);
			dataGEANT.setInterval(60);
			dataGEANT.setPacketsize(41); 
		}	 
		
		for(DelayJitterLossInterfacePair dataLHCOPN : djlIPLHCOPN){
			 dataLHCOPN.setPrecedence("0x0");
			 dataLHCOPN.setGroupsize(9);
			 dataLHCOPN.setInterval(60);
			 dataLHCOPN.setPacketsize(41);
		}	 
		
		for(DelayJitterLossInterfacePair dataXWIN : djlIPXWIN){
			dataXWIN.setPrecedence("0x0");
			dataXWIN.setGroupsize(9);
			dataXWIN.setInterval(60);
			dataXWIN.setPacketsize(41);
		}	
		
		//Call the Method, which creates the DelayJitterLoss Data X-WiN XML request
		getDJLData(djlIPXWIN, startDate, endDate, urlXWIN);
		
		List<DelayJitterLossData> measuredValues = new LinkedList<DelayJitterLossData>();
		DelayJitterLossData servicenameXWIN = new DelayJitterLossData();
		//add the servicename manually
		servicenameXWIN.setServiceName("X-Win");
		//Send the response back in order to save the measured DelayJitterLoss Data X-WiN inside the database
		if (!djlIPXWIN.isEmpty() && !(djlIPXWIN.get(0).getData()==null)) {
				measuredValues.add(servicenameXWIN);
				for(DelayJitterLossInterfacePair retData : djlIPXWIN ){
							System.out.println(retData);
							measuredValues.addAll(retData.getData());			
				}  
				return  measuredValues; 
		}
		else
			throw new FetchDJLDataException(); 
	  
		//TODO Get DelayJitterLossData anpassen für GEANT und LHCOPN
		
//		getDJLData(djlIPGEANT, startDate, endDate, urlGEANT);
//		if (!djlIPGEANT.isEmpty() && !(djlIPGEANT.get(0).getData()==null))
//			return djlpiGEANT.get(0).getData();
//		else
//			throw new FetchDJLDataException();
		
//		getDJLData(djlIPLHCOPN, startDate, endDate, urlLHCOPN);
//		if (!djlIPLHCOPN.isEmpty() && !(djlIPLHCOPN.get(0).getData()==null))
//			return djlpiLHCOPN.get(0).getData();
//		else
//			throw new FetchDJLDataException();
		
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<DelayJitterLossInterfacePair> getDelayJitterLossInterfacePairs(String serverUrl, long startTime, long endTime) throws FetchFailException
	{
		SOAPBuilder soapB = new SOAPBuilder();
		IConverter conv;
		SOAPMessage request = soapB.getDelayJitterLossInterfacePairsRequest(startTime, endTime);
		// For debug purposes
		writeSOAPToFile(request, "DJLIRequest.xml");
		try
		{
			if (request != null)
			{
				String response;
				response = sendSoapRequest(serverUrl, request);
				// For debug purposes
				writeStringToFile(response, "DJLIResponse.xml");
				conv = new XML2Java();
				
				return (List<DelayJitterLossInterfacePair>) conv.convertDJLIPairList(response, serverUrl, startTime, endTime);

			}
			else
				throw new FetchFailException();
		}
		catch (FetchFailException e)
		{
			throw new FetchDJLInterfaceException();
		}
	}

	
	public Map<String, String> getDelayJitterLossServices() throws FetchFailException
	{
		try
		{
			return getService(DJL_S_File);
		}
		catch (FetchFailException e)
		{
			throw new FetchDJLServiceException();
		}
	}

	/**************************************************************************************************************************/
	/*                                               Throughput                                                               */
	/**************************************************************************************************************************/
	public Map<String, String> getThroughputServices() throws FetchFailException
	{
		try
		{
			return getService(THROUGHPUT_S_File);
		}
		catch (FetchFailException e)
		{
			throw new FetchThroughServiceException();
		}
	}
	
@SuppressWarnings("unchecked")
	
	public List<ThroughputInterfacePair> getThroughputInterfacePairs(String serverUrl, long startTime, long endTime) throws FetchFailException
	{
		SOAPBuilder soapB = new SOAPBuilder();
		IConverter conv;
		SOAPMessage request = soapB.getThroughputInterfacePairRequest(startTime, endTime);
		// For debug purposes
		writeSOAPToFile(request, "tIRequest.xml");

		try
		{
			if (request != null)
			{
				String response;
				response = sendSoapRequest(serverUrl, request);
				//System.out.println(response);
				{
					// For debug purposes
					writeStringToFile(response, "tIResponse.xml");
					conv = new XML2Java();
					return (List<ThroughputInterfacePair>) conv.convertThroughputIPairList(response, serverUrl, startTime, endTime);
				}
			}
			else
				throw new FetchFailException();
		}
		catch (FetchFailException e)
		{
			throw new FetchThroughInterfaceException();
		}
	}
	
	
	public List<ThroughputInterfacePair> getThroughputData(String serverURL, List<ThroughputInterfacePair> list, long startTime, long endTime) throws FetchFailException
	{
		if (!list.isEmpty())
		{
			return getTData(list, startTime, endTime, serverURL);
		}
		else
			throw new FetchThroughDataExcpetion();
	}

	
	public List<ThroughputData> getThroughputData(List<ThroughputInterfaceDB> interfaceList,
			List<ThroughputServiceDB> serviceList, long startTime, long endTime) throws FetchFailException
	{
		List<ThroughputInterfacePair> tIPGEANT = new ArrayList<ThroughputInterfacePair>();
		List<ThroughputInterfacePair> dataRequestInt = new ArrayList<ThroughputInterfacePair>();
		List<ThroughputInterfaceDB> iList = interfaceList;
		List<ThroughputServiceDB>   sList = serviceList;
		String urlGEANT = "";
		
		//Read the urls out of each DelayJitterLoss Service in order to use them
		//for the specific interfaces
		
					urlGEANT = sList.get(0).getServiceURL();
			
			  
		   System.out.println("GEANT: " +urlGEANT);
		
		
		//Read the sources and destinations out of each DelayJitterloss Interface, combine them with
		//the urls from above and save everything inside a List for further treatment
		for(ThroughputInterfaceDB interfaces : iList) {
			
			if(interfaces!=null){
			ThroughputInterfacePair lePair = new ThroughputInterfacePair(interfaces.getSrcInterface(), interfaces.getDestInterface(), interfaces.getMID()); 
			tIPGEANT.add(lePair);}		
		}
		
		//Pre-set the features precedence, groupsize, interval and packetsize for each created list.
		//These features are needed for the XML Request later
		for(ThroughputInterfacePair dataGEANT : tIPGEANT){
			dataGEANT.setSrcType("ipv4");
			dataGEANT.setDestType("ipv4");
//			dataGEANT.setProtocol("TCP");
//			dataGEANT.setInterval(6);
		}	 
		
		//Call the Method, which creates the DelayJitterLoss Data X-WiN XML request
		dataRequestInt = getTData(tIPGEANT, startTime, endTime, urlGEANT);
		

		List<ThroughputData> measuredValues = new LinkedList<ThroughputData>();
		//ThroughputData servicenameXWIN = new ThroughputData();
		//add the servicename manually
		//servicenameXWIN.setServiceName("X-Win");
		//Send the response back in order to save the measured DelayJitterLoss Data X-WiN inside the database
		if (!dataRequestInt.isEmpty() && !(dataRequestInt.get(0).getData()==null)) {
				//measuredValues.add(servicenameXWIN);
				for(ThroughputInterfacePair retData : dataRequestInt ){
							System.out.println(retData);
							measuredValues.addAll(retData.getData());			
				}  
				return  measuredValues; 
		}
		else
			throw new FetchDJLDataException(); 
	}
	
	
	
	
	
	@Override/**
	 * fetch throughput data from a single interface
	 * @param Interface
	 *           the single interface from which we want to fetch data
	 *               
	 */
	public List<ThroughputData> getThroughputData(String serverURL,ThroughputInterfacePair Interface, long startTime, long endTime)
			throws FetchFailException {
		// TODO Auto-generated method stub
		List<ThroughputInterfacePair> list = new ArrayList<>();
		list.add(Interface);
		
		getTData(list, startTime, endTime, serverURL);
		
			if (!list.isEmpty())
				return list.get(0).getData();
			else
				
			throw new FetchDJLDataException();
	}

	

	/****************Fetch Top K utilization *********************/
	
	/**
	 * fetch top 5 utilization
	 * 
	 * @param startTime
	 *            Start time of measurement period in milliseconds
	 * @param endTime
	 *            End time of measurement period in milliseconds
	 * @param serverURL
	 *            Server URL of service
	 * @return A list with 5 utilization data
	 *         
	 */
	
	
	
	public List<UtilizationData> getTopFiveUtilization(String serverURL, long startTime, long endTime) throws FetchFailException
	{
		List<UtilizationInterface> UIList;
		List<UtilizationData> UDList= new LinkedList<UtilizationData>();
		List<UtilizationData> MaximumUDList = new LinkedList<UtilizationData>();
		//get all interfaces
		UIList=this.getUtilizationInterfaces(serverURL);
		
		System.out.println(UIList.size());
		
		// create a min-heap, initialize the top 5 array
		UtilizationData[] top= new UtilizationData[5];
		UtilMinHeap heap = new UtilMinHeap(top,true); 
		
		// for each interface, get a list of data
//		for(UtilizationInterface UI:UIList){
		for(int i=50;i<=100;i++){                // for test, only choose a part of interfaces
			UtilizationInterface UI=UIList.get(i);
		String currentInterface=UI.getName();
		String currentDirection=UI.getDirection();
		String currentHostName=UI.getHostname();
		String currentDescription=UI.getDescription();
		if(currentDescription==null){currentDescription="No Description";}// sometimes the description is null
		long capacity=UI.getCapacity();
	
		UDList=this.getUtilizationData(serverURL,UI, startTime, endTime);
       		if(UDList!=null)	
			{
       			System.out.println("size"+UDList.size());
			// find top "5 %" +1 data by creating a min-heap of size 5%, ignore the top 5%   
			int HeapSize=(int)(UDList.size()*0.05)+1;
			UtilizationData[] topFivePercent= new UtilizationData[HeapSize];
			UtilMinHeap heap2 = new UtilMinHeap(topFivePercent,true); 
			for(UtilizationData currentData:UDList)
			{
			UtilizationData root=heap2.getRoot();	
//			 System.out.println("load:"+currentData.getValue()+"   capcaity"+capacity);
			currentData.setUtilPercentage(Double.valueOf(currentData.getValue())/capacity*100);//change value to percentage compare to capacity
		
			if(currentData.getUtilPercentage()>root.getUtilPercentage())
			{heap2.setRoot(currentData);}
			}
			
			// the first data in the array is the smallest, abandon the top 5% data assuming they are error measurement
			//add interface information
		    topFivePercent[0].setdirection(currentDirection);
		    topFivePercent[0].setInterface(currentInterface);// name
		    topFivePercent[0].setHostName(currentHostName);//host name
		    topFivePercent[0].setCapacity(capacity);
		    topFivePercent[0].setDescription(currentDescription);
		    MaximumUDList.add(topFivePercent[0]);
				
			}
		}
			for(UtilizationData currentData:MaximumUDList){
				UtilizationData root=heap.getRoot();
				if(currentData.getUtilPercentage()>root.getUtilPercentage())
					{heap.setRoot(currentData);}
//				System.out.println(heap.getRoot().toString());
			}
	
			List<UtilizationData> topList =new LinkedList<UtilizationData>();
			for(int i=0;i<=4;i++){
				topList.add(top[i]);
			}
			
			System.out.println(topList);
			return topList;
			}

	
	
	/*************************without kicking out error measurement *******************************/
	
	public List<UtilizationData> getTopFiveUtilizationError(String serverURL, long startTime, long endTime) throws FetchFailException
	{
		List<UtilizationInterface> UIList;
		List<UtilizationData> UDList= new LinkedList<UtilizationData>();
		List<UtilizationData> MaximumUDList = new LinkedList<UtilizationData>();
		
		UIList=this.getUtilizationInterfaces(serverURL);
		System.out.println(UIList);
		// create a min-heap, initialize the top 5 array
		UtilizationData[] top= new UtilizationData[5];
		UtilMinHeap heap = new UtilMinHeap(top,true); 
		
		// for each interface, get a list of data, then choose the maximum ten data and put them into a list
		for(UtilizationInterface UI:UIList){
//		for(int i=400;i<=410;i++){
//			UtilizationInterface UI=UIList.get(i);
		String currentInterface=UI.getName();
		String currentDirection=UI.getDirection();
		String currentHostName=UI.getHostname();
		long capacity=UI.getCapacity();
		UDList=this.getUtilizationData(serverURL,UI, startTime, endTime);
//		UDList=this.getUtilizationDataMDB(serverURL, currentInterface,currentDirection, startTime, endTime);
         System.out.println(UDList.size());
		if(UDList.size()!=0)	
			{
		int MaxValueIndex=0;
		for(int j=0;j<=UDList.size()-1;j++){
			if(Double.valueOf(UDList.get(j).getValue())>Double.valueOf(UDList.get(MaxValueIndex).getValue()))
				MaxValueIndex=j;
		}
			
		   UDList.get(MaxValueIndex).setdirection(currentDirection);
		   UDList.get(MaxValueIndex).setInterface(currentInterface);
		   UDList.get(MaxValueIndex).setHostName(currentHostName);
		   UDList.get(MaxValueIndex).setCapacity(capacity);
		   MaximumUDList.add( UDList.get(MaxValueIndex));
				
			}
		}

	
	
			for(UtilizationData currentData:MaximumUDList){
				UtilizationData root=heap.getRoot();
				if(currentData.getUtilPercentage()>root.getUtilPercentage())
					{heap.setRoot(currentData);}
//				System.out.println(heap.getRoot().toString());
			}
	
			List<UtilizationData> topList =new LinkedList<UtilizationData>();
			for(int i=0;i<=4;i++){
				topList.add(top[i]);
			}
			return topList;
			}
	
		
	
	
	

	
	/*************************************/
	
	/**
		 * fetch top 5 DJL data
		 * 
		 * @param startTime
		 *            Start time of measurement period in milliseconds
		 * @param endTime
		 *            End time of measurement period in milliseconds
		 * @param serverURL
		 *            Server URL of service
		 * @return A list with 5 DJL data
		 *         
		 */
		
		public List<DelayJitterLossData> getTopFiveDelay(String serverURL, long startTime, long endTime) throws FetchFailException
		{
	
			List<DelayJitterLossInterfacePair> DIList;
			List<DelayJitterLossData> DDList= new LinkedList<DelayJitterLossData>();
			List<DelayJitterLossData> MaximumDDList = new LinkedList<DelayJitterLossData>();
			
			
			DIList=this.getDelayJitterLossInterfacePairs(serverURL,startTime,endTime);
			System.out.println(DIList.size());
			
			// create a min-heap, initialize the top 5 array
			DelayJitterLossData[] top= new DelayJitterLossData[5];
			DelayMinHeap heap = new DelayMinHeap(top,true); 
			
			
			for(DelayJitterLossInterfacePair DI:DIList){
	//		for(int i=0;i<=20;i++){           // for test, only choose a part of interfaces
	//			DelayJitterLossInterfacePair DI=DIList.get(i);
			    String currentDestInterface=DI.getDestination();
			    String currentSrcInterface=DI.getSource();
	// for each interface, get a list of data
			DDList=this.getDelayJitterLossData(serverURL, currentSrcInterface,currentDestInterface, startTime, endTime);
	//		DDList=this.getDelayJitterLossData(serverURL, "Aachen_DFN","Augsburg_DFN", startTime, endTime);
			// cluster algorithm: divide data of each interface into several pseudo clusters
			if(DDList!=null)	
				{
				LinkedList<Cluster> pseudoClusterLists = new LinkedList<Cluster>();
				LinkedList<Cluster> realClusterLists = new LinkedList<Cluster>();
				double threshold=0.00004;// threshold = 40 microseconds 
				Cluster currentcluster=new Cluster(DDList.get(0));
	           
				for(DelayJitterLossData dldata:DDList)
				{   
	 				if(Math.abs(dldata.getMinDelay()-currentcluster.getReferPoint().getMinDelay())<threshold)
					{
						currentcluster.merge(dldata);
					}
					
					else { 
						pseudoClusterLists.add(currentcluster);
					       currentcluster=new Cluster(dldata);
	
					     }
						
				}
				
				pseudoClusterLists.add(currentcluster);
				
				System.out.println("pseudo cluster size"+pseudoClusterLists.size());
				
				
				// check whether the clusters above are real. if not, merge it
				realClusterLists.add(pseudoClusterLists.get(0));
				Cluster refercluster;
				Cluster nextcluster;
			   
				for(int j=1;j<pseudoClusterLists.size();j++)
				{  
					 refercluster=realClusterLists.getLast();
					 nextcluster=pseudoClusterLists.get(j);
					
					if(nextcluster.getSize()<3||Math.abs(refercluster.getIntrinsicDelay()-nextcluster.getIntrinsicDelay())<threshold)
					{  
						realClusterLists.remove();
						refercluster.merge(nextcluster);
						realClusterLists.add(refercluster);
					}
					else
					{
					   realClusterLists.add(nextcluster);	
					}
					
				}
				System.out.println("Real cluster size"+realClusterLists.size());
			
			int HeapSize=(int)(DDList.size()*0.01)+1;
			DelayJitterLossData[] topOnePercent= new DelayJitterLossData[HeapSize];
			DelayMinHeap heap2 = new DelayMinHeap(topOnePercent,true); 
			List<DelayJitterLossData> ClusterDDList = new LinkedList<DelayJitterLossData>();
			int maxIndexInCluster=0;
			for(Cluster cluster:realClusterLists){	
			 double c =cluster.getIntrinsicDelay();
			
				// use min-heap to calculate top 1%
			for(DelayJitterLossData currentData:cluster.getData())
			{
				DelayJitterLossData root=heap2.getRoot();	
	//		 System.out.println("route delay:"+currentData.getMaxDelay()+"   C:"+c);
				if(c!=0)//ignore infinity percentage when c=0
			currentData.setMaxDelayPercentage(100*(currentData.getMaxDelay()-c)/c);//change value to percentage compare to intrinsic delay
				else currentData.setMaxDelayPercentage(0);
				
			if(currentData.getMaxDelayPercentage()>root.getMaxDelayPercentage())
			{heap2.setRoot(currentData);
			}
			}
			// pick out maximum in a cluster
				topOnePercent[0].setSender(currentSrcInterface);
				topOnePercent[0].setReceiver(currentDestInterface);
			
			    ClusterDDList.add(topOnePercent[0]);
			}
			// pick out maximum of a interface 
			System.out.println("Cluster data: "+ClusterDDList.size());
			for(int k=0;k<ClusterDDList.size();k++)
				
			{
				if(ClusterDDList.get(k).getMaxDelayPercentage()>ClusterDDList.get(maxIndexInCluster).getMaxDelayPercentage())
					maxIndexInCluster=k;
			}
				MaximumDDList.add(ClusterDDList.get(maxIndexInCluster));
			}
			}
			System.out.println("Interface data: "+MaximumDDList.size());
	        //choose top five interfaces
				for(DelayJitterLossData currentData:MaximumDDList){
					DelayJitterLossData root=heap.getRoot();
					if(currentData.getMaxDelayPercentage()>root.getMaxDelayPercentage())
						{heap.setRoot(currentData);}
					System.out.println(heap.getRoot().toString());
				}
				
				List<DelayJitterLossData> topList =new LinkedList<DelayJitterLossData>();
				for(int i=0;i<=4;i++){
					topList.add(top[i]);
				}
				System.out.println(topList);
				return topList;
				}



	/**
	 * fetch top 5 jitter data
	 * 
	 * @param startTime
	 *            Start time of measurement period in milliseconds
	 * @param endTime
	 *            End time of measurement period in milliseconds
	 * @param serverURL
	 *            Server URL of service
	 * @return A list with 5 DJL data
	 *         
	 */
	
	public List<DelayJitterLossData> getTopFiveJitter(String serverURL, long startTime, long endTime) throws FetchFailException
	{

		List<DelayJitterLossInterfacePair> JIList;
		List<DelayJitterLossData> JDList= new LinkedList<DelayJitterLossData>();
		List<DelayJitterLossData> MaximumJDList = new LinkedList<DelayJitterLossData>();
		
		JIList=this.getDelayJitterLossInterfacePairs(serverURL,startTime,endTime);
		System.out.println(JIList);
		
		
		// create a min-heap storing final 5 top interface
		DelayJitterLossData[] top= new DelayJitterLossData[5];
		JitterMinHeap heap = new JitterMinHeap(top,true); 
		// for each interface, get a list of data, then choose the maximum data and put them into a list
		for(DelayJitterLossInterfacePair JI:JIList){
//		for(int i=10;i<=20;i++){          // for test, only choose a part of interfaces
//			DelayJitterLossInterfacePair JI=JIList.get(i);
			
		String currentDestInterface=JI.getDestination();
		String currentSrcInterface=JI.getSource();

		JDList=this.getDelayJitterLossData(serverURL, currentSrcInterface,currentDestInterface, startTime, endTime);
		System.out.println(JDList.size());
		if(JDList!=null)	
			{
			//use min-heap to calculate top 1% percent
			int HeapSize=(int)(JDList.size()*0.01)+1;
			DelayJitterLossData[] topOnePercent= new DelayJitterLossData[HeapSize];
			JitterMinHeap heap2 = new JitterMinHeap(topOnePercent,true); 
			
			for(DelayJitterLossData currentData:JDList)
			{
				DelayJitterLossData root=heap2.getRoot();	
				if(currentData.getMinDelay()!=0)
				currentData.setMaxJitterPercentage(Math.abs(currentData.getMaxIpdvJitter()/currentData.getMinDelay()*100));
				else currentData.setMaxJitterPercentage(0);
		
			if(Math.abs(currentData.getMaxJitterPercentage())>Math.abs(root.getMaxJitterPercentage()))
			{heap2.setRoot(currentData);}
			}
			// kick out 1%, choose the maximum in the rest 
			topOnePercent[0].setSender(currentSrcInterface);
			topOnePercent[0].setReceiver(currentDestInterface);
		   MaximumJDList.add(topOnePercent[0]);
			}
			}
		
           // calculate the top five interfaces
			for(DelayJitterLossData currentData:MaximumJDList){
				DelayJitterLossData root=heap.getRoot();
				if(Math.abs(currentData.getMaxJitterPercentage())>Math.abs(root.getMaxJitterPercentage()))
					{heap.setRoot(currentData);}
				System.out.println(top[0]);
			}
	// sort the five interface values
			List<DelayJitterLossData> topList =new LinkedList<DelayJitterLossData>();
			for(int i=0;i<=4;i++){
				topList.add(top[i]);
			}
			return topList;
			}
	
	/***********get interface with high loss
	 * @throws FetchFailException *******************/
	public List<DelayJitterLossInterfacePair> getTopLossInterfaces(String serverURL, long startTime, long endTime) throws FetchFailException 
	{

		List<DelayJitterLossInterfacePair> LIList;
		List<DelayJitterLossData> LDList= new LinkedList<DelayJitterLossData>();
		List<DelayJitterLossInterfacePair> TopLossInterfaces = new LinkedList<DelayJitterLossInterfacePair>();
		
		LIList=this.getDelayJitterLossInterfacePairs(serverURL,startTime,endTime);
		System.out.println(LIList.size());

		
		for(DelayJitterLossInterfacePair LI:LIList){
//		for(int i=5;i<=20;i++){            // for test, only choose a part of interfaces
//			DelayJitterLossInterfacePair LI=LIList.get(i);
			int numberOfLoss=0;
		String currentDestInterface=LI.getDestination();
		String currentSrcInterface=LI.getSource();
		LDList=this.getDelayJitterLossData(serverURL, currentSrcInterface,currentDestInterface, startTime, endTime);
		
		if(LDList.size()!=0)	
		{
			for(DelayJitterLossData currentData:LDList)
		{
		numberOfLoss=numberOfLoss+currentData.getLoss();	
		}
			System.out.println("loss number: "+numberOfLoss);
		if(numberOfLoss>=3)
		{LI.setLossNumber(numberOfLoss);
		TopLossInterfaces.add(LI);
		}
		}
		}
		System.out.println(TopLossInterfaces);
		return TopLossInterfaces;
			}
	
	
	public List<ThroughputData> getTopFiveThroughput(String serverURL, long startTime, long endTime) throws FetchFailException 
	{
	List<ThroughputInterfacePair> TIList;
	List<ThroughputData> TDList= new LinkedList<ThroughputData>();
	List<ThroughputData> MaximumData = new LinkedList<ThroughputData>();
	
	TIList=this.getThroughputInterfacePairs(serverURL,startTime,endTime);
	System.out.println(TIList);
	
//	for(ThroughputInterfacePair TI:TIList)
		for(int j=0;j<=50;j++)
	{         // for test, only choose a part of interfaces
		ThroughputInterfacePair TI=TIList.get(j);	
	TDList=this.getThroughputData(serverURL,TI, startTime, endTime);
	String srcInterface=TI.getSrcAddress();
	String destInterface=TI.getDestAddress();
		if(TDList==null) {System.out.println("no data");}
		else
		{
			System.out.println("size: "+TDList.size());
	    int maxIndex=0;
	    for(int i=0;i<TDList.size();i++)
	    {
	    	if(TDList.get(maxIndex).getValue()<TDList.get(i).getValue())
	    	{maxIndex=i;}
	    }
	    TDList.get(maxIndex).setDestInterface(destInterface);
	    TDList.get(maxIndex).setSrcInterface(srcInterface);
	    MaximumData.add(TDList.get(maxIndex));
		}
	}
	
	System.out.println("Maximum data list: "+MaximumData.size());
		ThroughputData[] topfive=new ThroughputData[5];
		ThroughputMinHeap heap=new ThroughputMinHeap(topfive,true);
		
		for(ThroughputData currentData:MaximumData){
			ThroughputData root=heap.getRoot();
			if(currentData.getValue()>root.getValue())
				{heap.setRoot(currentData);}
			System.out.println(heap.getRoot().toString());
		}
		
		List<ThroughputData> topList =new LinkedList<ThroughputData>();
		for(int i=0;i<=4;i++){
			topList.add(topfive[i]);
		}
		System.out.println(topList);
		return topList;
		}
	
	
	@Override/**
	 * fetch Utilization data from a single interface
	 * 
	 */
	public List<UtilizationData> getUtilizationDataMDB(String serverURL, String name, String direction, long startTime, long endTime) throws FetchFailException
	{
		SOAPBuilder soapB = new SOAPBuilder();
		List<UtilizationInterface> list = new ArrayList<>();
		list.add(new UtilizationInterface(name,direction));
		getUData(list, startTime, endTime, serverURL, soapB);
		if (!list.isEmpty())
			return list.get(0).getData();
		else
			
		throw new FetchUtilDataException();
	}

	/** @param Interface
	   *           the single interface from which we want to fetch data 
	   * 
	   * */
	public List<UtilizationData> getUtilizationData(String serverURL,UtilizationInterface UI, long startTime, long endTime) throws FetchFailException
	{
		SOAPBuilder soapB = new SOAPBuilder();
		List<UtilizationInterface> list = new ArrayList<>();
		list.add(UI);
		getUData(list, startTime, endTime, serverURL, soapB);
		if (!list.isEmpty())
			return list.get(0).getData();
		else
			
		throw new FetchUtilDataException();
	}

	/***********************************************************************************************************
	 * *************************************************************************************************************************/
	
	public List<UtilizationInterface> getUtilizationData(String serverURL, List<UtilizationInterface> list, long startTime, long endTime) throws FetchFailException
	{
		SOAPBuilder soapB = new SOAPBuilder();
		if (!list.isEmpty())
		{
			getUData(list, startTime, endTime, serverURL, soapB);
			return list;
		}
		else
			throw new FetchUtilDataException();
	}

	public List<UtilizationData> getUtilizationData(List<UtilizationServiceDB> services,
			List<UtilizationInterfaceDB> interfaces, long startTime, long endTime) throws FetchFailException
	{
		SOAPBuilder soapB = new SOAPBuilder();
		List<UtilizationServiceDB> sList = services;
		List<UtilizationInterfaceDB> iList = interfaces;
		List<UtilizationInterface> uIPAPAN = new ArrayList<UtilizationInterface>();
		List<UtilizationInterface> uIPGEANT = new ArrayList<UtilizationInterface>();
		String urlGEANT = "";
		String urlAPAN = "";
		
		//Read the urls out of each DelayJitterLoss Service in order to use them
		//for the specific interfaces
		for(UtilizationServiceDB singleServices : sList){
			if( singleServices!=null && singleServices.getServiceName().equalsIgnoreCase("GEANT PRODUCTION")  ){
					urlGEANT = singleServices.getServiceURL();
			} else if(singleServices!=null && singleServices.getServiceName().equalsIgnoreCase("APAN-JP") ){
					urlAPAN = singleServices.getServiceURL();
					}
		System.out.println("GEANT: " +urlGEANT+ " \nAPAN: " +urlAPAN);
		}
			
		//Read the sources and destinations out of each DelayJitterloss Interface, combine them with
		//the urls from above and save everything inside a List for further treatment
		for(UtilizationInterfaceDB singleInterfaces : iList) {
			if( singleInterfaces!=null && singleInterfaces.getServiceName().equalsIgnoreCase("GEANT PRODUCTION") &&
					singleInterfaces.getdirection()!=null){
				uIPGEANT.add(new UtilizationInterface(singleInterfaces.getInterfaceName(), singleInterfaces.getdirection()));
			} else if( singleInterfaces!=null && singleInterfaces.getServiceName().equalsIgnoreCase("APAN-JP") && 
					singleInterfaces.getdirection()!=null ){
						uIPAPAN.add(new UtilizationInterface(singleInterfaces.getInterfaceName(), singleInterfaces.getdirection()));
					}		
		}
		
		//Call the Method, which creates the Utilization Data APAN-JP XML request
		getUData(uIPAPAN, startTime, endTime, urlAPAN, soapB);
		
		List<UtilizationData> measuredValues = new LinkedList<UtilizationData>();
		UtilizationData servicenameAPAN = new UtilizationData();
		//add the servicename manually
		servicenameAPAN.setserviceName("APAN-JP");
		//Send the response back in order to save the measured Utilization Data APAN-JP inside the database
		if (!uIPAPAN.isEmpty() && !(uIPAPAN.get(0).getData()==null)) {
				measuredValues.add(servicenameAPAN);
				for(UtilizationInterface retData : uIPAPAN ){		
							System.out.println(retData);
							if(retData.getData()!=null){
								measuredValues.addAll(retData.getData());
							}
				}  
				return  measuredValues; 
		}
		else
			throw new FetchDJLDataException();
	}
	


	@SuppressWarnings("unchecked")
	
	public List<UtilizationInterface> getUtilizationInterfaces(String serverUrl) throws FetchFailException
	{
		SOAPBuilder soapB = new SOAPBuilder();
		IConverter conv;
		SOAPMessage request = soapB.getUtilizationInterfaceRequest();
		// For debug purposes
		 writeSOAPToFile(request, "UtilizationIRequest.xml");
		try
		{
			if (request != null)
			{
				String response;
				response = sendSoapRequest(serverUrl, request);

				// For debug purposes
				writeStringToFile(response, "responseUtilizationI.xml");
				conv = new XML2Java();
				return (List<UtilizationInterface>) conv.convertUtilizationIList(response, serverUrl);

			}
			else
				throw new FetchFailException();
		}
		catch (FetchFailException e)
		{
			throw new FetchUtilInterfaceException();
		}
	}

	
	public Map<String, String> getUtilizationServices() throws FetchFailException
	{
		try
		{
			return getService(UTILIZATION_S_File);
		}
		catch (FetchFailException e)
		{
			throw new FetchUtilServiceException();
		}
	}

	/**
	 * Fetches delay, jitter, loss data.
	 * 
	 * @param list
	 *            List with interface pairs
	 * @param startTime
	 *            Start time of measurement period in milliseconds
	 * @param endTime
	 *            End time of measurement period in milliseconds
	 * @param serverURL
	 *            Server URL of service
	 * @throws FetchFailException 
	 */
	
	
	private void getDJLData(List<DelayJitterLossInterfacePair> list, long startTime, long endTime, String serverURL) throws FetchDJLDataException
	{
		SOAPBuilder soapB = new SOAPBuilder();
		IConverter conv;
		SOAPMessage request = soapB.getDelayJitterLossDataRequest(list, startTime, endTime);
		// For debug purposes
		 writeSOAPToFile(request, "requestDJLDatacassandra.xml");
		try
		{
			if (request != null)
			{
				String response;
				response = sendSoapRequest(serverURL, request);
				
				// For debug purposes
				writeStringToFile(response, "responseDJLDatacassandra.xml");
				conv = new XML2Java();
				if (!conv.fillDJLData(response, list))
					throw new FetchFailException();
			}
			else
				throw new FetchFailException();
		}
		catch (FetchFailException e)
		{
		throw new FetchDJLDataException();
		}
	}

	/**
	 * Fetches throughput data.
	 * 
	 * @param list
	 *            List with interface pairs
	 * @param startTime
	 *            Start time of measurement period in milliseconds
	 * @param endTime
	 *            End time of measurement period in milliseconds
	 * @param serverURL
	 *            Server URL of service
	 * @return A list with troughput itnerface pairs containing read data or
	 *         null
	 */
	@SuppressWarnings("unchecked")
	private List<ThroughputInterfacePair> getTData(List<ThroughputInterfacePair> list, long startTime, long endTime, String serverURL) throws FetchFailException
	{
		SOAPBuilder soapB = new SOAPBuilder();
		IConverter conv;
		SOAPMessage request = soapB.getThroughputDataRequest(list, startTime, endTime);
		// For debug purposes
		writeSOAPToFile(request, "tDRequest.xml");
		try
		{
			if (request != null)
			{
	
				String response;
				response = sendSoapRequest(serverURL, request);
	
				// For debug purposes
				writeStringToFile(response, "tDResponse.xml");
				conv = new XML2Java();
				List<ThroughputInterfacePair> retList = (List<ThroughputInterfacePair>) conv.fillThroughputData(response, list);
				if (!retList.isEmpty())
				{
					return retList;
				}
				else
					throw new FetchFailException();
	
			}
			else
				throw new FetchFailException();
		}
		catch (FetchFailException e)
		{
			throw new FetchThroughDataExcpetion();
		}
	
	}

	/**
		 * Fetches utilization data.
		 * 
		 * @param list
		 *            List with interface pairs
		 * @param startTime
		 *            Start time of measurement period in milliseconds
		 * @param endTime
		 *            End time of measurement period in milliseconds
		 * @param serverURL
		 *            Server URL of service
		 * @return true when data was read
		 */
		private void getUData(List<UtilizationInterface> list, long startTime, long endTime, String serverURL, SOAPBuilder soapB) throws FetchFailException
		{
			IConverter conv;
			SOAPMessage request = soapB.getUtilizationDataRequest(list, startTime, endTime);
			// For debug purposes
			 writeSOAPToFile(request, "UtilizationDRequest.xml");
			try
			{
				if (request != null)
				{
					String response;
					response = sendSoapRequest(serverURL, request);
	
					// For debug purposes
					writeStringToFile(response, "responseUtilizationD.xml");
					conv = new XML2Java();
					if (!conv.fillUtilizationData(response, list, startTime, endTime))
						throw new FetchFailException();
	
				}
				else
					throw new FetchFailException();
			}
			catch (FetchFailException e)
			{
	             System.out.println("no data");
//	             			throw new FetchUtilDataException();
			}
		}



	/**
	 * Returns a map of services read from a file.
	 * 
	 * @param filename
	 *            Filename
	 * @return Service map (key = name; value = url)
	 */
	private Map<String, String> getService(String filename) throws FetchFailException
	{
		String line;
		String[] service;
		try (BufferedReader br = new BufferedReader(new FileReader(new File(filename))))
		{
			Map<String, String> map = new HashMap<>();
			while ((line = br.readLine()) != null)
			{
				service = line.split(SPLIT);
				map.put(service[0], service[1]);
			}
			return map;
		}
		catch (IOException e)
		{
			System.out.println("problem to read: " + filename);
			throw new FetchFailException();
		}
	}



	/**
	 * Send SOAP messages over HTTP.
	 * 
	 * @param uri
	 *            URI of the service
	 * @param request
	 *            SOAP request
	 * @return True, if responding
	 * 
	 */
	private String sendSoapRequest(String uri, SOAPMessage request) throws FetchFailException
	{

		try
		{
			HttpURLConnection conn;
			URL url = new URL(uri);

			conn = (HttpURLConnection) url.openConnection();

			conn.setRequestMethod(REQUEST_METHOD);
			conn.setRequestProperty(CONTENT_TYPE, CONTENT_CHARSET);
			conn.setRequestProperty(ACCEPT, ACCEPT_PARAM);
			conn.setRequestProperty(SOAP_ACTION, SOAP_ACTION_VALUE);
			conn.setDoInput(true);
			conn.setDoOutput(true);

			// Send request
			try (OutputStream requestOs = conn.getOutputStream())
			{
				request.writeTo(conn.getOutputStream());
			}
			catch (SOAPException | IOException e)
			{
				System.out.println("fail in send SOAP request");
				throw new FetchFailException();
			}
			// Receive response
			try
			{
				InputStream in = conn.getInputStream();
				InputStreamReader is = new InputStreamReader(in);
				StringBuilder sb = new StringBuilder();
				BufferedReader br = new BufferedReader(is);
				String read = br.readLine();
				while (read != null)
				{
					sb.append(read);
					read = br.readLine();
				}
				conn.disconnect();
				
				if (sb.toString().isEmpty())
					throw new FetchFailException();

				return sb.toString();
			}
			catch (IOException ex)
			{
				System.out.println("fail in receive response");
				throw new FetchFailException();
			}

		}
		catch (IOException ex)
		{
			System.out.println("The server with uri: " + uri + " is down");
			throw new FetchFailException();
		}
	}

	/**
	 * Write SOAP Message to File. For debug purposes.
	 * 
	 * @param request
	 *            SOAP Message to write
	 * @param filename
	 *            Filename
	 */
	private void writeSOAPToFile(SOAPMessage request, String filename)
	{
		try (FileOutputStream fos = new FileOutputStream(filename))
		{
			request.writeTo(fos);
		}
		catch (IOException | SOAPException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Write String to File. . For debug purposes.
	 * 
	 * @param response
	 *            String to write
	 * @param filename
	 *            Filename
	 */
	@SuppressWarnings("unused")
	private void writeStringToFile(String response, String filename)
	{
		try (PrintWriter pw = new PrintWriter(filename))
		{
			pw.print(response);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}









	
}
