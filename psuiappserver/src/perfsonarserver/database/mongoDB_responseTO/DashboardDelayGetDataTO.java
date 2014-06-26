package perfsonarserver.database.mongoDB_responseTO;

import perfsonarserver.database.mongoDBImpl.DataAccess;
/**
 * 
 * @author Zhou Chen
 * 
 */
public class DashboardDelayGetDataTO
{
	/** time of measurement */
	private long timestamp;

	private String service;
	/** delay value */
	private double maxDelay;
	/** maximal jitter */
	private String srcInterface;
	/** minimal jitter */
	private String destInterface;


	/**
	 * Constructor for an empty object
	 */
	public DashboardDelayGetDataTO()
	{
		this.timestamp = 0;
		this.maxDelay = 0;
	    this.destInterface="";
	    this.srcInterface="";
	    this.service="";
	    
	
	}

	/**
	 * Constructor
	 * 
	 * @param timestamp
	 *            time of measurement
	 * @param maxDelay
	 *            maximal delay
	 * @param srcInterface
	 *            source interface
	 * @param destInterface
	 *            destination interface
	 * @param service
	 *            service name
	 * 
	 */
	public DashboardDelayGetDataTO(long timestamp, double maxDelay, String srcInterface,String destInterface,String service)
	{
		this.timestamp = timestamp;
		this.maxDelay = maxDelay;
		this.srcInterface=srcInterface;
		this.destInterface=destInterface;
		this.service=service;
		
	}

	/**
	 * Get timestamp
	 * 
	 * @return time of measurement as long
	 */
	public long getTimestamp()
	{
		return timestamp;
	}

	/**
	 * Get timestamp
	 * 
	 * @return time of measurement as "yyyy-MM-ddTHH-mm-ss-SSS"
	 */
	public String getTimestampString()
	{
		return DataAccess.convertDateToString(timestamp);
	}

	/**
	 * Set timestamp
	 * 
	 * @param timestamp
	 *            time of measurement as long
	 */
	public void setTimestamp(long datum)
	{
		this.timestamp = datum;
	}

	/**
	 * Get maximal delay
	 * 
	 * @return maximal delay
	 */
	public double getMaxDelay()
	{
		return maxDelay;
	}

	/**
	 * Set maximal delay
	 * 
	 * @param maxDelay
	 *            maximal delay
	 */
	public void setMaxDelay(double maxDelay)
	{
		this.maxDelay = maxDelay;
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
		this.srcInterface=srcInterface;
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
		this.destInterface=destInterface;
	}

	
	public String getService()
	{
		return service;
	}
	
	public void setService(String service)
	
	{
		this.service=service;
	}
	/**
	 * To string for debugging purpose
	 */
	@Override
	public String toString()
	{
		return "DelayJitterLossDataTO [datum=" + timestamp + ", maxDelay=" + maxDelay + ", source interface=" + srcInterface + ", destination interface=" + destInterface + ", service=" + service + "]";
	}
}

