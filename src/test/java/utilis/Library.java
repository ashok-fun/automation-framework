package utilis;

import java.io.FileInputStream;
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

public class Library {

	public static String driverPath = System.getProperty("user.dir").toString() + "\\Drivers";
	public static Properties EnvConfig = new Properties();
	public static String URL = "";

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

		FileInputStream file1 = new FileInputStream("./src/test/resources/UAT/testAutomation.properties");
		Library.EnvConfig.load(file1);

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
			we.click();
			unHighlight(driver, we);
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
		String returnValue ="";
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
			}else {
				driver.switchTo().window(currentWindow);
			}
		}
	}

}
