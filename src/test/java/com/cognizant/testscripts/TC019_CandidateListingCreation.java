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

public class TC019_CandidateListingCreation extends BaseClass {

	@Test(groups = {"candidate", "Positive Testing", "Functional Testing"})
	public void TC019_Candidate_Listing_Creation() {
		CreateListing createListing = openCreateListingPage();
		String uniquePgName = fillMandatoryCandidateDetails(createListing);

		MyListings myListings = publishAndWaitForMyListings(createListing);

		Assert.assertTrue(myListings.isListingDisplayed(uniquePgName),
				"Candidate-created listing should be displayed in My Listings.");
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

	private String fillMandatoryCandidateDetails(CreateListing createListing) {
		String uniquePgName = "Candidate Test PG " + System.currentTimeMillis();

		createListing.clearAndType(createListing.getPgNameTextField(), uniquePgName);
		createListing.clearAndType(createListing.getLocalityAndLandmarkTextField(), "Near Navalur");

		new Select(createListing.getCityDropdown()).selectByVisibleText("Chennai");
		new Select(createListing.getOfficeAreaDropdown()).selectByVisibleText("Siruseri");
		new Select(createListing.getSharingTypeDropdown()).selectByVisibleText("Double (2 beds)");
		new Select(createListing.getVacantBedsDropdown()).selectByVisibleText("1 of 2");
		new Select(createListing.getPgTypeDropdown()).selectByVisibleText("Co-Living PG (anyone)");

		createListing.clearAndType(createListing.getRentTextField(), "8000");

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
