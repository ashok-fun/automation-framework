package stepdefinitions;

import java.util.HashMap;

import businessfunctions.DashboardBF;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import testRunner.WebTestBase;
import utilis.Library;

public class DashboardSD extends WebTestBase{
	HashMap<String, String> testData;
	
	
	@Given("Login to application")
	public void login_to_application() throws Exception {
		String query = "SELECT * FROM [login$] Where UserType='CustomerFrontEnd'";
		String finenameAndLocation = "TestData\\TestData.xls";
		testData = Library.getDataFromExcel(query, finenameAndLocation);
		System.out.println("testData " +testData);
		driver=WebTestBase.getDriver();
		DashboardBF.login(driver, testData.get("userId1"), testData.get("password1"));
	    
	}

	@When("Navigate to Dashboard page")
	public void navigate_to_dashboard_page() {
		System.out.println("Navigate to Dashboard page");
	    
	}

	@Then("Validate the Dashboard page")
	public void validate_the_dashboard_page() {
		System.out.println("Validate the Dashboard page");
	    
	}
	
	
	@Then("Logff from the Application")
	public void logff_from_the_application() throws Exception {
	    DashboardBF.logoff(driver);
	    driver=WebTestBase.closeDriver();
	    throw new io.cucumber.java.PendingException();
	}

	@Given("Login to application with <UserType>")
	public void login_to_application_with_user_type() {
	    // Write code here that turns the phrase above into concrete actions
	    throw new io.cucumber.java.PendingException();
	}

}
