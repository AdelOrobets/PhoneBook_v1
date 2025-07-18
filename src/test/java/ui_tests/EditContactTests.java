package ui_tests;

import dto.ContactLombok;
import dto.UserLombok;
import manager.ApplicationManager;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pages.ContactsPage;
import utils.TestDataFactory;
import utils.TestNGListener;

import java.util.List;

@Listeners(TestNGListener.class)
public class EditContactTests extends ApplicationManager {

    private static final Logger logger = LoggerFactory.getLogger(EditContactTests.class);

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

    // Positive Tests
    @Test(groups = {"smoke", "regression"})
    public void testEditContactByName() {
        ContactLombok contact = addContact();
        String name = contact.getName();
        List<WebElement> contacts = contactsPage.getContactElements();
        softAssert.assertTrue(contactsPage.isContactPresentInList(name, contacts),
                "Contact is not present in the list after adding");

        ContactLombok editedContact = TestDataFactory.editedContact(contact);
        String newName = editedContact.getName();
        logger.info("Edited contact generated with new name: {}", newName);

        contactsPage.openContactCardByName(name);
        contactsPage.clickEditButton();
        addPage.clearContactForm();
        addPage.fillContactForm(editedContact);

        contactsPage.waitContactIsUpdated(name, newName);
        List<WebElement> updatedContacts = contactsPage.getContactElements();
        boolean oldContact = contactsPage.isContactAbsentByName(name, updatedContacts);
        boolean newContact = contactsPage.isContactPresentInList(newName, updatedContacts);

        softAssert.assertTrue(oldContact, "Original contact is still present after editing");
        softAssert.assertTrue(newContact, "Edited contact is not present after editing");
        logger.info("Contact '{}' successfully updated to '{}'", name, newName);
        softAssert.assertAll();
    }
}
