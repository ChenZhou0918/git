package perfsonarserver.database.mongoDBImpl;

import java.text.ParseException;
import java.util.List;

import perfsonarserver.appConnect.RequestTO;
import perfsonarserver.database.exception.FetchNothingException;
import perfsonarserver.database.exception.FindNothingException;
import perfsonarserver.database.mongoDB_responseTO.*;
import perfsonarserver.fetchData.exception.FetchFailException;

/**
 * Interface for the database requests.
 * 
 * @author Benjamin Konrad, Sascha Degener£¬zc
 */
public interface IDataAccessMongoDB
{

	public List<DashboardDelayGetDataTO> getDashboardDelayGetData(RequestTO to) throws ParseException, FetchFailException;

	public List<DashboardJitterGetDataTO> getDashboardJitterGetData(RequestTO to) throws ParseException, FetchFailException;

	public List<DashboardLossGetDataTO> getDashboardLossGetData(RequestTO to) throws ParseException, FetchFailException;

	public List<DashboardUDGetDataTO> getDashboardUDGetData(RequestTO to) throws ParseException;

	public List<DashboardThroughputGetDataTO> getDashboardThroughputGetData(RequestTO request) throws ParseException, FetchFailException;

	public List<DelayJitterLossDataTO> getDelayJitterLossData(RequestTO to) throws ParseException, FindNothingException, FetchNothingException;

	public List<DelayJitterLossInterfaceTO> getDelayJitterLossInterface(RequestTO to) throws  FindNothingException, FetchNothingException;

	public List<DelayJitterLossServiceTO> getDelayJitterLossService(RequestTO to)throws FindNothingException, FetchNothingException;

	public List<PathSegmentsGetDataTO> getPathSegmentsGetData(RequestTO to);

	public List<ThroughputInterfaceTO> getThroughputInterface(RequestTO to) throws  FindNothingException, FetchNothingException;

	public List<ThroughputServiceTO> getThroughputService(RequestTO to) throws  FindNothingException, FetchNothingException;

	public List<ThroughputGetDataTO> getThrougputGetData(RequestTO to) throws ParseException, FetchNothingException, FindNothingException;

	public List<UtilizationDataTO> getUtilizationData(RequestTO to) throws ParseException, FetchNothingException, FindNothingException;

	public List<UtilizationInterfaceTO> getUtilizationInterface(RequestTO to);

	public List<UtilizationServiceTO> getUtilizationService(RequestTO to);

}