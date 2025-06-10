package pages;

import dto.UserLombok;
import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.security.SecureRandom;
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

    @FindBy(xpath = "//*[contains(text(), 'Registration failed')]")
    WebElement errorMsgRegistration;

    @FindBy(xpath = "//*[contains(text(), 'Login Failed')]")
    WebElement errorMsgLogin;

    public boolean isLoginFormDisplayed() {
        return isElementPresent(loginForm);
    }

    public void typeLoginForm(UserLombok user) {
        fillCredentials(user);
        submitButton.click();
    }

    public void typeRegistrationForm(UserLombok user) {
        fillCredentials(user);
        registrationButton.click();
    }

    private void fillCredentials(UserLombok user) {
        inputEmail.clear();
        inputEmail.sendKeys(user.getUsername());
        inputPassword.clear();
        inputPassword.sendKeys(user.getPassword());
    }

    public void closeAlert() {
        try {
            Alert alert = new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.alertIsPresent());
            System.out.println("Alert text: " + alert.getText());
            alert.accept();
        } catch (Exception e) {
            System.out.println("No alert present: " + e.getMessage());
        }
    }

    public boolean isLoginErrorMessageDisplayed(String expectedMessage) {
        return isTextInElementPresent(errorMsgLogin, expectedMessage);
    }

    public boolean isRegistrationErrorMessageDisplayed(String expectedMessage) {
        return isTextInElementPresent(errorMsgRegistration, expectedMessage);
    }

    public static String generateEmail(int length) {
        return generateRandomString(length, "abcdefghijklmnopqrstuvwxyz0123456789") + "@gmail.com";
    }

    public static String generateInvalidEmailNoAtSymbol(int length) {
        return generateRandomString(length, "abcdefghijklmnopqrstuvwxyz0123456789") + "gmail.com";
    }

    public static String generateInvalidEmailNoDomain(int length) {
        return generateRandomString(length, "abcdefghijklmnopqrstuvwxyz0123456789") + ".com";
    }

    public static String generatePassword(int length) {
        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lower = "abcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String special = "@$#^&*!";

        String allChars = upper + lower + digits + special;
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        password.append(upper.charAt(random.nextInt(upper.length())));
        password.append(lower.charAt(random.nextInt(lower.length())));
        password.append(digits.charAt(random.nextInt(digits.length())));
        password.append(special.charAt(random.nextInt(special.length())));

        for (int i = 4; i < length; i++) {
            password.append(allChars.charAt(random.nextInt(allChars.length())));
        }
        return password.toString();
    }

    public static String generatePasswordInvalidNoSymbol(int length) {
        return generateRandomString(length, "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789");
    }

    public static String generatePasswordInvalidNoDigit(int length) {
        return generateRandomString(length, "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz@$#^&*!");
    }

    private static String generateRandomString(int length, String characters) {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }
        return sb.toString();
    }
}
