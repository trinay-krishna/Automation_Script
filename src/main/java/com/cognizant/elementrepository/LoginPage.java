package com.cognizant.elementrepository;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage{
	
	public WebDriver driver;
	
	public LoginPage(WebDriver driver) {
		PageFactory.initElements(driver, this);
		this.driver = driver;
	}
	
	@FindBy(id="login-email")
	private WebElement emailTextField;
	
	@FindBy(id="login-password")
	private WebElement passwordTextField;
	
	@FindBy(id="login-submit-btn")
	private WebElement submitButton;
	
	public WebElement getEmailTextField() {
		return this.emailTextField;
	}
	
	public WebElement getPasswordTextField() {
		return this.passwordTextField;
	}
	
	public HomePage login(String username, String password) {
		emailTextField.sendKeys(username);
		passwordTextField.sendKeys(password);
		
		submitButton.click();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		return new HomePage(driver);
		
	}
	

	

}
