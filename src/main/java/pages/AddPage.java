package pages;

import dto.ContactLombok;
import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class AddPage extends BasePage {

    public AddPage(WebDriver driver) {
        super(driver);
    }

    private static final Logger logger = LoggerFactory.getLogger(AddPage.class);

    @FindBy(xpath = "//input[@placeholder='Name']")
    WebElement inputName;

    @FindBy(xpath = "//input[@placeholder='Last Name']")
    WebElement inputLastName;

    @FindBy(xpath = "//input[@placeholder='Phone']")
    WebElement inputPhone;

    @FindBy(xpath = "//input[@placeholder='email']")
    WebElement inputEmail;

    @FindBy(xpath = "//input[@placeholder='Address']")
    WebElement inputAddress;

    @FindBy(xpath = "//input[@placeholder='description']")
    WebElement inputDescription;

    @FindBy(xpath = "//b/.")
    WebElement saveButton;

    public void fillContactForm(ContactLombok contact) {
        logger.info("Filling contact form with data:");
        logger.info("Name: {}", contact.getName());
        logger.info("LastName: {}", contact.getLastName());
        logger.info("Phone: {}", contact.getPhone());
        logger.info("Email: {}", contact.getEmail());
        logger.info("Address: {}", contact.getAddress());
        logger.info("Description: {}", contact.getDescription());

        inputName.sendKeys(contact.getName());
        inputLastName.sendKeys(contact.getLastName());
        inputPhone.sendKeys(contact.getPhone());
        inputEmail.sendKeys(contact.getEmail());
        inputAddress.sendKeys(contact.getAddress());
        inputDescription.sendKeys(contact.getDescription());
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.elementToBeClickable(saveButton)).click();
    }

    public String closeAlertReturnText() {
        Alert alert = new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.alertIsPresent());
        String text = alert.getText();
        alert.accept();
        return text;
    }

    public void clickSaveButton() {
        saveButton.click();
    }

    public void fillContactFormFromFileCSV(ContactLombok contact) {
        inputName.sendKeys(contact.getName());
        inputLastName.sendKeys(contact.getLastName());
        inputPhone.sendKeys(contact.getPhone());
        inputEmail.sendKeys(contact.getEmail());
        inputAddress.sendKeys(contact.getAddress());
        inputDescription.sendKeys(contact.getDescription());
    }
}

