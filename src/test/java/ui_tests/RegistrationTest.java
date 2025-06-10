package ui_tests;

import dto.UserLombok;
import io.qameta.allure.*;
import manager.ApplicationManager;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.ContactsPage;
import pages.HomePage;
import pages.LoginPage;
import utils.HeaderMenuItem;

@Feature("Registration")
public class RegistrationTest extends ApplicationManager {

    @Test
    @Story("Successful registration with valid data")
    @Severity(SeverityLevel.BLOCKER)
    @Owner("Adel Orobets")
    public void testSuccessfulRegistration() {
        loginPage.typeRegistrationForm(testUser);
        ContactsPage contactsPage = new ContactsPage(driver);
        Assert.assertTrue(contactsPage.isNoContactsMessageDisplayed(), "No Contacts here!");
    }

    // Negative helper
    private void registrationAndAssertFailure(UserLombok user) {
        loginPage.typeRegistrationForm(user);
        loginPage.closeAlert();

        boolean errorDisplayed = loginPage.isRegistrationErrorMessageDisplayed("Registration failed");
        System.out.println("Error displayed: " + errorDisplayed);

        if (!errorDisplayed) {
            Assert.fail("BUG: Registration succeeded with invalid data: " + user.getUsername()
                    + " / " + user.getPassword());
        } else {
            System.out.println("Test passed: Error message correctly displayed");
        }
    }

    @Test
    public void testNegative_UserAlreadyExist() {
        loginPage.typeRegistrationForm(testUser);
        Assert.assertTrue(new ContactsPage(driver)
                .isNoContactsTextPresent("No Contacts here!"));

        new HomePage(driver).clickHeaderMenuItem(HeaderMenuItem.SIGNOUT);
        loginPage.typeRegistrationForm(testUser);
        loginPage.closeAlert();
        Assert.assertTrue(loginPage.isRegistrationErrorMessageDisplayed("Registration failed"));
    }

    @Test
    public void testNegative_emptyUsername() {
        registrationAndAssertFailure(new UserLombok("", testUser.getPassword()));
    }

    @Test
    public void testNegative_emptyPassword() {
        registrationAndAssertFailure(new UserLombok(testUser.getUsername(), ""));
    }

    @Test
    public void testNegative_invalidUsernameFormat() {
        String email = LoginPage.generateInvalidEmailNoAtSymbol(10);
        registrationAndAssertFailure(new UserLombok(email, testUser.getPassword()));
    }

    @Test
    public void testNegative_invalidUsernameDomain() {
        String email = LoginPage.generateInvalidEmailNoDomain(10);
        registrationAndAssertFailure(new UserLombok(email, testUser.getPassword()));
    }

    @Test
    public void testNegative_invalidUsername_withSpace() {
        registrationAndAssertFailure(new UserLombok(testUser.getUsername() + " ", testUser.getPassword()));
    }

    @Test
    public void testNegative_invalidPasswordShort() {
        String shortPwd = LoginPage.generatePassword(4);
        registrationAndAssertFailure(new UserLombok(testUser.getUsername(), shortPwd));
    }

    /**
     * Known bug: REG-2
     * According to requirements, password should be between 8 and 15 characters.
     * Expected: Registration should fail when password length > 15.
     * Actual: Registration succeeds with password length > 15.
     */
    @Test(description = "Negative test: password too long (>15) — expected to fail due to bug REG-2")
    public void testNegative_invalidPasswordLong() {
        String longPwd = LoginPage.generatePassword(16);
        System.out.println("Sending email = " + testUser.getUsername() + ", passwordLong = " + longPwd);
        registrationAndAssertFailure(new UserLombok(testUser.getUsername(), longPwd));
    }

    @Test
    public void testNegative_invalidPasswordNoDigit() {
        String pwdNoDigit = LoginPage.generatePasswordInvalidNoDigit(8);
        System.out.println("Sending email = " + testUser.getUsername() + ", passwordNoDigit = " + pwdNoDigit);
        registrationAndAssertFailure(new UserLombok(testUser.getUsername(), pwdNoDigit));
    }

    @Test
    public void testNegative_invalidPasswordNoSymbol() {
        String pwdNoSymbol = LoginPage.generatePasswordInvalidNoSymbol(9);
        System.out.println("Sending email = " + testUser.getUsername() + ", passwordNoSymbol = " + pwdNoSymbol);
        registrationAndAssertFailure(new UserLombok(testUser.getUsername(), pwdNoSymbol));
    }
}