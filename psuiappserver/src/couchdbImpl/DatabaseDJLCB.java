package perfsonarserver.database.couchdbImpl;

import java.net.UnknownHostException;


public class DatabaseDJLCB extends DatabaseDriverCB{
	

	public static DatabaseDJLCB DJLC;
	
	public DatabaseDJLCB() {
		
		dbName = "perfsonarDJLCB";
		if(client == null)
			try {
				init();
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
	}
	
	public static DatabaseDJLCB getInstance(){
		if(DJLC == null)
			DJLC = new DatabaseDJLCB();
		
		return DJLC;
	}	

}
