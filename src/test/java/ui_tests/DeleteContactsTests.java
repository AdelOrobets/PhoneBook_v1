package ui_tests;

import dto.ContactLombok;
import dto.UserLombok;
import manager.ApplicationManager;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pages.*;
import utils.TestDataFactory;
import utils.TestNGListener;

import java.util.List;

@Listeners(TestNGListener.class)
public class DeleteContactsTests extends ApplicationManager {

    private static final Logger logger = LoggerFactory.getLogger(DeleteContactsTests.class);

    SoftAssert softAssert = new SoftAssert();

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
    @Test(groups = "smoke")
    public void testDeleteContactByName() {
        ContactLombok contact = addContact();
        String name = contact.getName();
        List<WebElement> contacts = contactsPage.getContactElements();

        Assert.assertTrue(contactsPage.isContactPresentInList(name, contacts),
                "Contact is present before deletion");
        contactsPage.deleteContactByName(name);
        Assert.assertTrue(contactsPage.isContactAbsentByName(name, contacts),
                "Contact should be deleted");
    }

    // Negative tests
    @Test
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
