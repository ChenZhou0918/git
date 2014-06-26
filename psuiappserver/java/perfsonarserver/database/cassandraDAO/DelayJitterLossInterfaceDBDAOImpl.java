package perfsonarserver.database.cassandraDAO;

import java.util.LinkedList;
import java.util.List;

import me.prettyprint.hector.api.Keyspace;
import perfsonarserver.database.database_to.DelayJitterLossInterfaceDB;
import perfsonarserver.database.database_to.DelayJitterLossServiceDB;


public class DelayJitterLossInterfaceDBDAOImpl extends ColumnFamilyDAO<String, DelayJitterLossInterfaceDB> implements DelayJitterLossInterfaceDBDAO {
public DelayJitterLossInterfaceDBDAOImpl(Keyspace keyspace) {
		
		super(keyspace, String.class, DelayJitterLossInterfaceDB.class, "DelayJitterLossInterface");
		// TODO Auto-generated constructor stub
	}



	@Override
	public void save(DelayJitterLossInterfaceDB djlSInt) {
		super.save(djlSInt.getDelayJitterLossInterfaceID(), djlSInt);
	}
	
	public List<DelayJitterLossInterfaceDB> find() {
		int i = 1;
		List<DelayJitterLossInterfaceDB> interfaceList = new LinkedList<DelayJitterLossInterfaceDB>();
		DelayJitterLossInterfaceDB found;
		
		do {
			 found = super.find("interface_"+i);
			
			interfaceList.add(found);
			i++;
		} while (found!=null);
		
		return interfaceList;
		
	}



	@Override
	public void delete(DelayJitterLossInterfaceDB djlSInt) {
		super.delete(djlSInt.getServiceName());
	}
}
