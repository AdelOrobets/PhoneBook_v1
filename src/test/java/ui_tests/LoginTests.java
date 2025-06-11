package ui_tests;

import dto.UserLombok;
import io.qameta.allure.Feature;
import manager.ApplicationManager;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.ContactsPage;
import pages.HomePage;
import utils.HeaderMenuItem;
import utils.TestDataFactory;

import static utils.HeaderMenuItem.SIGNOUT;

@Feature("Login")
public class LoginTests extends ApplicationManager {

    public UserLombok userRegistration() {
        UserLombok user = TestDataFactory.validUser();
        openLoginPage();
        loginPage.typeRegistrationForm(user);
        new HomePage(getDriver()).clickHeaderMenuItem(HeaderMenuItem.SIGNOUT);
        return user;
    }

    @Test
    public void testLoginFormDisplayed() {
        Assert.assertTrue(loginPage.isLoginFormDisplayed(),
                "Login Form is not displayed");
    }

    @Test
    public void testUserCanLoginAfterRegistration() {
        UserLombok validUser = userRegistration();
        openLoginPage();
        loginPage.typeLoginForm(validUser);
        Assert.assertTrue(new ContactsPage(driver).isContactsPageDisplayed(), "login failed");

    }

    /**
     * Known bug: REG-1
     * Expected: Email casing should be ignored during login.
     * Email addresses should be treated as case-insensitive.
     * Actual: Login fails if the email address casing does not exactly match the registered casing.
     */
    @Test(description = "Positive test: successful Login with uppercase Email — expected to pass, but to bug REG-1")
    public void testUserLogin_uppercaseEmail() {
        UserLombok validUser = userRegistration();
        String upperCaseEmail = validUser.getUsername().toUpperCase();
        String password = validUser.getPassword();
        openLoginPage();
        loginPage.fillCredentials(upperCaseEmail, password);
        loginPage.clickLoginButton();
        Assert.assertTrue(new ContactsPage(driver).isContactsPageDisplayed(), "login failed");
    }

    // Negative helper
    private void closeAlertAndAssertFailure() {
        loginPage.closeAlert();
        Assert.assertTrue(loginPage.errorMessageContains(loginPage.getErrorMsgLogin(),
                "Login Failed with code 401"));
    }

    // Negative tests
    @Test
    public void loginNegativeTest_unregisteredUser() {
        UserLombok unregisteredUser = TestDataFactory.validUser();
        openLoginPage();
        loginPage.typeLoginForm(unregisteredUser);
        closeAlertAndAssertFailure();
    }

    @Test
    public void loginNegativeTest_wrongPassword() {
        UserLombok validUser = userRegistration();
        String validEmail = validUser.getUsername();
        String wrongPassword = "WrongPass1$";
        openLoginPage();
        loginPage.fillCredentials(validEmail, wrongPassword);
        loginPage.clickLoginButton();
        closeAlertAndAssertFailure();
    }

    @Test
    public void loginNegativeTest_emptyUsername() {
        UserLombok invalidUser = TestDataFactory.userWithoutEmail();
        openLoginPage();
        loginPage.typeLoginForm(invalidUser);
        closeAlertAndAssertFailure();
    }

    @Test
    public void loginNegativeTest_emptyPassword() {
        UserLombok invalidUser = TestDataFactory.userWithoutPassword();
        openLoginPage();
        loginPage.typeLoginForm(invalidUser);
        closeAlertAndAssertFailure();
    }

    @Test
    public void loginNegativeTest_invalidUsernameFormat() {
        UserLombok invalidUser = TestDataFactory.invalidEmailNoAtSymbol();
        openLoginPage();
        loginPage.typeLoginForm(invalidUser);
        closeAlertAndAssertFailure();
    }

    @Test
    public void loginNegativeTest_invalidUsernameDomain() {
        UserLombok invalidUser = TestDataFactory.invalidEmailNoDomain();
        openLoginPage();
        loginPage.typeLoginForm(invalidUser);
        closeAlertAndAssertFailure();
    }

    @Test
    public void loginNegativeTest_invalidUsername_withSpace() {
        UserLombok invalidUser = TestDataFactory.invalidEmailWithSpace();
        openLoginPage();
        loginPage.typeLoginForm(invalidUser);
        closeAlertAndAssertFailure();
    }

    @Test
    public void loginNegativeTest_invalidPasswordShort() {
        UserLombok invalidUser = TestDataFactory.invalidPasswordTooShort();
        openLoginPage();
        loginPage.typeLoginForm(invalidUser);
        closeAlertAndAssertFailure();
    }

    @Test
    public void loginNegativeTest_invalidPasswordLong() {
        UserLombok invalidUser = TestDataFactory.invalidPasswordTooLong();
        openLoginPage();
        loginPage.typeLoginForm(invalidUser);
        closeAlertAndAssertFailure();
    }

    @Test
    public void loginNegativeTest_invalidPasswordNoDigit() {
        UserLombok invalidUser = TestDataFactory.invalidPasswordNoDigit();
        openLoginPage();
        loginPage.typeLoginForm(invalidUser);
        closeAlertAndAssertFailure();
    }

    @Test
    public void loginNegativeTest_invalidPasswordNoSymbol() {
        UserLombok invalidUser = TestDataFactory.invalidPasswordNoSymbol();
        openLoginPage();
        loginPage.typeLoginForm(invalidUser);
        closeAlertAndAssertFailure();
    }
}