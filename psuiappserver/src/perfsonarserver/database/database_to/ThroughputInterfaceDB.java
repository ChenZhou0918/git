package perfsonarserver.database.database_to;

import java.util.List;
//import me.prettyprint.cassandra.model.HColumnImpl;
import perfsonarserver.database.response_to.DelayJitterLossInterfaceTO;
import perfsonarserver.database.response_to.ThroughputInterfaceTO;



/**
 * Transfer object to easily convert java-objects into database-objects.
 * 
 * @author Benjamin Konrad, Sascha Degener
 */
public class ThroughputInterfaceDB
{
	

	/** service name */
	private String serviceName;
	private String MID;
	private String throughputID;
	private String srcInterface;
	private String destInterface;

	/**
	 * Constructor for an empty object
	 */
	public ThroughputInterfaceDB()
	{
		//super();
		this.serviceName = "";
		this.srcInterface = "";
		this.destInterface = "";
	}

	/**
	 * Constructor
	 * 
	 * @param serviceName
	 *            service name
	 * @param interfac
	 *            interface
	 */
	public ThroughputInterfaceDB(String serviceName, String srcInterface, String destInterface, String MID)
	{
		//super(srcInterface, destInterface);
		this.serviceName = serviceName;
		this.srcInterface = srcInterface;
		this.destInterface = destInterface;
		this.MID = MID;
	}

	public String getThroughputID(){
		return throughputID;
	}
	
	public void setThroughputID(String throughputID){
		this.throughputID = throughputID;
	}
	
	public String getSrcInterface(){
		return srcInterface;
	}
	
	public void setSrcInterface(String srcInterface){
		this.srcInterface = srcInterface;
	}
	
	public String getDestInterface(){
		return destInterface;
	}
	
	public void setDestInterface(String destInterface){
		this.destInterface = destInterface;
	}

	/**
	 * @return the mID
	 */
	public String getMID()
	{
		return MID;
	}

	/**
	 * @param mID the mID to set
	 */
	public void setMID(String mID)
	{
		MID = mID;
	}
	/**
	 * Get service name
	 * 
	 * @return service name
	 */
	public String getServiceName()
	{
		return serviceName;
	}

	/**
	 * Set service name
	 * 
	 * @param serviceName
	 *            service name
	 */
	public void setServiceName(String serviceName)
	{
		this.serviceName = serviceName;
	}
}
