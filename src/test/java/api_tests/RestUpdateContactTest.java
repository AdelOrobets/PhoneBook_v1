package api_tests;

import dto.ContactLombok;
import dto.ErrorMessageDto;
import dto.ResponseMessageDto;
import io.restassured.response.Response;
import manager.ContactController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import utils.TestDataFactory;
import utils.TestNGListener;

@Listeners(TestNGListener.class)
public class RestUpdateContactTest extends ContactController {

    private static final Logger logger = LoggerFactory.getLogger(RestUpdateContactTest.class);

    private String createValidContactAndExtractId(ContactLombok contact) {
        Response response = addNewContactRequest(contact, tokenDto);
        return response.as(ResponseMessageDto.class).getMessage().split("ID: ")[1];
    }

    private void logResponse(Response response) {
        logger.info("[RESPONSE] Status Code: {}", response.getStatusCode());
        logger.info("[RESPONSE] Body:\n{}", response.getBody().asString());
    }

    private void validateBadRequest(Response response, String... expectedMessages) {
        logResponse(response);
        SoftAssert softAssert = new SoftAssert();

        ErrorMessageDto error = response.body().as(ErrorMessageDto.class);
        softAssert.assertEquals(response.getStatusCode(), 400, "Expected 400 Bad Request");
        softAssert.assertEquals(error.getError(), "Bad Request", "Unexpected error message");

        for (String msg : expectedMessages) {
            softAssert.assertTrue(response.getBody().asString().contains(msg),
                    "Expected error message to contain: " + msg);
        }
        softAssert.assertAll();
    }

    // Positive Tests
    @Test
    public void updateContactPositiveTest() {
        SoftAssert softAssert = new SoftAssert();

        ContactLombok original = TestDataFactory.validContactForAPI();
        String id = createValidContactAndExtractId(original);

        ContactLombok updated = ContactLombok.builder()
                .id(id)
                .name("UpdatedName")
                .lastName("UpdatedLastName")
                .phone("0501234567")
                .email("updated@updated.com")
                .address("Updated Address")
                .description("Updated Description")
                .build();

        Response updateResponse = updateContactRequest(updated, tokenDto);
        logResponse(updateResponse);

        ResponseMessageDto updateMessage = updateResponse.as(ResponseMessageDto.class);
        softAssert.assertEquals(updateResponse.getStatusCode(), 200,
                "Contact update failed");
        softAssert.assertTrue(updateMessage.getMessage().contains("Contact was updated"),
                "Expected update confirmation message");
        softAssert.assertAll();
    }

    // Negative Tests
    @Test
    public void updateContactNegativeTest_nullId() {
        ContactLombok contact = TestDataFactory.validContactForAPI();
        contact.setId(null);

        Response response = updateContactRequest(contact, tokenDto);
        validateBadRequest(response, "must not be blank");
    }

    @Test
    public void updateContactNegativeTest_withEmptyFields() {
        ContactLombok contact = TestDataFactory.allFieldsEmpty();

        Response response = updateContactRequest(contact, tokenDto);
        validateBadRequest(response, "must not be blank");
    }

    // BUG: server successfully updates contact with Invalid Name "@123"
    @Test
    public void updateContactNegativeTest_withInvalidName() {
        ContactLombok contact = TestDataFactory.validContactForAPI();
        contact.setId(createValidContactAndExtractId(contact));
        contact.setName("@123");

        Response response = updateContactRequest(contact, tokenDto);
        validateBadRequest(response, "must be a well-formed name");
    }

    @Test
    public void updateContactNegativeTest_withInvalidPhoneLetter() {
        ContactLombok contact = TestDataFactory.validContactForAPI();
        contact.setId(createValidContactAndExtractId(contact));
        contact.setPhone("050123456a");

        Response response = updateContactRequest(contact, tokenDto);
        validateBadRequest(response, "Phone number must contain only digits!");
    }

    @Test
    public void updateContactNegativeTest_withInvalidLengthPhone() {
        ContactLombok contact = TestDataFactory.validContactForAPI();
        contact.setId(createValidContactAndExtractId(contact));
        contact.setPhone("050123456");

        Response response = updateContactRequest(contact, tokenDto);
        validateBadRequest(response, "length min 10, max 15");
    }
}
