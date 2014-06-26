package perfsonarserver.database.mongoDBImpl;

public class DatabaseDJL extends DatabaseDriver
{
	public static DatabaseDJL DJL;
	
	public DatabaseDJL()
	{
		dbName = "perfsonarDJL";
		
		if(mc == null)
			init();
		
		db = mc.getDB(dbName);
	}
	
	public static DatabaseDJL getInstance()
	{
		if(DJL == null)
			DJL = new DatabaseDJL();
		
		return DJL;
			
	}
}
