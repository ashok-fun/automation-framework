package orep;

import utilis.Constants;

public class DashboardOR {
	
	public static final String customerFrontEndLogin = Constants.XPATH + "~" + "//*[@id=\"Main\"]/section[1]/div/div/div[2]/div/div/div[2]/div/div/div[1]/div/a/small";
	public static final String userID = Constants.XPATH + "~" + "//input[@type='email' and @placeholder='Email']";
	public static final String password = Constants.XPATH + "~" + "//input[@type='password' and @placeholder='Password']";
	public static final String login = Constants.XPATH + "~" + "//span[@class='ladda-label' and contains(text(),'Login')]";
	public static final String home = Constants.XPATH + "~" + "//a[@title='home' and contains(text(),'Home')]";
	

}
