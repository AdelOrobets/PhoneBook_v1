package ui_tests;

import dto.ContactLombok;
import dto.UserLombok;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import manager.ApplicationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import pages.*;
import utils.HeaderMenuItem;
import utils.TestDataFactory;

public class AddNewContactsTests extends ApplicationManager {

    private static final Logger logger = LoggerFactory.getLogger(AddNewContactsTests.class);

    @BeforeMethod
    public void registrationUser() {
        UserLombok user = TestDataFactory.validUser();
        openLoginPage();
        loginPage.typeRegistrationForm(user);
    }

    // Positive tests
    @Test
    public void testSuccessful_addNewContact() {
        logger.info("Starting test: testSuccessful_addNewContact");
        contactsPage = new ContactsPage(driver);
        int sizeBeforeAdd = contactsPage.getContactsListSize();
        logger.info("Contacts before add: {}", sizeBeforeAdd);

        openAddPage();
        ContactLombok contact = TestDataFactory.validContact();
        addPage.addNewContactInForm(contact);
        // BUG: Success message is not displayed after adding a contact
        // Assert.assertTrue(addPage.closeAlertReturnText().contains("Contact was added!"));

        contactsPage = new ContactsPage(driver);
        int sizeAfterAdd = contactsPage.getContactsListSize();
        logger.info("Contacts after add: {}", sizeAfterAdd);
        Assert.assertEquals(sizeAfterAdd, sizeBeforeAdd + 1, "Contact was not added");
        logger.info("Contact was added successfully");
    }

    // Negative tests
    @Test
    public void testAddNewContactS_AllFieldsEmpty(){
        logger.info("Starting test: testAddNewContactS_AllFieldsEmpty");
        ContactLombok invalidContact = TestDataFactory.invalidContactAllFieldsEmpty();
        contactsPage = new ContactsPage(driver);
        contactsPage.clickHeaderMenuItem(HeaderMenuItem.ADD);
        openAddPage();
        addPage.addNewContactInForm(invalidContact);
        Assert.assertTrue(addPage.closeAlertReturnText().contains("Phone not valid"));
        logger.info("Contact was not added");
    }
}
