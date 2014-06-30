package perfsonarserver.database.general;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.List;

import perfsonarserver.appConnect.RequestTO;
import perfsonarserver.database.database_to.DelayJitterLossInterfaceDB;
import perfsonarserver.database.database_to.DelayJitterLossServiceDB;
import perfsonarserver.database.exception.FetchNothingException;
import perfsonarserver.database.exception.FindNothingException;
import perfsonarserver.database.response_to.*;
import perfsonarserver.fetchDataAndProcess.exception.FetchFailException;

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

	public List<DelayJitterLossDataTO> getDelayJitterLossData(RequestTO to) throws ParseException, FindNothingException, FetchNothingException;
	
	public List<DelayJitterLossDataTO> getDelayJitterLossData(RequestTO to, List<DelayJitterLossServiceDB> services,
			List<DelayJitterLossInterfaceDB> interfaces) throws ParseException, FindNothingException, FetchNothingException;
	
	public List<DelayJitterLossInterfaceTO> getDelayJitterLossInterface(RequestTO to) throws  FindNothingException, FetchNothingException;

	public void getDelayJitterLossService(RequestTO to)throws FindNothingException, FetchNothingException, FetchFailException, FileNotFoundException;

	public List<PathSegmentsGetDataTO> getPathSegmentsGetData(RequestTO to);

	public List<ThroughputInterfaceTO> getThroughputInterface(RequestTO to) throws  FindNothingException, FetchNothingException;

	public List<ThroughputServiceTO> getThroughputService(RequestTO to) throws  FindNothingException, FetchNothingException;

	public List<ThroughputGetDataTO> getThrougputGetData(RequestTO to) throws ParseException, FetchNothingException, FindNothingException;

	public List<UtilizationDataTO> getUtilizationData(RequestTO to) throws ParseException, FetchNothingException, FindNothingException;

	public List<UtilizationInterfaceTO> getUtilizationInterface(RequestTO to);

	public List<UtilizationServiceTO> getUtilizationService(RequestTO to);

}