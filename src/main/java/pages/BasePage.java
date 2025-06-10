package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import utils.HeaderMenuItem;

public abstract class BasePage {
    protected WebDriver driver;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(new AjaxElementLocatorFactory(driver, 20), this);
    }

    public <T extends BasePage> T clickHeaderMenuItem(HeaderMenuItem headerMenuItem) {
        WebElement element = driver.findElement(By.xpath(headerMenuItem.getLocator()));
        element.click();
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

    public boolean isTextInElementPresent(WebElement element, String text) {
        try {
            return element.getText().contains(text);
        } catch (Exception e) {
            System.out.println("Element not found or text not present: " + e.getMessage());
            return false;
        }
    }
}