package perfsonarserver.fetchData.transferObjects;

/**
 * Object to store data for throughput.
 * 
 * @author Clemens Schlei, Florian Rueffer
 * 
 */
public class ThroughputData
{

	/**
	 * the interval inside each test, each interval:0-6 6-12 12-18 18-24 24-30  average:0-30
	 */
	private String interval;
	
	/**
	 * units used for the value, byte or bit
	 */
	private String numBytesUnits;
	
	/**
	 * time stamp for the entire measurement
	 */
	private long timeValue;
	
	/**
	 * the actual value of the Throughput
	 */
	private double value;
	
	/**
	 * the unit of the value, bit/sec
	 */
	private String valueUnits;

	/**
	 * @return the interval
	 */
	public String getInterval()
	{
		return interval;
	}

	/**
	 * @param interval
	 *            the interval to set
	 */
	public void setInterval(String interval)
	{
		this.interval = interval;
	}

	/**
	 * @return the numBytesUnits
	 */
	public String getNumBytesUnits()
	{
		return numBytesUnits;
	}

	/**
	 * @param numBytesUnits
	 *            the numBytesUnits to set
	 */
	public void setNumBytesUnits(String numBytesUnits)
	{
		this.numBytesUnits = numBytesUnits;
	}

	/**
	 * @return the timeValue
	 */
	public long getTimeValue()
	{
		return timeValue;
	}

	/**
	 * @param timeValue
	 *            the timeValue to set
	 */
	public void setTimeValue(long timeValue)
	{
		this.timeValue = timeValue;
	}

	/**
	 * @return the value
	 */
	public double getValue()
	{
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(double value)
	{
		this.value = value;
	}

	/**
	 * @return the valueUnits
	 */
	public String getValueUnits()
	{
		return valueUnits;
	}

	/**
	 * @param valueUnits
	 *            the valueUnits to set
	 */
	public void setValueUnits(String valueUnits)
	{
		this.valueUnits = valueUnits;
	}

	@Override
	public String toString()
	{
		return "ThroughputData [interval=" + interval + ", numBytesUnits=" + numBytesUnits + ", timeValue=" + timeValue + ", value=" + value + ", valueUnits=" + valueUnits + "]";
	}

}
