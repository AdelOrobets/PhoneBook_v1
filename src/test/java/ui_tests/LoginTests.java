package ui_tests;

import dto.UserLombok;
import io.qameta.allure.*;
import manager.ApplicationManager;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.ContactsPage;
import pages.HomePage;
import pages.LoginPage;

import static utils.HeaderMenuItem.SIGNOUT;

@Feature("Login")
public class LoginTests extends ApplicationManager {

    @BeforeMethod
    public void prepareUser() {
        createUniqueTestUser();
        openLoginPage();
    }

    @Test
    public void testLoginFormDisplayed() {
        Assert.assertTrue(loginPage.isLoginFormDisplayed(), "Login Form is not displayed");
    }

    @Test
    @Story("Successful Login with valid data")
    @Severity(SeverityLevel.BLOCKER)
    @Owner("Adel Orobets")
    public void testSuccessfulLogin() {
        loginPage.typeRegistrationForm(testUser);
        Assert.assertTrue(new ContactsPage(driver).isContactsPageDisplayed(), "Registration failed");

        new HomePage(driver).clickHeaderMenuItem(SIGNOUT);

        loginPage.typeLoginForm(testUser);
        loginPage.closeAlert();
        Assert.assertTrue(new ContactsPage(driver).isContactsPageDisplayed(), "login failed");
    }

    /**
     * Known bug: REG-1
     * Expected: Email casing should be ignored during login.
     * Email addresses should be treated as case-insensitive.
     * Actual: Login fails if the email address casing does not exactly match the registered casing.
     */
    @Test(description = "Positive test: successful Login with uppercase Email — expected to pass, but to bug REG-1")
    public void testSuccessfulLogin_uppercaseEmail() {
        String email = testUser.getUsername().toUpperCase();
        loginPage.typeRegistrationForm(new UserLombok(email, testUser.getPassword()));
        Assert.assertTrue(new ContactsPage(driver).isContactsPageDisplayed(), "Registration failed");

        new HomePage(driver).clickHeaderMenuItem(SIGNOUT);

        loginPage.typeLoginForm(new UserLombok(email, testUser.getPassword()));
        Assert.assertTrue(new ContactsPage(driver).isContactsPageDisplayed(), "login failed");
    }

    // Negative helper
    private void loginAndAssertFailure(UserLombok user) {
        loginPage.typeLoginForm(user);
        boolean alertAppeared = loginPage.closeAlert();
        Assert.assertTrue(alertAppeared, "BUG: Expected Alert not displayed");
    }

    @Test
    public void loginNegativeTest_unregisteredUser() {
        loginAndAssertFailure(new UserLombok(testUser.getUsername(), testUser.getPassword()));
    }

    // Different passwords for registration and login
    @Test
    public void loginNegativeTest_wrongPassword() {
        String wrongPassword = LoginPage.generatePassword(9);
        loginAndAssertFailure(new UserLombok(testUser.getUsername(), wrongPassword));
    }

    @Test
    public void loginNegativeTest_emptyUsername() {
        loginAndAssertFailure(new UserLombok("", testUser.getPassword()));
    }

    @Test
    public void loginNegativeTest_emptyPassword() {
        loginAndAssertFailure(new UserLombok(testUser.getUsername(), ""));
    }

    // Email without @
    @Test
    public void loginNegativeTest_invalidUsernameFormat() {
        String email = LoginPage.generateInvalidEmailNoAtSymbol(10);
        loginAndAssertFailure(new UserLombok(email, testUser.getPassword()));
    }

    @Test
    public void loginNegativeTest_invalidUsernameDomain() {
        String email = LoginPage.generateInvalidEmailNoDomain(10);
        loginAndAssertFailure(new UserLombok(email, testUser.getPassword()));
    }

    @Test
    public void loginNegativeTest_invalidUsername_withSpace() {
        String emailWithSpace = testUser.getUsername().stripTrailing() + " ";
        loginAndAssertFailure(new UserLombok(emailWithSpace, testUser.getPassword()));
    }

    @Test
    public void loginNegativeTest_invalidPasswordShort() {
        String shortPwd = LoginPage.generatePassword(1);
        loginAndAssertFailure(new UserLombok(testUser.getUsername(), shortPwd));
    }

    @Test
    public void loginNegativeTest_invalidPasswordLong() {
        String longPwd = LoginPage.generatePassword(16);
        System.out.println("Sending email = " + testUser.getUsername() + ", passwordLong = " + longPwd);
        loginAndAssertFailure(new UserLombok(testUser.getUsername(), longPwd));
    }

    @Test
    public void loginNegativeTest_invalidPasswordNoDigit() {
        String pwdNoDigit = LoginPage.generatePasswordInvalidNoDigit(8);
        System.out.println("Sending email = " + testUser.getUsername() + ", passwordNoDigit = " + pwdNoDigit);
        loginAndAssertFailure(new UserLombok(testUser.getUsername(), pwdNoDigit));
    }

    @Test
    public void loginNegativeTest_invalidPasswordNoSymbol() {
        String pwdNoSymbol = LoginPage.generatePasswordInvalidNoSymbol(8);
        System.out.println("Sending email = " + testUser.getUsername() + ", passwordNoSymbol = " + pwdNoSymbol);
        loginAndAssertFailure(new UserLombok(testUser.getUsername(), pwdNoSymbol));
    }
}