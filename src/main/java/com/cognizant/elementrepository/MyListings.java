package com.cognizant.elementrepository;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MyListings {

	private WebDriver driver;
	private WebDriverWait wait;

	public MyListings(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		PageFactory.initElements(driver, this);
	}

	public void waitUntilLoaded() {
		wait.until(ExpectedConditions.urlContains("/my-listings"));
		wait.until(ExpectedConditions.or(
				ExpectedConditions.presenceOfElementLocated(By.id("my-listings-grid")),
				ExpectedConditions.presenceOfElementLocated(By.id("my-listings-empty"))));
	}

	public WebElement getListingCardByPgName(String pgName) {
		String xpath = "//div[contains(@class,'pg-card')][.//div[contains(@class,'pg-name') and normalize-space()='"
				+ pgName + "']]";

		return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
	}

	public boolean isListingDisplayed(String pgName) {
		try {
			return getListingCardByPgName(pgName).isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

	public String getListingCardText(String pgName) {
		return getListingCardByPgName(pgName).getText();
	}

	public boolean verifyBasicListingDetails(String pgName, String officeCampus, String rent) {
		String cardText = getListingCardText(pgName).replace(",", "");

		return cardText.contains(pgName)
				&& cardText.contains(officeCampus)
				&& cardText.contains(rent)
				&& cardText.contains("AVAILABLE")
				&& cardText.contains("beds vacant")
				&& cardText.toLowerCase().contains("expires");
	}
	
	public boolean hasEditAndDeleteButtons(String pgName) {
		WebElement card = getListingCardByPgName(pgName);

		return card.findElements(By.cssSelector("[id^='my-listing-edit-btn-']")).size() > 0
				&& card.findElements(By.cssSelector("[id^='my-listing-delete-btn-']")).size() > 0;
	}
	
	public void clickEditButton(String pgName) {
		WebElement card = getListingCardByPgName(pgName);
		card.findElement(By.cssSelector("[id^='my-listing-edit-btn-']")).click();
	}
	
	public void clickDeleteButton(String pgName) {
		WebElement card = getListingCardByPgName(pgName);
		card.findElement(By.cssSelector("[id^='my-listing-delete-btn-']")).click();
	}

	public boolean isDeleteConfirmationDisplayed(String pgName) {
		WebElement card = getListingCardByPgName(pgName);
		return card.findElements(By.cssSelector("[id^='my-listing-delete-confirm-']")).size() > 0;
	}

	public void confirmDelete(String pgName) {
		WebElement card = getListingCardByPgName(pgName);
		card.findElement(By.cssSelector("[id^='my-listing-delete-confirm-']")).click();

		String xpath = "//div[contains(@class,'pg-card')][.//div[contains(@class,'pg-name') and normalize-space()='"
				+ pgName + "']]";

		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(xpath)));
	}
}
