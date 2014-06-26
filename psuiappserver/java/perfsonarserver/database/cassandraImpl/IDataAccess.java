package perfsonarserver.database.cassandraImpl;

import java.text.ParseException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import perfsonarserver.appConnect.RequestTO;
import perfsonarserver.database.database_to.DelayJitterLossDataDB;
import perfsonarserver.database.database_to.DelayJitterLossInterfaceDB;
import perfsonarserver.database.database_to.DelayJitterLossServiceDB;
import perfsonarserver.database.database_to.ThroughputInterfaceDB;
import perfsonarserver.database.database_to.ThroughputServiceDB;
import perfsonarserver.database.database_to.UtilizationInterfaceDB;
import perfsonarserver.database.database_to.UtilizationServiceDB;
import perfsonarserver.database.exception.FetchNothingException;
import perfsonarserver.database.exception.FindNothingException;
import perfsonarserver.database.response_to.*;
import perfsonarserver.fetchData.IServerRequest;
import perfsonarserver.fetchData.exception.FetchFailException;
import perfsonarserver.fetchData.transferObjects.DelayJitterLossData;
import perfsonarserver.fetchData.transferObjects.UtilizationData;

/**
 * Interface for the database requests.
 * 
 * @author Benjamin Konrad, Sascha Degener, Mirco Bohlmann
 */
public interface IDataAccess
{

	public List<DashboardDelayGetDataTO> getDashboardDelayGetData(RequestTO to);

	public List<DashboardJitterGetDataTO> getDashboardJitterGetData(RequestTO to);

	public List<DashboardLossGetDataTO> getDashboardLossGetData(RequestTO to);

	public List<DashboardUDGetDataTO> getDashboardUDGetData(RequestTO to);

	public List<DelayJitterLossDataDB> getDelayJitterLossData(RequestTO to) throws ParseException, FindNothingException, FetchNothingException;
	
	public List<DelayJitterLossData> getDelayJitterLossData(RequestTO to, List<DelayJitterLossServiceDB> services,
			List<DelayJitterLossInterfaceDB> interfaces) throws ParseException, FindNothingException, FetchNothingException;
	
	public List<DelayJitterLossInterfaceTO> getDelayJitterLossInterface(RequestTO to) throws  FindNothingException, FetchNothingException;

	public void getDelayJitterLossService(RequestTO to)throws FindNothingException, FetchNothingException, FetchFailException, InterruptedException, ExecutionException;

	public List<PathSegmentsGetDataTO> getPathSegmentsGetData(RequestTO to);

	public List<ThroughputInterfaceDB> getThroughputInterface(IServerRequest request) throws FindNothingException, FetchNothingException;

	public List<ThroughputServiceDB> getThroughputService(RequestTO to) throws  FindNothingException, FetchNothingException;

	public List<ThroughputGetDataTO> getThrougputGetData(RequestTO to) throws ParseException, FetchNothingException, FindNothingException;

	public List<UtilizationDataTO> getUtilizationData(RequestTO to) throws ParseException, FetchNothingException, FindNothingException;

	public void getUtilizationInterface(RequestTO to);

	public List<UtilizationServiceTO> getUtilizationService(RequestTO to);



	List<UtilizationData> getUtilizationData(RequestTO to,
			List<UtilizationServiceDB> services,
			List<UtilizationInterfaceDB> interfaces) throws ParseException,
			FindNothingException, FetchNothingException;

}