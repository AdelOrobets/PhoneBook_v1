package api_tests;

import dto.ErrorMessageDto;
import dto.TokenDto;
import dto.UserLombok;
import io.restassured.response.Response;
import manager.AuthenticationController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import utils.BaseAPI;
import utils.TestDataFactory;
import utils.TestNGListener;

import java.time.LocalDate;

@Listeners(TestNGListener.class)
public class RestRegistrationTests extends AuthenticationController implements BaseAPI {

    private static final Logger logger = LoggerFactory.getLogger(RestRegistrationTests.class);

    SoftAssert softAssert = new SoftAssert();

    private void logResponse(Response response) {
        String responseBody = response.getBody().asString();
        logger.info("[RESPONSE] Body:\n{}", responseBody);
    }

    private void assertStatusCode(Response response, int expectedCode, SoftAssert softAssert) {
        int statusCode = response.getStatusCode();
        logger.info("[RESPONSE] Status Code: {}", statusCode);
        softAssert.assertEquals(statusCode, expectedCode, "Unexpected status code");
    }

    private void assertBadRequest(ErrorMessageDto errorMessageDto, SoftAssert softAssert) {
        softAssert.assertEquals(errorMessageDto.getError(), "Bad Request");
        softAssert.assertTrue(errorMessageDto.getPath().contains("/v1/user/registration/usernamepassword"));
        logger.info("[TIMESTAMP]: {}", LocalDate.now());
        softAssert.assertEquals(LocalDate.now().toString(), errorMessageDto.getTimestamp().substring(0, 10));
    }

    @Test(groups = {"smoke", "regression"})
    public void registrationPositiveTest_200() {
        UserLombok user = TestDataFactory.validUser();
        logger.info("Registering new user: {}", user);

        Response response = register_returnResponse(user);
        logResponse(response);

        assertStatusCode(response, 200, softAssert);
        TokenDto tokenDto = response.body().as(TokenDto.class);
        logger.info("[TOKEN]: {}", tokenDto);
        softAssert.assertTrue(tokenDto.toString().contains("token"));
        softAssert.assertAll();
    }

    @Test(groups = "regression")
    public void registrationNegativeTest_wrongEmail_400() {
        UserLombok user = TestDataFactory.invalidEmailNoAtSymbol();
        logger.info("Registering new user (wrong email): {}", user);

        Response response = register_returnResponse(user);
        logResponse(response);

        assertStatusCode(response, 400, softAssert);
        ErrorMessageDto error = response.body().as(ErrorMessageDto.class);
        logger.info("[ERROR MESSAGE]:\n{}", error);
        assertBadRequest(error, softAssert);
        softAssert.assertTrue(error.getMessage().toString().contains("must be a well-formed email address"));
        softAssert.assertAll();
    }

    @Test(groups = "regression")
    public void registrationNegativeTest_emptyEmail_400() {
        UserLombok user = TestDataFactory.userWithoutEmail();
        logger.info("Registering new user (empty email): {}", user);

        Response response = register_returnResponse(user);
        logResponse(response);

        assertStatusCode(response, 400, softAssert);
        ErrorMessageDto error = response.body().as(ErrorMessageDto.class);
        logger.info("[ERROR MESSAGE]:\n{}", error);
        assertBadRequest(error, softAssert);
        softAssert.assertTrue(error.getMessage().toString().contains("must not be blank"));
        softAssert.assertAll();
    }


    // BUG: The server response does not match the expected JSON schema
    // Expected format for "timestamp" is ISO 8601 string ("2025-07-08T16:47:28.902Z"),
    // but we receive a long Unix timestamp (1751992757573)
    // Expected schema:
    // {
    //   "timestamp": "2025-07-08T16:47:28.902Z",
    //   "status": 0,
    //   "error": "string",
    //   "message": {},
    //   "path": "string"
    // }
    // Actual response:
    // timestamp=1751992757573, status=400, error=Bad Request, message=null,
    // path=/v1/user/registration/usernamepassword)
    @Test(groups = "regression")
    public void registrationNegativeTest_emptyPassword_400() {
        UserLombok user = TestDataFactory.userWithoutPassword();
        logger.info("Registering new user (empty password): {}", user);

        Response response = register_returnResponse(user);
        logResponse(response);

        assertStatusCode(response, 400, softAssert);
        ErrorMessageDto error = response.body().as(ErrorMessageDto.class);
        logger.info("[ERROR MESSAGE]:\n{}", error);
        assertBadRequest(error, softAssert);
        softAssert.assertAll();
    }

    @Test(groups = "regression")
    public void registrationNegativeTest_userAlreadyExists_409() {
        UserLombok user = TestDataFactory.validUser();
        logger.info("Registering user Already Exists: {}", user);
        register_void(user);

        Response response = register_returnResponse(user);
        logResponse(response);

        assertStatusCode(response, 409, softAssert);
        ErrorMessageDto error = response.body().as(ErrorMessageDto.class);
        softAssert.assertEquals(error.getError(), "Conflict");
        softAssert.assertTrue(error.getMessage().toString().contains("User already exists"));
        softAssert.assertTrue(error.getPath().contains("/v1/user/registration/usernamepassword"));
        logger.info("[ERROR MESSAGE]:\n{}", error);
        logger.info("[TIMESTAMP]: {}", LocalDate.now());
        softAssert.assertEquals(LocalDate.now().toString(), error.getTimestamp().substring(0, 10));
        softAssert.assertAll();
    }
}
