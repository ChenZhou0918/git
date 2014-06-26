package perfsonarserver.database.cassandraDAO;

import java.util.LinkedList;
import java.util.List;
import me.prettyprint.hector.api.Keyspace;

import perfsonarserver.database.database_to.ThroughputServiceDB;

public class ThroughputServiceDBDAOImpl extends ColumnFamilyDAO<String, ThroughputServiceDB> implements ThroughputServiceDBDAO {

	private static final String COL_FAMILY_NAME = "ThroughputService";
	public ThroughputServiceDBDAOImpl(Keyspace keyspace) {
		
		super(keyspace, String.class, ThroughputServiceDB.class, "ThroughputService");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void save(ThroughputServiceDB throuSData) {
		super.save(throuSData.getThroughputID(), throuSData);
		
	}
	
	
	@Override
	public List<ThroughputServiceDB> find() {
		int i = 1;
		
		List<ThroughputServiceDB> serviceList = new LinkedList<ThroughputServiceDB>();
		ThroughputServiceDB found;
		do {
			 found = super.find("service_"+i);
			// System.out.println("found: "+found.getServiceName());
			serviceList.add(found);
			i++;
		} while (found!=null);
		
			return serviceList;
	}
	
	@Override
	public void delete(ThroughputServiceDB tSData) {
		super.delete(tSData.getServiceName());
		
	}


}
