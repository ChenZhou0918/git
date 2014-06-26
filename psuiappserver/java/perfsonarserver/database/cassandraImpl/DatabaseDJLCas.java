package perfsonarserver.database.cassandraImpl;

import java.net.UnknownHostException;


public class DatabaseDJLCas extends DatabaseDriverCas {
	
	/** */
	protected String keyspaceName = "perfsonar";

	/** Column Family Name for Delay Jitter Loss */
	protected String djlDataCF      = "DelayJitterLossData";
	protected String djlServiceCF   = "DelayJitterLossService";
	protected String djlInterfaceCF = "DelayJitterLossInterface";

	

	protected String ServicedeleteTime = "ServiceDeleteAt";

	
	public static DatabaseDJLCas DJLC;
	
	public DatabaseDJLCas() {
		
		dbName = "perfsonarDJLCas";
		try {
			init();
			setConfig(keyspaceName);
			createColumnFamily(keyspaceName,djlDataCF);
			System.out.println("Column Family DelayJitterLossData erstellt");
			createColumnFamily(keyspaceName,djlServiceCF);
			System.out.println("Column Family DelayJitterLossService erstellt");
			createColumnFamily(keyspaceName,djlInterfaceCF);
			System.out.println("Column Family DelayJitterLossInterface erstellt");
			
			System.out.println("Verbindung zur perfsonarDJLCas besteht");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static DatabaseDJLCas getInstance(){
		if(DJLC == null)
			DJLC = new DatabaseDJLCas();
		
		return DJLC;
	}	

}
