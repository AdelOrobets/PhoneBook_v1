package api_tests;

import dto.ContactsDto;
import dto.ContactLombok;
import io.restassured.response.Response;
import manager.ContactController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

@Listeners(utils.TestNGListener.class)
public class RestGetUserContactsTests extends ContactController {

    private static final Logger logger = LoggerFactory.getLogger(RestGetUserContactsTests.class);

    @Test
    public void getAllUserContactsPositiveTest() {
        Response response = getAllUserContacts();

        int statusCode = response.getStatusCode();
        logger.info("[RESPONSE] status Code: {}", statusCode);
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(statusCode, 200, "Expected status code 200");

        ContactsDto contactsDto = response.body().as(ContactsDto.class);
        ContactLombok[] contactsArray = contactsDto.getContacts();
        softAssert.assertNotNull(contactsArray, "Contacts array should not be null");
        softAssert.assertTrue(contactsArray.length > 0, "Expected at least one contact");
        logger.info("[CONTACTS COUNT] {}", contactsArray.length);

        for (int i = 0; i < contactsArray.length; i++) {
            logger.info("[CONTACT #{}] {}", i + 1, contactsArray[i]);
        }

        softAssert.assertAll();
    }
}
