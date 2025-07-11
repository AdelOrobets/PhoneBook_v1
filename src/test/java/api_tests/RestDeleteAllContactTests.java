package api_tests;

import dto.ContactLombok;
import dto.ContactsDto;
import dto.ResponseMessageDto;
import io.restassured.response.Response;
import manager.ContactController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import utils.TestDataFactory;
import utils.TestNGListener;

@Listeners(TestNGListener.class)
public class RestDeleteAllContactTests extends ContactController {

    private static final Logger logger = LoggerFactory.getLogger(RestDeleteAllContactTests.class);

    private void logResponse(Response response) {
        logger.info("[RESPONSE] Status Code: {}", response.getStatusCode());
        logger.info("[RESPONSE] Body:\n{}", response.getBody().asString());
    }

    SoftAssert softAssert = new SoftAssert();

    @Test(groups = {"smoke", "contacts"})
    public void deleteAllContactsTest() {
        for (int i = 0; i < 3; i++) {
            ContactLombok contact = TestDataFactory.validContactForAPI();
            Response response = addNewContactRequest(contact, tokenDto);
            Assert.assertEquals(response.getStatusCode(), 200,
                    "Failed to create contact #" + i);
        }

        Response getResponse = getAllUserContacts();
        ContactsDto contactsDto = getResponse.as(ContactsDto.class);
        ContactLombok[] contacts = contactsDto.getContacts();

        if (contacts == null || contacts.length == 0) {
            logger.info("No contacts found to delete");
            softAssert.assertTrue(true, "No contacts to delete");
        } else {
            logger.info("Found {} contacts to delete", contacts.length);

            for (ContactLombok contact : contacts) {
                String id = contact.getId();
                logger.info("Deleting contact with ID: {}", id);

                Response deleteResponse = deleteContactByIdRequest(id, tokenDto);
                logResponse(deleteResponse);

                softAssert.assertEquals(deleteResponse.getStatusCode(), 200,
                        "Failed to delete contact with ID: " + id);

                ResponseMessageDto messageDto = deleteResponse.as(ResponseMessageDto.class);
                softAssert.assertTrue(messageDto.getMessage().contains("Contact was deleted"),
                        "Unexpected response message for contact ID: " + id);
            }
        }
        softAssert.assertAll();
    }
}
