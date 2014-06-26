package perfsonarserver.database.response_to;

public abstract class UtilizationInterfaceTO
{
	private String interfaceName;
	
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
	abstract public String getInterfaceName();

	/**
	 * @param name the name to set
	 */
	public abstract void setInterfaceName(String interfaceName);
	
	
}