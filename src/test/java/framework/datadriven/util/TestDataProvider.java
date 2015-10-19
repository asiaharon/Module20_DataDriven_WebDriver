package framework.datadriven.util;

import java.lang.reflect.Method;

import org.testng.annotations.DataProvider;

import framework.datadriven.TestBase;

public class TestDataProvider extends TestBase{
	
	// #############################   G E T    D A T A    S U I T E    P O R T F O L I O   ########################################
	@DataProvider(name="DataProvider_Suite_PortFolio")
	public static Object[][] getDataSuitePortFolio(Method m){
		
		init();
		Xls_Reader xlsFile = new Xls_Reader(System.getProperty("user.dir") + prop.getProperty("xlsFileLocation")+ Constants.PORTFOLIO_SUITE + ".xlsx");

		return Utility.getData(m.getName(), xlsFile);
		
	}
	
	// ##################################   G E T    D A T A    S U I T E    S T O C K   ############################################
	@DataProvider(name="DataProvider_Suite_Stock")
	public static Object[][] getDataSuiteStock(Method m){
		
		init();
		Xls_Reader xlsFile = new Xls_Reader(System.getProperty("user.dir") + prop.getProperty("xlsFileLocation") + Constants.STOCK_SUITE + ".xlsx");

		return Utility.getData(m.getName(), xlsFile);
		
	}
	
	// ########################################   G E T    D A T A    S U I T E    C   ############################################
	@DataProvider(name="DataProvider_SuiteC")
	public static Object[][] getDataSuiteC(Method m){
		
		init();
		Xls_Reader xlsFile = new Xls_Reader(System.getProperty("user.dir") + prop.getProperty("xlsFileLocation") + Constants.THIRD_SUITE + ".xlsx");

		return Utility.getData(m.getName(), xlsFile);
		
	}
	

}
