package perfsonarserver.database.mongoDB_DatabaseTO;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import perfsonarserver.database.mongoDB_responseTO.*;

public class UtilizationInterfaceDB extends UtilizationInterfaceTO
{
	public static final String INTERFACENAME_COL = "interfaceName";
	public static final String SERVICENAME_COL = "serviceName";
	public static final String HOSTNAME_COL = "hostName";
	private String serviceName="";
	private String hostName="";
	
	public UtilizationInterfaceDB()
	{
		super();
	}
	
	public UtilizationInterfaceDB(String interfaceName, String serviceName)
	{
		super(interfaceName);
		this.serviceName = serviceName;
	}
	
	public UtilizationInterfaceDB(String interfaceName, String serviceName,
			String hostname) {
		super(interfaceName);
		this.serviceName = serviceName;
		this.hostName=hostname;
		// TODO Auto-generated constructor stub
	}

	public DBObject toDBObject()
	{
		BasicDBObject dbObjDataTO = new BasicDBObject();
		{
			dbObjDataTO.append(INTERFACENAME_COL, this.getInterfaceName());
			dbObjDataTO.append(SERVICENAME_COL, this.getServiceName());
			dbObjDataTO.append(HOSTNAME_COL, this.getHostName());
		}
		return dbObjDataTO;
	}

	public static UtilizationInterfaceTO toClass(DBObject dbObj)
	{
		UtilizationInterfaceTO tpInterfaceTO = new UtilizationInterfaceTO();
		{
			tpInterfaceTO.setInterfaceName((String) dbObj.get(INTERFACENAME_COL));
			tpInterfaceTO.setHostName((String) dbObj.get(HOSTNAME_COL));
		}
		return tpInterfaceTO;
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
	public String getHostName()
	{
		return hostName;
	}

	/**
	 * @param serviceName the serviceName to set
	 */
	public void setHostName(String hostName)
	{
		this.hostName = hostName;
	}
}
