package perfsonarserver.database.cassandraDAO;

import java.util.List;
import perfsonarserver.database.database_to.UtilizationServiceDB;


public interface UtilizationServiceDBDAO {
	
	public void save (UtilizationServiceDB UtilSData);
	List<UtilizationServiceDB> find();
	public void delete(UtilizationServiceDB UtilSData);

}
