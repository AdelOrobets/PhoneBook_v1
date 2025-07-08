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

import java.time.LocalDate;

@Listeners(utils.TestNGListener.class)
public class RestAddNewContactTests extends ContactController {

    private static final Logger logger = LoggerFactory.getLogger(RestAddNewContactTests.class);

    @Test
    public void addNewContactPositiveTest() {
        ContactLombok contact = TestDataFactory.validContactForAPI();
        Response response = addNewContactRequest(contact, tokenDto);

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(response.getStatusCode(), 200, "Expected 200 OK");
        logger.info("[RESPONSE] Status Code: {}", response.getStatusCode());
        logger.info("[RESPONSE] Body:\n{}", response.getBody().asString());

        if (response.getStatusCode() == 200) {
            ResponseMessageDto messageDto = response.body().as(ResponseMessageDto.class);
            softAssert.assertTrue(messageDto.getMessage().contains("Contact was added!"),
                    "Expected success message in response");
        }

        softAssert.assertAll();
    }

    @Test
    public void addNewContactNegativeTest_wrongToken() {
        ContactLombok contact = TestDataFactory.validContactForAPI();
        TokenDto wrongToken = TokenDto.builder().token("wrong").build();
        Response response = addNewContactRequest(contact, wrongToken);

        ErrorMessageDto error = response.body().as(ErrorMessageDto.class);

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(error.getStatus(), 401, "Expected status 401 Unauthorized");
        logger.info("[RESPONSE] Status Code: {}", response.getStatusCode());
        logger.info("[RESPONSE] Body:\n{}", response.getBody().asString());
        softAssert.assertEquals(error.getError(), "Unauthorized");
        softAssert.assertTrue(error.getMessage().toString().contains("JWT strings must contain exactly 2 period characters"));
        softAssert.assertTrue(error.getPath().contains("/v1/contacts"));
        logger.info("[TIMESTAMP]: {}", LocalDate.now());
        softAssert.assertEquals(LocalDate.now().toString(), error.getTimestamp().substring(0, 10));

        softAssert.assertAll();
    }
}
