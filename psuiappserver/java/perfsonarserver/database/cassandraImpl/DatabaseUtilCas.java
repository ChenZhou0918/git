package perfsonarserver.database.cassandraImpl;

import java.net.UnknownHostException;


public class DatabaseUtilCas extends DatabaseDriverCas{
	
	/** */
	protected String keyspaceName = "perfsonar";

	/** Column Family Name for Utilization*/
	protected String uDataCF      = "UtilizationData";
	protected String uInterfaceCF = "UtilizationInterface";
	protected String uServiceCF   = "UtilizationService";
	
public static DatabaseUtilCas DUT;
	
	public DatabaseUtilCas() {
		
		dbName = "perfsonarUtilCas";
		try {
			init();
			setConfig(keyspaceName);
			createColumnFamily(keyspaceName,uDataCF);
			System.out.println("Column Family ThroughputData has been created");
			createColumnFamily(keyspaceName,uInterfaceCF);
			System.out.println("Column Family ThroughputInterface has been created");
			createColumnFamily(keyspaceName,uServiceCF);
			System.out.println("Column Family ThroughputService has been created");
			System.out.println("Successfully connected to perfsonarUtilCas");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public static DatabaseUtilCas getInstance(){
		if(DUT == null)
			DUT = new DatabaseUtilCas();
		
		return DUT;
	}	

}
