package perfsonarserver.database.database_to;

import java.util.List;
//import me.prettyprint.cassandra.model.HColumnImpl;


import perfsonarserver.database.response_to.ThroughputInterfaceTO;
import perfsonarserver.database.response_to.UtilizationInterfaceTO;

public class UtilizationInterfaceDB extends UtilizationInterfaceTO
{
	
	
	private String serviceName;
	private String interfaceName;
	private String utilInterfaceID;
	private long capacity;
	private String ipType;
	private String direction;
	
	public UtilizationInterfaceDB()
	{
		super();
	}
	
	public UtilizationInterfaceDB(String interfaceName,  long capacity, String ipType, String direction)
	{
		//super(interfaceName);
		this.interfaceName = interfaceName;
		this.capacity = capacity;
		this.ipType = ipType;
		this.direction = direction;
	}
	
	public String getutilInterfaceID()
	{
		return utilInterfaceID;
	}
	
	public void setutilInterfaceID(String utilInterfaceID)
	{
		this.utilInterfaceID = utilInterfaceID;
	}
	
	public long getCapacity(){
		return capacity;
	}
	
	public void setCapacity(long capacity)
	{
		this.capacity = capacity;
	}
	
	public String getipType(){
		return ipType;
	}
	
	public void setipType(String ipType)
	{
		this.ipType = ipType;
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
	public String getInterfaceName() {
	
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
		
	}
	
	public void setdirection(String direction)
	{
		this.direction = direction;
	}
	
	public String getdirection(){
		return direction;
	}
}
