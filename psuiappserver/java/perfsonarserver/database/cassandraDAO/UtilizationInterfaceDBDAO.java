package perfsonarserver.database.cassandraDAO;

import java.util.List;
import perfsonarserver.database.database_to.UtilizationInterfaceDB;

public interface UtilizationInterfaceDBDAO {
	
	public void save (UtilizationInterfaceDB uInt);
	List<UtilizationInterfaceDB> find();
	public void delete(UtilizationInterfaceDB uInt);

}
