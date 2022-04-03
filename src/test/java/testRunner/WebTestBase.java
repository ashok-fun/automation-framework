package testRunner;

import java.util.HashMap;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;

import utilis.Library;

public class WebTestBase {
	protected static HashMap<String, String> properties = new HashMap<String, String>();
	protected ThreadLocal<String> thisClassMethod = new ThreadLocal<String>();
	public static RemoteWebDriver driver = null;
	public static String browserName;
	public static String browserChannel;

	public static void baseBeforeClass(String browserName1, String browserChannel1) throws Exception {
		browserName = browserName1;
		browserChannel = browserChannel1;

	}

	public static RemoteWebDriver getDriver() throws Exception {

		if (driver == null) {
			try {
				driver = Library.startWebDriver(driver, browserName);
			} catch (Exception e) {
				Assert.fail();
			}
		}
		return driver;

	}
	
	public static RemoteWebDriver closeDriver() throws Exception {
		if (driver != null) {
			driver.close();
			driver.quit();
			driver=null;
		}
		return driver;
	}

}
