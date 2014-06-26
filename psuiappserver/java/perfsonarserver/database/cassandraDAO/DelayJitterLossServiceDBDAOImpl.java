package perfsonarserver.database.cassandraDAO;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.serializers.UUIDSerializer;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.ColumnSlice;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.SliceQuery;
import perfsonarserver.database.database_to.DelayJitterLossServiceDB;

public class DelayJitterLossServiceDBDAOImpl extends ColumnFamilyDAO<String, DelayJitterLossServiceDB> implements DelayJitterLossServiceDBDAO {

	private static final String COL_FAMILY_NAME = "DelayJitterLossService";
	public DelayJitterLossServiceDBDAOImpl(Keyspace keyspace) {
		
		super(keyspace, String.class, DelayJitterLossServiceDB.class, "DelayJitterLossService");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void save(DelayJitterLossServiceDB djlSData) {
		super.save(djlSData.getDelayJitterLossServiceID(), djlSData);
		
	}
	
	
	@Override
	public List<DelayJitterLossServiceDB> find() {
		int i = 1;
		
		List<DelayJitterLossServiceDB> serviceList = new LinkedList<DelayJitterLossServiceDB>();
		DelayJitterLossServiceDB found;
		do {
			 found = super.find("service_"+i);
			// System.out.println("found: "+found.getServiceName());
			serviceList.add(found);
			i++;
		} while (found!=null);
		
			return serviceList;
	}

	@Override
	public void delete(DelayJitterLossServiceDB djlSData) {
		super.delete(djlSData.getServiceName());
		
	}
}
