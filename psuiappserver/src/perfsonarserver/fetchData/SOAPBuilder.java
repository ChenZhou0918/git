package perfsonarserver.fetchData;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import perfsonarserver.fetchData.consts.MessageAttConsts;
import perfsonarserver.fetchData.consts.MessageElementConsts;
import perfsonarserver.fetchData.consts.MessageNSConsts;
import perfsonarserver.fetchData.consts.SOAPBuilderConsts;
import perfsonarserver.fetchData.transferObjects.DelayJitterLossInterfacePair;
import perfsonarserver.fetchData.transferObjects.ThroughputInterfacePair;
import perfsonarserver.fetchData.transferObjects.UtilizationInterface;

/**
 * This class provides methods to generate soap messages.
 * 
 * @author Clemens Schlei, Florian Rueffer
 * 
 */
public class SOAPBuilder
{

	/**
	 * Resolution value which should be used for requests.
	 */
	private int resolutionValue = SOAPBuilderConsts.RESOLUTION_DEFAULT;

	/**
	 * Packetsize value which should be used for requests.
	 */
	private int packetsizeValue = SOAPBuilderConsts.PACKETSIZE_DEFAULT;

	/**
	 * Interval value which should be used for requests.
	 */
	private int intervalValue = SOAPBuilderConsts.INTERVAL_DEFAULT;

	/**
	 * Groupsize value which should be used for requests.
	 */
	private int groupsizeValue = SOAPBuilderConsts.GROUPSIZE_DEFAULT;

	/**
	 * Precedence value which should be used for requests.
	 */
	private String precedenceValue = SOAPBuilderConsts.PRECEDENCE_DEFAULT;

	/**
	 * Consolidation value which should be used for requests.
	 */
	private String consolidationFunctionValue = SOAPBuilderConsts.CONSOLIDATION_DEFAULT;

	/**
	 * DJL-Type constant (DJL = Delay, Jitter, Loss).
	 */
	public static final String DJL = "DJL";
	/**
	 * METADATA_BWCTL1-Type constant.
	 */
	public static final String METADATA_BWCTL1 = MessageElementConsts.METADATA + "-" + MessageNSConsts.BWCTL + "1";
	/**
	 * THROUGHPUT_INTERFACES-Type constant.
	 */
	public static final String THROUGHPUT_INTERFACES = "ThroughputInterfaces";
	/**
	 * THROUGHPUT_DATA-Type constant.
	 */
	public static final String THROUGHPUT_DATA = "ThroughputData";
	/**
	 * UTILIZATION_DATA-Type constant.
	 */
	public static final String UTILIZATION_DATA = "UtilizationData";
	/**
	 * UTILIZATION_INTERFACES-Type constant.
	 */
	public static final String UTILIZATION_INTERFACES = "UtilizationInterfaces";

	/**
	 * @return the consolidationFunctionValue
	 */
	public String getConsolidationFunctionValue()
	{
		return consolidationFunctionValue;
	}

	/**
	 * Generates a SOAP-Request for DelayJitterLoss-Data.
	 * 
	 * @param list
	 *            List containing Interface-Pairs
	 * @param startTime
	 *            Start time of measurement period in milliseconds
	 * @param endTime
	 *            End time of measurement period in milliseconds
	 * @return SOAP-Request or null
	 */
	public SOAPMessage getDelayJitterLossDataRequest(List<DelayJitterLossInterfacePair> list, long startTime, long endTime)
	{
		
		try
		{
			SOAPMessage message = createNewMessage();
			SOAPBodyElement messageElement = newMessage(SOAPBuilder.DJL, message);
			SOAPElement currentMetadata;
			// Generate for every Interface-Pair the needed xml-elements
			for (int i = 0; i < list.size(); i++)
			{
				DelayJitterLossInterfacePair currentInterface = list.get(i);
				
				currentMetadata = addDJLDRSources(messageElement, i, currentInterface);
				addDJLSourcesParam(currentMetadata, i, currentInterface);
				//addHadesElement(startTime, endTime, messageElement);
				addTimeParam(startTime, endTime, messageElement, i);
				
				addDataElement(messageElement, i);
				
				
//				DelayJitterLossInterfacePair currentInterface = list.get(i);
//				currentMetadata = addDJLDRSources(messageElement, i, currentInterface);
//				addDJLSourcesParam(currentMetadata, i, currentInterface);
//				addTimeParam(startTime, endTime, messageElement, i);
//				addDataElement(messageElement, i);
			}
			return message;
		}
		catch (SOAPException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Generates a SOAP-Request for DelayJitterLoss-Interfaces.
	 * 
	 * @param startTime
	 *            Start time of measurement period in milliseconds
	 * @param endTime
	 *            End time of measurement period in milliseconds
	 * @return SOAP-Request or null
	 */
	public SOAPMessage getDelayJitterLossInterfacePairsRequest(long startTime, long endTime)
	{

		try
		{
			SOAPMessage message = createNewMessage();
			SOAPBodyElement messageElement = newMessage(SOAPBuilder.DJL, message);

			addHadesElement(startTime, endTime, messageElement);
			addDataElement(messageElement, SOAPBuilderConsts.DEFAULT_ELEMENT_NR);
			return message;
		}
		catch (SOAPException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @return the groupsizeValue
	 */
	public int getGroupsizeValue()
	{
		return groupsizeValue;
	}

	/**
	 * @return the intervalValue
	 */
	public int getIntervalValue()
	{
		return intervalValue;
	}

	/**
	 * @return the packetsizeValue
	 */
	public int getPacketsizeValue()
	{
		return packetsizeValue;
	}

	/**
	 * @return the precedenceValue
	 */
	public String getPrecedenceValue()
	{
		return precedenceValue;
	}

	/**
	 * @return the resolutionValue
	 */
	public int getResolutionValue()
	{
		return resolutionValue;
	}

	/**
	 * Generates a SOAP-Request for Throughput-Data.
	 * 
	 * @param list
	 *            List containing Interface-Pairs
	 * @param startTime
	 *            Start time of measurement period in milliseconds
	 * @param endTime
	 *            End time of measurement period in milliseconds
	 * @return SOAP-Request or null
	 */
	public SOAPMessage getThroughputDataRequest(List<ThroughputInterfacePair> list, long startTime, long endTime)
	{
		try
		{
			ThroughputInterfacePair currentInterface;
			SOAPMessage message = createNewMessage();
			SOAPBodyElement messageElement = newMessage(SOAPBuilder.THROUGHPUT_DATA, message);
			// Generate for every Interface-Pair the needed xml-elements
			for (int i = 0; i < list.size(); i++)
			{
				currentInterface = list.get(i);
				addTDRSources(messageElement, currentInterface, i);
				addTimeParam(startTime, endTime, messageElement, i);
				addDataElement(messageElement, i);
			}
			return message;
		}
		catch (SOAPException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Generates a SOAP-Request for Throughput-Interfaces.
	 * 
	 * @param startTime
	 *            Start time of measurement period in milliseconds
	 * @param endTime
	 *            End time of measurement period in milliseconds
	 * @return SOAP-Request or null
	 */
	public SOAPMessage getThroughputInterfacePairRequest(long startTime, long endTime)
	{
		try
		{
			SOAPMessage message = createNewMessage();
			SOAPBodyElement messageElement = newMessage(SOAPBuilder.THROUGHPUT_INTERFACES, message);

			addBWCTLElement(messageElement);
			addBWCTLMetadataElement(messageElement);
			addTimeParam(startTime, endTime, messageElement, SOAPBuilderConsts.DEFAULT_ELEMENT_NR);
			addDataElement(messageElement, SOAPBuilderConsts.DEFAULT_ELEMENT_NR);
			return message;
		}
		catch (SOAPException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Generates a SOAP-Request for Utilization-Data.
	 * 
	 * @param list
	 *            List containing Interface-Pairs
	 * @param startTime
	 *            Start time of measurement period in milliseconds
	 * @param endTime
	 *            End time of measurement period in milliseconds
	 * @return SOAP-Request or null
	 */
	public SOAPMessage getUtilizationDataRequest(List<UtilizationInterface> list, long startTime, long endTime)
	{
		try
		{
			SOAPMessage message = createNewMessage();
			SOAPBodyElement messageElement = newMessage(SOAPBuilder.UTILIZATION_DATA, message);
						
			UtilizationInterface currentI;
			// Generate for every Interface the needed xml-elements
			for (int i = 0; i < list.size(); i++)
			{
				currentI = list.get(i);
				addNetutilElement(messageElement, currentI, i);
				addNetutilParamElement(startTime, endTime, messageElement, i);
				addDataElement(messageElement, i);
			}
			return message;
		}
		catch (SOAPException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Generates a SOAP-Request for Utilization-Intefaces.
	 * 
	 * @return SOAP-Request or null
	 */
	public SOAPMessage getUtilizationInterfaceRequest()
	{

		try
		{
			SOAPMessage message = createNewMessage();
			SOAPBodyElement messageElement = newMessage(SOAPBuilder.UTILIZATION_INTERFACES, message);

			addEmptyNetutilElement(messageElement);
			addSubElement(MessageNSConsts.NMWG, MessageElementConsts.DATA, MessageAttConsts.ID, MessageElementConsts.DATA + 1, MessageAttConsts.METADATA_ID_REF, MessageElementConsts.METADATA + 1, messageElement);
//			 addDataElement(messageElement,
//			 SOAPBuilderConsts.DEFAULT_ELEMENT_NR);
			return message;
		}
		catch (SOAPException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param consolidationFunctionValue
	 *            the consolidationFunctionValue to set
	 */
	public void setConsolidationFunctionValue(String consolidationFunctionValue)
	{
		this.consolidationFunctionValue = consolidationFunctionValue;
	}

	/**
	 * @param groupsizeValue
	 *            the groupsizeValue to set
	 */
	public void setGroupsizeValue(int groupsizeValue)
	{
		this.groupsizeValue = groupsizeValue;
	}

	/**
	 * @param intervalValue
	 *            the intervalValue to set
	 */
	public void setIntervalValue(int intervalValue)
	{
		this.intervalValue = intervalValue;
	}

	/**
	 * @param packetsizeValue
	 *            the packetsizeValue to set
	 */
	public void setPacketsizeValue(int packetsizeValue)
	{
		this.packetsizeValue = packetsizeValue;
	}

	/**
	 * @param precedenceValue
	 *            the precedenceValue to set
	 */
	public void setPrecedenceValue(String precedenceValue)
	{
		this.precedenceValue = precedenceValue;
	}

	/**
	 * @param resolutionValue
	 *            the resolutionValue to set
	 */
	public void setResolutionValue(int resolutionValue)
	{
		this.resolutionValue = resolutionValue;
	}

	/**
	 * Adds a bwctl element to the message.
	 * 
	 * @param messageElement
	 *            Message
	 * @throws SOAPException
	 */
	private void addBWCTLElement(SOAPBodyElement messageElement) throws SOAPException
	{
		SOAPElement currentMetadata;
		SOAPElement currentSubElement;
		currentMetadata = addMetadata(MessageNSConsts.NMWG, SOAPBuilder.METADATA_BWCTL1, messageElement);
		currentSubElement = addSubElement(MessageNSConsts.BWCTL, MessageElementConsts.SUBJECT, currentMetadata);
		addSubElement(MessageNSConsts.NMWGT, MessageElementConsts.END_POINT, currentSubElement);
		addSubElement(MessageNSConsts.NMWG, MessageAttConsts.EVENT_TYPE, currentMetadata).addTextNode(MessageNSConsts.BWCTL_EVENT);
	}

	/**
	 * Adds a bwctl metadata element to the message.
	 * 
	 * @param messageElement
	 * @throws SOAPException
	 */
	private void addBWCTLMetadataElement(SOAPBodyElement messageElement) throws SOAPException
	{
		SOAPElement currentMetadata;
		currentMetadata = addMetadata(MessageNSConsts.NMWG, MessageElementConsts.METADATA + (SOAPBuilderConsts.DEFAULT_ELEMENT_NR + 1), messageElement);
		addTREmptySources(currentMetadata, SOAPBuilderConsts.DEFAULT_ELEMENT_NR);
		addSubElement(MessageNSConsts.NMWG, MessageAttConsts.PARAMETERS, MessageAttConsts.ID, MessageAttConsts.PARAM + (SOAPBuilderConsts.DEFAULT_ELEMENT_NR + 1), currentMetadata);
	}

	/**
	 * Adds a data element to the message.
	 * 
	 * @param messageElement
	 *            Message
	 * @param i
	 *            Current index
	 * @throws SOAPException
	 */
	private void addDataElement(SOAPBodyElement messageElement, int i) throws SOAPException
	{
		addSubElement(MessageNSConsts.NMWG, MessageElementConsts.DATA, MessageAttConsts.ID, MessageElementConsts.DATA + (i + 1), MessageAttConsts.METADATA_ID_REF, MessageElementConsts.METADATA + (i + 1) + MessageAttConsts.MINUS_TWO, messageElement);
	}

	/**
	 * Adds a sources element to the message, used in delay, jitter, loss
	 * requests.
	 * 
	 * @param messageElement
	 *            Message
	 * @param i
	 *            Current index
	 * @param currentInterface
	 *            Current interface
	 * @return Metadata element
	 * @throws SOAPException
	 */
	private SOAPElement addDJLDRSources(SOAPBodyElement messageElement, int i, DelayJitterLossInterfacePair currentInterface) throws SOAPException
	{
		SOAPElement currentMetadata;
		SOAPElement currentSubElement;
		currentMetadata = addMetadata(MessageNSConsts.NMWG, MessageElementConsts.METADATA + (i + 1), messageElement);
		currentSubElement = addSubElement(MessageNSConsts.HADES, MessageElementConsts.SUBJECT, MessageAttConsts.ID, MessageElementConsts.SUBJECT + (i + 1), currentMetadata);
		currentSubElement = addSubElement(MessageNSConsts.NMWGT, MessageElementConsts.END_POINT_PAIR, currentSubElement);
		addSubElement(MessageNSConsts.NMWGT, MessageAttConsts.SRC, MessageAttConsts.VALUE, currentInterface.getSource(), MessageAttConsts.TYPE, MessageAttConsts.IF_NAME, currentSubElement);
		addSubElement(MessageNSConsts.NMWGT, MessageAttConsts.DST, MessageAttConsts.VALUE, currentInterface.getDestination(), MessageAttConsts.TYPE, MessageAttConsts.IF_NAME, currentSubElement);
		addSubElement(MessageNSConsts.NMWG, MessageAttConsts.EVENT_TYPE, currentMetadata).addTextNode(MessageNSConsts.HADES_EVENT);
		return currentMetadata;
	}

	/**
	 * Adds a sources param element to the message, used in delay, jitter, loss
	 * requests.
	 * 
	 * @param currentMetadata
	 *            Current metadata element
	 * @param i
	 *            Current index
	 * @param pre
	 *            Precedence value
	 * @param groups
	 *            Group size value
	 * @param interval
	 *            Interval value
	 * @param packets
	 *            Packet size value
	 * @throws SOAPException
	 */
	private void addDJLRSourcesParam(SOAPElement currentMetadata, int i, String pre, int groups, int interval, int packets) throws SOAPException
	{
		addDJLSourcesParam(currentMetadata, i, new DelayJitterLossInterfacePair(interval, groups, pre, packets));
	}

	/**
	 * Adds a sources param element to the message, used in delay, jitter, loss
	 * requests.
	 * 
	 * @param currentMetadata
	 *            Current metadata
	 * @param i
	 *            Current index
	 * @param currentInterface
	 *            Current interface
	 * @throws SOAPException
	 */
	private void addDJLSourcesParam(SOAPElement currentMetadata, int i, DelayJitterLossInterfacePair currentInterface) throws SOAPException
	{
		SOAPElement currentSubElement;
		currentSubElement = addSubElement(MessageNSConsts.HADES, MessageAttConsts.PARAMETERS, MessageAttConsts.ID, MessageAttConsts.PARAM + (i + 1), currentMetadata);
		addSubElement(MessageNSConsts.NMWG, MessageAttConsts.PARAMETER, MessageAttConsts.VALUE, currentInterface.getPrecedence(), MessageAttConsts.NAME, MessageAttConsts.PRECEDENCE, currentSubElement);
		addSubElement(MessageNSConsts.NMWG, MessageAttConsts.PARAMETER, MessageAttConsts.VALUE, String.valueOf(currentInterface.getGroupsize()), MessageAttConsts.NAME, MessageAttConsts.GROUPSIZE, currentSubElement);
		addSubElement(MessageNSConsts.NMWG, MessageAttConsts.PARAMETER, MessageAttConsts.VALUE, String.valueOf(currentInterface.getInterval()), MessageAttConsts.NAME, MessageAttConsts.INTERVAL, currentSubElement);
		addSubElement(MessageNSConsts.NMWG, MessageAttConsts.PARAMETER, MessageAttConsts.VALUE, String.valueOf(currentInterface.getPacketsize()), MessageAttConsts.NAME, MessageAttConsts.PACKETSIZE, currentSubElement);
	}

	/**
	 * Adds an netutil element to the message, which doesn't contain any set
	 * interface parameters.
	 * 
	 * @param messageElement
	 *            Message
	 * @throws SOAPException
	 */
	private void addEmptyNetutilElement(SOAPBodyElement messageElement) throws SOAPException
	{
		SOAPElement currentMetadata;
		SOAPElement currentSubElement;
		currentMetadata = addMetadata(MessageNSConsts.NMWG, MessageElementConsts.METADATA + (SOAPBuilderConsts.DEFAULT_ELEMENT_NR + 1), messageElement);
		currentSubElement = addSubElement(MessageNSConsts.NETUTIL, MessageElementConsts.SUBJECT, MessageAttConsts.ID, MessageElementConsts.SUBJECT + (SOAPBuilderConsts.DEFAULT_ELEMENT_NR + 1), currentMetadata);
		addSubElement(MessageNSConsts.NMWGT, MessageElementConsts.INTERFACE, currentSubElement);
		addSubElement(MessageNSConsts.NMWG, MessageAttConsts.EVENT_TYPE, currentMetadata).addTextNode(MessageNSConsts.UTILIZATION_EVENT);
	}

	/**
	 * Adds envelop namespaces.
	 * 
	 * @param message
	 * @throws SOAPException
	 */
	private void addEnvelopNamespaces(SOAPMessage message) throws SOAPException
	{
		SOAPEnvelope envelope = message.getSOAPPart().getEnvelope();
		addNamespaces(getEnvelopNamespaces(), envelope);
		envelope.addAttribute(new QName(MessageNSConsts.SOAP_ENV_ENCODING_STYLE), MessageNSConsts.SOAPENC_NS);
	}

	/**
	 * Adds an hades element to the message.
	 * 
	 * @param startTime
	 *            Start time of measurement period in milliseconds
	 * @param endTime
	 *            End time of measurement period in milliseconds
	 * @param messageElement
	 *            Message
	 * @throws SOAPException
	 */
	private void addHadesElement(long startTime, long endTime, SOAPBodyElement messageElement) throws SOAPException
	{
		SOAPElement currentMetadata;
		currentMetadata = addMetadata(MessageNSConsts.NMWG, MessageElementConsts.METADATA + (SOAPBuilderConsts.DEFAULT_ELEMENT_NR + 1), messageElement);
		addSubElement(MessageNSConsts.HADES, MessageElementConsts.SUBJECT, MessageAttConsts.ID, MessageElementConsts.SUBJECT + (SOAPBuilderConsts.DEFAULT_ELEMENT_NR + 1), currentMetadata);
		addSubElement(MessageNSConsts.NMWG, MessageAttConsts.EVENT_TYPE, currentMetadata).addTextNode(MessageNSConsts.HADES_EVENT);

		addDJLRSourcesParam(currentMetadata, SOAPBuilderConsts.DEFAULT_ELEMENT_NR, precedenceValue, groupsizeValue, intervalValue, packetsizeValue);
		addTimeParam(startTime, endTime, messageElement, SOAPBuilderConsts.DEFAULT_ELEMENT_NR);
	}

	/**
	 * Adds a new metadata element.
	 * 
	 * @param prefix
	 *            Prefix
	 * @param idValue
	 *            Id value
	 * @param element
	 *            Element to which the metadata element should be added
	 * @return Reference to metadata element
	 * @throws SOAPException
	 */
	private SOAPElement addMetadata(String prefix, String idValue, SOAPElement element) throws SOAPException
	{
		return addSubElement(prefix, MessageElementConsts.METADATA, MessageAttConsts.ID, idValue, element);
	}

	/**
	 * Adds namespaces to the parameterized element .
	 * 
	 * @param map
	 *            Map with namespaces; Key->Namespace-Name, Value->Namespace url
	 * @param element
	 *            SOAP-Element to which the namespaces should be added
	 * @throws SOAPException
	 */
	private void addNamespaces(Map<String, String> map, SOAPElement element) throws SOAPException
	{
		Iterator<String> it = map.keySet().iterator();
		String currentKey, currentValue;
		while (it.hasNext())
		{
			currentKey = it.next();
			currentValue = map.get(currentKey);
			element.addNamespaceDeclaration(currentKey, currentValue);
		}
	}

	/**
	 * Adds a netutil element to the message.
	 * 
	 * @param messageElement
	 *            Message
	 * @param currentI
	 *            Current interface
	 * @param i
	 *            Current index
	 * @throws SOAPException
	 */
	private void addNetutilElement(SOAPBodyElement messageElement, UtilizationInterface currentI, int i) throws SOAPException
	{
		SOAPElement currentMetadata;
		SOAPElement currentSubElement;
		currentMetadata = addMetadata(MessageNSConsts.NMWG, MessageElementConsts.METADATA + (i + 1), messageElement);
		currentSubElement = addSubElement(MessageNSConsts.NETUTIL, MessageElementConsts.SUBJECT, MessageAttConsts.ID, MessageElementConsts.SUBJECT + (i + 1), currentMetadata);
		currentSubElement = addSubElement(MessageNSConsts.NMWGT, MessageElementConsts.INTERFACE, currentSubElement);
		addSubElement(MessageNSConsts.NMWGT, MessageElementConsts.IF_NAME, currentSubElement).addTextNode(currentI.getName());
		if (!currentI.getIpAddress().isEmpty() && currentI.getIpType() != null)
		{
			addSubElement(MessageNSConsts.NMWGT, MessageElementConsts.IF_ADDRESS, MessageAttConsts.TYPE, currentI.getIpType(), currentSubElement).addTextNode(currentI.getIpAddress());

		}
		addSubElement(MessageNSConsts.NMWGT, MessageElementConsts.HOST_NAME, currentSubElement).addTextNode(currentI.getHostname());
		addSubElement(MessageNSConsts.NMWGT, MessageElementConsts.DIRECTION, currentSubElement).addTextNode(currentI.getDirection());
		addSubElement(MessageNSConsts.NMWG, MessageAttConsts.EVENT_TYPE, currentMetadata).addTextNode(MessageNSConsts.UTILIZATION_EVENT);
	}

	/**
	 * Adds a netutil parameter element to the message.
	 * 
	 * @param startTime
	 *            Start time of measurement period in milliseconds
	 * @param endTime
	 *            End time of measurement period in milliseconds
	 * @param messageElement
	 *            Message
	 * @param i
	 *            Current index
	 * @throws SOAPException
	 */
	private void addNetutilParamElement(long startTime, long endTime, SOAPBodyElement messageElement, int i) throws SOAPException
	{
		SOAPElement currentSubElement;
		currentSubElement = addTimeParam(startTime, endTime, messageElement, i);
		addSubElement(MessageNSConsts.NMWG, MessageAttConsts.PARAMETER, MessageAttConsts.NAME, MessageAttConsts.CONSOLIDATION_FUNCTION, currentSubElement).addTextNode(consolidationFunctionValue);
		addSubElement(MessageNSConsts.NMWG, MessageAttConsts.PARAMETER, MessageAttConsts.NAME, MessageAttConsts.RESOLUTION, currentSubElement).addTextNode(String.valueOf(resolutionValue));
	}

	/**
	 * Adds a new element to the parameterized element.
	 * 
	 * @param prefix
	 *            Prefix
	 * @param name
	 *            Element name
	 * @param element
	 *            SOAP-Element to which the element should be added
	 * @return Reference to new element
	 * @throws SOAPException
	 */
	private SOAPElement addSubElement(String prefix, String name, SOAPElement element) throws SOAPException
	{
		return element.addChildElement(name, prefix);
	}

	/**
	 * Adds a new element with one attribute to the parameterized element.
	 * 
	 * @param prefix
	 *            Prefix
	 * @param name
	 *            Element name
	 * @param attName
	 *            Attribute name
	 * @param attValue
	 *            Attribute value
	 * @param element
	 *            SOAP-Element to which the element should be added
	 * @return Reference to new element
	 * @throws SOAPException
	 */
	private SOAPElement addSubElement(String prefix, String name, String attName, String attValue, SOAPElement element) throws SOAPException
	{
		SOAPElement newElement = addSubElement(prefix, name, element);
		newElement.addAttribute(new QName(attName), attValue);
		return newElement;
	}

	/**
	 * Adds a new element with two attributes to the parameterized element.
	 * 
	 * @param prefix
	 *            Prefix
	 * @param name
	 *            Element name
	 * @param attName
	 *            First attribute name
	 * @param attValue
	 *            First attribute value
	 * @param att2Name
	 *            Second attribute name
	 * @param att2Value
	 *            Second attribute value
	 * @param element
	 *            SOAP-Element to which the element should be added
	 * @return Reference to new element
	 * @throws SOAPException
	 */
	private SOAPElement addSubElement(String prefix, String name, String attName, String attValue, String att2Name, String att2Value, SOAPElement element) throws SOAPException
	{
		SOAPElement newElement = addSubElement(prefix, name, attName, attValue, element);
		newElement.addAttribute(new QName(att2Name), att2Value);
		return newElement;
	}

	/**
	 * Adds a sources element to the message. Used in requests for throughput
	 * data.
	 * 
	 * @param messageElement
	 *            Message
	 * @param currentInterface
	 *            Current interface
	 * @param i
	 *            Current index
	 * @throws SOAPException
	 */
	private void addTDRSources(SOAPBodyElement messageElement, ThroughputInterfacePair currentInterface, int i) throws SOAPException
	{
		SOAPElement currentMetadata;
		SOAPElement currentSubElement;
		currentMetadata = addMetadata(MessageNSConsts.NMWG, MessageElementConsts.METADATA + (i + 1), messageElement);
		currentSubElement = addTREmptySources(currentMetadata, i);
		addSubElement(MessageNSConsts.NMWGT, MessageAttConsts.SRC, MessageAttConsts.VALUE, currentInterface.getSrcAddress(), MessageAttConsts.TYPE, currentInterface.getSrcType(), currentSubElement);
		addSubElement(MessageNSConsts.NMWGT, MessageAttConsts.DST, MessageAttConsts.VALUE, currentInterface.getDestAddress(), MessageAttConsts.TYPE, currentInterface.getDestType(), currentSubElement);
	}

	/**
	 * Adds a time element to the message.
	 * 
	 * @param startTime
	 *            Start time of measurement period in milliseconds
	 * @param endTime
	 *            End time of measurement period in milliseconds
	 * @param messageElement
	 *            Message
	 * @param i
	 *            Current index
	 * @return Reference to the parameters element within the time element
	 * @throws SOAPException
	 */
	private SOAPElement addTimeParam(long startTime, long endTime, SOAPBodyElement messageElement, int i) throws SOAPException
	{
		SOAPElement currentMetadata;
		SOAPElement currentSubElement;
		currentMetadata = addMetadata(MessageNSConsts.NMWG, MessageElementConsts.METADATA + (i + 1) + MessageAttConsts.MINUS_TWO, messageElement);
		addSubElement(MessageNSConsts.SELECT, MessageElementConsts.SUBJECT, MessageAttConsts.ID, MessageElementConsts.SUBJECT + (i + 1) + MessageAttConsts.MINUS_TWO, MessageAttConsts.METADATA_ID_REF, MessageElementConsts.METADATA + (i + 1), currentMetadata);
		addSubElement(MessageNSConsts.NMWG, MessageAttConsts.EVENT_TYPE, currentMetadata).addTextNode(MessageNSConsts.OPS_SELECT_EVENT);
		// return element
		currentSubElement = addSubElement(MessageNSConsts.SELECT, MessageAttConsts.PARAMETERS, MessageAttConsts.ID, MessageAttConsts.PARAM + (i + 1) + MessageAttConsts.MINUS_TWO, currentMetadata);
		addSubElement(MessageNSConsts.NMWG, MessageAttConsts.PARAMETER, MessageAttConsts.NAME, MessageAttConsts.START_TIME, currentSubElement).addTextNode(Long.toString(startTime / MessageAttConsts.UNIX_FACTOR));
		addSubElement(MessageNSConsts.NMWG, MessageAttConsts.PARAMETER, MessageAttConsts.NAME, MessageAttConsts.END_TIME, currentSubElement).addTextNode(Long.toString(endTime / MessageAttConsts.UNIX_FACTOR));
		return currentSubElement;
	}

	/**
	 * Adds an not set sources element.
	 * 
	 * @param currentMetadata
	 *            Element
	 * @param i
	 *            Current index
	 * @return Reference to added element
	 * @throws SOAPException
	 */
	private SOAPElement addTREmptySources(SOAPElement currentMetadata, int i) throws SOAPException
	{
		SOAPElement currentSubElement;
		currentSubElement = addSubElement(MessageNSConsts.IPERF, MessageElementConsts.SUBJECT, MessageAttConsts.ID, MessageElementConsts.SUBJECT + (SOAPBuilderConsts.DEFAULT_ELEMENT_NR + 1), currentMetadata);
		currentSubElement = addSubElement(MessageNSConsts.NMWGT, MessageElementConsts.END_POINT_PAIR, currentSubElement);
		addSubElement(MessageNSConsts.NMWG, MessageAttConsts.EVENT_TYPE, currentMetadata).addTextNode(MessageNSConsts.IPERF_EVENT);
		return currentSubElement;
	}

	/**
	 * Creates an empty soap message.
	 * 
	 * @return New soap message or null
	 */
	private SOAPMessage createNewMessage()
	{
		MessageFactory factory;
		try
		{
			factory = MessageFactory.newInstance(SOAPConstants.SOAP_1_1_PROTOCOL);

			return factory.createMessage();
			
		}
		catch (SOAPException e)
		{
			e.printStackTrace();		
		}
		return null;
	}

	/**
	 * Returns a map, which contains namespaces for the soap envelop.
	 * 
	 * @return HashMap<String, String>; Key-> Namespace name, Value-> Namespace
	 *         url
	 */
	private HashMap<String, String> getEnvelopNamespaces()
	{
		HashMap<String, String> namespaces = new HashMap<>();
		namespaces.put(MessageNSConsts.XSI, MessageNSConsts.XSI_NS);
		namespaces.put(MessageNSConsts.SOAPENC, MessageNSConsts.SOAPENC_NS);
		namespaces.put(MessageNSConsts.XSD, MessageNSConsts.XSD_NS);
		return namespaces;
	
	}

	/**
	 * Returns a map, which contains namespaces for the soap message element.
	 * 
	 * @param type
	 *            Type of message
	 * @return HashMap<String, String>; Key-> Namespace name, Value-> Namespace
	 *         url
	 */
	private HashMap<String, String> getMessageNamespaces(String type)
	{
		HashMap<String, String> messageNamespaces = new HashMap<>();
		switch (type)
		{
		case SOAPBuilder.THROUGHPUT_DATA:
		case SOAPBuilder.THROUGHPUT_INTERFACES:
		{
			messageNamespaces.put(MessageNSConsts.BWCTL, MessageNSConsts.BWCTL_2_0_NS);
			messageNamespaces.put(MessageNSConsts.IPERF, MessageNSConsts.IPERF_2_0_NS);
			messageNamespaces.put(MessageNSConsts.SELECT, MessageNSConsts.SELECT_2_0_NS);
			break;
		}
		case SOAPBuilder.DJL:
		{
			messageNamespaces.put(MessageNSConsts.HADES, MessageNSConsts.HADES_NS);
			messageNamespaces.put(MessageNSConsts.SELECT, MessageNSConsts.SELECT_NS);

			break;
		}
		case SOAPBuilder.UTILIZATION_DATA:
		case SOAPBuilder.UTILIZATION_INTERFACES:
		{
			messageNamespaces.put(MessageNSConsts.ERRORS, MessageNSConsts.ERRORS_2_0_NS);
			messageNamespaces.put(MessageNSConsts.NETUTIL, MessageNSConsts.NETUTIL_2_0_NS);
			messageNamespaces.put(MessageNSConsts.SELECT, MessageNSConsts.SELECT_2_0_NS);
			messageNamespaces.put(MessageNSConsts.SNMP, MessageNSConsts.SNMP_2_0_NS);
			messageNamespaces.put(MessageNSConsts.DISCARDS, MessageNSConsts.DISCARDS_2_0_NS);
			break;
		}
		}
		messageNamespaces.put(MessageNSConsts.NMWGT, MessageNSConsts.NMWGT_2_0_NS);
		messageNamespaces.put(MessageNSConsts.NMTM, MessageNSConsts.NMTM_2_0_NS);
		messageNamespaces.put(MessageNSConsts.PERFSONAR, MessageNSConsts.PERFSONAR_NS);
		return messageNamespaces;
	}

	/**
	 * Generates a new message element.
	 * 
	 * @param type
	 *            Type of message
	 * @param message
	 * @return Reference to the new message element
	 * @throws SOAPException
	 */
	private SOAPBodyElement newMessage(String type, SOAPMessage message) throws SOAPException
	{
		addEnvelopNamespaces(message);
		SOAPBody body = message.getSOAPBody();
		QName messageName = new QName(MessageNSConsts.NMWG_2_0_NS, MessageElementConsts.MESSAGE, MessageNSConsts.NMWG);
		SOAPBodyElement messageElement = body.addBodyElement(messageName);

		addNamespaces(getMessageNamespaces(type), messageElement);
		messageElement.addAttribute(new QName(MessageNSConsts.XMLNS), MessageNSConsts.XMLNS_NAMESPACE);
		String mType = null;
		switch (type)
		{
		case SOAPBuilder.UTILIZATION_DATA:
		case SOAPBuilder.THROUGHPUT_DATA:
		case SOAPBuilder.DJL:
		{
			mType = MessageAttConsts.SETUP_DATA_REQUEST;
			break;
		}
		case SOAPBuilder.UTILIZATION_INTERFACES:
		case SOAPBuilder.THROUGHPUT_INTERFACES:
		{
			mType = MessageAttConsts.METADATA_KEY_REQUEST;
			break;
		}
		}

		messageElement.addAttribute(new QName(MessageAttConsts.TYPE), mType);

		return messageElement;
	}

}
