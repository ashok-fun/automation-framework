package utilis;

import java.io.FileInputStream;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

public class Library {
	

	public static String driverPath = System.getProperty("user.dir").toString() + "\\Drivers";
	public static Properties EnvConfig = new Properties();
	public static String URL ="";

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
		FileInputStream file1 = new FileInputStream("/testAutomation/src/test/resources/UAT/testAutomation.properties");
		EnvConfig.load(file1);
	}
	
	public static void killdrivers() throws Exception{
		Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe");
		Runtime.getRuntime().exec("taskkill /F /IM geckodriver.exe");
		Runtime.getRuntime().exec("taskkill /F /IM msedgedriver.exe");
		
		
	}
	}


