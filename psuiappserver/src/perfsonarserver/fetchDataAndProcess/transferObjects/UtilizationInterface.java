package perfsonarserver.fetchDataAndProcess.transferObjects;

import java.util.List;

/**
 * Object to store interface pair for utilization.
 * 
 * @author Clemens Schlei, Florian Rueffer
 * 
 */
public class UtilizationInterface
{

	/**
	 * IP type (ipv4|ipv6).
	 */
	private String ipType ="";
	/**
	 * IP address.
	 */
	private String ipAddress = "";
	/**
	 * Hostname.
	 */
	private String hostname ="";
	/**
	 * Capacity.
	 */
	private long capacity;
	/**
	 * Unit of capacity.
	 */
	private String capacityValueUnits;
	/**
	 * direction (in|out).
	 */
	private String direction="in";
	/**
	 * Description.
	 */
	private String description;
	/**
	 * Start time of measurement period in milliseconds.
	 */
	private long startTime;
	/**
	 * End time of measurement period in milliseconds.
	 */
	private long endTime;
	/**
	 * Name.
	 */
	private String name ="";
	/**
	 * URI of the service.
	 */
	private String uri;
	/**
	 * Measurement ID that identifies an exact measurement.
	 */
	private String mid;
	/**
	 * List of data for this interface.
	 */
	private List<UtilizationData> data;

	// TODO Unknown values, description needs to be added.
	private long dataSourceMinValue;
	private int dataSourceHeartbeat;
	private int dataSourceStep;
	private long dataSourceMaxValue;
	private String dataSourceType;

	/**
	 * Default constructor.
	 */
	public UtilizationInterface()
	{
		super();
	}

	/**
	 * Constructor for existing Utilization Interface.
	 * 
	 * @param ipType
	 *            IP type (ipv4|ipv6)
	 * @param ipAddress
	 *            IP address
	 * @param hostname
	 *            Hostname
	 */
	
	public UtilizationInterface(String name, String direction)
	{
		super();
		this.name = name;
		this.direction = direction;
	}

	/**
	 * @return the ipType
	 */
	public String getIpType()
	{
		return ipType;
	}

	/**
	 * @param ipType
	 *            the ipType to set
	 */
	public void setIpType(String ipType)
	{
		this.ipType = ipType;
	}

	/**
	 * @return the ipAddress
	 */
	public String getIpAddress()
	{
		return ipAddress;
	}

	/**
	 * @param ipAddress
	 *            the ipAddress to set
	 */
	public void setIpAddress(String ipAddress)
	{
		this.ipAddress = ipAddress;
	}

	/**
	 * @return the hostname
	 */
	public String getHostname()
	{
		return hostname;
	}

	/**
	 * @param hostname
	 *            the hostname to set
	 */
	public void setHostname(String hostname)
	{
		this.hostname = hostname;
	}

	/**
	 * @return the capacity
	 */
	public long getCapacity()
	{
		return capacity;
	}

	/**
	 * @param capacity
	 *            the capacity to set
	 */
	public void setCapacity(long capacity)
	{
		this.capacity = capacity;
	}

	/**
	 * @return the capacityValueUnits
	 */
	public String getCapacityValueUnits()
	{
		return capacityValueUnits;
	}

	/**
	 * @param capacityValueUnits
	 *            the capacityValueUnits to set
	 */
	public void setCapacityValueUnits(String capacityValueUnits)
	{
		this.capacityValueUnits = capacityValueUnits;
	}

	/**
	 * @return the direction
	 */
	public String getDirection()
	{
		return direction;
	}

	/**
	 * @param direction
	 *            the direction to set
	 */
	public void setDirection(String direction)
	{
		this.direction = direction;
	}

	/**
	 * @return the description
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}

	/**
	 * @return the startTime
	 */
	public long getStartTime()
	{
		return startTime;
	}

	/**
	 * @param startTime
	 *            the startTime to set
	 */
	public void setStartTime(long startTime)
	{
		this.startTime = startTime;
	}

	/**
	 * @return the endTime
	 */
	public long getEndTime()
	{
		return endTime;
	}

	/**
	 * @param endTime
	 *            the endTime to set
	 */
	public void setEndTime(long endTime)
	{
		this.endTime = endTime;
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @return the uri
	 */
	public String getUri()
	{
		return uri;
	}

	/**
	 * @param uri
	 *            the uri to set
	 */
	public void setUri(String uri)
	{
		this.uri = uri;
	}

	/**
	 * @return the mid
	 */
	public String getMid()
	{
		return mid;
	}

	/**
	 * @param mid
	 *            the mid to set
	 */
	public void setMid(String mid)
	{
		this.mid = mid;
	}

	/**
	 * @return the data
	 */
	public List<UtilizationData> getData()
	{
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(List<UtilizationData> data)
	{
		this.data = data;
	}

	/**
	 * @return the dataSourceMinValue
	 */
	public long getDataSourceMinValue()
	{
		return dataSourceMinValue;
	}

	/**
	 * @param dataSourceMinValue
	 *            the dataSourceMinValue to set
	 */
	public void setDataSourceMinValue(long dataSourceMinValue)
	{
		this.dataSourceMinValue = dataSourceMinValue;
	}

	/**
	 * @return the dataSourceHeartbeat
	 */
	public int getDataSourceHeartbeat()
	{
		return dataSourceHeartbeat;
	}

	/**
	 * @param dataSourceHeartbeat
	 *            the dataSourceHeartbeat to set
	 */
	public void setDataSourceHeartbeat(int dataSourceHeartbeat)
	{
		this.dataSourceHeartbeat = dataSourceHeartbeat;
	}

	/**
	 * @return the dataSourceStep
	 */
	public int getDataSourceStep()
	{
		return dataSourceStep;
	}

	/**
	 * @param dataSourceStep
	 *            the dataSourceStep to set
	 */
	public void setDataSourceStep(int dataSourceStep)
	{
		this.dataSourceStep = dataSourceStep;
	}

	/**
	 * @return the dataSourceMaxValue
	 */
	public long getDataSourceMaxValue()
	{
		return dataSourceMaxValue;
	}

	/**
	 * @param dataSourceMaxValue
	 *            the dataSourceMaxValue to set
	 */
	public void setDataSourceMaxValue(long dataSourceMaxValue)
	{
		this.dataSourceMaxValue = dataSourceMaxValue;
	}

	/**
	 * @return the dataSourceType
	 */
	public String getDataSourceType()
	{
		return dataSourceType;
	}

	/**
	 * @param dataSourceType
	 *            the dataSourceType to set
	 */
	public void setDataSourceType(String dataSourceType)
	{
		this.dataSourceType = dataSourceType;
	}

	@Override
	public String toString()
	{
		return "UtilizationInterface [ipType=" + ipType + ", ipAddress=" + ipAddress + ", hostname=" + hostname + ", capacity=" + capacity + ", capacityValueUnits=" + capacityValueUnits + ", direction=" + direction + ", description=" + description + ", startTime=" + startTime + ", endTime=" + endTime + ", name=" + name + ", uri=" + uri + ", mid=" + mid + ", data=" + data + ", dataSourceMinValue=" + dataSourceMinValue + ", dataSourceHeartbeat=" + dataSourceHeartbeat + ", dataSourceStep=" + dataSourceStep + ", dataSourceMaxValue=" + dataSourceMaxValue + ", dataSourceType=" + dataSourceType + "]";
	}
}
