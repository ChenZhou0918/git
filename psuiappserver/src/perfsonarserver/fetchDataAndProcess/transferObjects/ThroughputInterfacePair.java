package perfsonarserver.fetchDataAndProcess.transferObjects;

import java.util.List;

/**
 * Object to store interface pair for throughput.
 * 
 * @author Clemens Schlei, Florian Rueffer
 * 
 */
public class ThroughputInterfacePair
{
	/**
	 * Start time of measurement period in milliseconds.
	 */
	private long startTime;
	/**
	 * End time of measurement period in milliseconds.
	 */
	private long endTime;
	/**
	 * Address type of source (ipv4|ipv6).
	 */
	private String srcType;
	/**
	 * Address of source (DNS|IP).
	 */
	private String srcAddress;
	/**
	 * Address type of destination (ipv4|ipv6).
	 */
	private String destType;
	/**
	 * Address of destination (DNS|IP).
	 */
	private String destAddress;
	/**
	 * Protocol type (for example TCP).
	 */
	private String protocol;
	/**
	 * Measurement interval in seconds (default 60s).
	 */
	private int interval;
	/**
	 * Measurement ID that identifies an exact measurement.
	 */
	private String mid;
	/**
	 * URI of the service.
	 */
	private String uri;
	/**
	 * List of data for this interface pair.
	 */
	private List<ThroughputData> data;

	/**
	 * Default constructor.
	 */
	public ThroughputInterfacePair()
	{
		super();
	}
	
	public ThroughputInterfacePair(String srcAddress, String destAddress, String MID)
	{
		super();
		this.srcAddress = srcAddress;
		this.destAddress = destAddress;
		this.mid = MID;
	}

	/**
	 * Constructor for existing Throughput Interface pair.
	 * 
	 * @param srcType
	 *            Address type of source (ipv4|ipv6)
	 * @param srcAddress
	 *            Address of source (DNS|IP)
	 * @param destType
	 *            Address type of destination (ipv4|ipv6)
	 * @param destAddress
	 *            Address of destination (DNS|IP)
	 * @param mid
	 *            Measurement ID that identifies an exact measurement
	 */
	public ThroughputInterfacePair(String srcType, String srcAddress, String destType, String destAddress, String mid)
	{
		super();
		this.srcType = srcType;
		this.srcAddress = srcAddress;
		this.destType = destType;
		this.destAddress = destAddress;
		this.mid = mid;
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
	 * @return the srcType
	 */
	public String getSrcType()
	{
		return srcType;
	}

	/**
	 * @param srcType
	 *            the srcType to set
	 */
	public void setSrcType(String srcType)
	{
		this.srcType = srcType;
	}

	/**
	 * @return the srcAddress
	 */
	public String getSrcAddress()
	{
		return srcAddress;
	}

	/**
	 * @param srcAddress
	 *            the srcAddress to set
	 */
	public void setSrcAddress(String srcAddress)
	{
		this.srcAddress = srcAddress;
	}

	/**
	 * @return the destType
	 */
	public String getDestType()
	{
		return destType;
	}

	/**
	 * @param destType
	 *            the destType to set
	 */
	public void setDestType(String destType)
	{
		this.destType = destType;
	}

	/**
	 * @return the destAddress
	 */
	public String getDestAddress()
	{
		return destAddress;
	}

	/**
	 * @param destAddress
	 *            the destAddress to set
	 */
	public void setDestAddress(String destAddress)
	{
		this.destAddress = destAddress;
	}

	/**
	 * @return the protocol
	 */
	public String getProtocol()
	{
		return protocol;
	}

	/**
	 * @param protocol
	 *            the protocol to set
	 */
	public void setProtocol(String protocol)
	{
		this.protocol = protocol;
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
	public String getMid()
	{
		return mid;
	}

	/**
	 * @param mid
	 *            the mid to set
	 */
	public void setMid(String mid)
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
	 * @return the data
	 */
	public List<ThroughputData> getData()
	{
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(List<ThroughputData> data)
	{
		this.data = data;
	}

	@Override
	public String toString()
	{
		return "ThroughputInterfacePair [startTime=" + startTime + ", endTime=" + endTime + ", srcType=" + srcType + ", srcAddress=" + srcAddress + ", destType=" + destType + ", destAddress=" + destAddress + ", protocol=" + protocol + ", interval=" + interval + ", mid=" + mid + ", uri=" + uri + ", data=" + data + "]";
	}

}
