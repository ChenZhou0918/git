package perfsonarserver.database.couchdbImpl;

import java.net.UnknownHostException;

public class DatabaseUtilizationCB extends DatabaseDriverCB {
public static DatabaseUtilizationCB UC;
	
	public DatabaseUtilizationCB() {
		dbName = "perfsonarUtilizationCB";
		if(client == null)
			try {
				init();
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	}
	
	
	public static DatabaseUtilizationCB getInstance(){
		if(UC == null)
			UC = new DatabaseUtilizationCB();
		
		return UC;
	}	


}
