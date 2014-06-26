package perfsonarserver.fetchData.transferObjects;

/**
 * Object to store data for utilization.
 * 
 * @author Clemens Schlei, Florian Rueffer
 * 
 */
public class UtilizationData
{

	/**
	 * Measuring time.
	 */
	private long time;
	/**
	 * Measuring value.
	 */
	private String value;
	/**
	 * Unit of value.
	 */
	private String valueUnits;
	
	private String serviceName;

	
	public String getserviceName()
	{
		return serviceName;
	}
	
	public void setserviceName(String serviceName)
	{
		this.serviceName = serviceName;
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

	/**
	 * @return the value
	 */
	public String getValue()
	{
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(String value)
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "UtilizationData [time=" + time + ", value=" + value + ", valueUnits=" + valueUnits + "]";
	}
}
