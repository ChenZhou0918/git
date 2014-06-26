package perfsonarserver.database.cassandraDAO;

import java.util.List;
import perfsonarserver.database.database_to.ThroughputServiceDB;

public interface ThroughputServiceDBDAO {
	
	public void save (ThroughputServiceDB throuSData);
	List<ThroughputServiceDB> find();
	public void delete(ThroughputServiceDB throuSData);

}
