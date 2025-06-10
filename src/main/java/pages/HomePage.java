package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class HomePage extends BasePage {

    public HomePage(WebDriver driver) {
        super(driver);
        driver.get("https://telranedu.web.app");
    }

    @FindBy(xpath = "//h1[text()='Home Component']")
    WebElement textHomePage;

    public boolean isHomePageDisplayed() {
        return isElementPresent(textHomePage);
    }
}
