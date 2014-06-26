
package debug;
import java.text.ParseException;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;

import perfsonarserver.appConnect.DatabaseRequest;
import perfsonarserver.appConnect.RequestTO;
import perfsonarserver.database.mongoDBImpl.DataAccess;
import perfsonarserver.fetchData.exception.FetchFailException;

/******Only for debug use !!      getDashboardUtil*******************************************************************/
public class DebugTest {

	public static void main(String[] args) throws ParseException, FetchFailException{
		
		final String JS_SERVICEUTILIZATION= "ServiceUtilization";
		final String JS_SERVICEDELAYJITTERLOSS = "ServiceDelayJitterLoss";
		final String JS_TOPUTILIZATIONVALUES = "TopUtilizationValues";
		final String JS_TOPDELAYVALUES = "TopDelayValues";
		final String JS_TOPJITTERVALUES = "TopJitterValues";

		
RequestTO request=new RequestTO();



/****test dashboard****/

//request.setFeatureName("DashboardGetUtilizationTop5");
//request.setFeatureName("DashboardGetOverview");
//request.setServiceDelayJitterLoss("GEANT_production");
//request.setServiceUtilization("APAN-JP");
//request.setServiceThroughput("GEANT-Diagnostic");
//request.setGetNonCached(true);


/****test get Data****/
request.setFeatureName("DashboardLoss");
request.setServiceDelayJitterLoss("X-WiN");
//request.setFeatureName("DelayJitterLossGetData");
//request.setFeatureName("UtilizationGetData");
//request.setService("APAN-JP");
//request.setService("X-WiN");
//request.setGetNonCached(true);
//request.setSourceInterface("1000BASE-X(SFP) 6/5 e6/5");
//request.setSourceInterface("Aachen_DFN");
//request.setDestinationInterface("Berlin_ADH_DFN");
//request.setSourceInterface("UK_London_GN");
//request.setStartDate("2014-06-15 20-16-29-000");
//request.setEndDate("2014-06-15 24-18-40-000");

/****test get service****/
//request.setFeatureName("DelayJitterLossGetService");
//request.setFeatureName("UtilizationGetService");

//request.setGetNonCached(true);
//request.setService("X-WiN");
 

/****test get interface****/
//request.setFeatureName("DelayJitterLossGetSourceInterfaces");
//request.setFeatureName("DelayJitterLossGetDestinationInterfaces");
//request.setFeatureName("UtilizationGetInterface");
//request.setFeatureName("ThroughputGetSourceInterfaces");
//request.setGetNonCached(true);
//request.setService("GEANT-PRODUCTION");
//request.setService("GEANT_production");
//request.setService("APAN-JP");
//request.setService("X-WiN");
//request.setService("GEANT-Diagnostic");

DatabaseRequest dbrequest=new DatabaseRequest();
JsonArray values=dbrequest.dbRequest(request);
JsonObject response=new JsonObject();


/********overview response**********/
//response.add("Utilization",values.get(0));
//response.add("Delay",values.get(1));
//response.add("jitter",values.get(2));

/********top 5  response**********/
//for(int i=0;i<=4;i++)
//{
//	int n=i+1;
//response.add("Top"+n, values.get(i));
//}


System.out.println(values);
//System.out.println(values.size());
		
	}
}
