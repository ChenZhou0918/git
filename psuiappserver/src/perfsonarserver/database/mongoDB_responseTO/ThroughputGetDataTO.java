package perfsonarserver.database.mongoDB_responseTO;

/**
 * Transfer Object for Throughput Data
 * 
 * @author Benjamin Konrad, Sascha Degener
 */
public class ThroughputGetDataTO
{
	/** time of measurement */
	private long timestamp;
	/** measured throughput */
	private double measuredThroughput;
	/** measured interval, each time stamp can contain several interval */
	private String interval;
	
	/**
	 * Constructor for an empty object
	 */
	public ThroughputGetDataTO()
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
	public ThroughputGetDataTO(long timestamp, double mThroughput, String interval)
	{
		this.timestamp = timestamp;
		this.measuredThroughput = mThroughput;
		this.interval = interval;
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
	
	
}
