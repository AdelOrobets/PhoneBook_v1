package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HomePage extends BasePage {

    private static final String URL = "https://telranedu.web.app";

    @FindBy(xpath = "//h1[text()='Home Component']")
    WebElement textHomePage;

    public HomePage(WebDriver driver) {
        super(driver);
        driver.get(URL);
        new WebDriverWait(driver, Duration.ofSeconds(30))
                .until(ExpectedConditions.visibilityOf(textHomePage));
    }

    public boolean isHomePageDisplayed() {
        return waitForVisibility(textHomePage, 10);
    }
}
