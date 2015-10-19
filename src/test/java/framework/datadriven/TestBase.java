package framework.datadriven;

import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.SkipException;

import framework.datadriven.util.Constants;
import framework.datadriven.util.Utility;
import framework.datadriven.util.Xls_Reader;

public class TestBase {

	public static Properties prop;
	public static Properties or;
	public static Logger APPLICATION_LOG = Logger.getLogger("devpinoyLogger");
	public RemoteWebDriver driver; // For Grid
	//public WebDriver driver;

	// #######################################################   I N I T   #######################################################
	public static void init(){
        
		// Load project properties file
		if(prop == null){
			String path=System.getProperty("user.dir")+"\\src\\test\\resources\\project.properties";
			prop = new Properties();

			try {
				FileInputStream fs = new FileInputStream(path);
				prop.load(fs);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// Load object repository properties file
		if(or == null){
			String path=System.getProperty("user.dir")+"\\src\\test\\resources\\OR.properties";
			or = new Properties();

			try {
				FileInputStream fs = new FileInputStream(path);
				or.load(fs);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	// ###########################################   I N I T I A L I Z E   L O G S   ########################################
	// This function overrides the application.log creation and creates instead a log file for each test 
	public void initializeLogs(Class<?> class1){

		FileAppender appender = new FileAppender();
		// configure the appender here, with file location, etc
		appender.setFile(System.getProperty("user.dir")+ "//reports//xls reports//" + CustomListener.xlsReportFolderName + "//" + class1.getName() + ".log");
		appender.setLayout(new PatternLayout("%d %-5p [%c{1}] %m%n"));
		appender.setAppend(false);
		appender.activateOptions();

		APPLICATION_LOG = Logger.getLogger(class1);
		APPLICATION_LOG.setLevel(Level.DEBUG);
		APPLICATION_LOG.addAppender(appender);
		
	}


	// ###########################################   V A L I D A T E    R U N M O D E S   ########################################
	public void validateRunmodes(String testName, String suiteName, String dataRunmode){

		APPLICATION_LOG.debug("Validating runmode: for '" + testName + "' in suite '" + suiteName + "'");

		init();

		//suite runmode
		boolean suiteRunmode = Utility.isSuiteRunnable(suiteName, new Xls_Reader(System.getProperty("user.dir") + prop.getProperty("xlsFileLocation") + "Suite.xlsx"));
		boolean testRunmode=Utility.isTestCaseRunnable(testName, new Xls_Reader(System.getProperty("user.dir") + prop.getProperty("xlsFileLocation") + suiteName + ".xlsx"));
		boolean dataSetRunmode=false;

		if(dataRunmode.equals(Constants.RUNMODE_YES))
			dataSetRunmode=true;

		if(!(suiteRunmode && testRunmode && dataSetRunmode)){
			APPLICATION_LOG.debug("Skipping: the testcase '" + testName + "' inside the suite '"+ suiteName + "'");
			throw new SkipException("Skipping: the testcase '" + testName + "' inside the suite '"+ suiteName + "'");
		}

	}

	/********************************************   G E N E R I C    F U N C T I O N S   *****************************************/

	// ##############################################   O P E N    B R O W S E R    ###############################################
	public void openBrowser(String browserType){
		
		/*if(browserType.equals("Mozilla")){
			ProfilesIni allProfiles = new ProfilesIni();
			FirefoxProfile profile = allProfiles.getProfile("Selenium_User");
			driver= new FirefoxDriver(profile);
		}else if(browserType.equals("Chrome")){
			System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + prop.getProperty("chromeDriverLocation"));
			driver = new ChromeDriver();
		}
		
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		*/ 
		
		// For Grid2 support
		
		try{
			DesiredCapabilities cap = new DesiredCapabilities();
			
			if(browserType.equals("Mozilla")){
				cap.setBrowserName("firefox");
				cap.setPlatform(Platform.ANY);
			}else if(browserType.equals("Chrome")){
				cap.setBrowserName("chrome");
				cap.setPlatform(Platform.ANY);// iexplore
			}
			
			try{
				driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"),cap);
			}catch(MalformedURLException e) {
				e.printStackTrace();
			}
			
			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			
		}catch(Exception e){
			APPLICATION_LOG.debug("Not able to open browser - " + e.getMessage());
			Assert.fail("Not able to open browser - " + e.getMessage());
		}
		
	}

	// ##################################################   N A V I G A T E   ###################################################
	public void navigate(String URLKey){

		try{
			driver.get(prop.getProperty(URLKey));
		}catch(Exception e){
			APPLICATION_LOG.debug("FAILED: navigate function: not openned - " + URLKey);
			Assert.fail("FAILED: navigate function: not openned - " + URLKey);
		}

	}

	// ####################################################   C L I C K   ########################################################
	public void click(String identifier){

		try{
			if(identifier.endsWith("_xpath"))
				driver.findElement(By.xpath(or.getProperty(identifier))).click();
			else if(identifier.endsWith("_id"))
				driver.findElement(By.id(or.getProperty(identifier))).click();
			else if(identifier.endsWith("_name"))
				driver.findElement(By.name(or.getProperty(identifier))).click();
		}catch(NoSuchElementException e){
			APPLICATION_LOG.debug("FAILED: click function: Element not found - " + identifier);
			Assert.fail("FAILED: click function: Element not found - " + identifier);
		}

	}

	// ######################################################   I N P U T   ######################################################
	public void input(String identifier,String data){

		try{
			if(identifier.endsWith("_xpath"))
				driver.findElement(By.xpath(or.getProperty(identifier))).sendKeys(data);
			else if(identifier.endsWith("_id"))
				driver.findElement(By.id(or.getProperty(identifier))).sendKeys(data);
			else if(identifier.endsWith("_name"))
				driver.findElement(By.name(or.getProperty(identifier))).sendKeys(data);
		}catch(NoSuchElementException e){
			APPLICATION_LOG.debug("FAILED: input function: Element not found - "+ identifier );
			Assert.fail("FAILED: input function: Element not found - "+ identifier);
		}

	}

	// ##############################################   V E R I F Y    T I T L E   #############################################
	public boolean verifyTitle(String expectedTitleKey){

		String expectedTitle = or.getProperty(expectedTitleKey);
		String actualTitle = driver.getTitle();

		if(expectedTitle.equals(actualTitle))
			return true;
		else
			APPLICATION_LOG.debug("FAILED: Function verifyTitle - Expected:  " + expectedTitle + " Actual: " + actualTitle );
		return false;
	}

	// #########################################   I S    E L E M E N T    P R E S E N T   ######################################
	public boolean isElementPresent(String identifier){

		int size=0;

		if(identifier.endsWith("_xpath"))
			size = driver.findElements(By.xpath(or.getProperty(identifier))).size();
		else if(identifier.endsWith("_id"))
			size = driver.findElements(By.id(or.getProperty(identifier))).size();
		else if(identifier.endsWith("_name"))
			size = driver.findElements(By.name(or.getProperty(identifier))).size();
		else // not in prop file
			size=driver.findElements(By.xpath(identifier)).size();

		if(size>0)
			return true;
		else
			APPLICATION_LOG.debug("FAILED: Function isElementPresent- The element '" + identifier + "' does not exist!" );
		return false;
	}

	// ##################################################   G E T    T E X T   #################################################
	public String getText(String identifier){

		String  text="";

		try{
			if(identifier.endsWith("_xpath"))
				text = driver.findElement(By.xpath(or.getProperty(identifier))).getText();
			else if(identifier.endsWith("_id"))
				text = driver.findElement(By.id(or.getProperty(identifier))).getText();
			else if(identifier.endsWith("_name"))
				text = driver.findElement(By.name(or.getProperty(identifier))).getText();
		}catch(NoSuchElementException e ){
			APPLICATION_LOG.debug("FAILED: getText function: Element not found - " + identifier);
		}

		return text;

	}

	// ######################################################   Q U I T   #######################################################
	public void quit(){

		if(driver!=null){
			driver.quit();
			driver=null;
		}
	}

	/*********************************   A P P L I C A T I O N    S P E C I F I C    F U N C T I O N S   ************************/

	// ##################################################   D O    L O G I N   ##################################################
	public void doLogin(String browser,String username,String password) throws InterruptedException{

		openBrowser(browser);
		navigate("testSiteURL");

		Assert.assertTrue(isElementPresent("money_link_xpath"), "Element not found - 'money_link_xpath'");
		click("money_link_xpath");

		Assert.assertTrue(isElementPresent("myProtfolio_link_xpath"), "Element not found - 'myProtfolio_link_xpath'");
		click("myProtfolio_link_xpath");

		Thread.sleep(5000L);
		//Assert.assertTrue(verifyTitle("portfolioLoginPage_txt"), "Title do not much");
		input("loginUsername_input_xpath", username);
		Thread.sleep(3000L);
		click("continueEmail_btn_xpath");

		input("loginPassword_input_xpath",password);
		Thread.sleep(3000L);
		click("rememberMe_chk_xpath");
		click("continueLogin_btn_xpath");

	}

	// ########################################   D E F A U L T    D O    L O G I N   ############################################
	public void doDefaultLogin(String browser) throws InterruptedException{

		doLogin(browser, prop.getProperty("defaultUsername"), prop.getProperty("defaultPassword"));

	}
}
