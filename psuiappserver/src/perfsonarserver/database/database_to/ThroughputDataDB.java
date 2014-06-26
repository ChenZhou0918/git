package perfsonarserver.database.database_to;



import perfsonarserver.database.response_to.ThroughputGetDataTO;

/**
 * Transfer object to easily convert java-objects into database-objects.
 * 
 * @author Benjamin Konrad, Sascha Degener
 */
public class ThroughputDataDB extends ThroughputGetDataTO
{
	/** constant for col "timestamp" inside database */
	public static final String TIMESTAMP_COL = "timestamp";
	/** constant for col "interval" inside database */
	public static final String INTERVAL_COL= "interval";
	/** constant for col "measuredThroughput" inside database */
	public static final String MEASUREDTHROUGHPUT_COL = "measuredThroughput";
	/** constant for col "serviceName" inside database */
	public static final String SERVICENAME_COL = "serviceName";
	/** constant for col "srcInterface" inside database */
	public static final String SRCINTERFACE_COL = "srcInterface";
	/** constant for col "destInterface" inside database */
	public static final String DESTINTERFACE_COL = "destInterface";

	/** service name */
	private String serviceName;
	/** source interface */
	private String srcInterface;
	/** destination interface */
	private String destInterface;
	/** data id*/
	private String tDataID;

	/**
	 * Constructor for an empty object
	 */
	public ThroughputDataDB()
	{
		super();
	}

	/**
	 * Constructor
	 * 
	 * @param timestamp
	 *            time of measurement
	 * @param measuredThroughput
	 *            measured throughput
	 * @param serviceName
	 *            service Name
	 * @param srcInterface
	 *            source interface
	 * @param destInterface
	 *            destination interface
	 */
	public ThroughputDataDB(long timestamp, String interval, double measuredThroughput, String serviceName, String srcInterface, String destInterface)
	{
		super(timestamp, measuredThroughput, interval);

		this.serviceName = serviceName;
		this.srcInterface = srcInterface;
		this.destInterface = destInterface;
	}

	public String gettDataID(){
		return tDataID;
	}
	
	public void settDataID(String tDataID){
		this.tDataID = tDataID;
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

	/**
	 * Get source interface
	 * 
	 * @return source interface
	 */
	public String getSrcInterface()
	{
		return srcInterface;
	}

	/**
	 * Set source interface
	 * 
	 * @param srcInterface
	 *            source interface
	 */
	public void setSrcInterface(String srcInterface)
	{
		this.srcInterface = srcInterface;
	}

	/**
	 * Get destination interface
	 * 
	 * @return destination interface
	 */
	public String getDestInterface()
	{
		return destInterface;
	}

	/**
	 * Set destination interface
	 * 
	 * @param destInterface
	 *            destination interface
	 */
	public void setDestInterface(String destInterface)
	{
		this.destInterface = destInterface;
	}

}
