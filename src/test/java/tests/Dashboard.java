package tests;

import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import businessfunctions.DashboardBF;
import orep.DashboardOR;
import utilis.Library;

public class Dashboard extends Annotations {
	public static String userID = "user@phptravels.com";
	public static String password = "demouser";
	public static int noOfSecs =20;

	@Test(enabled = true, priority = 1)
	@Parameters({ "browserName" })
	public void Dashboard_TC001(String browserName) throws Exception {
		try {
			DashboardBF.login(driver, userID, password);

		} catch (Exception e) {

		} finally {
		}
	}

}
