package tests;

import java.util.HashMap;

import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import businessfunctions.DashboardBF;
import orep.DashboardOR;
import utilis.Library;

public class Dashboard extends Annotations {
	
	public static int noOfSecs =20;
	HashMap<String, String> testData;

	@Test(enabled = true, priority = 1)
	@Parameters({ "browserName" })
	public void Dashboard_TC001(String browserName) throws Exception {
		try {
			String query = "SELECT * FROM [login$] Where UserType='CustomerFrontEnd'";
			String finenameAndLocation = "TestData\\TestData.xls";
			testData = Library.getDataFromExcel(query, finenameAndLocation);
			System.out.println("testData " +testData);
			DashboardBF.login(driver, testData.get("userId1"), testData.get("password1"));

		} catch (Exception e) {

		} finally {
		}
	}

}
