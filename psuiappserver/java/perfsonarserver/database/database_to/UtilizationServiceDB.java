package perfsonarserver.database.database_to;

import me.prettyprint.cassandra.model.HColumnImpl;

import perfsonarserver.database.response_to.ThroughputServiceTO;
import perfsonarserver.database.response_to.UtilizationServiceTO;

public class UtilizationServiceDB extends UtilizationServiceTO
{

	
	private String serviceURL;
	private String serviceName;
	private String utilServiceID;
	
	public UtilizationServiceDB()
	{
		super();
	}
	
	public UtilizationServiceDB(String serviceName, String serviceURL)
	{
		super(serviceName);
		this.serviceURL = serviceURL;
		this.serviceName = serviceName;
	}
	
	public String getUtilServiceID()
	{
		return utilServiceID;
	}
	
	public void setUtilServiceID(String utilServiceID){
		this.utilServiceID = utilServiceID;
	}

	/**
	 * @return the serviceURL
	 */
	public String getServiceURL()
	{
		return serviceURL;
	}

	/**
	 * @param serviceURL the serviceURL to set
	 */
	public void setServiceURL(String serviceURL)
	{
		this.serviceURL = serviceURL;
	}

	@Override
	public String getServiceName() {
		
		return serviceName;
	}

	@Override
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
		
	}
}
