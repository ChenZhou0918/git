//package ba2013perfSONAR.Server.cassandraTest;
//
//import java.io.IOException;
//
//import org.apache.cassandra.thrift.CassandraDaemon;
//import org.apache.thrift.transport.TTransportException;
///**
// * An embedded, in-memory cassandra storage service that listens
// * on the thrift interface as configured in storage-conf.xml
// * This kind of service is useful when running unit tests of
// * services using cassandra for example.
// *
// 
// * This is the implementation of https://issues.apache.org/jira/browse/CASSANDRA-740
// *
// 
// * How to use:
// * In the client code create a new thread and spawn it with its {@link Thread#start()} method.
// * Example:
// *
// *      // Tell cassandra where the configuration files are.
//        System.setProperty("storage-config", "conf");
// 
//        cassandra = new EmbeddedCassandraService();
//        cassandra.init();
// 
//        // spawn cassandra in a new thread
//        Thread t = new Thread(cassandra);
//        t.setDaemon(true);
//        t.start();
// 
// */
//public class EmbeddedCassandraService implements Runnable {
//
//	CassandraDaemon cassandraDaemon;
//	 
//    public void init() throws TTransportException, IOException
//    {
//        cassandraDaemon = new CassandraDaemon();
//        cassandraDaemon.init(null);
//    }
// 
//    public void run()
//    {
//        cassandraDaemon.start();
//    }
//
//}
