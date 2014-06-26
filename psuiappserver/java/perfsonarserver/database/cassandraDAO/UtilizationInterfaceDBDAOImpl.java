package perfsonarserver.database.cassandraDAO;

import java.util.LinkedList;
import java.util.List;
import me.prettyprint.hector.api.Keyspace;
import perfsonarserver.database.database_to.UtilizationInterfaceDB;

public class UtilizationInterfaceDBDAOImpl extends ColumnFamilyDAO<String, UtilizationInterfaceDB> implements UtilizationInterfaceDBDAO{

public UtilizationInterfaceDBDAOImpl(Keyspace keyspace) {
		
		super(keyspace, String.class, UtilizationInterfaceDB.class, "UtilizationInterface");
		
	}

	@Override
	public void save(UtilizationInterfaceDB uInt) {
		super.save(uInt.getutilInterfaceID(), uInt);
	}
	
	public List<UtilizationInterfaceDB> find() {
		int i = 1;
		List<UtilizationInterfaceDB> interfaceList = new LinkedList<UtilizationInterfaceDB>();
		UtilizationInterfaceDB found;
		
		do {
			 found = super.find("interface_"+i);
			
			interfaceList.add(found);
			i++;
		} while (found!=null);
		
		return interfaceList;
		
	}

	@Override
	public void delete(UtilizationInterfaceDB uInt) {
		super.delete(uInt.getServiceName());
	}
}
