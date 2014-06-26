package perfsonarserver.database.response_to;

import java.io.Serializable;

/**
 * Transfer Object for DelayJitterLoss Services
 * 
 * @author Benjamin Konrad, Sascha Degener
 */
public class DelayJitterLossServiceTO  implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8486934543094960330L;
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