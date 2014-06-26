package perfsonarserver.database.mongoDB_responseTO;

import perfsonarserver.database.mongoDBImpl.DataAccess;

public class UtilizationDataTO
{
	private long timestamp;
	private double measuredUtil;
	private String direction;
	private String Interface;
	
	public UtilizationDataTO()
	{
	}
	
	public UtilizationDataTO(long timestamp, double measuredUtil, String direction)
	{
		this.timestamp=timestamp;
		this.measuredUtil = measuredUtil;
		this.direction = direction;
	}

	/**
	 * @return the timeStamp
	 */
	public long getTimeStamp()
	{
		return timestamp;
	}
	public String getTimestampString()
	{
		return DataAccess.convertDateToString(timestamp*1000);
	}
	/**
	 * @param timeStamp the timeStamp to set
	 */
	public void setTimeStamp(long timestamp)
	{
		this.timestamp = timestamp;
	}

	/**
	 * @return the utilData
	 */
	public double getMeasuredUtil()
	{
		return measuredUtil;
	}

	/**
	 * @param utilData the utilData to set
	 */
	public void setMeasuredUtil(double measuredUtil)
	{
		this.measuredUtil = measuredUtil;
	}

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