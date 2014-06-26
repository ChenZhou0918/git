package perfsonarserver.database.mongoDBImpl;

public class DatabaseThrough extends DatabaseDriver
{
	public static DatabaseThrough through;
	
	public DatabaseThrough()
	{
		dbName = "perfsonarThrough";
		
		if(mc == null)
			init();
		
		db = mc.getDB(dbName);
	}
	
	public static DatabaseThrough getInstance()
	{
		if(through == null)
			through = new DatabaseThrough();
		
		return through;
			
	}
}
