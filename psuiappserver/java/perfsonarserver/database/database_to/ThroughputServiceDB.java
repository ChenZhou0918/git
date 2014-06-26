package perfsonarserver.database.database_to;

import me.prettyprint.cassandra.model.HColumnImpl;
import perfsonarserver.database.response_to.DelayJitterLossServiceTO;
import perfsonarserver.database.response_to.ThroughputServiceTO;



/**
 * Transfer object to easily convert java-objects into database-objects.
 * 
 * @author Benjamin Konrad, Sascha Degener
 */
public class ThroughputServiceDB extends ThroughputServiceTO {

	/** service url */
	private String serviceURL;
	private String serviceID;
	private String serviceName;

	/**
	 * Constructor for an empty object
	 */
	public ThroughputServiceDB()
	{
		super();
		serviceURL = "";
		serviceName = "";
	}

	/**
	 * Constructor
	 * 
	 * @param serviceName
	 *            service name
	 * @param serviceURL
	 *            service url
	 */
	public ThroughputServiceDB(String serviceName, String serviceURL)
	{
		super(serviceName);
		this.serviceURL = serviceURL;
		this.serviceName = serviceName;
	}
	
	public void setThroughputID(String serviceID){
		this.serviceID = serviceID;
	}
	
	public String getThroughputID(){
		return serviceID;
	}


	/**
	 * Get service url
	 * 
	 * @return service url
	 */
	public String getServiceURL()
	{
		return serviceURL;
	}

	/**
	 * Set service url
	 * 
	 * @param serviceURL
	 *            service url
	 */
	public void setServiceURL(String serviceURL)
	{
		this.serviceURL = serviceURL;
	}
	
	public String getServiceName(){
		return serviceName;
	}
	
	public void setServiceName(String serviceName){
		this.serviceName = serviceName;
	}
}
