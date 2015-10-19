package framework.datadriven.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.ITestResult;
import org.testng.Reporter;

// This class helps to continue a test despite an assertion failure (use try and catch around the assertion
// and call the 'ErrorUtil.addVerificationFailures(e)')
public class ErrorUtil {

	private static Map<ITestResult,List> verificationFailuresMap = new HashMap<ITestResult,List>();
	private static Map<ITestResult,List> skipMap = new HashMap<ITestResult,List>();

	// #################################   A D D    V E R I F I C A T I O N    F A I L U R E   #####################################
	public static void addVerificationFailure(Throwable e) {
		
		List verificationFailures = getVerificationFailures();
		verificationFailuresMap.put(Reporter.getCurrentTestResult(), verificationFailures);
		verificationFailures.add(e);
		
	}
	
	// #################################   G E T    V E R I F I C A T I O N    F A I L U R E   #####################################
	public static List getVerificationFailures() {
		
		List verificationFailures = verificationFailuresMap.get(Reporter.getCurrentTestResult());
		return verificationFailures == null ? new ArrayList() : verificationFailures;
		
	}

}

