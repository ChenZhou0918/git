package perfsonarserver.fetchDataAndProcess;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.filter.ElementFilter;
import org.jdom2.input.DOMBuilder;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;
import org.xml.sax.SAXException;

import perfsonarserver.fetchDataAndProcess.JdomParser;
import perfsonarserver.fetchDataAndProcess.consts.MessageAttConsts;
import perfsonarserver.fetchDataAndProcess.consts.MessageElementConsts;
import perfsonarserver.fetchDataAndProcess.consts.MessageNSConsts;
import perfsonarserver.fetchDataAndProcess.transferObjects.DelayJitterLossData;
import perfsonarserver.fetchDataAndProcess.transferObjects.DelayJitterLossInterfacePair;
import perfsonarserver.fetchDataAndProcess.transferObjects.ThroughputData;
import perfsonarserver.fetchDataAndProcess.transferObjects.ThroughputInterfacePair;
import perfsonarserver.fetchDataAndProcess.transferObjects.UtilizationData;
import perfsonarserver.fetchDataAndProcess.transferObjects.UtilizationInterface;


/**
 * 
 * Class for XML to java object converting.
 * 
 * @author Clemens Schlei, Florian Rueffer
 * 
 */
public class XML2Java implements IConverter
{

	@Override
	public Object convertDJLIPairList(String response, String uri, long startTime, long endTime)
	{
		Document doc = openDoc(response);
		List<DelayJitterLossInterfacePair> list = new ArrayList<>();
		Iterator<Element> allMetadatas = getMetadataIT(doc);
		Element currentMetadata;
		DelayJitterLossInterfacePair currentInterface;
		String id;

		while (allMetadatas.hasNext())
		{
			currentMetadata = allMetadatas.next();
			id = currentMetadata.getAttributeValue(MessageAttConsts.ID);
			// If metadata id starts with "result"
			if (id.startsWith(MessageAttConsts.RESULT))
			{
				currentInterface = new DelayJitterLossInterfacePair(uri, startTime, endTime);
				Element parametersElement = currentMetadata.getChild(MessageAttConsts.PARAMETERS, currentMetadata.getNamespace());
				Iterator<Element> parameters = parametersElement.getChildren().iterator();
				getDJLParameter(currentInterface, parameters);
				list.add(currentInterface);
			}
		}
		return list;
	}

	@Override
	public Object convertThroughputIPairList(String response, String uri, long startTime, long endTime)
	{
		Document doc = openDoc(response);
		List<ThroughputInterfacePair> list = new ArrayList<>();
		Iterator<Element> allMetadatas = getMetadataIT(doc);
		Element currentMetadata;
		ThroughputInterfacePair currentInterface;
		while (allMetadatas.hasNext())
		{
			currentMetadata = allMetadatas.next();
			currentInterface = setThroughputSources(uri, startTime, endTime, currentMetadata);
			setThroughputIPI(currentMetadata, currentInterface);
			list.add(currentInterface);
		}
		return list;
	}

	@Override
	public Object convertUtilizationIList(String response, String uri)
	{
		Document doc = openDoc(response);
		List<UtilizationInterface> list = new ArrayList<>();
		Iterator<Element> allMetadatas = getMetadataIT(doc);
		Element currentMetadata, currentIElement;
		UtilizationInterface currentInterface;
		while (allMetadatas.hasNext())
		{
			currentMetadata = allMetadatas.next();
			currentIElement = getInterfaceElement(currentMetadata);

			currentInterface = new UtilizationInterface();
			currentInterface.setUri(uri);
			fillUtilizationInterface(currentIElement, currentInterface);
			list.add(currentInterface);
		}
		return list;
	}
	
	/**
	 * Sets the attributes of an UtilizationInterface-Object. The values are
	 * read from an xml-element.
	 * 
	 * @param currentIElement
	 *            XML-Element
	 * @param currentInterface
	 *            Current interface
	 */
	private void fillUtilizationInterface(Element currentIElement, UtilizationInterface currentInterface)
	{
		currentInterface.setIpAddress(currentIElement.getChildText(MessageElementConsts.IF_ADDRESS, currentIElement.getNamespace()));
		currentInterface.setIpType(currentIElement.getChild(MessageElementConsts.IF_ADDRESS, currentIElement.getNamespace()).getAttributeValue(MessageAttConsts.TYPE));
		currentInterface.setHostname(currentIElement.getChildText(MessageElementConsts.HOST_NAME, currentIElement.getNamespace()));
		currentInterface.setName(currentIElement.getChildText(MessageElementConsts.IF_NAME, currentIElement.getNamespace()));
		currentInterface.setDescription(currentIElement.getChildText(MessageElementConsts.IF_DESCRIPTION, currentIElement.getNamespace()));
		currentInterface.setDirection(currentIElement.getChildText(MessageElementConsts.DIRECTION, currentIElement.getNamespace()));
		currentInterface.setCapacity(Long.valueOf(currentIElement.getChildText(MessageElementConsts.CAPACITY, currentIElement.getNamespace())));
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean fillDJLData(String response, Object list)
	{
		boolean trigger = false;
		List<DelayJitterLossInterfacePair> iList = (List<DelayJitterLossInterfacePair>) list;
		List<DelayJitterLossData> dList;
		Document doc = openDoc(response);
		//Trying to get the source and destination of the DelayJitterLoss interfaces...so far, didn´t work out
		//List<DelayJitterLossInterfacePair> interfaceList = getDataInterfaces(doc);
		Iterator<Element> allDatas = getDataIT(doc);
		
		Element currentData;
		
		DelayJitterLossInterfacePair currentInterface;
		while (allDatas.hasNext())
		{
			currentData = allDatas.next();
			String parameter = currentData.getAttributeValue(MessageAttConsts.ID);
			System.out.println(parameter);
			String idRef = currentData.getAttributeValue(MessageAttConsts.METADATA_ID_REF);
			
			idRef = idRef.substring(MessageElementConsts.METADATA.length(), idRef.indexOf(MessageAttConsts.MINUS_TWO));
			if(idRef.contains("etadata"))
			{
				return trigger;
			}
			System.out.println(idRef.toString());
			currentInterface = iList.get(Integer.valueOf(idRef) - 1);
		
			dList = new ArrayList<DelayJitterLossData>();
			
			trigger = getDJLData(trigger, dList,iList, currentData);
			currentInterface.setData(dList);
		}
		return trigger;
		
	}
	
	/**
	 * Gets all data included in the current data element.
	 * 
	 * @param trigger
	 *            init bool value
	 * @param dList
	 *            List
	 * @param currentData
	 *            Current data element
	 * @return true, if data was read
	 */
	private boolean getDJLData(boolean trigger, List<DelayJitterLossData> dList,
			List<DelayJitterLossInterfacePair> iList,
			Element currentData)
	{
		 
		DelayJitterLossData currentInterfaceData;
		Iterator<Element> datumElements = currentData.getChildren().iterator();
		Element currentDatum;

		while (datumElements.hasNext())
		{
			currentDatum = datumElements.next();
			currentInterfaceData = new DelayJitterLossData();
			currentInterfaceData.setMedDelay(Double.valueOf(currentDatum.getAttributeValue(MessageAttConsts.MED_DELAY)));
			currentInterfaceData.setMedIpdvJitter(Double.valueOf(currentDatum.getAttributeValue(MessageAttConsts.MED_IPDV_JITTER)));
			currentInterfaceData.setMinIpdvJitter(Double.valueOf(currentDatum.getAttributeValue(MessageAttConsts.MIN_IPDV_JITTER)));
			currentInterfaceData.setTime((long) (Double.valueOf(currentDatum.getAttributeValue(MessageAttConsts.TIME)).doubleValue() * MessageAttConsts.UNIX_FACTOR));
			currentInterfaceData.setSync(currentDatum.getAttributeValue(MessageAttConsts.SYNC));
			currentInterfaceData.setMinDelay(Double.valueOf(currentDatum.getAttributeValue(MessageAttConsts.MIN_DELAY)));
			currentInterfaceData.setMaxDelay(Double.valueOf(currentDatum.getAttributeValue(MessageAttConsts.MAX_DELAY)));
			currentInterfaceData.setDuplicates(Integer.valueOf(currentDatum.getAttributeValue(MessageAttConsts.DUPLICATES)));
			currentInterfaceData.setLoss(Integer.valueOf(currentDatum.getAttributeValue(MessageAttConsts.LOSS)));
			currentInterfaceData.setMaxIpdvJitter(Double.valueOf(currentDatum.getAttributeValue(MessageAttConsts.MAX_IPDV_JITTER)));
			dList.add(currentInterfaceData); 
			trigger = true;
		}
		return trigger;
	}  

	@SuppressWarnings("unchecked")
	@Override
	public Object fillThroughputData(String response, Object list)
	{

		List<ThroughputInterfacePair> iList = (List<ThroughputInterfacePair>) list;
		HashMap<String, ThroughputInterfacePair> iMap = new HashMap<>();

		convertListToMap(iList, iMap);

		Document doc = openDoc(response);
		Iterator<Element> allData = getDataIT(doc);
		Element currentData;

		while (allData.hasNext())
		{
			currentData = allData.next();
			linkThroughputData(iMap, currentData);
		}
		iList = new ArrayList<>();
		convertMapToList(iList, iMap);
		return iList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean fillUtilizationData(String response, Object list, long startTime, long endTime)
	{
		Document doc = openDoc(response);
		List<UtilizationInterface> uList = (List<UtilizationInterface>) list;
		Iterator<Element> allData = getDataIT(doc);
		Iterator<Element> allMetadata = getMetadataIT(doc);

		Element currentData, currentMetadata;
		UtilizationInterface currentInterface = null;
		int i = 0;
		boolean trigger = false;
		String cMetaID, cRefID, oldID = "";
		try
		{
			while (allData.hasNext())
			{
				currentMetadata = allMetadata.next();
				cMetaID = currentMetadata.getAttributeValue(MessageAttConsts.ID);
				currentData = allData.next();
				cRefID = currentData.getAttributeValue(MessageAttConsts.METADATA_ID_REF);
				// Skip error elements
				if (cMetaID.startsWith(MessageAttConsts.RESULT))
				{
					i++;
					continue;
				}
				
				if(cMetaID.contains(MessageAttConsts.ID_CHAR))
					cMetaID = cMetaID.substring(0, cMetaID.lastIndexOf(MessageAttConsts.ID_CHAR));
				
				if(cRefID.contains(MessageAttConsts.ID_CHAR))
					cRefID = cRefID.substring(0, cRefID.lastIndexOf(MessageAttConsts.ID_CHAR));

				
				currentInterface = uList.get(i);
				
				// When a new interface pair is found...
				if (!cMetaID.equals(oldID))
				{
					// ... check currentInterface != Interface in
					// Metadata-Element
					if (!isCorrectInterface(currentMetadata, currentInterface))
					{
						// Interfaces weren't the same
						// Save old value of "i"
						int iBackup = i;
						boolean isCorrect = false;
						// Increment index for interface list and test the new
						// interface till the interfaces are equal or list size
						// is reached
						do
						{
							i++;
							currentInterface = uList.get(i);
						}
						while (!(isCorrect = isCorrectInterface(currentMetadata, currentInterface)) && i < uList.size() - 1);

						// When the loop before didn't find an equal interface,
						// restore "i" and decrement i in search for correct
						// interface
						if (!isCorrect)
						{
							i = iBackup;
							do
							{
								i--;
								currentInterface = uList.get(i);
							}
							while (!(isCorrect = isCorrectInterface(currentMetadata, currentInterface)) && i > 0);
						}
						// When no interface was found, skip element (shouldn't
						// be happening)
						if (!isCorrect)
						{
							continue;
						}
					}
					else
					{
						i++;
					}
				}
				oldID = cMetaID;
				trigger = setData(startTime, endTime, currentData, currentInterface);

				setDataSourceParams(currentData, currentInterface);
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return trigger;
	}

	/**
	 * Converts a list of "ThroughputInterfacePairs" classes to an hash map.
	 * 
	 * @param iList
	 *            List
	 * @param iMap
	 *            Map to which the list content should be added
	 */
	private void convertListToMap(List<ThroughputInterfacePair> iList, HashMap<String, ThroughputInterfacePair> iMap)
	{
		ThroughputInterfacePair currentInterface;
		for (int i = 0; i < iList.size(); i++)
		{
			currentInterface = iList.get(i);
			iMap.put(currentInterface.getMid(), currentInterface);
		}
	}

	/**
	 * Converts a hash map to a list of "ThroughputInterfacePairs" classes.
	 * 
	 * @param iList
	 *            List to which the map content should be added
	 * @param iMap
	 *            Map
	 */
	private void convertMapToList(List<ThroughputInterfacePair> iList, HashMap<String, ThroughputInterfacePair> iMap)
	{
		Iterator<String> it = iMap.keySet().iterator();
		while (it.hasNext())
		{
			iList.add(iMap.get(it.next()));
		}
	}

	/**
	 * Returns a new "ThroughputData" object which contains data from the
	 * parameterized datum element.
	 * 
	 * @param currentDatum
	 *            Datum element
	 * @return New ThroughputData" object
	 */
	private ThroughputData fillThroughputData(Element currentDatum)
	{
		ThroughputData currentInterfaceData;
		currentInterfaceData = new ThroughputData();
		currentInterfaceData.setInterval(currentDatum.getAttributeValue(MessageAttConsts.INTERVAL));
		currentInterfaceData.setNumBytesUnits(currentDatum.getAttributeValue(MessageAttConsts.NUM_BYTES_UNITS));
		currentInterfaceData.setTimeValue((Long.valueOf(currentDatum.getAttributeValue(MessageAttConsts.TIME_VALUE)) * MessageAttConsts.UNIX_FACTOR));
		currentInterfaceData.setValue(Double.valueOf(currentDatum.getAttributeValue(MessageAttConsts.VALUE)));
		currentInterfaceData.setValueUnits(currentDatum.getAttributeValue(MessageAttConsts.VALUE_UNITS));
		return currentInterfaceData;
	}



	/**
	 * Returns an iterator for data elements within an soap message.
	 * 
	 * @param doc
	 *            XML document
	 * @return Iterator which contains data elements
	 */
	private Iterator<Element> getDataIT(Document doc)
	{
		Element root = doc.getRootElement();
		Element body = root.getChild(MessageElementConsts.BODY_CONST, root.getNamespace());
		Element message = body.getChild(MessageElementConsts.MESSAGE, Namespace.getNamespace(MessageNSConsts.NMWG_2_0_NS));
		Iterator<Element> allDatas = message.getChildren(MessageElementConsts.DATA, message.getNamespace()).iterator();
		return allDatas;
	}
	
	private List<DelayJitterLossInterfacePair> getDataInterfaces(Document doc){
		  Element parameterElement;
	        List<Element> parameterElementeListe = new LinkedList<Element>();
	        List<DelayJitterLossInterfacePair> dljInterfaceListe = new ArrayList<>();
	        Iterator<Element> it = null;
	        DelayJitterLossInterfacePair djli = new DelayJitterLossInterfacePair();
		
		Element root = doc.getRootElement();
		Element body = root.getChild(MessageElementConsts.BODY_CONST, root.getNamespace());
		Element message = body.getChild(MessageElementConsts.MESSAGE, Namespace.getNamespace(MessageNSConsts.NMWG_2_0_NS));
		
		//Die Nachkömmlinge des Metadata Elements auslesen
        Iterator<Element> metadata =  message.getDescendants(new ElementFilter());
        while(metadata.hasNext()) {
           parameterElement =  metadata.next();
           //Nur die Parameterelemente für die weiter Verarbeitung in der Liste speichern
    	   if(parameterElement.getChild("parameter", Namespace.getNamespace("http://ggf.org/ns/nmwg/base/2.0/")) != null) {   	
    		   parameterElementeListe.add(parameterElement);
    	   } 
        }
     
        //Liste der Parameterelemente durchgehen und Attribute rauslesen
       	for(Element pm : parameterElementeListe) {
       		
       		if(pm!=null && pm.getAttributeValue("id").contains("param")){
       			Element parameter = pm.getChild("parameter", Namespace.getNamespace("http://ggf.org/ns/nmwg/base/2.0/"));
       			 it =  pm.getDescendants(new ElementFilter());
       			 djli = new DelayJitterLossInterfacePair();
       			
       			//Attributwerte den DelayJitterLossInterfacePair Objekten zuweisen
       			while(it.hasNext()) {
       				parameter = it.next();
       				if(parameter != null && parameter.getAttributeValue("name").equals("receiver")) {
            			djli.setDestination(parameter.getAttributeValue("value"));
            		}    
            		if(parameter != null && parameter.getAttributeValue("name").equals("sender")) {
            				djli.setSource(parameter.getAttributeValue("value"));
            		}	
            		
       			}
       			if(djli.getDestination()!=null && djli.getSource()!=null){
       				dljInterfaceListe.add(djli);
       			}		
       		}
       		else {
       			
       		}
       	} for(DelayJitterLossInterfacePair data : dljInterfaceListe){
       		System.out.println(data.getSource());
   		System.out.println(data.getDestination());
    		
    	} 	
	
		return dljInterfaceListe; 	
	}


	

	/**
	 * Gets all parameter values from the iterator and sets them in the current
	 * interface.
	 * 
	 * @param currentInterface
	 *            Current interface
	 * @param parameters
	 *            Iterator containing parameter elements
	 */
	private void getDJLParameter(DelayJitterLossInterfacePair currentInterface, Iterator<Element> parameters)
	{
		while (parameters.hasNext())
		{
			Element currentParameter = parameters.next();
			String name = currentParameter.getAttributeValue(MessageAttConsts.NAME);
			switch (name)
			{
			case MessageAttConsts.DESTINATION:
			{
				currentInterface.setDestination(currentParameter.getAttributeValue(MessageAttConsts.VALUE));
				break;
			}
			case MessageAttConsts.GROUPSIZE:
			{
				currentInterface.setGroupsize(Integer.valueOf(currentParameter.getAttributeValue(MessageAttConsts.VALUE)));
				break;
			}
			case MessageAttConsts.INTERVAL:
			{
				currentInterface.setInterval(Integer.valueOf(currentParameter.getAttributeValue(MessageAttConsts.VALUE)));
				break;
			}
			case MessageAttConsts.MID:
			{
				currentInterface.setMid(Integer.valueOf(currentParameter.getAttributeValue(MessageAttConsts.VALUE)));
				break;
			}
			case MessageAttConsts.PRECEDENCE:
			{
				currentInterface.setPrecedence(currentParameter.getAttributeValue(MessageAttConsts.VALUE));
				break;
			}
			case MessageAttConsts.DESTINATION_IP:
			{
				currentInterface.setDestinationIP(currentParameter.getAttributeValue(MessageAttConsts.VALUE));
				break;
			}
			case MessageAttConsts.SOURCE:
			{
				currentInterface.setSource(currentParameter.getAttributeValue(MessageAttConsts.VALUE));
				break;
			}
			case MessageAttConsts.SOURCE_IP:
			{
				currentInterface.setSourceIP(currentParameter.getAttributeValue(MessageAttConsts.VALUE));
				break;
			}
			case MessageAttConsts.PACKETSIZE:
			{
				currentInterface.setPacketsize(Integer.valueOf(currentParameter.getAttributeValue(MessageAttConsts.VALUE)));
				break;
			}
			}
		}
	}

	/**
	 * Returns an netutil interface element when existent.
	 * 
	 * @param currentMetadata
	 *            Current metadata element
	 * @return Element or null
	 */
	private Element getInterfaceElement(Element currentMetadata)
	{
		return currentMetadata.getChild(MessageElementConsts.SUBJECT, Namespace.getNamespace(MessageNSConsts.NETUTIL_2_0_NS)).getChild(MessageElementConsts.INTERFACE, Namespace.getNamespace(MessageNSConsts.NMWGT_2_0_NS));
	}

	/**
	 * Returns an iterator for metadata elements within an soap message.
	 * 
	 * @param doc
	 *            XML document
	 * @return Iterator which contains metadata elements
	 */
	private Iterator<Element> getMetadataIT(Document doc)
	{
		Element root = doc.getRootElement();
		Element body = root.getChild(MessageElementConsts.BODY_CONST, root.getNamespace());
		Element message = body.getChild(MessageElementConsts.MESSAGE, Namespace.getNamespace(MessageNSConsts.NMWG_2_0_NS));
		Iterator<Element> allMetadatas = message.getChildren(MessageElementConsts.METADATA, message.getNamespace()).iterator();
		return allMetadatas;
	}

	/**
	 * Gets all data included in the current data element.
	 * 
	 * @param dataList
	 *            List
	 * @param currentData
	 *            Current data element
	 * @return true, if data was read
	 */
	private boolean getUtilizationDataValues(List<UtilizationData> dataList, Element currentData)
	{
		boolean trigger = false;
		UtilizationData cUD;
		Element cDatum;
		Iterator<Element> allDatums;
		String cValue;
		allDatums = currentData.getChildren(MessageElementConsts.DATUM, Namespace.getNamespace(MessageNSConsts.NETUTIL_BASE)).iterator();
		while (allDatums.hasNext())
		{
			cDatum = allDatums.next();
			cValue = cDatum.getAttributeValue(MessageAttConsts.VALUE);
			if (!cValue.equalsIgnoreCase(MessageAttConsts.NAN))
			{
				cUD = new UtilizationData();
				cUD.setValue(cValue);
				cUD.setTime(Long.valueOf(cDatum.getAttributeValue(MessageAttConsts.TIME_VALUE)));
				cUD.setValueUnits(cDatum.getAttributeValue(MessageAttConsts.VALUE_UNITS));
				dataList.add(cUD);
				trigger = true;
			}
		}
		return trigger;
	}

	/**
	 * Compare interface from list with interface in xml element.
	 * 
	 * @param currentMetadata
	 *            Metadata element
	 * @param currentInterface
	 *            Interface from list
	 * @return true, when interfaces are equal
	 */
	private boolean isCorrectInterface(Element currentMetadata, UtilizationInterface currentInterface)
	{

		boolean returnValue;
		String eName, iName, eDirection, iDirection;
		Element interfaceElement = currentMetadata.getChild(MessageElementConsts.SUBJECT, Namespace.getNamespace(MessageNSConsts.NETUTIL_2_0_NS)).getChild(MessageElementConsts.INTERFACE, Namespace.getNamespace(MessageNSConsts.NMWGT_2_0_NS));
		//eName = interfaceElement.getChildText(MessageElementConsts.IF_NAME, interfaceElement.getNamespace());
		//eDirection = interfaceElement.getChildText(MessageElementConsts.DIRECTION, interfaceElement.getNamespace());
		eName = interfaceElement.getChildText(MessageElementConsts.IF_NAME, interfaceElement.getNamespace());
		eDirection = interfaceElement.getChildText(MessageElementConsts.DIRECTION, interfaceElement.getNamespace());
		//iName = currentInterface.getName();
		//iDirection = currentInterface.getDirection();
		iName = currentInterface.getName();
		iDirection = currentInterface.getDirection();
		returnValue = (eName.equals(iName) && eDirection.equals(iDirection));
		return returnValue;

	}

	/**
	 * Links the content of the current read data element to an object of the
	 * hash map.
	 * 
	 * @param iMap
	 *            Hash map containing interfaces
	 * @param currentData
	 *            Data element
	 */
	private void linkThroughputData(HashMap<String, ThroughputInterfacePair> iMap, Element currentData)
	{
		Element currentDatum;
		ThroughputInterfacePair currentInterface;
		ThroughputData currentInterfaceData;
		List<Element> children;
		Iterator<Element> itData;
		String id;
		List<ThroughputData> dataList;
		if (!(children = currentData.getChildren()).isEmpty())
		{
			id = currentData.getAttributeValue(MessageAttConsts.METADATA_ID_REF);
			if (id.contains("_"))
			{
				id = id.substring(0, id.indexOf("_"));
			}
			if (iMap.containsKey(id))
			{
				itData = children.iterator();
				dataList = new ArrayList<>();
				currentInterface = iMap.get(id);
				while (itData.hasNext())
				{
					currentDatum = itData.next();
					currentInterfaceData = fillThroughputData(currentDatum);
					dataList.add(currentInterfaceData);
				}
				currentInterface.setData(dataList);
			}
		}
	}

	/**
	 * Opens an xml-string as xml document.
	 * 
	 * @param xml
	 *            as String
	 * @return Document
	 */
	private Document openDoc(String xml)
	{
		try
		{
			StringReader sr = new StringReader(xml);
			return new SAXBuilder().build(sr);
		}
		catch (JDOMException | IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Fetch all the data from the data element and save it in a list.
	 * 
	 * @param startTime
	 *            Start time of measurement period in milliseconds
	 * @param endTime
	 *            End time of measurement period in milliseconds
	 * @param currentData
	 *            Current data element
	 * @param currentInterface
	 *            Current interface
	 * @return true, when data was read
	 */
	private boolean setData(long startTime, long endTime, Element currentData, UtilizationInterface currentInterface)
	{
		List<UtilizationData> dataList;
		boolean trigger;
		currentInterface.setStartTime(startTime);
		currentInterface.setEndTime(endTime);
		if (currentInterface.getData() != null)
		{
			trigger = getUtilizationDataValues(currentInterface.getData(), currentData);
		}
		else
		{
			dataList = new ArrayList<>();
			trigger = getUtilizationDataValues(dataList, currentData);
			currentInterface.setData(dataList);
		}
		return trigger;
	}

	/**
	 * Sets data source attributes of an "UtilizationInterface" object with data
	 * read from a data element.
	 * 
	 * @param currentData
	 *            Data element
	 * @param currentInterface
	 *            UtilizationInterface object
	 */
	private void setDataSourceParams(Element currentData, UtilizationInterface currentInterface)
	{
		Iterator<Element> allParameter;
		Element cParam, cParameters;
		cParameters = currentData.getChild(MessageAttConsts.PARAMETERS, currentData.getNamespace());
		if (cParameters != null)
		{
			allParameter = cParameters.getChildren().iterator();
			while (allParameter.hasNext())
			{
				cParam = allParameter.next();
				switch (cParam.getAttributeValue(MessageAttConsts.NAME))
				{
				case MessageAttConsts.DATA_SOURCE_STEP:
				{
					currentInterface.setDataSourceStep(Integer.valueOf(cParam.getText()));
					break;
				}
				case MessageAttConsts.DATA_SOURCE_TYPE:
				{
					currentInterface.setDataSourceType(cParam.getText());
					break;
				}
				case MessageAttConsts.DATA_SOURCE_HEARTBEAT:
				{
					currentInterface.setDataSourceHeartbeat(Integer.valueOf(cParam.getText()));
					break;
				}
				case MessageAttConsts.DATA_SOURCE_MIN_VALUE:
				{
					currentInterface.setDataSourceMinValue(Long.valueOf(cParam.getText()));
					break;
				}
				case MessageAttConsts.DATA_SOURCE_MAX_VALUE:
				{
					currentInterface.setDataSourceMaxValue(Long.valueOf(cParam.getText()));
					break;
				}
				}
			}
		}
	}

	/**
	 * Set the protocol and interval values in the current interface.
	 * 
	 * @param currentMetadata
	 *            Current metadata
	 * @param currentInterface
	 *            Current interface
	 */
	private void setThroughputIPI(Element currentMetadata, ThroughputInterfacePair currentInterface)
	{
		Element parameters = currentMetadata.getChild(MessageAttConsts.PARAMETERS, Namespace.getNamespace(MessageNSConsts.IPERF_2_0_NS));
		Iterator<Element> allParameter = parameters.getChildren().iterator();
		Element currentParameter;
		String name;
		while (allParameter.hasNext())
		{
			currentParameter = allParameter.next();
			name = currentParameter.getAttributeValue(MessageAttConsts.NAME);
			switch (name)
			{
			case MessageAttConsts.PROTOCOL:
			{
				currentInterface.setProtocol(currentParameter.getAttributeValue(MessageAttConsts.VALUE));
				break;
			}
			case MessageAttConsts.INTERVAL:
			{
				currentInterface.setInterval(Integer.valueOf(currentParameter.getAttributeValue(MessageAttConsts.VALUE)));
				break;
			}
			}
		}
	}

	/**
	 * Creates a new ThroughputInterfacePair and sets source and destination
	 * values.
	 * 
	 * @param uri
	 *            Server url
	 * @param startTime
	 *            Start time of measurement period in milliseconds
	 * @param endTime
	 *            End time of measurement period in milliseconds
	 * @param currentMetadata
	 *            Current metadata
	 * @return New ThroughputInterfacePair object
	 */
	private ThroughputInterfacePair setThroughputSources(String uri, long startTime, long endTime, Element currentMetadata)
	{
		ThroughputInterfacePair currentInterface;
		String id;
		currentInterface = new ThroughputInterfacePair();
		currentInterface.setStartTime(startTime);
		currentInterface.setEndTime(endTime);
		currentInterface.setUri(uri);
		id = currentMetadata.getAttributeValue(MessageAttConsts.ID);
		currentInterface.setMid(id);

		Element subject = currentMetadata.getChild(MessageElementConsts.SUBJECT, Namespace.getNamespace(MessageNSConsts.IPERF_2_0_NS));
		Element endPointPair = subject.getChild(MessageElementConsts.END_POINT_PAIR, Namespace.getNamespace(MessageNSConsts.NMWGT_2_0_NS));
		Element endSrc = endPointPair.getChild(MessageAttConsts.SRC, endPointPair.getNamespace());
		currentInterface.setSrcType(endSrc.getAttributeValue(MessageAttConsts.TYPE));
		currentInterface.setSrcAddress(endSrc.getAttributeValue(MessageAttConsts.VALUE));
		Element endDest = endPointPair.getChild(MessageAttConsts.DST, endPointPair.getNamespace());
		currentInterface.setDestType(endDest.getAttributeValue(MessageAttConsts.TYPE));
		currentInterface.setDestAddress(endDest.getAttributeValue(MessageAttConsts.VALUE));
		return currentInterface;
	}

	@Override
	public List<DelayJitterLossInterfacePair> getDelayJitterLossInterfacePair() {
		// TODO Auto-generated method stub
		return null;
	}
		
}


