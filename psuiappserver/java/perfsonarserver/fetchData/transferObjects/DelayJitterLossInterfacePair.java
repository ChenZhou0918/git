package perfsonarserver.fetchData.transferObjects;

import java.io.Serializable;
import java.util.List;

/**
 * Object to store interface pair for delay, jitter, loss.
 * 
 * @author Clemens Schlei, Florian Rueffer
 * 
 */
public class DelayJitterLossInterfacePair 
{
	 private static DelayJitterLossInterfacePair instance = null;
	
	private String sourceIP;
	private String source;
	private String destinationIP;
	private String destination;
	private int interval;
	private int mid;
	private String uri;
	private long startTime;
	private long endTime;
	private List<DelayJitterLossData> data;
	private int groupsize;
	private String precedence;
	private int packetsize;
	private String metadataID;
	private String serviceName;
	private String destInterface;
	private String srcInterface;

	public DelayJitterLossInterfacePair() {
		
		
	}
	
	 public static DelayJitterLossInterfacePair getInstance() {
	        if (instance == null) {
	            instance = new DelayJitterLossInterfacePair();
	        }
	        return instance;
	    }
	/**
	 * Constructor for existing DelayJitterLoss InterfacePair.
	 * 
	 * @param uri
	 *            URI of the service
	 * @param startTime
	 *            Start time of measurement period in milliseconds
	 * @param endTime
	 *            End time of measurement period in milliseconds
	 */
	public DelayJitterLossInterfacePair(String uri, long startTime, long endTime)
	{
		super();
		this.uri = uri;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	/**
	 * Constructor for existing DelayJitterLoss InterfacePair.
	 * 
	 * @param source
	 *            Source DNS name
	 * @param destination
	 *            Destination DNS name
	 * @param uri
	 *            URI of the service
	 */
	public DelayJitterLossInterfacePair(String source, String destination, String uri)
	{
		super();
		this.source = source;
		this.destination = destination;
		this.uri = uri;
	}

	/**
	 * Constructor for existing DelayJitterLoss InterfacePair.
	 * 
	 * @param interval
	 *            Measurement interval in seconds (default 60s)
	 * @param groupsize
	 *            Groupsize
	 * @param precedence
	 *            Precedence
	 * @param packetsize
	 *            Packetsize
	 */
	public DelayJitterLossInterfacePair(int interval, int groupsize, String precedence, int packetsize)
	{
		super();
		this.interval = interval;
		this.groupsize = groupsize;
		this.precedence = precedence;
		this.packetsize = packetsize;
	}
	
	// Setzt alle Werte zurück
    public void Reset () {
    	this.interval = 0;
		this.groupsize = 0;
		this.precedence = "";
		this.packetsize = 0;
		this.source = "";
		this.destination = "";
		this.uri = "";
		this.startTime = 0;
		this.endTime = 0;
		this.sourceIP = "";
		this.source = "";
    }

    /**
	 * Get source interface
	 * 
	 * @return source interface
	 */
	public String getSrcInterface()
	{
		return srcInterface;
	}

	/**
	 * Set source interface
	 * 
	 * @param srcInterface
	 *            source interface
	 */
	public void setSrcInterface(String srcInterface)
	{
		this.srcInterface = srcInterface;
	}

	/**
	 * Get destination interface
	 * 
	 * @return destination interface
	 */
	public String getDestInterface()
	{
		return destInterface;
	}

	/**
	 * Set destination interface
	 * 
	 * @param destInterface
	 *            destination interface
	 */
	public void setDestInterface(String destInterface)
	{
		this.destInterface = destInterface;
	}
	
    
	/**
	 * Get service name
	 * 
	 * @return service name
	 */
	public String getServiceName()
	{
		return serviceName;
	}

	/**
	 * Set service name
	 * 
	 * @param serviceName
	 *            service name
	 */
	public void setServiceName(String serviceName)
	{
		this.serviceName = serviceName;
	}
	/**
	 * @return the sourceIP
	 */
	public String getSourceIP()
	{
		return sourceIP;
	}

	/**
	 * @param sourceIP
	 *            the sourceIP to set
	 */
	public void setSourceIP(String sourceIP)
	{
		
		this.sourceIP = sourceIP;
	}

	/**
	 * @return the source
	 */
	public String getSource()
	{
		return source;
	}

	/**
	 * @param source
	 *            the source to set
	 */
	public void setSource(String source)
	{
		if (source == null) throw new NullPointerException("source");
		this.source = source;
	}

	/**
	 * @return the destinationIP
	 */
	public String getDestinationIP()
	{
		return destinationIP;
	}

	/**
	 * @param destinationIP
	 *            the destinationIP to set
	 */
	public void setDestinationIP(String destinationIP)
	{
		this.destinationIP = destinationIP;
	}

	/**
	 * @return the destination
	 */
	public String getDestination()
	{
		return destination;
	}

	/**
	 * @param destination
	 *            the destination to set
	 */
	public void setDestination(String destination)
	{
		this.destination = destination;
	}

	/**
	 * @return the interval
	 */
	public int getInterval()
	{
		return interval;
	}

	/**
	 * @param interval
	 *            the interval to set
	 */
	public void setInterval(int interval)
	{
		this.interval = interval;
	}

	/**
	 * @return the mid
	 */
	public int getMid()
	{
		return mid;
	}

	/**
	 * @param mid
	 *            the mid to set
	 */
	public void setMid(int mid)
	{
		this.mid = mid;
	}

	/**
	 * @return the uri
	 */
	public String getUri()
	{
		return uri;
	}

	/**
	 * @param uri
	 *            the uri to set
	 */
	public void setUri(String uri)
	{
		this.uri = uri;
	}

	/**
	 * @return the startTime
	 */
	public long getStartTime()
	{
		return startTime;
	}

	/**
	 * @param startTime
	 *            the startTime to set
	 */
	public void setStartTime(long startTime)
	{
		this.startTime = startTime;
	}

	/**
	 * @return the endTime
	 */
	public long getEndTime()
	{
		return endTime;
	}

	/**
	 * @param endTime
	 *            the endTime to set
	 */
	public void setEndTime(long endTime)
	{
		this.endTime = endTime;
	}

	/**
	 * @return the data
	 */
	public List<DelayJitterLossData> getData()
	{
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(List<DelayJitterLossData> data)
	{
		this.data = data;
	
	}

	/**
	 * @return the groupsize
	 */
	public int getGroupsize()
	{
		return groupsize;
	}

	/**
	 * @param groupsize
	 *            the groupsize to set
	 */
	public void setGroupsize(int groupsize)
	{
		this.groupsize = groupsize;
	}

	/**
	 * @return the precedence
	 */
	public String getPrecedence()
	{
		return precedence;
	}

	/**
	 * @param precedence
	 *            the precedence to set
	 */
	public void setPrecedence(String precedence)
	{
		this.precedence = precedence;
	}

	/**
	 * @return the packetsize
	 */
	public int getPacketsize()
	{
		return packetsize;
	}

	/**
	 * @param packetsize
	 *            the packetsize to set
	 */
	public void setPacketsize(int packetsize)
	{
		this.packetsize = packetsize;
	}
	
	public void setMetadataID(String metadataid){
		this.metadataID = metadataid;
	}
	
	public String getMetadataID(){
		return this.metadataID;
	}
	
	public void restmetadataid(){
		this.metadataID = "";
	}

	@Override
	public String toString()
	{
		return "DelayJitterLossInterfacePair [metadataID="+metadataID+"serviceName="+serviceName+"sourceIP=" + sourceIP + ", source=" + source + ", destinationIP=" + destinationIP + ", destination=" + destination + ", interval=" + interval + ", mid=" + mid + ", uri=" + uri + ", startTime=" + startTime + ", endTime=" + endTime + ", data=" + data + ", groupsize=" + groupsize + ", precedence=" + precedence + ", packetsize=" + packetsize + "]";
	}

}
