package utilis;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;

public class Library {

	public static String driverPath = System.getProperty("user.dir").toString() + "\\Drivers";
	public static Properties EnvConfig = new Properties();
	public static Properties MainConfig = new Properties();
	public static String env = "";
	public static String URL = "";
	public static String resourceLocation = System.getProperty("user.dir").toString() + "\\src\\test\\resources\\";

	@SuppressWarnings("deprecation")
	public static synchronized RemoteWebDriver startWebDriver(RemoteWebDriver driver, String browserName) {

		if (driver == null) {
			switch (browserName) {

			case "chrome": {
				System.setProperty("webdriver.chrome.driver", driverPath + "\\chromedriver.exe");
				driver = new ChromeDriver();
				break;
			}
			case "firefox": {
				System.setProperty("webdriver.gecko.driver", driverPath + "\\geckodriver.exe");
				driver = new FirefoxDriver();
				break;
			}
			case "edge": {
				System.setProperty("webdriver.edge.driver", driverPath + "\\msedgedriver.exe");
				driver = new EdgeDriver();
				break;
			}

			}

			driver.manage().window().maximize();
			driver.manage().deleteAllCookies();
			// driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
			// driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			driver.get(EnvConfig.getProperty("URL"));

		}
		return driver;
	}

	public static void beforeSuite() throws Exception {

		FileInputStream file1 = new FileInputStream("./src/test/resources/Pointer.properties");
		Library.MainConfig.load(file1);
		Library.env = Library.MainConfig.getProperty("ENV");

		FileInputStream file2 = new FileInputStream("./src/test/resources/UAT/testAutomation.properties");
		Library.EnvConfig.load(file2);

	}

	public static void killdrivers() throws Exception {
		Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe");
		Runtime.getRuntime().exec("taskkill /F /IM geckodriver.exe");
		Runtime.getRuntime().exec("taskkill /F /IM msedgedriver.exe");

	}

	public static boolean sendKeys(RemoteWebDriver driver, String locatorTypeAndValue, String inputText,
			boolean throwException, int noOfSecs) throws Exception {
		WebElement we = getWebElement(driver, locatorTypeAndValue, throwException, noOfSecs);
		if (we != null) {
			highlight(driver, we);
			we.clear();
			we.sendKeys(inputText);
			unHighlight(driver, we);

		}
		return false;

	}

	public static boolean click(RemoteWebDriver driver, String locatorTypeAndValue, boolean throwException,
			int noOfSecs) throws Exception {
		WebElement we = getWebElement(driver, locatorTypeAndValue, throwException, noOfSecs);
		if (we != null) {
			highlight(driver, we);
			unHighlight(driver, we);
			we.click();

			return true;
		} else {
			return false;
		}

	}

	public static boolean clickbyJavaScript(RemoteWebDriver driver, String locatorTypeAndValue, boolean throwException,
			int noOfSecs) throws Exception {
		WebElement we = getWebElement(driver, locatorTypeAndValue, throwException, noOfSecs);
		if (we != null) {
			highlight(driver, we);
			unHighlight(driver, we);
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].click", we);
			return true;
		} else {
			return false;
		}

	}

	public static WebElement getWebElement(RemoteWebDriver driver, String locatorTypeAndValue, boolean throwException,
			int noOfSecs) throws Exception {
		By by = getByObject(locatorTypeAndValue);
		WebElement we;

		try {
			@SuppressWarnings("deprecation")
			WebDriverWait wait = new WebDriverWait(driver, noOfSecs);
			we = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
			return we;
		} catch (Exception ex) {

		}

		return null;

	}

	public static String getText(RemoteWebDriver driver, String locatorTypeAndValue, boolean throwException,
			int noOfSecs) throws Exception {
		String returnValue = "";
		WebElement we = getWebElement(driver, locatorTypeAndValue, throwException, noOfSecs);
		if (we != null) {
			highlight(driver, we);
			returnValue = we.getText().toString();
		}
		return returnValue;

	}

	public static By getByObject(String locatorTypeAndValue) throws Exception {

		By by;
		String locatorType = locatorTypeAndValue.split("~")[0];
		String locatorValue = locatorTypeAndValue.split("~")[1];
		if (locatorType.equals(Constants.CLASSNAME)) {
			by = By.className(locatorValue);
		} else if (locatorType.equals(Constants.CSSSELECTOR)) {
			by = By.cssSelector(locatorValue);
		} else if (locatorType.equals(Constants.LINKTEXT)) {
			by = By.linkText(locatorValue);
		} else if (locatorType.equals(Constants.NAME)) {
			by = By.name(locatorValue);
		} else if (locatorType.equals(Constants.ID)) {
			by = By.id(locatorValue);
		} else if (locatorType.equals(Constants.PARTIALLINKTEXT)) {
			by = By.partialLinkText(locatorValue);
		} else if (locatorType.equals(Constants.TAGNAME)) {
			by = By.tagName(locatorValue);
		} else if (locatorType.equals(Constants.XPATH)) {
			by = By.xpath(locatorValue);
		} else {
			return null;
		}

		return by;
	}

	public static void highlight(RemoteWebDriver driver, WebElement element) throws Exception {

		JavascriptExecutor js = (JavascriptExecutor) driver;
		for (int i = 1; i <= 1; i++) {
			js.executeScript("arguments[0].setAttribute('style', 'background: yellow; border: 2px solid red;');",
					element);
			Thread.sleep(100);
			js.executeScript("arguments[0].setAttribute('style', '');", element);
			Thread.sleep(100);
			js.executeScript("arguments[0].setAttribute('style', 'background: yellow; border: 2px solid red;');",
					element);
			Thread.sleep(100);
		}

	}

	public static void unHighlight(RemoteWebDriver driver, WebElement element) throws Exception {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].setAttribute('style', 'background: yellow; border: 2px solid red;');", element);
		Thread.sleep(100);
		js.executeScript("arguments[0].setAttribute('style', '');", element);

	}

	public static void SwitchWindowByTitle(RemoteWebDriver driver, String windowTitle) {

		String currentWindow = driver.getWindowHandle();
		for (String winHandle : driver.getWindowHandles()) {
			if (driver.switchTo().window(winHandle).getTitle().contains(windowTitle)) {
				break;
			} else {
				driver.switchTo().window(currentWindow);
			}
		}
	}

	public static HashMap<String, String> getDataFromExcel(String query, String finenameAndLocation) throws Exception {

		HashMap<String, String> hm = new HashMap<String, String>();
		Recordset rs;
		ArrayList<String> finaNameList;
		StringBuilder fildNameString = new StringBuilder();
		String fieldName = null, fieldValue = null;
		int fieldCount = 0;

		finenameAndLocation = resourceLocation + env + "\\" + finenameAndLocation;
		System.out.println("finenameAndLocation " + finenameAndLocation);
		if (isNullOrBlank(query) || (isNullOrBlank(finenameAndLocation))) {
			throw new Exception(" query and finenameAndLocation are blank or null");
		}

		if (new File(finenameAndLocation).exists() == false) {
			throw new Exception("finenameAndLocation is does not exists");
		}

		Fillo f = new Fillo();
		query = query.replace("[", "");
		query = query.replace("]", "");
		query = query.replace("$", "");
		System.out.println("query " + query);
		com.codoid.products.fillo.Connection conn = f.getConnection(finenameAndLocation);
		rs = conn.executeQuery(query);

		finaNameList = rs.getFieldNames();

		for (String currentFieldname : finaNameList) {
			fieldCount = fieldCount + 1;
			if (fieldCount == 1) {
				fildNameString.append(currentFieldname);
			} else {
				fildNameString.append(";" + currentFieldname);
			}
		}

		long resultsCount = 1;

		while (rs.next()) {
			for (int i = 0; i < fieldCount; i++) {
				fieldName = finaNameList.get(i);
				fieldValue = (String) rs.getField(i).value();
				if (fieldValue == null) {
					fieldValue = "";
				}
				if (hm.containsKey(fieldName + resultsCount)) {
					resultsCount = resultsCount + 1;
					hm.put(fieldName + resultsCount, fieldValue);
					resultsCount = 1;
				} else {
					hm.put(fieldName + resultsCount, fieldValue);
				}
			}
		}

		if (conn != null) {
			conn.close();
		}
		if (rs != null) {
			rs.close();
		}
		hm.put("RESULTCOUNT", resultsCount + "");
		hm.put("FIELDCOUNT", fieldCount + "");
		hm.put("FIELDNAMES", fildNameString + "");

		return hm;

	}

	public static boolean isNullOrBlank(String ValueToBeChecked) {
		boolean returnValue;
		if (ValueToBeChecked != null) {
			if (!ValueToBeChecked.trim().equals("")) {
				returnValue = false;
			} else {
				returnValue = true;
			}
		} else {
			returnValue = true;
		}
		return returnValue;

	}

}
