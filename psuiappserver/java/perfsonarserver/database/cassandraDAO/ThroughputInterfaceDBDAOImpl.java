package perfsonarserver.database.cassandraDAO;

import java.util.LinkedList;
import java.util.List;
import me.prettyprint.hector.api.Keyspace;
import perfsonarserver.database.database_to.ThroughputInterfaceDB;

public class ThroughputInterfaceDBDAOImpl extends ColumnFamilyDAO<String, ThroughputInterfaceDB> implements ThroughputInterfaceDBDAO{

public ThroughputInterfaceDBDAOImpl(Keyspace keyspace) {
		
		super(keyspace, String.class, ThroughputInterfaceDB.class, "ThroughputInterface");
		
	}

	@Override
	public void save(ThroughputInterfaceDB tInt) {
		super.save(tInt.getThroughputID(), tInt);
	}
	
	public List<ThroughputInterfaceDB> find() {
		int i = 1;
		List<ThroughputInterfaceDB> interfaceList = new LinkedList<ThroughputInterfaceDB>();
		ThroughputInterfaceDB found;
		
		do {
			 found = super.find("interface_"+i);
			
			interfaceList.add(found);
			i++;
		} while (found!=null);
		
		return interfaceList;
		
	}



	@Override
	public void delete(ThroughputInterfaceDB tInt) {
		super.delete(tInt.getServiceName());
	}
}
