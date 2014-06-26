package perfsonarserver.database.mongoDB_responseTO;

public class UtilizationInterfaceTO
{
	private String interfaceName;
	private String HostName;
	public UtilizationInterfaceTO()
	{
	}
	
	public UtilizationInterfaceTO(String interfaceName)
	{
		this.interfaceName = interfaceName;
	}

	/**
	 * @return the name
	 */
	public String getInterfaceName()
	{
		return interfaceName;
	}

	/**
	 * @param name the name to set
	 */
	public void setInterfaceName(String interfaceName)
	{
		this.interfaceName = interfaceName;
	}

	public void setHostName(String HostName) {
		this.HostName=HostName;// TODO Auto-generated method stub
		
	}
	public String getHostName()
	{
		return HostName;
	}
	
	
}