package perfsonarserver.fetchData.transferObjects;

/**
 * Object to store data for delay, jitter, loss.
 * 
 * @author Clemens Schlei, Florian Rueffer,ZHOU CHEN
 * 
 */
public class DelayJitterLossData
{
	
	/**
	 * max jitter percentage compared to Min delay
	 */
	private double maxJitterPercentage;
	/**
	 * max route delay percentage compared to intrinsic delay
	 */
	private double maxDelayPercentage;
	/**
	 * Minimum Delay.
	 */
	private double minDelay;
	/**
	 * Maximum Delay.
	 */
	private double maxDelay;
	/**
	 * Medium Delay.
	 */
	private double medDelay;
	/**
	 * Medium IPDV (jitter).
	 */
	private double medIpdvJitter;
	/**
	 * Maximum IPDV (jitter).
	 */
	private double maxIpdvJitter;
	/**
	 * Minimum IPDV (jitter).
	 */
	private double minIpdvJitter;
	/**
	 * Unknown boolean value.
	 */
	private String sync;
	
	/**
	 * Duplicates.
	 */
	private int duplicates;
	/**
	 * Loss.
	 */
	private int loss;
	/**
	 * Measuring time.
	 */
	private long time;
	private String serviceName;
	private String sender;
	private String receiver;
	
	
	
	
	
	public void setMaxJitterPercentage(double maxJitterPercentage){
		this.maxJitterPercentage=maxJitterPercentage;
	}
	
	public double getMaxJitterPercentage()
	{
		return maxJitterPercentage;
	}
	
	
	
	
	
	public void setMaxDelayPercentage(double maxDelayPercentage){
		this.maxDelayPercentage=maxDelayPercentage;
	}
	
	public double getMaxDelayPercentage()
	{
		return maxDelayPercentage;
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
	public String getSender()
	{
		return sender;
	}

	/**
	 * Set source interface
	 * 
	 * @param srcInterface
	 *            source interface
	 */
	public void setSender(String sender)
	{
		this.sender = sender;
	}

	/**
	 * Get destination interface
	 * 
	 * @return destination interface
	 */
	public String getReceiver()
	{
		return receiver;
	}

	/**
	 * Set destination interface
	 * 
	 * @param destInterface
	 *            destination interface
	 */
	public void setReceiver(String receiver)
	{
		this.receiver = receiver;
	}

	/**
	 * @return the medDelay
	 */
	public double getMedDelay()
	{
		return medDelay;
	}

	/**
	 * @param medDelay
	 *            the medDelay to set
	 */
	public void setMedDelay(double medDelay)
	{
		this.medDelay = medDelay;
	}

	/**
	 * @return the medIpdvJitter
	 */
	public double getMedIpdvJitter()
	{
		return medIpdvJitter;
	}

	/**
	 * @param medIpdvJitter
	 *            the medIpdvJitter to set
	 */
	public void setMedIpdvJitter(double medIpdvJitter)
	{
		this.medIpdvJitter = medIpdvJitter;
	}

	/**
	 * @return the maxIpdvJitter
	 */
	public double getMaxIpdvJitter()
	{
		return maxIpdvJitter;
	}

	/**
	 * @param maxIpdvJitter
	 *            the maxIpdvJitter to set
	 */
	public void setMaxIpdvJitter(double maxIpdvJitter)
	{
		this.maxIpdvJitter = maxIpdvJitter;
	}

	/**
	 * @return the minIpdvJitter
	 */
	public double getMinIpdvJitter()
	{
		return minIpdvJitter;
	}

	/**
	 * @param minIpdvJitter
	 *            the minIpdvJitter to set
	 */
	public void setMinIpdvJitter(double minIpdvJitter)
	{
		this.minIpdvJitter = minIpdvJitter;
	}

	/**
	 * @return the sync
	 */
	public String getSync()
	{
		return sync;
	}

	/**
	 * @param sync
	 *            the sync to set
	 */
	public void setSync(String sync)
	{
		this.sync = sync;
	}

	/**
	 * @return the minDelay
	 */
	public double getMinDelay()
	{
		return minDelay;
	}

	/**
	 * @param minDelay
	 *            the minDelay to set
	 */
	public void setMinDelay(double minDelay)
	{
		this.minDelay = minDelay;
	}

	/**
	 * @return the maxDelay
	 */
	public double getMaxDelay()
	{
		return maxDelay;
	}

	/**
	 * @param maxDelay
	 *            the maxDelay to set
	 */
	public void setMaxDelay(double maxDelay)
	{
		this.maxDelay = maxDelay;
	}

	/**
	 * @return the duplicates
	 */
	public int getDuplicates()
	{
		return duplicates;
	}

	/**
	 * @param duplicates
	 *            the duplicates to set
	 */
	public void setDuplicates(int duplicates)
	{
		this.duplicates = duplicates;
	}

	/**
	 * @return the loss
	 */
	public int getLoss()
	{
		return loss;
	}

	/**
	 * @param loss
	 *            the loss to set
	 */
	public void setLoss(int loss)
	{
		this.loss = loss;
	}

	/**
	 * @return the time
	 */
	public long getTime()
	{
		return time;
	}

	/**
	 * @param time
	 *            the time to set
	 */
	public void setTime(long time)
	{
		this.time = time;
	}

	@Override
	public String toString()
	{
		return "DelayJitterLossData [serviceName="+serviceName+", srcInterface="+sender+"destInterface="+receiver+" medDelay=" + medDelay + ", medIpdvJitter=" + medIpdvJitter + ", maxIpdvJitter=" + maxIpdvJitter + ", minIpdvJitter=" + minIpdvJitter + ", sync=" + sync + ", minDelay=" + minDelay + ", maxDelay=" + maxDelay + ", duplicates=" + duplicates + ", loss=" + loss + ", time=" + time + "]";
	}

}
