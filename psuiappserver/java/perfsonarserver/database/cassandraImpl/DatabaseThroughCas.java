package perfsonarserver.database.cassandraImpl;

import java.net.UnknownHostException;


public class DatabaseThroughCas extends DatabaseDriverCas {
	
	/** */
	protected String keyspaceName = "perfsonar";

	/** Column Family Name for Throughput*/
	protected String tDataCF      = "ThroughputData";
	protected String tServiceCF   = "ThroughputService";
	protected String tInterfaceCF = "ThroughputInterface";

	
	
public static DatabaseThroughCas DTC;
	
	public DatabaseThroughCas() {
		
		dbName = "perfsonarThroughCas";
		try {
			init();
			setConfig(keyspaceName);
			createColumnFamily(keyspaceName,tDataCF);
			System.out.println("Column Family ThroughputData erstellt");
			createColumnFamily(keyspaceName,tServiceCF);
			System.out.println("Column Family ThroughputService erstellt");
			createColumnFamily(keyspaceName,tInterfaceCF);
			System.out.println("Column Family TroughputInterface erstellt");
			System.out.println("Verbindung zur perfsonarThroughCas besteht");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public static DatabaseThroughCas getInstance(){
		if(DTC == null)
			DTC = new DatabaseThroughCas();
		
		return DTC;
	}	

}
