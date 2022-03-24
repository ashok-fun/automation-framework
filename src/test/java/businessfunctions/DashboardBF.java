package businessfunctions;

import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import utilis.Library;

public class DashboardBF {

	@Test(enabled = true, priority=1)
	@Parameters({"browserName"})
	public void Dashboard_TC001 (String browserName) throws Exception
	{
		try {
			Library.startWebDriver(null, browserName);
			
		}
		catch (Exception e){
			
		}
		finally {}
	}

}
