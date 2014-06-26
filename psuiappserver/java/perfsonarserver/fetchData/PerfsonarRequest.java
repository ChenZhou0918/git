package perfsonarserver.fetchData;

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
import perfsonarserver.fetchData.exception.FetchDJLDataException;
import perfsonarserver.fetchData.exception.FetchDJLInterfaceException;
import perfsonarserver.fetchData.exception.FetchDJLServiceException;
import perfsonarserver.fetchData.exception.FetchFailException;
import perfsonarserver.fetchData.exception.FetchThroughDataExcpetion;
import perfsonarserver.fetchData.exception.FetchThroughInterfaceException;
import perfsonarserver.fetchData.exception.FetchThroughServiceException;
import perfsonarserver.fetchData.exception.FetchUtilDataException;
import perfsonarserver.fetchData.exception.FetchUtilInterfaceException;
import perfsonarserver.fetchData.exception.FetchUtilServiceException;
import perfsonarserver.fetchData.transferObjects.DelayJitterLossData;
import perfsonarserver.fetchData.transferObjects.DelayJitterLossInterfacePair;
import perfsonarserver.fetchData.transferObjects.ThroughputData;
import perfsonarserver.fetchData.transferObjects.ThroughputInterfacePair;
import perfsonarserver.fetchData.transferObjects.UtilizationData;
import perfsonarserver.fetchData.transferObjects.UtilizationInterface;
import perfsonarserver.fetchData.IServerRequest;
/**
 * This class perfomrs requestss into the perfSONAR network.
 * 
 * @author Clemens Schlei, Florian Rueffer
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
	public List<DelayJitterLossInterfacePair> getDelayJitterLossData(String serverURL, List<DelayJitterLossInterfacePair> list, long startTime, long endTime) throws FetchFailException
	{
		if (!list.isEmpty())
		{
			//getDJLData(list, startTime, endTime, serverURL);
			return list;
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
	/**************************************************************************************************************************/
	/*                                              Utilization                                                            */
	/**************************************************************************************************************************/

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
			throw new FetchUtilDataException();
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

	@Override
	public List<ThroughputData> getThroughputData(String serverURL,
			String source, String dest, String MID, long startTime, long endTime)
			throws FetchFailException {
		// TODO Auto-generated method stub
		return null;
	}

	
}
