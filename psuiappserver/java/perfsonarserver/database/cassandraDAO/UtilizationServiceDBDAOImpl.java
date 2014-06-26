package perfsonarserver.database.cassandraDAO;

import java.util.LinkedList;
import java.util.List;

import me.prettyprint.hector.api.Keyspace;
import perfsonarserver.database.database_to.ThroughputServiceDB;
import perfsonarserver.database.database_to.UtilizationServiceDB;

public class UtilizationServiceDBDAOImpl extends ColumnFamilyDAO<String, UtilizationServiceDB> implements UtilizationServiceDBDAO {


	private static final String COL_FAMILY_NAME = "UtilizatioService";
	public UtilizationServiceDBDAOImpl(Keyspace keyspace) {
		
		super(keyspace, String.class, UtilizationServiceDB.class, "UtilizationService");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void save(UtilizationServiceDB utilSData) {
		super.save(utilSData.getUtilServiceID(), utilSData);
		
	}
	
	
	@Override
	public List<UtilizationServiceDB> find() {
		int i = 1;
		
		List<UtilizationServiceDB> serviceList = new LinkedList<UtilizationServiceDB>();
		UtilizationServiceDB found;
		do {
			 found = super.find("service_"+i);
			// System.out.println("found: "+found.getServiceName());
			serviceList.add(found);
			i++;
		} while (found!=null);
		
			return serviceList;
	}
	
	@Override
	public void delete(UtilizationServiceDB utilSData) {
		super.delete(utilSData.getServiceName());
		
	}


}
