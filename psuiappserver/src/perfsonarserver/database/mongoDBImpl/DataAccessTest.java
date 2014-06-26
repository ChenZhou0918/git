package perfsonarserver.database.mongoDBImpl;

import static org.junit.Assert.*;

import java.text.ParseException;

import org.junit.BeforeClass;
import org.junit.Test;

import perfsonarserver.appConnect.RequestTO;
import perfsonarserver.database.exception.FetchNothingException;
import perfsonarserver.database.exception.FindNothingException;

public class DataAccessTest
{
	DataAccess da = DataAccess.getInstance();
	
	@BeforeClass
	public static void initial()
	{
		
	}

	@Test
	public void testGetDashboardDelayGetData()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testGetDashboardJitterGetData()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testGetDashboardLossGetData()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testGetDashboardUDGetData()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testGetDelayJitterLossData() throws FindNothingException, FetchNothingException, ParseException
	{
		RequestTO request = new RequestTO();
		{
			request.setSourceInterface("Augsburg_DFN");
			request.setDestinationInterface("Aachen_DFN");
			request.setStartDate("2013-10-21 23-00-00-000");
			request.setEndDate("2013-10-22 23-59-00-000");
			request.setFeatureName("");
			request.setGetNonCached(true);
			request.setService("X-WiN");
		}
		
		assertTrue("", da.getDelayJitterLossData(request).size()>=1);
	}

	@Test
	public void testGetDelayJitterLossInterface()
	{
		RequestTO request = new RequestTO();
		{
			request.setSourceInterface("");
			request.setDestinationInterface("");
			request.setStartDate("2013-03-31 23-00-00-000");
			request.setEndDate("2013-03-31 23-59-00-000");
			request.setFeatureName("");
			request.setGetNonCached(true);
			request.setService("X-WiN");
		}
		
		assertTrue("",da.getDelayJitterLossInterface(request).size()>=1);
	}

	@Test
	public void testGetDelayJitterLossService()
	{
		assertTrue("",da.getDelayJitterLossService(new RequestTO()).size()>=1);
	}

	@Test
	public void testGetPathSegmentsGetData()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testGetThroughputInterface()
	{
		RequestTO request = new RequestTO();
		{
			request.setSourceInterface("");
			request.setDestinationInterface("");
			request.setStartDate("2013-03-20 23-00-00-000");
			request.setEndDate("2013-03-31 23-59-00-000");
			request.setFeatureName("");
			request.setGetNonCached(true);
			request.setService("GEANT SQL-MA");
		}
		
		assertTrue("",da.getThroughputInterface(request).size()>=1);
	}

	@Test
	public void testGetThroughputService()
	{
		assertTrue(da.getThroughputService(new RequestTO()).size()>=1);
	}

	@Test
	public void testGetThrougputGetData() throws FetchNothingException, FindNothingException, ParseException
	{
		RequestTO request = new RequestTO();
		{
			request.setSourceInterface("62.40.122.246");
			request.setDestinationInterface("202.179.246.18");
			request.setStartDate("2013-10-20 23-00-00-000");
			request.setEndDate("2013-10-25 23-59-00-000");
			request.setFeatureName("");
			request.setGetNonCached(true);
			request.setService("GEANT SQL-MA");
		}
		
		assertTrue("",da.getThrougputGetData(request).size()>=1);
	}

	@Test
	public void testGetUtilizationData()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testGetUtilizationInterface()
	{
		fail("Not yet implemented");
	}

	@Test
	public void testGetUtilizationService()
	{
		fail("Not yet implemented");
	}

}
