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
import org.testng.annotations.Test;

import com.cognizant.base.BaseClass;
import com.cognizant.elementrepository.CreateListing;

public class TC021_UploadedImageCount extends BaseClass {

	@Test(groups = {"UI Testing", "Functional Testing", "Positive Testing"})
	public void TC021_Uploaded_Image_Count() {
		CreateListing createListing = openCreateListingPage();
		fillMandatoryEmployeeDetails(createListing, "Test PG Image Count Three", "8000");

		createListing.uploadPhotos(firstImagePaths(3));

		Assert.assertEquals(createListing.getDisplayedImageCount(), 3,
				"Image count should show 3 image(s).");

		driver.navigate().refresh();

		createListing = new CreateListing(driver);
		createListing.waitUntilLoaded();
		fillMandatoryEmployeeDetails(createListing, "Test PG Image Count One", "8000");

		createListing.uploadPhotos(firstImagePaths(1));

		Assert.assertEquals(createListing.getDisplayedImageCount(), 1,
				"Image count should show 1 image(s).");
	}

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

		if (!driver.getCurrentUrl().contains("/create-listing")) {
			WebElement createListingLink = wait.until(
					ExpectedConditions.elementToBeClickable(By.id("nav-create-listing-link")));
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", createListingLink);
		}

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
}
