package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.PropertiesReader;

import java.time.Duration;

public class HomePage extends BasePage {

    private static final String URL;

    static {
        URL = PropertiesReader.getProperty("config.properties", "base.url");
        if (URL == null || URL.isEmpty()) {
            throw new IllegalStateException("Property 'base.url' not found in config.properties");
        }
    }

    @FindBy(xpath = "//h1[text()='Home Component']")
    WebElement textHomePage;

    public HomePage(WebDriver driver) {
        super(driver);
        driver.get(URL);
        new WebDriverWait(driver, Duration.ofSeconds(20))
                .until(ExpectedConditions.visibilityOf(textHomePage));
    }

    public boolean isHomePageDisplayed() {
        return waitForVisibility(textHomePage, 10);
    }
}
