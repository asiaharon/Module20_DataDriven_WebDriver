package framework.datadriven.stockSuite;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import framework.datadriven.TestBase;
import framework.datadriven.util.Constants;
import framework.datadriven.util.ErrorUtil;
import framework.datadriven.util.TestDataProvider;

public class AddStockTest extends TestBase{
	
	@BeforeTest
	public void beforeTest(){
		
		initializeLogs(this.getClass());
		
	}
	
	@Test(dataProviderClass=TestDataProvider.class,dataProvider="DataProvider_Suite_Stock")
	public void addStock(Hashtable<String,String> table) throws InterruptedException{

		validateRunmodes("AddStock", Constants.STOCK_SUITE, table.get("Runmode"));
		APPLICATION_LOG.debug("Executing: addStockTest");

		doDefaultLogin(table.get("Browser"));

		try{
			Assert.assertTrue(verifyTitle("loginPageTitle"), "Titles did not match");
		}catch(Throwable t){
			ErrorUtil.addVerificationFailure(t);
		}

		click("addStock_btn_id");
		//driver.findElement(By.xpath(prop.getProperty("addStock_btn_id"))).sendKeys(Keys.ENTER);

		input("stockName_input_id", table.get("Stock Name"));

		click("openCalendar_link_id");

		//Select date
		String date=table.get("PurchaseDate");

		Date currentDate = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		Date dateToBeSelected =null;

		try {
			dateToBeSelected = formatter.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		String month = new SimpleDateFormat("MMMM").format(dateToBeSelected);		
		Calendar cal = Calendar.getInstance();
	    cal.setTime(dateToBeSelected);
	    int year = cal.get(Calendar.YEAR);
	    int day = cal.get(Calendar.DAY_OF_MONTH);
	    String month_yearExpected = month + " 20" + year;
	    
		while(true){
			
			String month_yearDisplayed = getText("monthAndYear_txt_xpath");
			if(month_yearDisplayed.equals(month_yearExpected))
				break; // correct month
			
			if(currentDate.after(dateToBeSelected))
				click("calBack_btn_xpath");
			else
				click("calFront_btn_xpath");
		}
		
		driver.findElement(By.xpath("//td[text()='"+ day + "']")).click();

		input("stockQuantity_input_id", table.get("Quantity"));
		input("purchagePrice_input_id", table.get("Price"));
	}
	
	@AfterMethod
	public void close(){
		
		quit();
		
	}

}
