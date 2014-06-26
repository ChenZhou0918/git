package perfsonarserver.database.mongoDB_DatabaseTO;

import perfsonarserver.database.mongoDB_responseTO.ThroughputInterfaceTO;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * Transfer object to easily convert java-objects into database-objects.
 * 
 * @author Benjamin Konrad, Sascha Degener
 */
public class ThroughputInterfaceDB extends ThroughputInterfaceTO
{
	/** constant for col "interface" inside database */
	public static final String SRCINTERFACE_COL = "srcInterface";
	/** constant */
	public static final String DESTINTERFACE_COL = "destInterface";
	/** constant */
	public static final String MID_COL ="MID";
	/** constant for col "serviceName" inside database */
	public static final String SERVICENAME_COL = "serviceName";

	/** service name */
	private String serviceName;
	private String MID;

	/**
	 * Constructor for an empty object
	 */
	public ThroughputInterfaceDB()
	{
		super();
		this.serviceName = "";
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
		super(srcInterface, destInterface);
		this.serviceName = serviceName;
		this.MID = MID;
	}

	/**
	 * Converts this java-object into a database-object
	 * 
	 * @return BasicDBObject database-object
	 */
	public DBObject toDBObject()
	{
		BasicDBObject dbObjDataTO = new BasicDBObject();
		{
			dbObjDataTO.append(SRCINTERFACE_COL, this.getSrcInterface());
			dbObjDataTO.append(DESTINTERFACE_COL, this.getDestInterface());
			dbObjDataTO.append(MID_COL, this.getMID());
			dbObjDataTO.append(SERVICENAME_COL, this.getServiceName());
		}
		return dbObjDataTO;
	}

	/**
	 * Converts a database-object into a java-object
	 * 
	 * @param dbObj
	 *            database-object to be converted
	 * @return {@link ThroughputInterfaceTO}
	 */
	public static ThroughputInterfaceTO toClass(DBObject dbObj)
	{
		ThroughputInterfaceTO tpInterfaceTO = new ThroughputInterfaceTO();
		{
			tpInterfaceTO.setSrcInterface((String) dbObj.get(SRCINTERFACE_COL));
			tpInterfaceTO.setDestInterface((String) dbObj.get(DESTINTERFACE_COL));
		}
		return tpInterfaceTO;
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
