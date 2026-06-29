package com.cognizant.base;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.cognizant.util.FrameworkConstants;
import com.cognizant.util.PropertyFileReader;

public class BasePage implements FrameworkConstants {
	
//	public static WebDriver driver;
//	public PropertyFileReader readFromPropertyFile;
//
//	@Parameters("browser")
//	@BeforeClass(alwaysRun = true)
//	public void openTheBrowser(@Optional("chrome") String browserName) {
//		if (browserName.equalsIgnoreCase("chrome")) {
//			driver = new ChromeDriver();
//			Reporter.log("Successfully Launched Chrome Browser", true);
//		} else if (browserName.equalsIgnoreCase("edge")) {
//			driver = new EdgeDriver();
//			Reporter.log("Successfully Launched Firefox Browser", true);
//		} else {
//			Reporter.log("Enter valid Browser name");
//		}
//		driver.manage().window().maximize();
//		Reporter.log("Browser window is maximized successfully", true);
//		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(IMPLICIT_TIMEOUT));
//	}
//	
//
//	@BeforeMethod(alwaysRun = true)
//	public void loginToApplication() {
//		readFromPropertyFile = new PropertyFileReader();
//		String url = readFromPropertyFile.getValueProperty("url");
//		String username = readFromPropertyFile.getValueProperty("username");
//		String password = readFromPropertyFile.getValueProperty("password");
//
//		driver.get(url);
//		loginPage = new LoginPage(driver);
//		homePage = loginPage.login(username, password);
//	}
//
//	@AfterMethod(alwaysRun = true)
//	public void logoutOfApplication() {
//		homePage.logout();
//	}
//
//	@AfterClass(alwaysRun = true)
//	public void closeTheBrowser() {
//		driver.quit();
//	}

}
