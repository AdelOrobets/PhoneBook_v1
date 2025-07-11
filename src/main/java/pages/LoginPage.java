package pages;

import dto.UserLombok;
import lombok.Getter;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.Alert;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage extends BasePage {

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(xpath = "//*[@id='root']/div[2]/div/form")
    WebElement loginForm;

    @FindBy(name = "email")
    WebElement inputEmail;

    @FindBy(name = "password")
    WebElement inputPassword;

    @FindBy(xpath = "//button[@type='submit']")
    WebElement submitButton;

    @FindBy(xpath = "//button[@name='registration']")
    WebElement registrationButton;

    @Getter
    @FindBy(xpath = "//*[contains(text(), 'Registration failed')]")
    WebElement errorMsgRegistration;

    @Getter
    @FindBy(xpath = "//*[contains(text(), 'Login Failed')]")
    WebElement errorMsgLogin;


    public boolean isLoginFormDisplayed() {
        return waitForVisibility(loginForm, 10);
    }

    public void typeLoginForm(UserLombok user) {
        fillCredentials(user.getUsername(), user.getPassword());
        clickLoginButton();
    }

    public ContactsPage clickLoginButton() {
        submitButton.click();
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.urlContains("/contacts"));
        return new ContactsPage(driver);
    }

    public void fillCredentials(String email, String password) {
        enterEmail(email);
        enterPassword(password);
    }

    public LoginPage enterEmail(String email) {
        inputEmail.clear();
        inputEmail.sendKeys(email);
        return this;
    }

    public LoginPage enterPassword(String password) {
        inputPassword.clear();
        inputPassword.sendKeys(password);
        return this;
    }

    public void typeRegistrationForm(UserLombok user) {
        fillCredentials(user.getUsername(), user.getPassword());
        registrationButton.click();
    }

    public boolean closeAlert() {
        try {
            Alert alert = new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.alertIsPresent());
            System.out.println("Alert text: " + alert.getText());
            alert.accept();
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }
}
