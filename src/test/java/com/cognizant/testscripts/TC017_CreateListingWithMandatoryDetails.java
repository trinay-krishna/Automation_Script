package com.cognizant.testscripts;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.cognizant.base.BaseClass;
import com.cognizant.elementrepository.CreateListing;
import com.cognizant.elementrepository.MyListings;
import com.cognizant.util.ExcelUtil;

public class TC017_CreateListingWithMandatoryDetails extends BaseClass {

	@DataProvider(name = "listRoomData")
	public Object[][] getListRoomData() {
		ExcelUtil excel = new ExcelUtil();
		return excel.getMultipleData("ListRoom");
	}

	@Test(
			dataProvider = "listRoomData",
			groups = {"Smoke Testing", "Positive Testing", "Functional Testing"})
	public void TC017_Create_Listing_With_Mandatory_Details(String tcId, String pgName, String locality,
			String city, String officeArea, String sharingType, String vacantBeds, String pgType, String rent,
			String foodRating, String serviceRating, String foodReview, String serviceReview) {

		String uniquePgName = pgName + " " + System.currentTimeMillis();

		CreateListing createListing = openCreateListingPage();
		fillListingDetails(
				createListing,
				uniquePgName,
				locality,
				city,
				officeArea,
				sharingType,
				vacantBeds,
				pgType,
				rent,
				foodRating,
				serviceRating,
				foodReview,
				serviceReview);

		MyListings myListings = publishAndWaitForMyListings(createListing);

		Assert.assertTrue(driver.getCurrentUrl().contains("/my-listings"),
				tcId + " failed: Application should redirect to My Listings.");

		Assert.assertTrue(myListings.isListingDisplayed(uniquePgName),
				tcId + " failed: Created listing should be displayed in My Listings.");

		Assert.assertTrue(myListings.verifyBasicListingDetails(uniquePgName, city + " - " + officeArea, rent),
				tcId + " failed: Listing card details are not correct.");
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

	private void fillListingDetails(CreateListing createListing, String pgName, String locality, String city,
			String officeArea, String sharingType, String vacantBeds, String pgType, String rent,
			String foodRating, String serviceRating, String foodReview, String serviceReview) {

		createListing.clearAndType(createListing.getPgNameTextField(), pgName);
		createListing.clearAndType(createListing.getLocalityAndLandmarkTextField(), locality);

		new Select(createListing.getCityDropdown()).selectByVisibleText(city);
		new Select(createListing.getOfficeAreaDropdown()).selectByVisibleText(officeArea);
		new Select(createListing.getSharingTypeDropdown()).selectByVisibleText(sharingType);
		new Select(createListing.getVacantBedsDropdown()).selectByVisibleText(vacantBeds);
		new Select(createListing.getPgTypeDropdown()).selectByVisibleText(pgType);

		createListing.clearAndType(createListing.getRentTextField(), rent);

		new Select(createListing.getFoodRatingDropdown()).selectByIndex(Integer.parseInt(foodRating) - 1);
		new Select(createListing.getServiceRatingDropdown()).selectByIndex(Integer.parseInt(serviceRating) - 1);

		createListing.clearAndType(createListing.getFoodReviewTextArea(), foodReview);
		createListing.clearAndType(createListing.getServiceReviewTextArea(), serviceReview);
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
