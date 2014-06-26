package perfsonarserver.database.cassandraDAO;

import java.util.LinkedList;
import java.util.List;

import me.prettyprint.hector.api.Keyspace;
import perfsonarserver.database.database_to.UtilizationDataDB;

public class UtilizationDataDBDAOImpl extends ColumnFamilyDAO<String, UtilizationDataDB> implements UtilizationDataDBDAO{

public UtilizationDataDBDAOImpl(Keyspace keyspace) {
		
		super(keyspace, String.class,  UtilizationDataDB.class, "UtilizationData");
		
	}

	@Override
	public void save(UtilizationDataDB uData) {
		super.save(uData.getutilDataID(), uData);
	}
	
	public List<UtilizationDataDB> find() {
		int i = 1;
		List<UtilizationDataDB> dataList = new LinkedList<UtilizationDataDB>();
		UtilizationDataDB found;
		
		do {
			 found = super.find("data_"+i);
			
			dataList.add(found);
			i++;
		} while (found!=null);
		
		return dataList;
		
	}



	@Override
	public void delete(UtilizationDataDB uData) {
		super.delete(uData.getServiceName());
	}
}
