package perfsonarserver.database.mongoDB_responseTO;

import perfsonarserver.database.mongoDBImpl.*;;

/**
 * Transfer Object for DelayJitterLoss Data
 * 
 * @author Benjamin Konrad, Sascha Degener
 */
public class DelayJitterLossDataTO
{
	/** time of measurement */
	private long timestamp;
	/** maximal delay */
	private double maxDelay;
	/** minimal delay */
	private double minDelay;
	/** maximal jitter */
	private double maxJitter;
	/** minimal jitter */
	private double minJitter;
	/** loss */
	private int loss;

	/**
	 * Constructor for an empty object
	 */
	public DelayJitterLossDataTO()
	{
		this.timestamp = 0;
		this.maxDelay = 0;
		this.minDelay = 0;
		this.maxJitter = 0;
		this.minJitter = 0;
		this.loss = 0;
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
	public DelayJitterLossDataTO(long timestamp, double maxDelay, double minDelay, double maxJitter, double minJitter, int loss)
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
	 * Get minimal delay
	 * 
	 * @return minimal delay
	 */
	public double getMinDelay()
	{
		return minDelay;
	}

	/**
	 * Set minimal delay
	 * 
	 * @param minDelay
	 *            minimal delay
	 */
	public void setMinDelay(double minDelay)
	{
		this.minDelay = minDelay;
	}

	/**
	 * Get maximal jitter
	 * 
	 * @return maximal jitter
	 */
	public double getMaxJitter()
	{
		return maxJitter;
	}

	/**
	 * Set maximal jitter
	 * 
	 * @param maxJitter
	 *            maximal jitter
	 */
	public void setMaxJitter(double maxJitter)
	{
		this.maxJitter = maxJitter;
	}

	/**
	 * Get minimal jitter
	 * 
	 * @return minimal jitter
	 */
	public double getMinJitter()
	{
		return minJitter;
	}

	/**
	 * Set minimal jitter
	 * 
	 * @param minJitter
	 *            minimal jitter
	 */
	public void setMinJitter(double minJitter)
	{
		this.minJitter = minJitter;
	}

	/**
	 * Get loss
	 * 
	 * @return loss
	 */
	public int getLoss()
	{
		return loss;
	}

	/**
	 * Set loss
	 * 
	 * @param loss
	 *            loss
	 */
	public void setLoss(int loss)
	{
		this.loss = loss;
	}

	/**
	 * To string for debugging purpose
	 */
	@Override
	public String toString()
	{
		return "DelayJitterLossDataTO [datum=" + timestamp + ", maxDelay=" + maxDelay + ", minDelay=" + minDelay + ", maxJitter=" + maxJitter + ", minJitter=" + minJitter + ", loss=" + loss + "]";
	}
}