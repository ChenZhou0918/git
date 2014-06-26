package perfsonarserver.database.response_to;

/**
 * Objekte dieser Klasse dienen Als Transfere Objekt f�r die Antwort auf
 * Anfragen.
 * 
 * @author Benjamin Konrad, Sascha Degener
 */
public class ThroughputServiceTO
{
	/** Service Name */
	private String serviceName;

	public ThroughputServiceTO()
	{
		this.serviceName = "";
	}

	/**
	 * Konstruktor
	 * 
	 * @param Service
	 *            Name
	 */
	public ThroughputServiceTO(String serviceName)
	{
		this.serviceName = serviceName;
	}

	/**
	 * @return the Service Name
	 */
	public String getServiceName()
	{
		return serviceName;
	}

	/**
	 * @param Service
	 *            Name the Service Name to set
	 */
	public void setServiceName(String serviceName)
	{
		this.serviceName = serviceName;
	}
}