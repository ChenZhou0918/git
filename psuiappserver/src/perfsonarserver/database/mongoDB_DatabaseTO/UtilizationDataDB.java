package perfsonarserver.database.mongoDB_DatabaseTO;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import perfsonarserver.database.mongoDB_responseTO.*;

public class UtilizationDataDB extends UtilizationDataTO
{
	public static final String TIMESTAMP_COL = "timestamp";
	public static final String MEASUREDUTIL_COL ="measuredUtil";
	public static final String DIRECTION_COL = "direction";
	public static final String INTERFACENAME_COL="interfaceName";
	public static final String SERVICENAME_COL = "serviceName";
	public static final String HOSTNAME_COL = "HostName";
	public static final String DESCRIPTION_COL = "Description";
	public static final String CAPACITY_COL = "capacity";
	
	private String interfaceName;
	private String serviceName;
	private String hostName;
	private String description;
	private long capacity;
	public UtilizationDataDB()
	{
		super();
	}
	
	public UtilizationDataDB(long timeStamp, double measuredUtil, String direction, String interfaceName, String serviceName)
	{
		super(timeStamp, measuredUtil, direction);
		this.interfaceName = interfaceName;
		this.serviceName = serviceName;
	}
	
	
	public UtilizationDataDB(long timeStamp, double measuredUtil, String direction, String interfaceName, String serviceName,String hostName
			,String description,long capacity)
	{
		super(timeStamp, measuredUtil, direction);
		this.interfaceName = interfaceName;
		this.serviceName = serviceName;
		this.hostName=hostName;
		this.description=description;
		this.capacity=capacity;
	}
	
	public DBObject toDBObject()
	{
		BasicDBObject dbj = new BasicDBObject();
		{
			dbj.append(TIMESTAMP_COL, getTimeStamp());
			dbj.append(MEASUREDUTIL_COL, getMeasuredUtil());
			dbj.append(DIRECTION_COL, getDirection());
			dbj.append(INTERFACENAME_COL, getInterfaceName());
			dbj.append(SERVICENAME_COL, getServiceName());
			dbj.append(HOSTNAME_COL,getHostName());
			dbj.append(DESCRIPTION_COL,getDescription());
			dbj.append(CAPACITY_COL,getCapacity());
		}
		return dbj;
	}
	
	public static UtilizationDataTO toClass(DBObject dbj)
	{
		return new UtilizationDataTO((long)dbj.get(TIMESTAMP_COL), (double)dbj.get(MEASUREDUTIL_COL), (String)dbj.get(DIRECTION_COL));
	}

	
	public static DashboardUDGetDataTO toDashClass(DBObject dbj)
	{
		return new DashboardUDGetDataTO((long)dbj.get(TIMESTAMP_COL), (double)dbj.get(MEASUREDUTIL_COL), (String)dbj.get(DIRECTION_COL),
				(String)dbj.get(INTERFACENAME_COL),(String)dbj.get(SERVICENAME_COL),(String)dbj.get(HOSTNAME_COL),(String)dbj.get(DESCRIPTION_COL)
				,(long)dbj.get(CAPACITY_COL));
		}

	/**
	 * @return the interfaceName
	 */
	public String getInterfaceName()
	{
		return interfaceName;
	}

	/**
	 * @param interfaceName the interfaceName to set
	 */
	public void setInterfaceName(String interfaceName)
	{
		this.interfaceName = interfaceName;
	}

	/**
	 * @return the serviceName
	 */
	public String getServiceName()
	{
		return serviceName;
	}

	/**
	 * @param serviceName the serviceName to set
	 */
	public void setServiceName(String serviceName)
	{
		this.serviceName = serviceName;
	}


	public String getDescription()
	{
		return description;
	}

	/**
	 * @param HostName the description to set
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}
	public String getHostName()
	{
		return hostName;
	}

	/**
	 * @param HostName the serviceName to set
	 */
	public void setHostName(String hostName)
	{
		this.hostName = hostName;
	}
	public long getCapacity()
	{
		return capacity;
	}

	/**
	 * @param HostName the serviceName to set
	 */
	public void setCapacity(long capacity)
	{
		this.capacity = capacity;
	}
	
}
