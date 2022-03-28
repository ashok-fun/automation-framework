package businessfunctions;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.Reporter;

import orep.DashboardOR;
import utilis.Library;

public class DashboardBF {
	public static int noOfSecs =20;

	public static void login(RemoteWebDriver driver, String userID, String Password)

	{
		try {
			Library.click(driver, DashboardOR.customerFrontEndLogin, true, noOfSecs);
			Library.SwitchWindowByTitle(driver, "Login - PHPTRAVELS");
			Library.sendKeys(driver, DashboardOR.userID, userID, false, noOfSecs);
			Library.sendKeys(driver, DashboardOR.password, Password, false, noOfSecs);
			Library.click(driver, DashboardOR.login, true, noOfSecs);
			String Actual = Library.getText(driver, Password, false, noOfSecs);
			Reporter.log("Verify login is successful", true);
			Assert.assertEquals("Home", Actual);
			
		} catch (Exception e) {
		}

	}

}
