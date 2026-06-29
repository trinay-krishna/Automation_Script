package com.cognizant.util;

public interface FrameworkConstants {
	
	String PROPERTYFILE_PATH = System.getProperty("user.dir") + "/src/test/resources/ConfigProperty/config.properties";
	
	int IMPLICIT_TIMEOUT = 10;
	int EXPLICIT_TIMEOUT = 10;
	
	String EXCEL_PATH = System.getProperty("user.dir") + "/src/test/resources/TestData/TestData.xlsx";
	
	String SCREENSHOT_PATH = System.getProperty("user.dir") + "/Errorshots";
}	
