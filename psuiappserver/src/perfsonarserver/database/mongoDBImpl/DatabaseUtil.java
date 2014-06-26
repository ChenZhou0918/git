package perfsonarserver.database.mongoDBImpl;

public class DatabaseUtil extends DatabaseDriver
{
	public static DatabaseUtil util;

	public DatabaseUtil()
	{
		dbName = "perfsonarUtil";

		if (mc == null)
			init();

		db = mc.getDB(dbName);
	}

	public static DatabaseUtil getInstance()
	{
		if (util == null)
			util = new DatabaseUtil();

		return util;

	}
}
