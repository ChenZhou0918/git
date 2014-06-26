package perfsonarserver.database.database_to;

import java.io.Serializable;

import perfsonarserver.database.cassandraImpl.DataAccessCas;
import perfsonarserver.database.response_to.DelayJitterLossDataTO;


/**
 * Transfer object to easily convert java-objects into database-objects.
 * 
 * @author Benjamin Konrad, Sascha Degener
 */
public class DelayJitterLossDataDB extends DelayJitterLossDataTO implements Serializable
{

	
	/**
	 * 
	 */
	//private static final long serialVersionUID = -8619743659587814928L;
	/** service name */
	private String serviceName;
	/** source interface */
	private String srcInterface;
	/** destination interface */
	private String destInterface;
	private String dataID;
//	public String minDelay;
//	public String maxDelay;
//	public String loss;
//	
//	public String maxJitter;
//	public String minJitter;

	

	/**
	 * Constructor for an empty object
	 */
	public DelayJitterLossDataDB()
	{
		super();
		this.serviceName = "";
		this.srcInterface = "";
		this.destInterface = "";
	}

	/**
	 * Constructor
	 * 
	 * @param timestamp
	 *            time of measurement
	 * @param maxDelay
	 *            maximal delay
	 * @param minDelay
	 *            minimal delay
	 * @param maxJitter
	 *            maximal jitter
	 * @param minJitter
	 *            minimal jitter
	 * @param loss
	 *            loss
	 * @param service
	 *            service name
	 * @param srcInterface
	 *            source interface
	 * @param destInterface
	 *            destination interface
	 */
	public DelayJitterLossDataDB(String timestamp, String maxDelay, String minDelay, String maxJitter, String minJitter, String loss)
	{
		super(timestamp, maxDelay, minDelay, maxJitter, minJitter, loss);
	
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
	
//	/**
//	 * Get timestamp
//	 * 
//	 * @return time of measurement as long
//	 */
//	public String getTimestamp()
//	{
//		return timestamp;
//	}
//
//	/**
//	 * Get timestamp
//	 * 
//	 * @return time of measurement as "yyyy-MM-ddTHH-mm-ss-SSS"
//	 */
//	public String getTimestampString()
//	{
//		return timestamp;
//	}
//
//
//	public void setTimestamp(String datum)
//	{
//		this.timestamp = datum;
//	}
//
//	/**
//	 * Get maximal delay
//	 * 
//	 * @return maximal delay
//	 */
//	public String getMaxDelay()
//	{
//		return maxDelay;
//	}
//
//	/**
//	 * Set maximal delay
//	 * 
//	 * @param maxDelay
//	 *            maximal delay
//	 */
//	public void setMaxDelay(String maxDelay)
//	{
//		this.maxDelay = maxDelay;
//	}
//
//	/**
//	 * Get minimal delay
//	 * 
//	 * @return minimal delay
//	 */
//	public String getMinDelay()
//	{
//		return minDelay;
//	}
//
//	/**
//	 * Set minimal delay
//	 * 
//	 * @param minDelay
//	 *            minimal delay
//	 */
//	public void setMinDelay(String minDelay)
//	{
//		this.minDelay = minDelay;
//	}
//
//	/**
//	 * Get maximal jitter
//	 * 
//	 * @return maximal jitter
//	 */
//	public String getMaxJitter()
//	{
//		return maxJitter;
//	}
//
//	/**
//	 * Set maximal jitter
//	 * 
//	 * @param maxJitter
//	 *            maximal jitter
//	 */
//	public void setMaxJitter(String maxJitter)
//	{
//		this.maxJitter = maxJitter;
//	}
//
	/**
	 * Get minimal jitter
	 * 
	 * @return minimal jitter
	 */
	public String getMinJitter()
	{
		return minJitter;
	}

	/**
	 * Set minimal jitter
	 * 
	 * @param minJitter
	 *            minimal jitter
	 */
	public void setMinJitter(String minJitter)
	{
		this.minJitter = minJitter;
	}
//
//	
//
	 public String getDelayJitterLossDataID() {
		    return dataID;
     }

	 public void setDelayJitterLossDataID(String dataID) {
		    this.dataID = dataID;
	 }
//		  
//	 public void setloss(String loss){
//		 this.loss = loss;
//	 }
//	 
//	 public String getloss(){
//		 return loss;
//	 }
//	 
		  
	/**
	* To string for debugging purpose
	*/
	public String toString()
    {
		return "DelayJitterLossDataDB [destinterface=" + destInterface + ", srcinterface=" + srcInterface + ", serviceName=" + serviceName+ "datum=" + timestamp + ", maxDelay=" + maxDelay + ", minDelay=" + minDelay + ", maxJitter=" + maxJitter + ", minJitter=" + minJitter + ", loss=" + loss +"]";
    }

}