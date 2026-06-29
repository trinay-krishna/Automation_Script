package com.cognizant.elementrepository;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.cognizant.base.BaseClass;

public class HomePage extends BaseClass {
	

	public HomePage(WebDriver driver) {
		PageFactory.initElements(driver, this);
	}
	
	@FindBy(id="nav-avatar")
	private WebElement profileIcon;
	
	@FindBy(id="nav-logout-btn")
	private WebElement logoutButton;
	
	@FindBy(id = "nav-create-listing-link")
	private WebElement createListingButton;
	
	
	@FindBy(id="nav-my-listings-link")
	private WebElement myListingsButton;
	
	public WebElement getCreateListingButton() {
		return this.createListingButton;
	}
	
	public WebElement getListingButton() {
		return this.myListingsButton;
	}
	
	
	public void logout() {
		profileIcon.click();
		logoutButton.click();
	}
}
