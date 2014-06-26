package perfsonarserver.database.mongoDBImpl;

import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.WriteConcern;

/**the base class for DatabaseDJL DatabaseThrough DatabaseUtil
 * wwd,ZC
 * 
 */
public class DatabaseDriver
{
	
	protected static MongoClient mc;
	
	protected String dbName;
	
	protected DB db;

	protected void init()
	{
		try
		{
			mc = new MongoClient("127.0.0.1",27017);
			setWriteConcern();
			System.out.println("Connect to the database: " + dbName);
		}
		catch (UnknownHostException e)
		{ 
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	private void setWriteConcern()
	{
		mc.setWriteConcern(WriteConcern.JOURNALED);
	}


	public void dropDatabase()
	{
		mc.dropDatabase(dbName);
		System.out.println("DB Dropped!");
	}

	
	public void dropCollection(String collection)
	{
		DBCollection coll = db.getCollection(collection);
		coll.drop();
	}

	
	public DBObject findOne(String collection)
	{
		DBCollection coll = db.getCollection(collection);
		return coll.findOne();
	}

	
	public DBObject findOne(String collection, DBObject search)
	{
		DBCollection coll = db.getCollection(collection);
		return coll.findOne(search);
	}


	public List<DBObject> find(String collection)
	{
		DBCollection coll = db.getCollection(collection);
		DBCursor dbc = coll.find();
		List<DBObject> list = new LinkedList<DBObject>();
		for (DBObject dbObj : dbc)
		{
			list.add(dbObj);
		}
		return list;
	}
	public List<DBObject> find(String collection, DBObject search)
	{
		DBCollection coll = db.getCollection(collection);
		DBCursor dbc = coll.find(search); 
		List<DBObject> list = new LinkedList<DBObject>();
		for (DBObject dbObj : dbc)
		{
			list.add(dbObj);
		}
		return list;
	}
	/**
	 * /find objects  ordered by certain key
	 * 
	 * @orderby key name: criteria for sorting
	 * 1: ascend, -1:descend
	 * 
	 */
	
	public List<DBObject> find(String collection, DBObject search, String orderby, int i)//find objects in order by certain key
	{
		DBCollection coll = db.getCollection(collection);
		DBCursor dbc = coll.find(search).sort(new BasicDBObject(orderby,i)); 
		List<DBObject> list = new LinkedList<DBObject>();
		for (DBObject dbObj : dbc)
		{
			list.add(dbObj);
		}
		return list;
	}


	public void insert(String collection, DBObject dbObj)
	{
		DBCollection coll = db.getCollection(collection);
		coll.insert(dbObj);
	}


	public void insert(String collection, List<DBObject> dbObjs)
	{
		DBCollection coll = db.getCollection(collection);
		for(DBObject dbo:dbObjs)
		coll.insert(dbo);
	}

	
	public void remove(String collection, DBObject query)
	{
		DBCollection coll = db.getCollection(collection);
		coll.remove(query);
	}

	public void update(String collection, DBObject query, DBObject update)
	{
		update(collection, query, update, false, false);
	}
	
	public void upsert(String collection, DBObject query, DBObject update)
	{
		update(collection, query, update, true, false);
	}
	
	public void update(String collection, DBObject query, DBObject update,Boolean upsert, Boolean multiple)
	{
		DBCollection coll = db.getCollection(collection);
		coll.update(query, update, upsert, multiple);
	}
	
	
}
