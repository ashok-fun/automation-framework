package testRunner;

import java.lang.reflect.Method;

import org.junit.runner.RunWith;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

import io.cucumber.junit.Cucumber;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import utilis.Library;

@RunWith(Cucumber.class)
@CucumberOptions(features = { "src/test/resources/features/Dashboard.feature" }, glue = {
		"stepdefinitions" }, tags = "@tag1")

public class DashboardRunner extends AbstractTestNGCucumberTests {

	@BeforeSuite
	public void beforeSuite() throws Exception {
		Library.killdrivers();
		Library.beforeSuite();

	}

	@Parameters({ "browserName", "browserChannel" })
	@BeforeClass
	public void beforeClass(Method method, String browserName1, String browserChannel) throws Exception {
		WebTestBase.baseBeforeClass(browserName1, browserChannel);

	}

	@BeforeTest
	public void beforeTest() throws Exception {

	}

	@AfterSuite
	public void afterSuite() throws Exception {

	}

	@AfterClass
	public void afterClass() throws Exception {

	}

	@AfterTest
	public void afterTest() throws Exception {

	}

}
