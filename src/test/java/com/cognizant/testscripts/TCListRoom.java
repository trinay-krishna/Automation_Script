package com.cognizant.testscripts;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.cognizant.base.BaseClass;
import com.cognizant.elementrepository.CreateListing;
import com.cognizant.elementrepository.MyListings;
import com.cognizant.util.ExcelUtil;

public class TCListRoom extends BaseClass {

	String createdPgName;
	String createdCity;
	String createdOfficeArea;
	String createdRent;

	private String[] firstImagePaths(int count) {
		Path imagesFolder = Paths.get(System.getProperty("user.dir"), "src", "test", "resources",
				"TestData", "images");

		try (Stream<Path> paths = Files.list(imagesFolder)) {
			List<String> images = paths
					.filter(Files::isRegularFile)
					.filter(path -> {
						String fileName = path.getFileName().toString().toLowerCase();
						return fileName.endsWith(".jpg")
								|| fileName.endsWith(".jpeg")
								|| fileName.endsWith(".png");
					})
					.sorted(Comparator.comparing(path -> path.getFileName().toString().toLowerCase()))
					.limit(count)
					.map(path -> path.toAbsolutePath().toString())
					.collect(Collectors.toList());

			Assert.assertTrue(images.size() >= count,
					"Expected at least " + count + " image(s) inside: " + imagesFolder);

			return images.toArray(new String[0]);

		} catch (IOException e) {
			throw new RuntimeException("Unable to read images folder: " + imagesFolder, e);
		}
	}

	private CreateListing openCreateListingPage() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		WebElement createListingLink = wait.until(
				ExpectedConditions.elementToBeClickable(By.id("nav-create-listing-link")));

		((JavascriptExecutor) driver).executeScript("arguments[0].click();", createListingLink);

		CreateListing createListing = new CreateListing(driver);
		createListing.waitUntilLoaded();

		return createListing;
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

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}

		if (driver.getCurrentUrl().contains("/create-listing")) {
			System.out.println("Create error: " + createListing.getCreateErrorText());
		}

		new WebDriverWait(driver, Duration.ofSeconds(10))
				.until(ExpectedConditions.urlContains("/my-listings"));

		MyListings myListings = new MyListings(driver);
		myListings.waitUntilLoaded();
		return myListings;
	}

	private String createEmployeeListing(String pgName, String rent, boolean uploadPhoto) {
		CreateListing createListing = openCreateListingPage();
		String uniquePgName = fillMandatoryEmployeeDetails(createListing, pgName, rent);

		if (uploadPhoto) {
			createListing.uploadPhotos(firstImagePaths(1));
		}

		MyListings myListings = publishAndWaitForMyListings(createListing);
		Assert.assertTrue(myListings.isListingDisplayed(uniquePgName),
				"Created listing should be displayed in My Listings.");

		return uniquePgName;
	}

	@DataProvider(name = "listRoomData")
	public Object[][] getListRoomData() {
		ExcelUtil excel = new ExcelUtil();
		return excel.getMultipleData("ListRoom");
	}

	@Test(
			priority = 1,
			dataProvider = "listRoomData",
			groups = {"Smoke Testing", "Positive Testing", "Functional Testing"})
	public void createListingWithMandatoryDetails(String tcId, String pgName, String locality, String city,
			String officeArea, String sharingType, String vacantBeds, String pgType,
			String rent, String foodRating, String serviceRating, String foodReview,
			String serviceReview) {

		createdPgName = pgName + " " + System.currentTimeMillis();
		createdCity = city;
		createdOfficeArea = officeArea;
		createdRent = rent;

		homePage.getCreateListingButton().click();

		CreateListing createListing = new CreateListing(driver);
		createListing.waitUntilLoaded();

		createListing.clearAndType(createListing.getPgNameTextField(), createdPgName);
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

		createListing.getPublishListingButton().click();

		new WebDriverWait(driver, Duration.ofSeconds(10))
				.until(ExpectedConditions.urlContains("/my-listings"));

		Assert.assertTrue(driver.getCurrentUrl().contains("/my-listings"),
				tcId + " failed: Application should redirect to My Listings.");
	}

	@Test(
			priority = 2,
			dependsOnMethods = "createListingWithMandatoryDetails",
			groups = {"Positive Testing", "Functional Testing", "UI Testing"})
	public void verifyCreatedListingInMyListings() {
		homePage.getListingButton().click();

		MyListings myListings = new MyListings(driver);
		myListings.waitUntilLoaded();

		Assert.assertTrue(myListings.isListingDisplayed(createdPgName),
				"Created listing should be displayed in My Listings.");

		Assert.assertTrue(myListings.verifyBasicListingDetails(
						createdPgName,
						createdCity + " - " + createdOfficeArea,
						createdRent),
				"Listing card details are not correct.");
	}

	@Test(groups = {"Positive Testing", "Functional Testing"})
	public void TC018_Listing_Without_Photos() {
		CreateListing createListing = openCreateListingPage();
		String uniquePgName = fillMandatoryEmployeeDetails(createListing, "Test PG No Photo", "8000");

		MyListings myListings = publishAndWaitForMyListings(createListing);

		Assert.assertTrue(myListings.isListingDisplayed(uniquePgName),
				"Listing without photos should be created and displayed in My Listings.");
	}

	@Test(groups = {"candidate", "Positive Testing", "Functional Testing"})
	public void TC019_Candidate_Listing_Creation() {
		CreateListing createListing = openCreateListingPage();
		String uniquePgName = "Candidate Test PG " + System.currentTimeMillis();

		createListing.clearAndType(createListing.getPgNameTextField(), uniquePgName);
		createListing.clearAndType(createListing.getLocalityAndLandmarkTextField(), "Near Navalur");

		new Select(createListing.getCityDropdown()).selectByVisibleText("Chennai");
		new Select(createListing.getOfficeAreaDropdown()).selectByVisibleText("Siruseri");
		new Select(createListing.getSharingTypeDropdown()).selectByVisibleText("Double (2 beds)");
		new Select(createListing.getVacantBedsDropdown()).selectByVisibleText("1 of 2");
		new Select(createListing.getPgTypeDropdown()).selectByVisibleText("Co-Living PG (anyone)");

		createListing.clearAndType(createListing.getRentTextField(), "8000");

		MyListings myListings = publishAndWaitForMyListings(createListing);

		Assert.assertTrue(myListings.isListingDisplayed(uniquePgName),
				"Candidate-created listing should be displayed in My Listings.");
	}

	@Test(groups = {"UI Testing", "Functional Testing", "Positive Testing"})
	public void TC020_Remove_Uploaded_Photo() {
		CreateListing createListing = openCreateListingPage();
		fillMandatoryEmployeeDetails(createListing, "Test PG Remove Photo", "8000");

		createListing.uploadPhotos(firstImagePaths(3));

		Assert.assertEquals(createListing.getPreviewCount(), 3,
				"Three uploaded image previews should be displayed.");

		createListing.removeUploadedPhoto(0);

		Assert.assertEquals(createListing.getPreviewCount(), 2,
				"After removing one image, two previews should remain.");
	}

	@Test(groups = {"UI Testing", "Functional Testing", "Positive Testing"})
	public void TC021_Uploaded_Image_Count() {
		CreateListing createListing = openCreateListingPage();
		fillMandatoryEmployeeDetails(createListing, "Test PG Image Count Four", "8000");

		createListing.uploadPhotos(firstImagePaths(3));

		Assert.assertEquals(createListing.getDisplayedImageCount(), 3,
				"Image count should show 3 image(s).");

		driver.get(driver.getCurrentUrl());

		createListing = new CreateListing(driver);
		createListing.waitUntilLoaded();

		fillMandatoryEmployeeDetails(createListing, "Test PG Image Count One", "8000");
		createListing.uploadPhotos(firstImagePaths(1));

		Assert.assertEquals(createListing.getDisplayedImageCount(), 1,
				"Image count should show 1 image(s).");
	}

	@Test(groups = {
			"Boundary Value Testing",
			"Negative Testing",
			"Positive Testing",
			"Functional Testing",
			"Invalid input testing(Invalid or blank fields)"})
	public void TC022_Rent_Boundary_Value() {
		SoftAssert softAssert = new SoftAssert();

		try {
			String minRentPg = createEmployeeListing("Test PG Min Rent", "1", false);
			softAssert.assertNotNull(minRentPg, "Minimum rent value should create listing.");
		} catch (Exception e) {
			softAssert.fail("Minimum rent value should create listing, but failed with: " + e.getMessage());
		}

		try {
			String maxRentPg = createEmployeeListing("Test PG Max Rent", "99999999", false);
			softAssert.assertNotNull(maxRentPg, "Maximum rent value should create listing.");
		} catch (Exception e) {
			softAssert.fail("Maximum rent value should create listing, but failed with: " + e.getMessage());
		}

		checkInvalidRentShowsUserFriendlyError("-1", softAssert);
		checkInvalidRentShowsUserFriendlyError("9999999999", softAssert);

		softAssert.assertAll();
	}

	private void checkInvalidRentShowsUserFriendlyError(String rent, SoftAssert softAssert) {
		try {
			CreateListing createListing = openCreateListingPage();

			createListing.clearCreateListingForm();

			fillMandatoryEmployeeDetails(
					createListing,
					"Test PG Invalid Rent " + rent + " " + System.currentTimeMillis(),
					rent);

			createListing.getPublishListingButton().click();

			Thread.sleep(1000);

			String actualError = createListing.getCreateErrorText();

			System.out.println("Rent value: " + rent);
			System.out.println("Error shown: " + actualError);

			softAssert.assertTrue(
					driver.getCurrentUrl().contains("/create-listing"),
					"Invalid rent " + rent + " should not redirect to My Listings.");

			softAssert.assertFalse(
					actualError == null || actualError.trim().isEmpty(),
					"Invalid rent " + rent + " should show an error message.");

			softAssert.assertFalse(
					actualError.toLowerCase().contains("sql"),
					"Invalid rent " + rent + " should not expose SQL error. Actual: " + actualError);

			softAssert.assertFalse(
					actualError.toLowerCase().contains("exception"),
					"Invalid rent " + rent + " should not expose exception details. Actual: " + actualError);

			softAssert.assertFalse(
					actualError.equalsIgnoreCase("Fill required fields")
							|| actualError.equalsIgnoreCase("Please fill all required (*) fields."),
					"Invalid rent " + rent
							+ " should show a proper rent validation message, not generic required-fields error. Actual: "
							+ actualError);

		} catch (Exception e) {
			softAssert.fail("Invalid rent " + rent + " check crashed with: " + e.getMessage());
		}
	}

	@Test(groups = {"UI Testing", "Functional Testing", "Positive Testing"})
	public void TC023_Created_Listing_Card() {
		String uniquePgName = createEmployeeListing("Test PG Card", "8000", false);

		MyListings myListings = new MyListings(driver);
		myListings.waitUntilLoaded();

		Assert.assertTrue(myListings.verifyBasicListingDetails(uniquePgName, "Chennai - Siruseri", "8000"),
				"Listing card should show name, location, rent, status, vacant beds, and expiry.");

		Assert.assertTrue(myListings.hasEditAndDeleteButtons(uniquePgName),
				"Listing card should have Edit and Delete buttons.");
	}
}