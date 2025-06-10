package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.WebDriver;

public class AboutPage extends BasePage {

    public AboutPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(xpath = "//h1[contains(text(), 'Contacts Web Application')]")
    WebElement textAbout;

    public boolean isAboutPageDisplayed() {
        return isElementPresent(textAbout);
    }
}