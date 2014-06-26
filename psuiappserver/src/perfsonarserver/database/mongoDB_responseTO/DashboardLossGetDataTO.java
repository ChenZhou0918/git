package perfsonarserver.database.mongoDB_responseTO;

public class DashboardLossGetDataTO{
/** source interface */
private String srcInterface;
/** destination interface */
private String destInterface;
private String serviceName;
private int lossNumber;
/**
 * Constructor for an empty object
 */
public DashboardLossGetDataTO()
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
public DashboardLossGetDataTO(String srcInterface, String destInterface)
{
	this.srcInterface = srcInterface;
	this.destInterface = destInterface;
}


public DashboardLossGetDataTO(String srcInterface, String destInterface, String service,int lossNumber)
{
	this.srcInterface = srcInterface;
	this.destInterface = destInterface;
	this.lossNumber=lossNumber;
	this.serviceName=service;
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



public int getLossNumber()
{
return lossNumber;
}

public void setLossNumber(int lossNumber)
{
this.lossNumber=lossNumber;
}
}