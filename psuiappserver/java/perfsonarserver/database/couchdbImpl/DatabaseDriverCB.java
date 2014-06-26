package perfsonarserver.database.couchdbImpl;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;


import com.couchbase.client.CouchbaseClient;


public class DatabaseDriverCB {
	
	
	protected CouchbaseClient client;
	protected String dbName;
	// Name of the Bucket to connect to
    protected String bucket = "DelayJitterLoss";
    // Password of the bucket (empty) string if none
    protected String password = "";

	
	protected void init() throws UnknownHostException {
		
		 //Verbindung zur Datenbank
		try {
	     	    // (Subset) of nodes in the cluster to establish a connection
			
			    System.setProperty("viewmode", "development"); 
				List<URI>  hosts = Arrays.asList(new URI("http://127.0.0.1:8091/pools"));
			    // Connect to the Cluster
			    client = new CouchbaseClient(hosts,"DelayJitterLoss", password);
			    
				} catch (IOException | URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			  System.out.println("Verbindung hergestellt");		 		 
	}	
}
