package ui_tests;

import dto.ContactLombok;
import dto.UserLombok;
import manager.ApplicationManager;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pages.*;
import utils.TestDataFactory;
import utils.TestNGListener;

import java.time.Duration;
import java.util.List;

@Listeners(TestNGListener.class)
public class DeleteContactsTests extends ApplicationManager {

    @BeforeMethod(alwaysRun = true)
    public void registrationUser() {
        UserLombok user = TestDataFactory.validUser();
        openLoginPage();
        loginPage.typeRegistrationForm(user);
    }

    public ContactLombok addContact(){
        contactsPage = new ContactsPage(driver);
        openAddPage();
        ContactLombok contact = TestDataFactory.validContact();
        addPage.fillContactForm(contact);
        return contact;
    }

    // Positive tests
    @Test(groups = {"smoke", "regression"})
    public void testDeleteContactByName() {
        SoftAssert softAssert = new SoftAssert();

        ContactLombok contact = addContact();
        String name = contact.getName();
        List<WebElement> initialContacts = contactsPage.getContactElements();
        softAssert.assertTrue(contactsPage.isContactPresentInList(name, initialContacts),
                "Contact is not present before deletion");

        contactsPage.deleteContactByName(name);
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(driver ->
                !contactsPage.isContactPresentInList(name, contactsPage.getContactElements()));

        List<WebElement> updatedContacts = contactsPage.getContactElements();
        softAssert.assertFalse(contactsPage.isContactPresentInList(name, updatedContacts),
                "Contact was not deleted");
        softAssert.assertAll();
    }

    // Negative tests
    @Test(groups = "regression")
    public void testDeleteContact_withInvalidName() {
        ContactLombok contact = addContact();
        String invalidName = "invalidName";
        List<WebElement> contactsBefore = contactsPage.getContactElements();

        Assert.assertFalse(contactsPage.isContactPresentInList(invalidName, contactsBefore),
                "Contact with invalid name is unexpectedly present");

        boolean deleted = contactsPage.deleteContactByInvalidName(invalidName);
        Assert.assertFalse(deleted, "Contact with invalid name was deleted");

        List<WebElement> contactsAfter = contactsPage.getContactElements();
        Assert.assertEquals(contactsAfter.size(), contactsBefore.size(),
                "Contacts list size changed after invalid deletion attempt");
    }
}
