package framework.datadriven.stockSuite;

import java.util.Hashtable;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import framework.datadriven.TestBase;
import framework.datadriven.util.Constants;
import framework.datadriven.util.ErrorUtil;
import framework.datadriven.util.TestDataProvider;

public class MultipleFailures extends TestBase{

	@BeforeTest
	public void beforeTest(){

		initializeLogs(this.getClass());

	}

	@Test(dataProviderClass=TestDataProvider.class,dataProvider="DataProvider_Suite_Stock")
	public void multipleFailures(Hashtable<String,String> table){

		validateRunmodes("MultipleFailures", Constants.STOCK_SUITE, table.get("Runmode"));
		APPLICATION_LOG.debug("Executing: multipleFailures");


		// First error
		try{
			Assert.assertEquals("A1", "B1");
		}catch(Throwable t){
			ErrorUtil.addVerificationFailure(t);			
		}
		
		try{
			Assert.assertEquals("A2", "B2");
		}catch(Throwable t){
			ErrorUtil.addVerificationFailure(t);			
		}
		
		// Second error
		//try{
			//Assert.assertTrue(verifyTitle("wrongTitle_txt"));
		//}catch(Exception e){
			//ErrorUtil.addVerificationFailure(e);			
		//}
		
		Assert.assertEquals("A3", "B3");

	}

}
