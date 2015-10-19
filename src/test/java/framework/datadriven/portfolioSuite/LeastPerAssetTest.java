package framework.datadriven.portfolioSuite;

import java.util.Hashtable;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import framework.datadriven.TestBase;
import framework.datadriven.util.Constants;
import framework.datadriven.util.TestDataProvider;

public class LeastPerAssetTest extends TestBase{
	
	@BeforeTest
	public void beforeTest(){
		
		initializeLogs(this.getClass());
		
	}
	
	@Test(dataProviderClass=TestDataProvider.class,dataProvider="DataProvider_Suite_PortFolio")
	public void leastPerAssetTest(Hashtable<String,String> table) throws InterruptedException{
		
		validateRunmodes("LeastPerAssetTest", Constants.PORTFOLIO_SUITE, table.get("Runmode"));
		APPLICATION_LOG.debug("Executing: leastPerAssetTest");
		
		doDefaultLogin(table.get(Constants.BROWSER_COL));
		
		String leastPerAsset = getText("leastPerAsset_txt_xpath");
		String temp[] = leastPerAsset.split("\\(");
		String companyName = temp[0].trim();
		String percentageChange = temp[1].split("\\)")[0].split("%")[0];
		
		Assert.assertTrue(isElementPresent("//a[text()='"+companyName+"']"), "Lease per asset company not found in table "+ companyName);
		Assert.assertTrue(isElementPresent("//td/span[text()='"+percentageChange+"']"), "xxx");
		
		// searching inside table
		
	}
	
	@AfterMethod
	public void close(){
		
		quit();
		
	}
}
