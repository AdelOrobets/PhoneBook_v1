package manager;

import dto.UserLombok;
import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import pages.HomePage;
import pages.LoginPage;

import java.time.Duration;

@Getter
public class ApplicationManager {

    public WebDriver driver;
    public LoginPage loginPage;
    public UserLombok testUser;

    public void createUniqueTestUser() {
        String email = LoginPage.generateEmail(8);
        String password = LoginPage.generatePassword(10);
        testUser = new UserLombok(email, password);
        System.out.println("Generated testUser: Email: " + email + ", Password: " + password);
    }

    public void openLoginPage() {
        new HomePage(driver).clickLoginHeaderLink();
        loginPage = new LoginPage(driver);
    }

    @BeforeMethod
    public void setUpTest() {
        if (driver == null) {
            driver = new ChromeDriver();
            driver.manage().window().maximize();
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        }
        createUniqueTestUser();
        openLoginPage();
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}