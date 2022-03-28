package tests;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;

import utilis.Library;

public class Annotations {
	RemoteWebDriver driver = null;

	@BeforeSuite(alwaysRun = true)
	public void beforeSuite() throws Exception {
		System.out.println("Running before suite");
		Library.killdrivers();
		Library.beforeSuite();
	}

	@BeforeClass(alwaysRun = true)
	public void beforeClass() throws Exception {
		System.out.println("Running before class");
	}

	@BeforeMethod(alwaysRun = true)
	@Parameters({ "browserName", "browserChannel" })
	public void befreMethod(String browserName, String browserChannel) {

		System.out.println("Running before method");
		driver = Library.startWebDriver(driver, browserName);
	}

}
