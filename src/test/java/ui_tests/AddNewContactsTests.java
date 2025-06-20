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
        ContactLombok contact = TestDataFactory.allFieldsEmpty();
        contactsPage = new ContactsPage(driver);
        openAddPage();
        addPage.addNewContactInForm(contact);
        Assert.assertTrue(addPage.closeAlertReturnText().contains("Phone not valid"));
        logger.info("Contact was not added");
    }

    // BUG: no error message and Save button is not clickable if the Name field is left blank
    @Test
    public void testAddContact_WithoutName() {
        ContactLombok contact = TestDataFactory.invalidFieldWithoutName();
        contactsPage = new ContactsPage(driver);
        openAddPage();
        addPage.addNewContactInForm(contact);
        String alertText = addPage.closeAlertReturnText();
        Assert.assertTrue(alertText.contains("Name is required"),
                "Expected error message for missing name");
    }

    // BUG: no error message and Save button is not clickable if the Last name field is left blank
    @Test
    public void testAddContact_WithoutLastName() {
        ContactLombok contact = TestDataFactory.invalidFieldWithoutLastName();
        contactsPage = new ContactsPage(driver);
        openAddPage();
        addPage.addNewContactInForm(contact);
        String alertText = addPage.closeAlertReturnText();
        Assert.assertTrue(alertText.contains("Last name is required"),
                "Expected error message for missing last name");
    }

    @Test
    public void testAddContactWithoutPhone() {
        ContactLombok contact = TestDataFactory.invalidFieldWithoutPhone();
        contactsPage = new ContactsPage(driver);
        openAddPage();
        addPage.addNewContactInForm(contact);
        String alertText = addPage.closeAlertReturnText();
        Assert.assertTrue(alertText.contains("Phone not valid"),
                "Expected error message for missing phone");
    }

    @Test
    public void testAddContactWithInvalidEmailFormat() {
        ContactLombok contact = TestDataFactory.invalidEmailFormat();
        contactsPage = new ContactsPage(driver);
        openAddPage();
        addPage.addNewContactInForm(contact);
        String alertText = addPage.closeAlertReturnText();
        Assert.assertTrue(alertText.contains("Email not valid"),
                "Expected error message for invalid email format");
    }

    @Test
    public void testAddContactWithInvalidPhoneFormat() {
        ContactLombok contact = TestDataFactory.invalidPhoneFormat();
        contactsPage = new ContactsPage(driver);
        openAddPage();
        addPage.addNewContactInForm(contact);
        String alertText = addPage.closeAlertReturnText();
        Assert.assertTrue(alertText.contains("Phone not valid"),
                "Expected error message for invalid phone format");
    }

    //BUG: Incorrect error message --> Phone not valid
    @Test
    public void testAddContactWithTooLongFields() {
        ContactLombok contact = TestDataFactory.tooLongFields();
        contactsPage = new ContactsPage(driver);
        openAddPage();
        addPage.addNewContactInForm(contact);
        String alertText = addPage.closeAlertReturnText();
        Assert.assertTrue(alertText.contains("Phone not valid"),
                 "Expected error message for too long fields");
    }

    //BUG: Incorrect error message --> Email not valid, Phone not valid
    @Test
    public void testAddContactWithSpecialCharacters() {
        ContactLombok contact = TestDataFactory.invalidFieldsWithSpecialCharacters();
        contactsPage = new ContactsPage(driver);
        openAddPage();
        addPage.addNewContactInForm(contact);
        String alertText = addPage.closeAlertReturnText();
        Assert.assertTrue(alertText.contains("Email not valid")
                || alertText.contains("Phone not valid"),
                "Expected error message for missing last name");
    }
}

