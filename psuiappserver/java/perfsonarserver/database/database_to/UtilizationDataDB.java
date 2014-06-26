package perfsonarserver.database.database_to;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.prettyprint.cassandra.model.HColumnImpl;


import perfsonarserver.database.cache_to.InterfaceCacheDB;
import perfsonarserver.database.response_to.DelayJitterLossInterfaceTO;
import perfsonarserver.database.response_to.UtilizationDataTO;

public class UtilizationDataDB extends UtilizationDataTO
{
	public static final String DIRECTION_COL = "direction";
	public static final String INTERFACENAME_COL="interfaceName";

	
	private String interfaceName;
	private String serviceName;
	private String utilDataID;
	private String timestamp;
	private String measuredUtil;
	private String valueUnits;
	
	public UtilizationDataDB()
	{
		super();
	}
	
	public UtilizationDataDB(long timeStamp, String measuredUtil, String direction, String interfaceName, String serviceName)
	{
		super(timeStamp, measuredUtil, direction);
		this.interfaceName = interfaceName;
		this.serviceName = serviceName;
	}
	
	public String getutilDataID()
	{
		return utilDataID;
	}
	
	public void setutilDataID(String utilDataID)
	{
		this.utilDataID = utilDataID;
	}
	
	public String getTimestamp()
	{
		return timestamp;
	}
	
	public void setTimestamp(String timestamp)
	{
		this.timestamp = timestamp;
	}
	
	
	
	public String getValueUnits()
	{
		return valueUnits;
	}
	
	public void setValueUnits(String valueUnits)
	{
		this.valueUnits = valueUnits;
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

	@Override
	public String getMeasuredUtil() {
		
		return measuredUtil;
	}

	@Override
	public void setMeasuredUtil(String measuredUtil) {
		this.measuredUtil = measuredUtil;
		
	}
}
