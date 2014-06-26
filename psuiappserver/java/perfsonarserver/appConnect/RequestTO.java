package perfsonarserver.appConnect;

/**
 * Transfer object for database request
 * 
 * @author Fabian Misiak, Reinhard Winkler, Mirco Bohlmann
 * 
 */
public class RequestTO
{
	private String featureName, service, sourceInterface, destinationInterface;
	private String startDate, endDate; // YYYY-MM-DD HH-MM-SS
	private Boolean getNonCached = true;

	public RequestTO()
	{

	}

	public String getFeatureName()
	{
		return featureName;
	}

	public void setFeatureName(String featureName)
	{
		this.featureName = featureName;
	}

	public String getService()
	{
		return service;
	}

	public void setService(String service)
	{
		this.service = service;
	}

	public String getSourceInterface()
	{
		return sourceInterface;
	}

	public void setSourceInterface(String sourceInterface)
	{
		this.sourceInterface = sourceInterface;
	}

	public String getDestinationInterface()
	{
		return destinationInterface;
	}

	public void setDestinationInterface(String destinationInterface)
	{
		this.destinationInterface = destinationInterface;
	}

	public String getStartDate()
	{
		return startDate;
	}

	public void setStartDate(String startDate)
	{
		this.startDate = startDate;
	}

	public String getEndDate()
	{
		return endDate;
	}

	public void setEndDate(String endDate)
	{
		this.endDate = endDate;
	}

	public Boolean getGetNonCached()
	{
		return getNonCached;
	}

	public void setGetNonCached(Boolean getNonCached)
	{
		this.getNonCached = getNonCached;
	}

	public String toString()
	{
		return ("FeatureName: " + featureName + "\nService: " + service + "\nSourceInterface: " + sourceInterface + "\nDestinationInterface: " + destinationInterface + "\nStartDate: " + startDate + "\nEndDate: " + endDate + "\nGetNonCached: " + getNonCached);
	}
}