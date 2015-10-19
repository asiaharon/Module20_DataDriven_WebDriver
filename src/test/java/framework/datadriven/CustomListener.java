package framework.datadriven;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.TestListenerAdapter;

import framework.datadriven.util.Xls_Reader;
import framework.datadriven.util.Constants;
import framework.datadriven.util.ErrorUtil;



public class CustomListener extends TestListenerAdapter implements IInvokedMethodListener,ISuiteListener{

	public static Hashtable<String,String> resultTable;
	public static ArrayList<String> keys; // index for the hashtable
	public static String xlsReportFolderName;
	public static String xlsReportFilePath;



	// #############################################   O N    T E S T    F A I L U R E   #########################################
	public void onTestFailure(ITestResult tr){
		
		// Report as single error message
		//report(tr.getName(), "FAILED: " + tr.getThrowable().getMessage()); // FAILED: errorMsg
		
		// To support multiple error messages reporting 
		List<Throwable> verificationFailures = ErrorUtil.getVerificationFailures();
		
		verificationFailures.add(tr.getThrowable()); // Add the last assertion failure (invoke without try/catch)
		
		String errMsg="";
		for(int i=0;i<verificationFailures.size();i++){
			// Each error message will be inside []
			errMsg=errMsg+"["+verificationFailures.get(i).getMessage()+"]-";
		}
		report(tr.getName(), "FAILED:" + errMsg); // FAILED: [errorMsg]-[errorMsg]-[errorMsg]

	}


	// #############################################   O N    T E S T    S K I P P E D   #########################################
	public void onTestSkipped(ITestResult tr) {
		System.out.println(tr.getName() +": " + tr.getThrowable().getMessage());
		report(tr.getName(), tr.getThrowable().getMessage());

	}

	// #############################################   O N    T E S T    S U C C E S S   #########################################
	public void onTestSuccess(ITestResult tr){

		report(tr.getName(), "PASSED");

	}

	// ###########################################   A F T E R     I N V O C A T I O N   #########################################
	public void afterInvocation(IInvokedMethod method, ITestResult result) {


	}

	// #########################################   B E F O R E     I N V O C A T I O N   #########################################
	public void beforeInvocation(IInvokedMethod arg0, ITestResult test) {



	}

	// #################################################   O N    S T A R T   ####################################################
	// Invokes when suite starts
	@Override
	public void onStart(ISuite suite) {

		System.out.println("Starting suite:" + suite.getName());

		// Initialize results hashtable to store all test results
		keys = new ArrayList<String>();
		resultTable  = new Hashtable<String,String>();

		// Create xls report if not exists
		if (xlsReportFolderName==null){
			// Create cls report folder
			Date currentDate = new Date();
			xlsReportFolderName = "Report_" + currentDate.toString().replace(":", "_");
			File folder = new File(System.getProperty("user.dir") + "//reports//xls reports//" + xlsReportFolderName);
			folder.mkdirs();

			// Copy xls report template to report folder 
			xlsReportFilePath = System.getProperty("user.dir") + "//reports//xls reports//" + xlsReportFolderName + "//Report.xlsx";
			File src = new File(System.getProperty("user.dir")+"//reports//xls reports//ReportTemplate.xlsx");
			File dest = new File(xlsReportFilePath);

			try {
				FileUtils.copyFile(src, dest);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// ################################################   O N    F I N I S H   ####################################################
	// Invokes when suite ends
	@Override
	public void onFinish(ISuite suite) {

		System.out.println("Finishing suite:" + suite.getName());
		System.out.println(resultTable);

		// Update xls report file with suite results
		if(!suite.getName().equals(Constants.ROOT_SUITE)){
			Xls_Reader xls = new Xls_Reader(xlsReportFilePath);

			// Add suite sheet
			xls.addSheet(suite.getName());

			// create column headers
			xls.setCellData(suite.getName(), 0, 1, "Test Case");
			xls.setCellData(suite.getName(), 1, 1, "Result");

			// Write test results
			for(int i=0; i<keys.size(); i++){
				String key = keys.get(i);
				String result = resultTable.get(key);
				xls.setCellData(suite.getName(), 0, i+2, key);
				xls.setCellData(suite.getName(), 1, i+2, result);
			}

			resultTable = null;
			keys = null;
		}
	}

	// #######################################################   R E P O R T   ###################################################
	public void report(String testName,String testResult){

		int iterationNumber=1; // To support multiple data set

		while(resultTable.containsKey(testName + "_Iteration_" + iterationNumber)){
			iterationNumber++;
		}

		keys.add(testName + "_Iteration_" + iterationNumber);
		resultTable.put(testName + "_Iteration_" + iterationNumber, testResult);

	}

}
