package perfsonarserver.database.response_to;

abstract public class UtilizationDataTO
{
	private long timeStamp;
	private String measuredUtil;
	private String direction;
	
	public UtilizationDataTO()
	{
	}
	
	public UtilizationDataTO(long timeStamp, String measuredUtil, String direction)
	{
		this.timeStamp=timeStamp;
		this.measuredUtil = measuredUtil;
		this.direction = direction;
	}

	/**
	 * @return the timeStamp
	 */
	public long getTimeStamp()
	{
		return timeStamp;
	}

	/**
	 * @param timeStamp the timeStamp to set
	 */
	public void setTimeStamp(long timeStamp)
	{
		this.timeStamp = timeStamp;
	}

	/**
	 * @return the utilData
	 */
	abstract public String getMeasuredUtil();

	/**
	 * @param utilData the utilData to set
	 */
	abstract public void setMeasuredUtil(String measuredUtil);

	/**
	 * @return the direction
	 */
	public String getDirection()
	{
		return direction;
	}

	/**
	 * @param direction the direction to set
	 */
	public void setDirection(String direction)
	{
		this.direction = direction;
	}
}