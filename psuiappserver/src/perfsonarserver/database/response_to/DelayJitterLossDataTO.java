package perfsonarserver.database.response_to;

import java.io.Serializable;



/**
 * Transfer Object for DelayJitterLoss Data
 * 
 * @author Benjamin Konrad, Sascha Degener
 */
public class DelayJitterLossDataTO implements Serializable
{
	/**
	 * 
	 */
	//private static final long serialVersionUID = -7553572633515854543L;
//	/** time of measurement */
//	protected long timestamp;
//	/** maximal delay */
//	protected double maxDelay;
//	/** minimal delay */
//	protected double minDelay;
//	/** maximal jitter */
//	protected double maxJitter;
//	/** minimal jitter */
//	protected double minJitter;
//	/** loss */
//	protected int loss;
	
	/**
	 * 
	 */
	//private static final long serialVersionUID = 803273444495719285L;
	/** time of measurement */
	protected String timestamp;
	/** maximal delay */
	protected String maxDelay;
	/** minimal delay */
	protected String minDelay;
	/** maximal jitter */
	protected String maxJitter;
	/** minimal jitter */
	protected String minJitter;
	/** loss */
	protected String loss;

	/**
	 * Constructor for an empty object
	 */
	public DelayJitterLossDataTO()
	{
		this.timestamp = "";
		this.maxDelay = "";
		this.minDelay = "";
		this.maxJitter = "";
		this.minJitter = "";
		this.loss = "";
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
	 */
	public DelayJitterLossDataTO(String timestamp, String maxDelay, String minDelay, String maxJitter, String minJitter, String loss)
	{
		this.timestamp = timestamp;
		this.maxDelay = maxDelay;
		this.minDelay = minDelay;
		this.maxJitter = maxJitter;
		this.minJitter = minJitter;
		this.loss = loss;
	}

	/**
	 * Get timestamp
	 * 
	 * @return time of measurement as long
	 */
	public String getTimestamp()
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
		return timestamp;
	}

	/**
	 * Set timestamp
	 * 
	 * @param timestamp
	 *            time of measurement as long
	 */
	public void setTimestamp(String datum)
	{
		this.timestamp = datum;
	}

	/**
	 * Get maximal delay
	 * 
	 * @return maximal delay
	 */
	public String getMaxDelay()
	{
		return maxDelay;
	}

	/**
	 * Set maximal delay
	 * 
	 * @param maxDelay
	 *            maximal delay
	 */
	public void setMaxDelay(String maxDelay)
	{
		this.maxDelay = maxDelay;
	}

	/**
	 * Get minimal delay
	 * 
	 * @return minimal delay
	 */
	public String getMinDelay()
	{
		return minDelay;
	}

	/**
	 * Set minimal delay
	 * 
	 * @param minDelay
	 *            minimal delay
	 */
	public void setMinDelay(String minDelay)
	{
		this.minDelay = minDelay;
	}

	/**
	 * Get maximal jitter
	 * 
	 * @return maximal jitter
	 */
	public String getMaxJitter()
	{
		return maxJitter;
	}

	/**
	 * Set maximal jitter
	 * 
	 * @param maxJitter
	 *            maximal jitter
	 */
	public void setMaxJitter(String maxJitter)
	{
		this.maxJitter = maxJitter;
	}

//	/**
//	 * Get minimal jitter
//	 * 
//	 * @return minimal jitter
//	 */
//	public double getMinJitter()
//	{
//		return minJitter;
//	}
//
//	/**
//	 * Set minimal jitter
//	 * 
//	 * @param minJitter
//	 *            minimal jitter
//	 */
//	public void setMinJitter(double minJitter)
//	{
//		this.minJitter = minJitter;
//	}

	
	  
	/**
	* To string for debugging purpose
	*/
	public String toString()
  {
		return "DelayJitterLossDataDB [datum=" + timestamp + ", maxDelay=" + maxDelay + ", minDelay=" + minDelay + ", maxJitter=" + maxJitter + ", minJitter=" + minJitter + ", loss=" + loss +"]";
  }
	
	 public void setloss(String loss){
		 this.loss = loss;
	 }
	 
	 public String getloss(){
		 return loss;
	 }
	 
	
	
}