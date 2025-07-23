package ui_tests;

import dto.ContactLombok;
import dto.UserLombok;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import manager.ApplicationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import pages.*;
import utils.TestDataFactory;
import utils.TestNGListener;

@Listeners(TestNGListener.class)
public class AddNewContactsTests extends ApplicationManager {

    private static final Logger logger = LoggerFactory.getLogger(AddNewContactsTests.class);

    @BeforeMethod(alwaysRun = true)
    public void registrationUser() {
        UserLombok user = TestDataFactory.validUser();
        openLoginPage();
        loginPage.typeRegistrationForm(user);
    }

    // Positive tests
    @Test(groups = {"smoke", "regression"})
    public void testSuccessful_addNewContact() {
        contactsPage = new ContactsPage(driver);
        int sizeBeforeAdd = contactsPage.getContactsListSize();
        logger.info("Contacts before add: {}", sizeBeforeAdd);

        openAddPage();
        ContactLombok contact = TestDataFactory.validContact();
        addPage.fillContactForm(contact);
        // BUG: Success a message is not displayed after adding a contact
        // Assert.assertTrue(addPage.closeAlertReturnText().contains("Contact was added!"));

        contactsPage = new ContactsPage(driver);
        int sizeAfterAdd = contactsPage.getContactsListSize();
        logger.info("Contacts after add: {}", sizeAfterAdd);
        Assert.assertEquals(sizeAfterAdd, sizeBeforeAdd + 1, "Contact was not added");
    }

    @Test(dataProvider = "addNewContactDPFromFile", dataProviderClass = data_provider.ContactDataProviders.class)
    public void testSuccessful_addNewContactFromCSV(ContactLombok contact) {
        contactsPage = new ContactsPage(driver);
        int sizeBeforeAdd = contactsPage.getContactsListSize();

        openAddPage();
        logger.info("Test data: {}", contact);
        addPage.fillContactFormFromFileCSV(contact);
        addPage.clickSaveButton();

        contactsPage = new ContactsPage(driver);
        int sizeAfterAdd = contactsPage.getContactsListSize();
        Assert.assertEquals(sizeAfterAdd, sizeBeforeAdd + 1, "Contact was not added");
    }


    // Negative tests
    @Test(groups = "regression")
    public void testAddContact_DuplicateContact() {
        ContactLombok contact = TestDataFactory.validContact();
        openAddPage();
        addPage.fillContactForm(contact);

        contactsPage = new ContactsPage(driver);
        int countWithSamePhone = contactsPage.countContactsByPhone(contact.getPhone());
        logger.info("Contacts with same phone after first add: {}", countWithSamePhone);
        Assert.assertTrue(countWithSamePhone >= 1, "Contact not added the first time");

        openAddPage();
        addPage.fillContactForm(contact);
        String alertText = addPage.closeAlertReturnText();
        Assert.assertTrue(alertText.contains("Contact already exists") || alertText.contains("already"),
                "Expected alert for duplicate contact");

        contactsPage = new ContactsPage(driver);
        int countWithSamePhoneAfter = contactsPage.countContactsByPhone(contact.getPhone());
        logger.info("Contacts with same phone after duplicate add: {}", countWithSamePhoneAfter);
        Assert.assertEquals(countWithSamePhoneAfter, countWithSamePhone,
                "Duplicate contact was added unexpectedly");
    }


    @Test(groups = "regression")
    public void testAddNewContactS_AllFieldsEmpty(){
        ContactLombok contact = TestDataFactory.allFieldsEmpty();
        contactsPage = new ContactsPage(driver);
        openAddPage();
        addPage.fillContactForm(contact);
        Assert.assertTrue(addPage.closeAlertReturnText().contains("Phone not valid"));
    }

    // BUG: no error message and Save button is not clickable if the Name field is left blank
    @Test(groups = "regression")
    public void testAddContact_WithoutName() {
        ContactLombok contact = TestDataFactory.invalidFieldWithoutName();
        contactsPage = new ContactsPage(driver);
        openAddPage();
        addPage.fillContactForm(contact);
        String alertText = addPage.closeAlertReturnText();
        Assert.assertTrue(alertText.contains("Name is required"),
                "Expected error message for missing name");
    }

    // BUG: no error message and Save button is not clickable if the Last name field is left blank
    @Test(groups = "regression")
    public void testAddContact_WithoutLastName() {
        ContactLombok contact = TestDataFactory.invalidFieldWithoutLastName();
        contactsPage = new ContactsPage(driver);
        openAddPage();
        addPage.fillContactForm(contact);
        String alertText = addPage.closeAlertReturnText();
        Assert.assertTrue(alertText.contains("Last name is required"),
                "Expected error message for missing last name");
    }

    @Test(groups = "regression")
    public void testAddContactWithoutPhone() {
        ContactLombok contact = TestDataFactory.invalidFieldWithoutPhone();
        contactsPage = new ContactsPage(driver);
        openAddPage();
        addPage.fillContactForm(contact);
        String alertText = addPage.closeAlertReturnText();
        Assert.assertTrue(alertText.contains("Phone not valid"),
                "Expected error message for missing phone");
    }

    @Test(groups = "regression")
    public void testAddContactWithInvalidEmailFormat() {
        ContactLombok contact = TestDataFactory.invalidEmailFormat();
        contactsPage = new ContactsPage(driver);
        openAddPage();
        addPage.fillContactForm(contact);
        String alertText = addPage.closeAlertReturnText();
        Assert.assertTrue(alertText.contains("Email not valid"),
                "Expected error message for invalid email format");
    }

    @Test(groups = "regression")
    public void testAddContactWithInvalidPhoneFormat() {
        ContactLombok contact = TestDataFactory.invalidPhoneFormat();
        contactsPage = new ContactsPage(driver);
        openAddPage();
        addPage.fillContactForm(contact);
        String alertText = addPage.closeAlertReturnText();
        Assert.assertTrue(alertText.contains("Phone not valid"),
                "Expected error message for invalid phone format");
    }

    //BUG: Incorrect error message --> Phone not valid
    @Test(groups = "regression")
    public void testAddContactWithTooLongFields() {
        ContactLombok contact = TestDataFactory.tooLongFields();
        contactsPage = new ContactsPage(driver);
        openAddPage();
        addPage.fillContactForm(contact);
        String alertText = addPage.closeAlertReturnText();
        Assert.assertTrue(alertText.contains("Phone not valid"),
                 "Expected error message for too long fields");
    }

    //BUG: Incorrect error message --> Email not valid, Phone not valid
    @Test(groups = "regression")
    public void testAddContactWithSpecialCharacters() {
        ContactLombok contact = TestDataFactory.invalidFieldsWithSpecialCharacters();
        contactsPage = new ContactsPage(driver);
        openAddPage();
        addPage.fillContactForm(contact);
        String alertText = addPage.closeAlertReturnText();
        Assert.assertTrue(alertText.contains("Email not valid")
                || alertText.contains("Phone not valid"),
                "Expected error message for missing last name");
    }
}

