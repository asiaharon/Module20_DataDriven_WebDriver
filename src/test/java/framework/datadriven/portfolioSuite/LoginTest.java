package framework.datadriven.portfolioSuite;

import java.util.Hashtable;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import framework.datadriven.TestBase;
import framework.datadriven.util.Constants;
import framework.datadriven.util.TestDataProvider;

public class LoginTest extends TestBase{
	
	@BeforeTest
	public void beforeTest(){
		
		initializeLogs(this.getClass());
		
	}
	
	@Test(dataProviderClass=TestDataProvider.class,dataProvider="DataProvider_Suite_PortFolio")
	public void loginTest(Hashtable<String,String> table){
		
		validateRunmodes("LoginTest", Constants.PORTFOLIO_SUITE, table.get("Runmode"));
		APPLICATION_LOG.debug("Executing: test1");
		
		openBrowser(table.get(Constants.BROWSER_COL));
		navigate("testSiteURL");
		
		Assert.assertTrue(isElementPresent("money_link_xpath"), "Element not found - 'money_link_xpath'");
		click("money_link_xpath");
		
		Assert.assertTrue(isElementPresent("myProtfolio_link_xpath"), "Element not found - 'myProtfolio_link_xpath'");
		click("myProtfolio_link_xpath");
		
		Assert.assertTrue(verifyTitle("portfolioLoginPage_txt"), "Title do not much");
		input("loginUsername_input_xpath",table.get(Constants.USERNAME_COL));
		click("continueEmail_btn_xpath");
		
		input("loginPassword_input_xpath",table.get(Constants.PASSWORD_COL));
		click("rememberMe_chk_xpath");
		click("continueLogin_btn_xpath");
		
		// Expected result
		boolean isSignoutLinkExist = isElementPresent("signout_link_xpath");
		
		if(!(((table.get(Constants.EXPECTED_RESULT_COL).equals("SUCCESS")) && isSignoutLinkExist)))
				Assert.fail("Not able to login with correct credentials");			
		else if(table.get(Constants.EXPECTED_RESULT_COL).equals("FAILURE")){
			if(isSignoutLinkExist){
				Assert.fail("Logged in with wrong credentials");			
			}
		}
	}
	
	@AfterMethod
	public void close(){
		
		quit();
		
	}
	
}
