package perfsonarserver.database.database_to;

import java.io.Serializable;


import perfsonarserver.database.response_to.DelayJitterLossInterfaceTO;




/**
 * Transfer object to easily convert java-objects into database-objects.
 * 
 * @author Benjamin Konrad, Sascha Degener
 */

public class DelayJitterLossInterfaceDB extends DelayJitterLossInterfaceTO implements Serializable
{

//	/** constant for col "srcInterface" inside database */
//	public static final String SRCINTERFACE_COL = "srcInterface";
//	/** constant for col "destInterface" inside database */
//	public static final String DESTINTERFACE_COL = "destInterface";
//	/** constant for col "serviceName" inside database */
//	public static final String SERVICENAME_COL = "serviceName";

	/**
	 * 
	 */
	
	/**
	 * 
	 */
//	private static final long serialVersionUID = -159398404505964139L;
	/** service name */
	private String serviceName;
	private String interfaceID;
	private String srcInterface;
	private String destInterface;
	/**
	 * Constructor for an empty object
	 */
	public DelayJitterLossInterfaceDB()
	{
		super();
		this.serviceName = "";
	}

	/**
	 * Constructor
	 * 
	 * @param serviceName
	 *            service name
	 * @param srcInterface
	 *            source interface
	 * @param destInterface
	 *            destination interface
	 */
	public DelayJitterLossInterfaceDB(String serviceName, String srcInterface, String destInterface)
	{
		super(srcInterface, destInterface);
		this.serviceName = serviceName;
		this.srcInterface = srcInterface;
		this.destInterface = destInterface;
	}

	/**
	 * Converts this java-object into a database-object
	 * 
	 * @return BasicDBObject database-object
	 */
//	public DBObject toDBObject()
//	{
//		BasicDBObject dbObjDataTO = new BasicDBObject();
//		{
//			dbObjDataTO.append(SRCINTERFACE_COL, this.getSrcInterface());
//			dbObjDataTO.append(DESTINTERFACE_COL, this.getDestInterface());
//			dbObjDataTO.append(SERVICENAME_COL, this.getServiceName());
//		}
//		return dbObjDataTO;
//	}

	/**
	 * Converts a database-object into a java-object
	 * 
	 * @param dbObj
	 *            database-object to be converted
	 * @return {@link DelayJitterLossInterfaceTO}
	 */
//	public static DelayJitterLossInterfaceTO toClass(DBObject dbObj)
//	{
//		DelayJitterLossInterfaceTO djlInterfaceTO = new DelayJitterLossInterfaceTO();
//		{
//			djlInterfaceTO.setSrcInterface((String)dbObj.get(SRCINTERFACE_COL));
//			djlInterfaceTO.setDestInterface((String)dbObj.get(DESTINTERFACE_COL));
//				 
//		}
//		return djlInterfaceTO;
//	}
	
//	public static DelayJitterLossInterfaceTO toType(List<HColumnImpl<String,String>> dbObj)
//	{
//		DelayJitterLossInterfaceTO djlInterfaceTO = new DelayJitterLossInterfaceTO();
//		{
//			for(HColumnImpl<String,String> temp : dbObj) {			
//				if(temp.getName().equalsIgnoreCase(SRCINTERFACE_COL))
//					djlInterfaceTO.setSrcInterface((String) temp.getValue());
//				else if(temp.getName().equalsIgnoreCase(DESTINTERFACE_COL))
//					djlInterfaceTO.setSrcInterface((String)temp.getValue());
//				} 
//		}
//		return djlInterfaceTO;
//	}
	
	/** For Cassandra Implementation: Put Objects into Map */
//	public Map<String, Object> processKeyValueMap() {
//		HashMap<String, Object> returnMap = new HashMap<String, Object>();
//		 {
//			returnMap.put(SERVICENAME_COL, this.getServiceName());
//			returnMap.put(SRCINTERFACE_COL, this.getSrcInterface());
//			returnMap.put(DESTINTERFACE_COL, this.getDestInterface());
//
//		}
//		return returnMap;
//	
//	}	

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
	
	 public String getDelayJitterLossInterfaceID() {
		    return interfaceID;
		  }

		  public void setDelayJitterLossInterfaceID(String interfaceID) {
		    this.interfaceID = interfaceID;
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
		  
		  public String toString()
			{
				return "DelayJitterLossInterfaceDB [interfaceID=" + interfaceID+ ", serviceName=" + serviceName + ", srcInterface=" + srcInterface+ ", desInterface=" +destInterface+"]";
			}
}