package perfsonarserver.database.mongoDB_responseTO;

/**
 * Transfer Object for DelayJitterLoss Interfaces
 * 
 * @author Benjamin Konrad, Sascha Degener
 */
public class DelayJitterLossInterfaceTO
{
	/** source interface */
	private String srcInterface;
	/** destination interface */
	private String destInterface;

	
	
	/**
	 * Constructor for an empty object
	 */
	public DelayJitterLossInterfaceTO()
	{
		this.srcInterface = "";
		this.destInterface = "";
	}

	/**
	 * Construktor
	 * 
	 * @param srcInterface
	 *            source interface
	 * @param destInterface
	 *            destination interface
	 */
	public DelayJitterLossInterfaceTO(String srcInterface, String destInterface)
	{
		this.srcInterface = srcInterface;
		this.destInterface = destInterface;
	}

	/**
	 * Get for source interface
	 * 
	 * @return source interface
	 */
	public String getSrcInterface()
	{
		return srcInterface;
	}

	/**
	 * Set for source interface
	 * 
	 * @param srcInterface
	 *            source interface to set
	 */
	public void setSrcInterface(String srcInterface)
	{
		this.srcInterface = srcInterface;
	}

	/**
	 * Get for destination interface
	 * 
	 * @return destination interface
	 */
	public String getDestInterface()
	{
		return destInterface;
	}

	/**
	 * Set for destination interface
	 * 
	 * @param destInterface
	 *            source interface to set
	 */
	public void setDestInterface(String destInterface)
	{
		this.destInterface = destInterface;
	}
	/**
	 * Get service name
	 * 
	 * @return service name
	 */

}