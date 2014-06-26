package perfsonarserver.database.cassandraDAO;

import java.util.LinkedList;
import java.util.List;

import me.prettyprint.hector.api.Keyspace;
import perfsonarserver.database.database_to.DelayJitterLossDataDB;
import perfsonarserver.database.database_to.DelayJitterLossServiceDB;



public class DelayJitterLossDataDBDAOImpl extends ColumnFamilyDAO<String, DelayJitterLossDataDB> implements DelayJitterLossDataDBDAO  {

	public DelayJitterLossDataDBDAOImpl(Keyspace keyspace) {
		
		super(keyspace, String.class, DelayJitterLossDataDB.class, "DelayJitterLossData");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void save(DelayJitterLossDataDB djlData) {
		super.save(djlData.getDelayJitterLossDataID(), djlData);
		
	}
	
	@Override
	public List<DelayJitterLossDataDB> find() {
		int i = 1;
		
		List<DelayJitterLossDataDB> dataList = new LinkedList<DelayJitterLossDataDB>();
		DelayJitterLossDataDB found;
		do {
			 found = super.find("data_"+i);
			// System.out.println("found: "+found.getServiceName());
			dataList.add(found);
			i++;
		} while (found!=null);
		
			return dataList;
	}

	@Override
	public void delete(DelayJitterLossDataDB djlData) {
		super.delete(djlData.getServiceName());
		
	}

	

}
