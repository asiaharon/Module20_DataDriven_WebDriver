package framework.datadriven.portfolioSuite;

import java.util.Hashtable;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import framework.datadriven.TestBase;
import framework.datadriven.util.Constants;
import framework.datadriven.util.TestDataProvider;

public class Test3 extends TestBase{
	
	@BeforeTest
	public void beforeTest(){
		
		initializeLogs(this.getClass());
		
	}
	
	@Test(dataProviderClass=TestDataProvider.class,dataProvider="DataProvider_Suite_PortFolio")
	public void test3(Hashtable<String,String> table){
		
		validateRunmodes("Test3", Constants.PORTFOLIO_SUITE, table.get("Runmode"));
		APPLICATION_LOG.debug("Executing: test3");
		
	}
	
}
