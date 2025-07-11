package ui_tests;

import dto.UserLombok;
import manager.ApplicationManager;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.ContactsPage;
import pages.HomePage;
import utils.HeaderMenuItem;
import utils.TestDataFactory;
import utils.TestNGListener;

@Listeners(TestNGListener.class)
public class RegistrationTests extends ApplicationManager {

    // Positive tests
    @Test(groups = "smoke")
    public void testSuccessfulRegistration() {
        UserLombok user = TestDataFactory.validUser();
        openLoginPage();
        loginPage.typeRegistrationForm(user);
        Assert.assertTrue(new ContactsPage(driver).isContactsPageDisplayed(),
                "Registration failed");
    }

    // Negative helper
    private void registrationAndAssertFailure(UserLombok user) {
        openLoginPage();
        loginPage.typeRegistrationForm(user);
        boolean alertAppeared = loginPage.closeAlert();
        Assert.assertTrue(alertAppeared, "BUG: Expected Alert not displayed");
        Assert.assertTrue(loginPage.errorMessageContains(loginPage.getErrorMsgRegistration(),
                "Registration failed"));
    }

    // Negative tests
    @Test
    public void testNegative_UserAlreadyExist() {
        // create new user
        UserLombok user = TestDataFactory.validUser();
        openLoginPage();
        loginPage.typeRegistrationForm(user);
        // exit
        new HomePage(getDriver()).clickHeaderMenuItem(HeaderMenuItem.LOGIN);
        // re-registration
        registrationAndAssertFailure(user);
    }

    @Test
    public void testNegative_emptyUsername() {
        UserLombok invalidUser = TestDataFactory.userWithoutEmail();
        registrationAndAssertFailure(invalidUser);
    }

    @Test
    public void testNegative_emptyPassword() {
        UserLombok invalidUser = TestDataFactory.userWithoutPassword();
        registrationAndAssertFailure(invalidUser);
    }

    @Test
    public void testNegative_invalidUsernameFormat() {
        UserLombok invalidUser = TestDataFactory.invalidEmailNoAtSymbol();
        registrationAndAssertFailure(invalidUser);
    }

    /**
     * Known bug: REG-5
     * Expected: Registration should be blocked if the email domain is invalid
     * (e.g., "test@gmail" without a top-level domain).
     * Actual: Registration succeeds with an invalid email
     * This test demonstrates that the email validation logic does not correctly enforce domain formatting.
     */
    @Test
    public void testNegative_invalidUsernameDomain() {
        UserLombok invalidUser = TestDataFactory.invalidEmailNoDomain();
        registrationAndAssertFailure(invalidUser);
    }

    @Test
    public void testNegative_invalidUsername_withSpace() {
        UserLombok invalidUser = TestDataFactory.invalidEmailWithSpace();
        registrationAndAssertFailure(invalidUser);
    }

    @Test
    public void testNegative_invalidPasswordShort() {
        UserLombok invalidUser = TestDataFactory.invalidPasswordTooShort();
        registrationAndAssertFailure(invalidUser);
    }

    /**
     * Known bug: REG-2
     * According to requirements, password should be between 8 and 15 characters.
     * Expected: Registration should fail when password length > 15.
     * Actual: Registration succeeds with password length > 15.
     */
    @Test
    public void testNegative_invalidPasswordLong() {
        UserLombok invalidUser = TestDataFactory.invalidPasswordTooLong();
        registrationAndAssertFailure(invalidUser);
    }

    @Test
    public void testNegative_invalidPasswordNoDigit() {
        UserLombok invalidUser = TestDataFactory.invalidPasswordNoDigit();
        registrationAndAssertFailure(invalidUser);
    }

    @Test
    public void testNegative_invalidPasswordNoSymbol() {
        UserLombok invalidUser = TestDataFactory.invalidPasswordNoSymbol();
        registrationAndAssertFailure(invalidUser);
    }
}