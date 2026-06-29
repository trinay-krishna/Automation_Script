package com.cognizant.elementrepository;

import java.io.File;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CreateListing {
	

	private WebDriver driver;
	private WebDriverWait wait;

	public CreateListing(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		PageFactory.initElements(driver, this);
	}

	@FindBy(id = "create-pg-name")
	private WebElement pgNameTextField;

	@FindBy(id = "create-locality")
	private WebElement localityAndLandmarkTextField;

	@FindBy(id = "create-city")
	private WebElement cityDropdown;

	@FindBy(id = "create-area")
	private WebElement officeAreaDropdown;

	@FindBy(id = "create-sharing-type")
	private WebElement sharingTypeDropdown;

	@FindBy(id = "create-available-beds")
	private WebElement vacantBedsDropdown;

	@FindBy(id = "create-pg-type")
	private WebElement pgTypeDropdown;

	@FindBy(id = "create-rent")
	private WebElement rentTextField;

	@FindBy(id = "create-food-rating")
	private WebElement foodRatingDropdown;

	@FindBy(id = "create-service-rating")
	private WebElement serviceRatingDropdown;

	@FindBy(id = "create-food-review")
	private WebElement foodReviewTextArea;

	@FindBy(id = "create-service-review")
	private WebElement serviceReviewTextArea;

	@FindBy(id = "create-submit-btn")
	private WebElement publishListingButton;
	
	@FindBy(id = "create-photo-input")
	private WebElement photoInput;

	public WebElement getPhotoInput() {
		return photoInput;
	}
	
	public void uploadPhoto(String imagePath) {
		photoInput.sendKeys(new File(imagePath).getAbsolutePath());
	}

	public void uploadPhotos(String... imagePaths) {
		for (String imagePath : imagePaths) {
			uploadPhoto(imagePath);
		}
	}
	
	public String getCreateErrorText() {
		if (driver.findElements(By.id("create-error")).size() > 0) {
			return driver.findElement(By.id("create-error")).getText();
		}
		return "";
	}
	


	private void scrollToElement(WebElement element) {
	    ((JavascriptExecutor) driver).executeScript(
	            "arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});",
	            element
	    );
	}

	private void clearField(WebElement element) {
	    scrollToElement(element);

	    ((JavascriptExecutor) driver).executeScript(
	            "arguments[0].value = '';" +
	            "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));" +
	            "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
	            element
	    );
	}

	public void clearAndType(WebElement element, String value) {
	    scrollToElement(element);

	    ((JavascriptExecutor) driver).executeScript(
	            "arguments[0].value = arguments[1];" +
	            "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));" +
	            "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
	            element,
	            value
	    );
	}

	public void clearCreateListingForm() {
	    clearField(pgNameTextField);
	    clearField(localityAndLandmarkTextField);
	    clearField(rentTextField);
	    clearField(foodReviewTextArea);
	    clearField(serviceReviewTextArea);
	}

	public WebElement getPgNameTextField() {
		return pgNameTextField;
	}

	public WebElement getLocalityAndLandmarkTextField() {
		return localityAndLandmarkTextField;
	}

	public WebElement getCityDropdown() {
		return cityDropdown;
	}

	public WebElement getOfficeAreaDropdown() {
		return officeAreaDropdown;
	}

	public WebElement getSharingTypeDropdown() {
		return sharingTypeDropdown;
	}

	public WebElement getVacantBedsDropdown() {
		return vacantBedsDropdown;
	}

	public WebElement getPgTypeDropdown() {
		return pgTypeDropdown;
	}

	public WebElement getRentTextField() {
		return rentTextField;
	}

	public WebElement getFoodRatingDropdown() {
		return foodRatingDropdown;
	}

	public WebElement getServiceRatingDropdown() {
		return serviceRatingDropdown;
	}

	public WebElement getFoodReviewTextArea() {
		return foodReviewTextArea;
	}

	public WebElement getServiceReviewTextArea() {
		return serviceReviewTextArea;
	}

	public WebElement getPublishListingButton() {
		return publishListingButton;
	}
	
	public int getPreviewCount() {
		return driver.findElements(By.cssSelector("#create-preview-grid .preview-tile")).size();
	}

	public int getDisplayedImageCount() {
		WebElement countElement = driver.findElement(
				By.xpath("//input[@id='create-photo-input']/ancestor::div[1]/span"));

		String countText = countElement.getText().replaceAll("[^0-9]", "");
		return Integer.parseInt(countText);
	}

	public void removeUploadedPhoto(int index) {
		driver.findElement(By.id("create-new-remove-" + index)).click();
	}
	
	public void clearAndEnterRent(String rent) {
		rentTextField.clear();
		rentTextField.sendKeys(rent);
	}

	public boolean isRentMarkedInvalid() {
		String className = rentTextField.getAttribute("class");
		return className != null && className.contains("invalid");
	}

	public boolean isCreateErrorDisplayed() {
		return driver.findElements(By.id("create-error")).size() > 0
				&& driver.findElement(By.id("create-error")).isDisplayed();
	}
	
	

	public void waitUntilLoaded() {
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("create-listing-card")));
	}

}
