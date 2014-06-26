package perfsonarserver.database.couchdbImpl;

import java.net.UnknownHostException;

public class DatabaseThroughputCB extends DatabaseDriverCB {
	
	public static DatabaseThroughputCB TC;
	
	public DatabaseThroughputCB() {
		dbName = "perfsonarThroughputCB";
		if(client == null)
			try {
				init();
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	}
	
	
	public static DatabaseThroughputCB getInstance(){
		if(TC == null)
			TC = new DatabaseThroughputCB();
		
		return TC;
	}	

}
