package perfsonarserver.database.mongoDB_responseTO;

/**
 * Transfer Object for DelayJitterLoss Services
 * 
 * @author Benjamin Konrad, Sascha Degener
 */
public class DelayJitterLossServiceTO
{
	/** Service Name */
	private String serviceName;

	/**
	 * Constructor for an empty object
	 */
	public DelayJitterLossServiceTO()
	{
		this.serviceName = "";
	}

	/**
	 * Constructor
	 * 
	 * @param serviceName
	 *            name of the service
	 */
	public DelayJitterLossServiceTO(String serviceName)
	{
		this.serviceName = serviceName;
	}

	/**
	 * Get for service name
	 * 
	 * @return serviceName name of the service
	 */
	public String getServiceName()
	{
		return serviceName;
	}

	/**
	 * Set for service name
	 * 
	 * @param serviceName
	 *            name of the service
	 */
	public void setServiceName(String serviceName)
	{
		this.serviceName = serviceName;
	}
}