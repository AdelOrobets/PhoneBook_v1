package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.HeaderMenuItem;

import java.time.Duration;

public abstract class BasePage {
    protected WebDriver driver;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(new AjaxElementLocatorFactory(driver, 20), this);
    }

    public <T extends BasePage> T clickHeaderMenuItem(HeaderMenuItem headerMenuItem) {
        String locator = headerMenuItem.getLocator();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(locator)));
            element.click();
        } catch (TimeoutException e) {
            throw new AssertionError("Failed to click header menu item: " + headerMenuItem.name() +
                    " Element not clickable within timeout. XPath: " + locator, e);
        }

        return switch (headerMenuItem) {
            case HOME -> (T) new HomePage(driver);
            case ABOUT -> (T) new AboutPage(driver);
            case LOGIN -> (T) new LoginPage(driver);
            case CONTACTS -> (T) new ContactsPage(driver);
            case ADD -> (T) new AddPage(driver);
            case SIGNOUT -> (T) new HomePage(driver); // redirect to HOME
            default -> throw new IllegalArgumentException("Invalid header menu item: " + headerMenuItem);
        };
    }

    public boolean isElementPresent(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean errorMessageContains(WebElement element, String expectedMessage) {
        try {
            return element.getText().contains(expectedMessage);
        } catch (Exception e) {
            System.out.println("Element not found or text not present: " + e.getMessage());
            return false;
        }
    }

    protected boolean waitForVisibility(WebElement element, int timeoutSeconds) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds))
                    .until(ExpectedConditions.visibilityOf(element));
            return element.isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }
}