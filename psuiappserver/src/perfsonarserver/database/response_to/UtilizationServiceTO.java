package perfsonarserver.database.response_to;

public abstract class UtilizationServiceTO
{
	/** Service Name */
	private String serviceName;

	public UtilizationServiceTO()
	{
	}

	/**
	 * constructor
	 * 
	 * @param Service Name
	 */
	public UtilizationServiceTO(String serviceName)
	{
		this.serviceName = serviceName;
	}

	/**
	 * @return the Service Name
	 */
	public abstract String getServiceName();

	/**
	 * @param Service
	 *            Name the Service Name to set
	 * @return 
	 */
	public abstract void setServiceName(String serviceName);
}