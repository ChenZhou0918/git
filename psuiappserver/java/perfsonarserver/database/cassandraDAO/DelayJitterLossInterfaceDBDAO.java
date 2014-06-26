package perfsonarserver.database.cassandraDAO;

import java.util.List;

import perfsonarserver.database.database_to.DelayJitterLossInterfaceDB;

public interface DelayJitterLossInterfaceDBDAO {
	
	public void save (DelayJitterLossInterfaceDB djlInt);
	List<DelayJitterLossInterfaceDB> find();
	public void delete(DelayJitterLossInterfaceDB djlSInt);

}

