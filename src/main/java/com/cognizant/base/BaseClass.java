package com.cognizant.base;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.Arrays;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;
import org.testng.SkipException;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.cognizant.elementrepository.HomePage;
import com.cognizant.elementrepository.LoginPage;
import com.cognizant.util.FrameworkConstants;
import com.cognizant.util.PropertyFileReader;

// Browser Setup and TearDown code.
public class BaseClass implements FrameworkConstants {
	
	public static WebDriver driver;
	public WebDriverWait explicitWait;
	public PropertyFileReader readFromPropertyFile;
	public LoginPage loginPage;
	public HomePage homePage;

	@Parameters("browser")
	@BeforeSuite(alwaysRun = true)
	public void openTheBrowser(@Optional("chrome") String browserName) {
		if (browserName.equalsIgnoreCase("chrome")) {

			driver = new ChromeDriver();
			Reporter.log("Successfully Launched Chrome Browser", true);
		} else if (browserName.equalsIgnoreCase("edge")) {
			driver = new EdgeDriver();
			Reporter.log("Successfully Launched Firefox Browser", true);
		} else {
			Reporter.log("Enter valid Browser name");
		}
		driver.manage().window().maximize();
		Reporter.log("Browser window is maximized successfully", true);
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(IMPLICIT_TIMEOUT));
	}
	

	@BeforeMethod(alwaysRun = true)
	public void loginToApplication(Method method) {
		homePage = null;
		readFromPropertyFile = new PropertyFileReader();
		String url = readFromPropertyFile.getValueProperty("url").replaceAll("/$", "");
		String usernameKey = "username";
		String passwordKey = "password";

		Test testAnnotation = method.getAnnotation(Test.class);
		if (testAnnotation != null && Arrays.asList(testAnnotation.groups()).contains("candidate")) {
			usernameKey = "candidateUsername";
			passwordKey = "candidatePassword";
		}

		String username = readFromPropertyFile.getValueProperty(usernameKey);
		String password = readFromPropertyFile.getValueProperty(passwordKey);

		if (isBlank(username) || isBlank(password)) {
			throw new SkipException("Credentials are missing for keys: " + usernameKey + " / " + passwordKey);
		}

		driver.get(url + "/login");
		loginPage = new LoginPage(driver);
		homePage = loginPage.login(username, password);
	}

	private boolean isBlank(String value) {
		return value == null || value.trim().isEmpty();
	}

	@AfterMethod(alwaysRun = true)
	public void logoutOfApplication() {
		if (homePage != null) {
			homePage.logout();
		}
	}

	@AfterSuite(alwaysRun = true)
	public void closeTheBrowser() {
		if (driver != null) {
			driver.quit();
		}
	}

}
