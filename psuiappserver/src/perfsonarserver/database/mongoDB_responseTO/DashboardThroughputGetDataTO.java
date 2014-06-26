package perfsonarserver.database.mongoDB_responseTO;

public class DashboardThroughputGetDataTO {
	/** time of measurement */
	private long timestamp;
	/** measured throughput */
	private double measuredThroughput;
	/** measured interval, each time stamp can contain several interval */
	private String interval;
	/** service name */
	private String serviceName;
	/** source interface */
	private String srcInterface;
	/** destination interface */
	private String destInterface;
	/**
	 * Constructor for an empty object
	 */
	public DashboardThroughputGetDataTO()
	{
	}

	/**
	 * constructor
	 * 
	 * @param timestamp
	 *            time of measurement
	 * @param mThroughput
	 *            measured throughput
	 */
	public DashboardThroughputGetDataTO(long timestamp, double mThroughput, String interval,String serviceName,String srcInterface,String destInterface)
	{
		this.timestamp = timestamp;
		this.measuredThroughput = mThroughput;
		this.interval = interval;
        this.destInterface=destInterface;
        this.srcInterface=srcInterface;
	}

	/**
	 * @return the interval
	 */
	public String getInterval()
	{
		return interval;
	}

	/**
	 * @param interval the interval to set
	 */
	public void setInterval(String interval)
	{
		this.interval = interval;
	}

	/**
	 * Get timestamp as long
	 * 
	 * @return time of measurement
	 */
	public long getTimestamp()
	{
		return timestamp;
	}

	/**
	 * Set timestamp
	 * 
	 * @param timestamp
	 *            time of measurement
	 */
	public void setTimestamp(long timestamp)
	{
		this.timestamp = timestamp;
	}

	/**
	 * Get measured throughput
	 * 
	 * @return measured troughput
	 */
	public double getMeasuredThroughput()
	{
		return measuredThroughput;
	}

	/**
	 * Set measured throughput
	 * 
	 * @param measuredThroughput
	 *            measured throughput
	 */
	public void setMeasuredThroughput(double measuredThroughput)
	{
		this.measuredThroughput = measuredThroughput;
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
}
