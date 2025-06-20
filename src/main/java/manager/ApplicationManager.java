package manager;

import dto.UserLombok;
import lombok.Getter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import pages.*;
import utils.HeaderMenuItem;

import java.time.Duration;

@Getter
public class ApplicationManager {

    public WebDriver driver;
    public HomePage homePage;
    public AboutPage aboutPage;
    public LoginPage loginPage;
    public ContactsPage contactsPage;
    public AddPage addPage;
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

    public void openAddPage() {
        addPage = contactsPage.clickHeaderMenuItem(HeaderMenuItem.ADD);
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