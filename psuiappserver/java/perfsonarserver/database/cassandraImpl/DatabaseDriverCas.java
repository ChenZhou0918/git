package perfsonarserver.database.cassandraImpl;

import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.lang.Long;

import perfsonarserver.database.cache_to.DataCacheDB;
import me.prettyprint.cassandra.model.ConfigurableConsistencyLevel;
import me.prettyprint.cassandra.model.HColumnImpl;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.service.CassandraHostConfigurator;
import me.prettyprint.cassandra.service.ColumnSliceIterator;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.HConsistencyLevel;
import me.prettyprint.hector.api.ddl.KeyspaceDefinition;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.ColumnSlice;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;
import me.prettyprint.hector.api.ddl.ComparatorType;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.SliceQuery;


public class DatabaseDriverCas {

	protected Cluster cluster   = null;
	protected Keyspace keySpace = null;
	protected KeyspaceDefinition keyspaceDef;
	protected String dbName;
	protected String keyspace;
	
	protected void init() throws UnknownHostException {
		
		 //Verbindung zur Datenbank
		 cluster = HFactory.getOrCreateCluster("TestCluster", new CassandraHostConfigurator("54.73.80.254:9160")); 
		 System.out.println("Cluster instanziiert");
		 
 	}	
	
	protected void setConfig(String keyspace) {
		KeyspaceDefinition keyspaceDef = cluster.describeKeyspace("TestCluster");

		// If keyspace does not exist, the CFs don't exist either. => create them.
		if (keyspaceDef == null) {
			this.keyspace = keyspace;
			 //Konsistenz konfigurieren. Level = 1 -> Warten bis mindestens 
			 //ein replizierter Knoten aktualisiert ist.
			 ConfigurableConsistencyLevel ccl = new ConfigurableConsistencyLevel(); 
			 ccl.setDefaultReadConsistencyLevel(HConsistencyLevel.ONE);
			 //keyspace erstellen und Konsistenzlevel übergeben.
			 keySpace = HFactory.createKeyspace(keyspace, cluster, ccl);
		}
		 
		 
	}
	public Keyspace getKeyspace(){
		return this.keySpace;
	}
	protected void createColumnFamily(String keyspace, String columnFamilyName){
		 //column Family erstellen.
		 ColumnFamilyDefinition cf = HFactory.createColumnFamilyDefinition(keyspace, columnFamilyName, ComparatorType.UTF8TYPE);
		 
		 cf.setKeyCacheSize(100);
		 cluster.addColumnFamily(cf);	
	}
	
	
	/** Description of searchForData(String key, String cf)
	 * 
	 * @param key	    Name of the Key within the specified Column family
	 * @param cf		Name of the Column Family
	 * @return			A List Column Names and Values
	 */
	public List<HColumnImpl<String, String>> searchForData(String key, String cf){
		System.out.println("Search for Data in " +cf+ " with key " +key);
		List<HColumnImpl<String, String>> columnList = null;
		SliceQuery<String, String, String> query = HFactory
				.createSliceQuery(keySpace, StringSerializer.get(), StringSerializer.get(), StringSerializer.get())
				.setKey(key).setColumnFamily(cf);
		ColumnSliceIterator<String, String, String> iterator = 
				new ColumnSliceIterator<String, String, String>(query, null, "\u00FFF", false);
		 columnList = new LinkedList<HColumnImpl<String, String>>();
		
		while(iterator.hasNext()) {
			HColumnImpl<String, String> column = (HColumnImpl<String, String>) iterator.next();	
			System.out.println("Found:");
			System.out.println("-------------" +cf+" "+key+" contains -----------------------------------");
			System.out.println("Column name = " +column.getName() + "; Column value = " +column.getValue());
			System.out.println("-------------------------------------------------------------------------");
			//columnList.add(column);
		}
		return columnList;		
	}
	
	/** Description of dropColumns(String key, String cf)
	* 
	* @param key	    Name of the Key within the specified Column family
	* @param cf		    Name of the Column Family
    *
	*/
	public void dropColumns(String key, String cf){
		SliceQuery<String, String, String> query = HFactory
				.createSliceQuery(keySpace, StringSerializer.get(), StringSerializer.get(), StringSerializer.get())
				.setKey(key).setColumnFamily(cf);
		ColumnSliceIterator<String, String, String> iterator = 
				new ColumnSliceIterator<String, String, String>(query, null, "\u00FFF", false);
		
		Mutator<String> mutator = HFactory.createMutator(keySpace, StringSerializer.get());
		
		while(iterator.hasNext()) {
			HColumnImpl<String, String> column = (HColumnImpl<String, String>) iterator.next();
			mutator.addDeletion(column.getName(), column.getValue());		
		}		
	}
	
	/** Description of getcolumnList(String columnFamily, String key)
	*  
	* @param cf		    Name of the Column Family
    * @param key	    Name of the Key within the specified Column family
    * 
	*/
	public List<HColumnImpl<String,String>>  getcolumnList(String columnFamily, String key) {
		System.out.println(key.toString());
		SliceQuery<String, String, String> query = HFactory
				.createSliceQuery(keySpace, StringSerializer.get(), StringSerializer.get(), StringSerializer.get())
				.setKey(key).setColumnFamily(columnFamily);
		
		ColumnSliceIterator<String, String,String> iterator = 
				new ColumnSliceIterator<String, String, String>(query, null, "\uFFFF", false);
		
		
		List<HColumnImpl<String, String>> columnList = new LinkedList<HColumnImpl<String, String>>();
		while(iterator.hasNext()) {
			HColumnImpl<String,String> column = (HColumnImpl<String, String>) iterator.next();
			System.out.println("-------------" +columnFamily+" "+key+" contains -----------------------------------");
			System.out.println("Column name = " +column.getName() + "; Column value = " +column.getValue());
			System.out.println("-------------------------------------------------------------------------");
			columnList.add(column);
	}
		return columnList;
	}
	 /** Description of getValueString(String cf, String key, String serviceName)
		*  
		* @param cf		     Name of the Column Family
	  * @param key	     Name of the Key within the specified Column family
	  * @param ColumnName  Name of the specefied Column the value is in
		*/
		public String getValueString(String cf, String key, String ColumnName){
			SliceQuery<String, String, String> sliceQuery = HFactory.createSliceQuery(keySpace, StringSerializer.get(), StringSerializer.get(), StringSerializer.get());
	      sliceQuery.setColumnFamily(cf).setKey(key);
	      sliceQuery.setColumnNames(ColumnName);
			
			QueryResult<ColumnSlice<String, String>> result = sliceQuery.execute();
			ColumnSlice<String, String> columnSlice = result.get();
			HColumn<String, String> column = columnSlice.getColumnByName(ColumnName);
			System.out.println("-------------" +cf+" "+key+" contains -----------------------------------");
			System.out.println("value: " + column.getValue());
			System.out.println("-------------------------------------------------------------------------");
			
			return column.getValue();
		}

	
	
	/** Description of getValueLong(String cf, String key, String serviceName)
	*  
	* @param cf		     Name of the Column Family
    * @param key	     Name of the Key within the specified Column family
    * @param ColumnName  Name of the specefied Column the value is in
	*/
	public Long getValueLong(String cf, String key, String serviceName){
		SliceQuery<String, String, Long> sliceQuery = HFactory.createSliceQuery(keySpace, StringSerializer.get(), StringSerializer.get(), LongSerializer.get());
        sliceQuery.setColumnFamily(cf).setKey(key);
        sliceQuery.setColumnNames(serviceName);
		
		QueryResult<ColumnSlice<String, Long>> result = sliceQuery.execute();
		ColumnSlice<String, Long> columnSlice = result.get();
		HColumn<String, Long> column = columnSlice.getColumnByName(serviceName);
		System.out.println("-------------" +cf+" "+key+" contains -----------------------------------");
		System.out.println("Column: " +column.getName()+"-> The value is " + column.getValue());
		System.out.println("-------------------------------------------------------------------------");
		
		return column.getValue();
	}
	
	public void insertData(String key, String cf,  Map<String, Object> data)   {
		
			System.out.println("ignoriert");
		SliceQuery<String, String,String> query = HFactory
				.createSliceQuery(keySpace, StringSerializer.get(), StringSerializer.get(), StringSerializer.get())
				.setKey(key).setColumnFamily(cf);
		ColumnSliceIterator<String, String, String> iterator = 
				new ColumnSliceIterator<String, String, String>(query, null, "\uFFFF", false);
		
		Mutator<String> mutator = HFactory.createMutator(keySpace, StringSerializer.get());
		System.out.println(data.toString());
		for (Object str : data.keySet())
		{
				mutator.insert(key, cf, HFactory.createColumn(str, data.get(str))); 	
		}
		
		while(iterator.hasNext()) {
			System.out.println("\n-------------" +cf+" "+key+" contains now---------------------------------");	
			HColumn<String, String> column =  iterator.next();
			Object v = column.getValue();
			System.out.println("Column name = " +column.getName() + "; Column value = " +v);
			System.out.println("\n-------------------------------------------------------------------------");
			//columnList.add(column);
		}
		 
		
	}
//	public void updateColumnFamily(String CF){
//		 KeyspaceDefinition definition = cluster.describeKeyspace(keyspace);
//	        if (definition == null)
//	            cluster.addKeyspace(converter.toKeySpaceDefinition(keySpaceDeclaration), true);
//	        else
//	        {
//	            KeyspaceDefinition keyspaceDefinition = converter.toKeySpaceDefinitionIgnoreColumnFamilyDefinitions(keySpaceDeclaration);
//	            cluster.updateKeyspace(keyspaceDefinition, true);
//	            Map<String, Integer> columnFamilyDefinitions = new HashMap<String, Integer>();
//	            for (ColumnFamilyDefinition columnFamilyDefinition : definition.getCfDefs())
//	            {
//	                columnFamilyDefinitions.put(columnFamilyDefinition.getName(), columnFamilyDefinition.getId());
//	            }
//	            for (ColumnFamilyDeclaration columnFamilyDeclaration : keySpaceDeclaration.columnFamilyDeclarations)
//	            {
//	                ColumnFamilyDefinition columnFamilyDefinition = converter.toColumnFamilyDefinition(columnFamilyDeclaration);
//	                if (columnFamilyDefinitions.containsKey(columnFamilyDefinition.getName()))
//	                {
//	                    columnFamilyDefinition.setId(columnFamilyDefinitions.get(columnFamilyDefinition.getName()));
//	                    cluster.updateColumnFamily(columnFamilyDefinition, true);
//	                } else
//	                    cluster.addColumnFamily(columnFamilyDefinition, true);
//	            }
//	        }
//	}
		public void insertDataLong(String key, String cf,  Map<String, Long> data)   {
			
			SliceQuery<String, String, Long> query = HFactory
					.createSliceQuery(keySpace, StringSerializer.get(), StringSerializer.get(), LongSerializer.get())
					.setKey(key).setColumnFamily(cf);
			ColumnSliceIterator<String, String, Long> iterator = 
					new ColumnSliceIterator<String, String, Long>(query, null, "\u00FFF", false);
			
			Mutator<String> mutator = HFactory.createMutator(keySpace, StringSerializer.get());
			
			for (String str : data.keySet())
			{
				mutator.insert(key, cf, HFactory.createColumn(str, data.get(key)));
			}
			while(iterator.hasNext()) {
				HColumnImpl<String, Long> column = (HColumnImpl<String, Long>) iterator.next();
				System.out.println("-------------" +cf+" "+key+" contains now---------------------------------");
				System.out.println("Column name = " +column.getName() + "; Column value = " +column.getValue());
				System.out.println("--------------------------------------------------------------------------");
				//columnList.add(column);
			}
			
	}
	
	
	public void dropcolumnFamily(String keyspace, String columnFamily){
		cluster.dropColumnFamily(keyspace, columnFamily);
		System.out.println("Column Family droped!");
	}
	
	
	
	public void insertOne(String key, String cf, String name, long value){
		try {
				
			Mutator<String> mutator = HFactory.createMutator(keySpace, StringSerializer.get());
			mutator.insert(key, cf, HFactory.createColumn(name, value));
			
			SliceQuery<String, String, String> query = HFactory
					.createSliceQuery(keySpace, StringSerializer.get(), StringSerializer.get(), StringSerializer.get())
					.setKey(key).setColumnFamily(cf);
			ColumnSliceIterator<String, String, String> iterator = 
					new ColumnSliceIterator<String, String, String>(query, null, "\u00FFF", false);
			while(iterator.hasNext()) {
				HColumnImpl<String, String> column = (HColumnImpl<String, String>) iterator.next();
				System.out.println("-------------" +cf+" "+key+" contains now---------------------------------");
				System.out.println("Column name = " +column.getName() + "; Column value = " +column.getValueBytes().getLong());
				System.out.println("--------------------------------------------------------------------------");
			}
			
		} catch (Exception ex) {
			System.out.println(ex);
			ex.getStackTrace();
		}
	}
		
	
	public void deleteColumn(String key, String columnFamily, String column) {
		if(keySpace != null && cluster != null) {
			StringSerializer stringSerializer = StringSerializer.get();
			Mutator<String> mutator = HFactory.createMutator(keySpace, stringSerializer);
			mutator.delete(key, columnFamily, column, stringSerializer);
		}
	}
	
	public void updateDJLData(String key, String columnFamily, DataCacheDB update){
		
		StringSerializer stringSerializer = StringSerializer.get();
        Mutator<String> mutator = HFactory.createMutator(keySpace,stringSerializer);
		Map<String, Object> updateMap= update.fromType();
            Set<Entry<String, Object>> set = updateMap.entrySet();
            Iterator<Entry<String, Object>> i = set.iterator();
            while (i.hasNext()) {
                Map.Entry entry = (Map.Entry) i.next();
                mutator.addInsertion(key, columnFamily
                        , HFactory.createStringColumn(entry
                        .getKey().toString(), entry.getValue().toString()));
                System.out.println("Updated..");
            }
            mutator.execute();
        }
	}
	
	
	
	
	
	


	


