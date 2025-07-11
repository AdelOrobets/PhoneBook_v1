package api_tests;

import dto.ContactLombok;
import dto.ErrorMessageDto;
import dto.ResponseMessageDto;
import dto.TokenDto;
import io.restassured.response.Response;
import manager.ContactController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import utils.TestDataFactory;
import utils.TestNGListener;

import java.time.LocalDate;

@Listeners(TestNGListener.class)
public class RestAddNewContactTests extends ContactController {

    private static final Logger logger = LoggerFactory.getLogger(RestAddNewContactTests.class);

    private void logResponse(Response response) {
        logger.info("[RESPONSE] Status Code: {}", response.getStatusCode());
        logger.info("[RESPONSE] Body:\n{}", response.getBody().asString());
    }

    private void assertErrorResponse(Response response, int expectedStatus,
                                     String expectedError, String expectedMessagePart) {
        SoftAssert softAssert = new SoftAssert();
        ErrorMessageDto error = response.as(ErrorMessageDto.class);
        softAssert.assertEquals(response.getStatusCode(), expectedStatus, "Unexpected status code");
        softAssert.assertEquals(error.getError(), expectedError, "Unexpected error type");
        softAssert.assertTrue(error.getMessage().toString().contains(expectedMessagePart),
                "Message doesn't contain expected part");
        softAssert.assertAll();
    }

    // Positive Tests
    @Test(retryAnalyzer = utils.RetryAnalyzer.class, groups = {"smoke", "contacts"})
    public void addNewContactPositiveTest() {
        ContactLombok contact = TestDataFactory.validContactForAPI();
        Response response = addNewContactRequest(contact, tokenDto);
        logResponse(response);

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(response.getStatusCode(), 200, "Expected 200 OK");

        if (response.getStatusCode() == 200) {
            ResponseMessageDto messageDto = response.as(ResponseMessageDto.class);
            softAssert.assertTrue(messageDto.getMessage().contains("Contact was added"),
                    "Expected success message in response");
        }
        softAssert.assertAll();
    }

    // Negative Tests
    @Test
    public void addNewContactNegativeTest_wrongToken() {
        ContactLombok contact = TestDataFactory.validContactForAPI();
        TokenDto wrongToken = TokenDto.builder().token("wrong").build();

        Response response = addNewContactRequest(contact, wrongToken);
        logResponse(response);

        SoftAssert softAssert = new SoftAssert();
        ErrorMessageDto error = response.as(ErrorMessageDto.class);

        softAssert.assertEquals(response.getStatusCode(), 401, "Expected 401 Unauthorized");
        softAssert.assertEquals(error.getError(), "Unauthorized");
        softAssert.assertTrue(error.getMessage().toString()
                .contains("JWT strings must contain exactly 2 period characters"));
        softAssert.assertTrue(error.getPath().contains("/v1/contacts"));
        softAssert.assertEquals(LocalDate.now().toString(), error.getTimestamp().substring(0, 10));
        softAssert.assertAll();
    }

    @Test
    public void addNewContactNegativeTest_withEmptyFields() {
        ContactLombok contact = TestDataFactory.allFieldsEmpty();
        Response response = addNewContactRequest(contact, tokenDto);
        logResponse(response);
        assertErrorResponse(response, 400, "Bad Request",
                "must not be blank");
    }

    // BUG: server successfully updates contact with Invalid Name "@123"
    @Test
    public void addNewContactNegativeTest_withInvalidName() {
        ContactLombok contact = TestDataFactory.validContactForAPI();
        contact.setName("@123");
        Response response = addNewContactRequest(contact, tokenDto);
        logResponse(response);
        assertErrorResponse(response, 400, "Bad Request",
                "must be a well-formed name");
    }

    @Test
    public void addNewContactNegativeTest_withInvalidPhoneLetter() {
        ContactLombok contact = TestDataFactory.validContactForAPI();
        contact.setPhone("12345a7890");
        Response response = addNewContactRequest(contact, tokenDto);
        logResponse(response);
        assertErrorResponse(response, 400, "Bad Request",
                "Phone number must contain only digits!");
    }

    @Test
    public void addNewContactNegativeTest_withInvalidLengthPhone() {
        ContactLombok contact = TestDataFactory.validContactForAPI();
        contact.setPhone("123456");  // меньше 10 символов
        Response response = addNewContactRequest(contact, tokenDto);
        logResponse(response);
        assertErrorResponse(response, 400, "Bad Request",
                "length min 10, max 15");
    }
}
