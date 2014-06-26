package perfsonarserver.database.cassandraDAO;

import java.util.List;

import perfsonarserver.database.database_to.DelayJitterLossDataDB;;

public interface DelayJitterLossDataDBDAO {
	  public void save(DelayJitterLossDataDB djlData);
	  List<DelayJitterLossDataDB> find();
	  public void delete(DelayJitterLossDataDB djlData);
	  
}
