package perfsonarserver.database.cassandraDAO;

import java.util.List;
import perfsonarserver.database.database_to.UtilizationDataDB;

public interface UtilizationDataDBDAO {
	
	public void save (UtilizationDataDB uData);
	List<UtilizationDataDB> find();
	public void delete(UtilizationDataDB uData);

}
