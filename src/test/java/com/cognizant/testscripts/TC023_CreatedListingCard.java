package com.cognizant.testscripts;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.cognizant.base.BaseClass;
import com.cognizant.elementrepository.CreateListing;
import com.cognizant.elementrepository.MyListings;

public class TC023_CreatedListingCard extends BaseClass {

	@Test(groups = {"UI Testing", "Functional Testing", "Positive Testing"})
	public void TC023_Created_Listing_Card() {
		String uniquePgName = createEmployeeListing("Test PG Card", "8000");

		MyListings myListings = new MyListings(driver);
		myListings.waitUntilLoaded();

		Assert.assertTrue(myListings.verifyBasicListingDetails(uniquePgName, "Chennai - Siruseri", "8000"),
				"Listing card should show name, location, rent, status, vacant beds, and expiry.");

		Assert.assertTrue(myListings.hasEditAndDeleteButtons(uniquePgName),
				"Listing card should have Edit and Delete buttons.");
	}

	private CreateListing openCreateListingPage() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		if (!driver.getCurrentUrl().contains("/create-listing")) {
			WebElement createListingLink = wait.until(
					ExpectedConditions.elementToBeClickable(By.id("nav-create-listing-link")));
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", createListingLink);
		}

		CreateListing createListing = new CreateListing(driver);
		createListing.waitUntilLoaded();
		return createListing;
	}

	private String createEmployeeListing(String pgName, String rent) {
		CreateListing createListing = openCreateListingPage();
		String uniquePgName = fillMandatoryEmployeeDetails(createListing, pgName, rent);

		MyListings myListings = publishAndWaitForMyListings(createListing);
		Assert.assertTrue(myListings.isListingDisplayed(uniquePgName),
				"Created listing should be displayed in My Listings.");

		return uniquePgName;
	}

	private String fillMandatoryEmployeeDetails(CreateListing createListing, String pgName, String rent) {
		String uniquePgName = pgName + " " + System.currentTimeMillis();

		createListing.clearAndType(createListing.getPgNameTextField(), uniquePgName);
		createListing.clearAndType(createListing.getLocalityAndLandmarkTextField(), "Near Navalur");

		new Select(createListing.getCityDropdown()).selectByVisibleText("Chennai");
		new Select(createListing.getOfficeAreaDropdown()).selectByVisibleText("Siruseri");
		new Select(createListing.getSharingTypeDropdown()).selectByVisibleText("Double (2 beds)");
		new Select(createListing.getVacantBedsDropdown()).selectByVisibleText("1 of 2");
		new Select(createListing.getPgTypeDropdown()).selectByVisibleText("Co-Living PG (anyone)");

		createListing.clearAndType(createListing.getRentTextField(), rent);

		new Select(createListing.getFoodRatingDropdown()).selectByIndex(4);
		new Select(createListing.getServiceRatingDropdown()).selectByIndex(4);

		createListing.clearAndType(createListing.getFoodReviewTextArea(), "Very good.");
		createListing.clearAndType(createListing.getServiceReviewTextArea(), "Very good.");

		return uniquePgName;
	}

	private MyListings publishAndWaitForMyListings(CreateListing createListing) {
		createListing.getPublishListingButton().click();

		if (driver.getCurrentUrl().contains("/create-listing")) {
			String createError = createListing.getCreateErrorText();
			if (!createError.trim().isEmpty()) {
				System.out.println("Create error: " + createError);
			}
		}

		new WebDriverWait(driver, Duration.ofSeconds(10))
				.until(ExpectedConditions.urlContains("/my-listings"));

		MyListings myListings = new MyListings(driver);
		myListings.waitUntilLoaded();
		return myListings;
	}
}
