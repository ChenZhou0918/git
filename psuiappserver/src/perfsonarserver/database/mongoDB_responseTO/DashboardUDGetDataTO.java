package perfsonarserver.database.mongoDB_responseTO;

import perfsonarserver.database.mongoDBImpl.DataAccess;

/**
 * 
 * @author Zhou Chen
 * 
 */
public class DashboardUDGetDataTO
{

		private long timeStamp;
		private double measuredUtil;
		private String direction;
		private String serviceName;
		private String Interface;
		private String HostName;
		private String description;
		private long capacity;
		
		
		public DashboardUDGetDataTO()
		{
		}
		
		public DashboardUDGetDataTO(long timeStamp, double measuredUtil, String direction,String Interface)
		{
			this.timeStamp=timeStamp;
			this.measuredUtil = measuredUtil;
			this.direction = direction;
			this.Interface=Interface;
			
			
		}
		
		public DashboardUDGetDataTO(long timeStamp, double measuredUtil, String direction,String Interface,String serviceName)
		{
			this.timeStamp=timeStamp;
			this.measuredUtil = measuredUtil;
			this.direction = direction;
			this.Interface=Interface;
			this.serviceName=serviceName;
			
		}
		
		public DashboardUDGetDataTO(long timeStamp, double measuredUtil, String direction,String Interface,String serviceName,String HostName,
				String description,long capacity)
		{
			this.timeStamp=timeStamp;
			this.measuredUtil = measuredUtil;
			this.direction = direction;
			this.Interface=Interface;
			this.serviceName=serviceName;
			this.HostName=HostName;
			this.description=description;
			this.capacity=capacity;
		}
		
		public String getInterface()
		{
			return Interface;
		}
		
		public void setInterface(String Interface)
		{
			this.Interface=Interface;
		}
		
		public String getserviceName()
		{
			return serviceName;
		}
		
		public void setserviceName(String serviceName)
		{
			this.serviceName = serviceName;
		}
		/**
		 * @return the timeStamp
		 */
		public long getTimeStamp()
		{
			return timeStamp;
		}

		/**
		 * @param timeStamp the timeStamp to set
		 */
		public void setTimeStamp(long timeStamp)
		{
			this.timeStamp = timeStamp;
		}
		public String getTimestampString()
		{
			return DataAccess.convertDateToString(timeStamp*1000);
		}

		/**
		 * @return the utilData
		 */
		public double getMeasuredUtil()
		{
			return measuredUtil;
		}

		/**
		 * @param utilData the utilData to set
		 */
		public void setMeasuredUtil(double measuredUtil)
		{
			this.measuredUtil = measuredUtil;
		}

		/**
		 * @return the direction
		 */
		public String getDirection()
		{
			return direction;
		}

		/**
		 * @param direction the direction to set
		 */
		public void setDirection(String direction)
		{
			this.direction = direction;
		}
		/**
		 * @return the Hostname
		 */
		public String getHostName()
		{
			return HostName;
		}

		/**
		 * @param Hostname
		 */
		public void setHostName(String HostName)
		{
			this.HostName=HostName;
		}
		
		/**
		 * @return the description
		 */
		public String getDescription()
		{
			return description;
		}

		/**
		 * @param description
		 */
		public void setDescription(String description)
		{
			this.description=description;
		}
		/**
		 * @return the capacity
		 */
		public long getcapacity()
		{
			return capacity;
		}

		/**
		 * @param capacity
		 */
		public void setCapacity(long capacity)
		{
			this.capacity=capacity;
		}
	}

