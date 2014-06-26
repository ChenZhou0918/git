package perfsonarserver.database.cassandraDAO;

import java.util.LinkedList;
import java.util.List;

import me.prettyprint.hector.api.Keyspace;
import perfsonarserver.database.database_to.ThroughputDataDB;
import perfsonarserver.database.database_to.ThroughputInterfaceDB;

public class ThroughputDataDBDAOImpl extends ColumnFamilyDAO<String, ThroughputDataDB> implements ThroughputDataDBDAO{

public ThroughputDataDBDAOImpl(Keyspace keyspace) {
		
		super(keyspace, String.class, ThroughputDataDB.class, "ThroughputData");
		
	}

	@Override
	public void save(ThroughputDataDB tData) {
		super.save(tData.gettDataID(), tData);
	}
	
	public List<ThroughputDataDB> find() {
		int i = 1;
		List<ThroughputDataDB> dataList = new LinkedList<ThroughputDataDB>();
		ThroughputDataDB found;
		
		do {
			 found = super.find("data_"+i);
			
			dataList.add(found);
			i++;
		} while (found!=null);
		
		return dataList;
		
	}



	@Override
	public void delete(ThroughputDataDB tData) {
		super.delete(tData.getServiceName());
	}

}
