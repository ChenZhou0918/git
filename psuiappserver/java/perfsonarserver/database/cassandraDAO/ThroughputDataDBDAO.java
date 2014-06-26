package perfsonarserver.database.cassandraDAO;

import java.util.List;
import perfsonarserver.database.database_to.ThroughputDataDB;


public interface ThroughputDataDBDAO {
	
	public void save (ThroughputDataDB tData);
	List<ThroughputDataDB> find();
	public void delete(ThroughputDataDB tData);

}
