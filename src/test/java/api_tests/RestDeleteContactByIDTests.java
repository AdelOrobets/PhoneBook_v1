package api_tests;

import dto.ContactLombok;
import dto.ErrorMessageDto;
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

import static io.restassured.RestAssured.given;
import static utils.PropertiesReader.getProperty;

@Listeners(TestNGListener.class)
public class RestDeleteContactByIDTests extends ContactController {

    private static final Logger logger = LoggerFactory.getLogger(RestDeleteContactByIDTests.class);

    private void logResponse(Response response) {
        logger.info("[RESPONSE] Status Code: {}", response.getStatusCode());
        logger.info("[RESPONSE] Body:\n{}", response.getBody().asString());
    }

    private String extractContactId(ContactLombok contact) {
        Response response = addNewContactRequest(contact, tokenDto);
        return response.as(ResponseMessageDto.class).getMessage().split("ID: ")[1];
    }

    private void validateErrorResponse(Response response, int expectedStatusCode,
                                       String expectedError, String expectedMessagePart) {
        logResponse(response);
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(response.getStatusCode(), expectedStatusCode,
                "Unexpected status code");

        ErrorMessageDto error = response.as(ErrorMessageDto.class);
        softAssert.assertEquals(error.getError(), expectedError, "Unexpected error value");
        softAssert.assertTrue(error.getMessage().toString().contains(expectedMessagePart),
                "Expected error message to contain: " + expectedMessagePart);
        softAssert.assertAll();
    }

    // Positive Test
    @Test(groups = {"smoke", "contacts"})
    public void deleteContactByIDPositiveTest200() {
        ContactLombok contact = TestDataFactory.validContactForAPI();
        String id = extractContactId(contact);

        Response deleteResponse = deleteContactByIdRequest(id, tokenDto);
        logResponse(deleteResponse);

        ResponseMessageDto messageDto = deleteResponse.as(ResponseMessageDto.class);
        Assert.assertTrue(messageDto.getMessage().contains("Contact was deleted!"),
                "Expected deletion message");
    }

    // Negative Tests
    /**
     * BUG: The backend returns 403 Forbidden with an empty body
     * instead of 401 Unauthorized with a proper JSON error message
     *
     * Expected behavior:
     * - HTTP status: 401 Unauthorized
     * - Content-Type: application/json
     * - JSON body with error details
     *
     * Actual behavior:
     * - HTTP status: 403 Forbidden
     * - No Content-Type header
     * - Empty response body
     *
     * This behavior violates standard REST API authentication practices
     */
    @Test
    public void deleteContactWithoutTokenNegativeTest401() {
        ContactLombok contact = TestDataFactory.validContactForAPI();
        String id = extractContactId(contact);

        Response deleteResponse = given()
                .baseUri(getProperty("config.properties", "baseUrlBackend"))
                .delete(ADD_NEW_CONTACT_URL + "/" + id)
                .thenReturn();

        logResponse(deleteResponse);
        Assert.assertEquals(deleteResponse.getStatusCode(), 401,
                "Expected 401 Unauthorized");

        String contentType = deleteResponse.getHeader("Content-Type");
        if (contentType != null && contentType.contains("application/json")) {
            ErrorMessageDto error = deleteResponse.as(ErrorMessageDto.class);
            Assert.assertEquals(error.getError(), "Unauthorized");
        } else {
            logger.info("BUG: Backend did not return a JSON body for unauthorized DELETE request");
        }
    }

    /**
     * BUG: Server returns 400 Bad Request instead of 404 Not Found
     * when trying to delete a non-existing contact
     *
     * Expected:
     *   - HTTP status: 404 Not Found
     *   - JSON error message: "Contact with ID ... not found"
     *
     * Actual:
     *   - HTTP status: 400 Bad Request
     */
    @Test
    public void deleteNonExistingContactNegativeTest404() {
        String invalidId = "1234567890abcdef123456789";
        Response response = deleteContactByIdRequest(invalidId, tokenDto);
        validateErrorResponse(response, 404, "Not Found",
                "not found");
    }
}
