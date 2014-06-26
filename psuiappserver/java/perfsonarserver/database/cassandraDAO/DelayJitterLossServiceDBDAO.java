package perfsonarserver.database.cassandraDAO;

import java.util.List;

import perfsonarserver.database.database_to.DelayJitterLossServiceDB;

public interface DelayJitterLossServiceDBDAO {
	
	public void save (DelayJitterLossServiceDB djlSData);
	List<DelayJitterLossServiceDB> find();
	public void delete(DelayJitterLossServiceDB djlSData);

}
