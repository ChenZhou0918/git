package perfsonarserver.database.mongoDB_responseTO;

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