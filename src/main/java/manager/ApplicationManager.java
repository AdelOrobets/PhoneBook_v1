package manager;

import dto.UserLombok;
import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import pages.AboutPage;
import pages.HomePage;
import pages.LoginPage;
import utils.HeaderMenuItem;

import java.time.Duration;

@Getter
public class ApplicationManager {

    public WebDriver driver;
    public HomePage homePage;
    public AboutPage aboutPage;
    public LoginPage loginPage;
    public UserLombok testUser;

    public WebDriver initDriver() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(20));
        return driver;
    }

    public void openLoginPage() {
        loginPage = new HomePage(driver).clickHeaderMenuItem(HeaderMenuItem.LOGIN);
    }

    public void openAboutPage() {
        aboutPage = new HomePage(driver).clickHeaderMenuItem(HeaderMenuItem.ABOUT);
    }


    @BeforeMethod
    public void setUpTest() {
        if (driver == null) {
            driver = initDriver();
        }
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}