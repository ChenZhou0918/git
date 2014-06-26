package perfsonarserver.fetchData.transferObjects;

/**
 * Object to store data for utilization.
 * 
 * @author Clemens Schlei, Florian Rueffer,ZHOU CHEN
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
	
	private double utilPercentage;
	private long capacity;
	private String description;
	private String serviceName;  
	private String direction;
	private String Interface;
	private String HostName;
	
	
	
	
	public double getUtilPercentage()
	{
		return utilPercentage;
	}
	
	public void setUtilPercentage(double utilPercentage)
	{
		this.utilPercentage=utilPercentage;
	}
	
	
	public  long getCapacity ()
	{
		return capacity;
	}
	
	public void setCapacity(long capacity)
	{
		this.capacity=capacity;
	}
	
	
	
	
	public String HostName()
	{
		return HostName;
	}
	
	public void setHostName(String HostName)
	{
		this.HostName=HostName;
	}
	
	
	public String getdirection()
	{
		return direction;
	}
	
	public void setdirection(String direction)
	{
		this.direction=direction;
	}
	
	
	public String getInterface()
	{
		return Interface;
	}
	
	public void setInterface(String Interface)
	{
		this.Interface=Interface;
	}
	
	public String getserviceName()
	{
		return serviceName;
	}
	
	public void setserviceName(String serviceName)
	{
		this.serviceName = serviceName;	
	}
	public String getDescription()
	{
		return description;
	}
	
	public void setDescription(String description)
	{
		this.description=description;
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
		return "UtilizationData [ServiceName"+serviceName+"InterfaceName: "+Interface+
				"Direction: "+direction+"time=" + time +", utilization percentage=" +utilPercentage  + ", capacity=" + capacity+ 
				", value=" + value + ", valueUnits=" + valueUnits + "]";
	}
}
