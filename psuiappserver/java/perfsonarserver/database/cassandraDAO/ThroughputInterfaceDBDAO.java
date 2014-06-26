package perfsonarserver.database.cassandraDAO;

import java.util.List;
import perfsonarserver.database.database_to.ThroughputInterfaceDB;

public interface ThroughputInterfaceDBDAO {
	
	public void save (ThroughputInterfaceDB tInt);
	List<ThroughputInterfaceDB> find();
	public void delete(ThroughputInterfaceDB tInt);

}
