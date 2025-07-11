package manager;

import dto.UserLombok;
import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import pages.*;
import utils.HeaderMenuItem;

@Getter
public class ApplicationManager {

    public WebDriver driver;
    public HomePage homePage;
    public AboutPage aboutPage;
    public LoginPage loginPage;
    public ContactsPage contactsPage;
    public AddPage addPage;
    public UserLombok testUser;

    @BeforeMethod(alwaysRun = true)
    @Parameters("browser")
    public void setUpTest(@Optional("chrome") String browser) {
        if (driver == null) {
            driver = initDriver(browser.toLowerCase());
            if (driver == null) {
                throw new IllegalStateException("WebDriver was not initialized. " +
                        "Browser may have failed to start.");
            }
        }
    }

    public WebDriver initDriver(String browser) {
        try {
            switch (browser) {
                case "edge":
                    System.setProperty("webdriver.edge.driver", "C:\\Tools\\msedgedriver.exe");
                    return new EdgeDriver();
                case "chrome":
                default:
                    System.setProperty("webdriver.chrome.driver", "C:\\Tools\\chromedriver.exe");
                    return new ChromeDriver();
            }
        } catch (Exception e) {
            System.err.println("Error during browser startup [" + browser + "]: " + e.getMessage());
            return null;
        }
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

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}