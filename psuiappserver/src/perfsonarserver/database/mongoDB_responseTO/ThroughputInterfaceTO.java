package perfsonarserver.database.mongoDB_responseTO;

/**
 * Transfer object for Throughput Interfaces (to)
 * 
 * @author Benjamin Konrad, Sascha Degener
 */
public class ThroughputInterfaceTO
{
	/** interface */
	private String srcInterface;
	private String destInterface;

	/**
	 * Constructor for an empty object
	 */
	public ThroughputInterfaceTO()
	{
	}

	/**
	 * Constructor
	 * 
	 * @param interface
	 */
	public ThroughputInterfaceTO(String srcInterface, String destInterface)
	{
		this.srcInterface = srcInterface;
		this.destInterface = destInterface;
	}

	/**
	 * Get interface
	 * 
	 * @return interface
	 */
	public String getSrcInterface()
	{
		return srcInterface;
	}

	/**
	 * Set interface
	 * 
	 * @param interfac
	 *            interface
	 */
	public void setSrcInterface(String srcInterface)
	{
		this.srcInterface = srcInterface;
	}

	/**
	 * @return the destInterface
	 */
	public String getDestInterface()
	{
		return destInterface;
	}

	/**
	 * @param destInterface the destInterface to set
	 */
	public void setDestInterface(String destInterface)
	{
		this.destInterface = destInterface;
	}
}
