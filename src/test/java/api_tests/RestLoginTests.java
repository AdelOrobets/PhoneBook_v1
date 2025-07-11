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
import utils.TestDataFactory;
import utils.TestNGListener;

import java.time.LocalDate;

@Listeners(TestNGListener.class)
public class RestLoginTests extends AuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(RestLoginTests.class);

    private void assertStatusCode(Response response, int expectedCode, SoftAssert softAssert) {
        int statusCode = response.getStatusCode();
        logger.info("[RESPONSE] Status Code: {}", statusCode);
        softAssert.assertEquals(statusCode, expectedCode, "Unexpected status code");
    }

    private void assertUnauthorizedError(ErrorMessageDto errorMessageDto, SoftAssert softAssert) {
        softAssert.assertEquals(errorMessageDto.getMessage(), "Login or Password incorrect");
        softAssert.assertEquals(errorMessageDto.getError(), "Unauthorized");
        softAssert.assertTrue(errorMessageDto.getPath().contains("v1/user/login/usernamepassword"));
        logger.info("[TIMESTAMP]: {}", LocalDate.now());
        softAssert.assertEquals(LocalDate.now().toString(), errorMessageDto.getTimestamp().substring(0, 10));
    }

    @Test(groups = "smoke")
    public void loginPositiveTest_200() {
        UserLombok user = TestDataFactory.validUser();
        logger.info("Login new user: {}", user);

        register_void(user);
        Response response = login(user);

        SoftAssert softAssert = new SoftAssert();
        assertStatusCode(response, 200, softAssert);

        TokenDto tokenDto = response.body().as(TokenDto.class);
        logger.info("[TOKEN]: {}", tokenDto);
        softAssert.assertTrue(tokenDto.toString().contains("token"));
        softAssert.assertAll();
    }

    @Test
    public void loginNegativeTest_wrongEmail_401() {
        UserLombok user = TestDataFactory.invalidEmailNoAtSymbol();
        logger.info("Login user (wrong email): {}", user);

        Response response = login(user);
        SoftAssert softAssert = new SoftAssert();
        assertStatusCode(response, 401, softAssert);

        ErrorMessageDto error = response.body().as(ErrorMessageDto.class);
        logger.info("[ERROR MESSAGE]:\n{}", error);
        assertUnauthorizedError(error, softAssert);
        softAssert.assertAll();
    }

    @Test
    public void loginNegativeTest_emptyEmail_401() {
        UserLombok user = TestDataFactory.userWithoutEmail();
        logger.info("Login user (empty email): {}", user);

        Response response = login(user);
        SoftAssert softAssert = new SoftAssert();
        assertStatusCode(response, 401, softAssert);

        ErrorMessageDto error = response.body().as(ErrorMessageDto.class);
        logger.info("[ERROR MESSAGE]:\n{}", error);
        assertUnauthorizedError(error, softAssert);
        softAssert.assertAll();
    }

    @Test
    public void loginNegativeTest_wrongPassword_401() {
        UserLombok user = TestDataFactory.invalidPasswordNoSymbol();
        logger.info("Login user (wrong password): {}", user);

        Response response = login(user);
        SoftAssert softAssert = new SoftAssert();
        assertStatusCode(response, 401, softAssert);

        ErrorMessageDto error = response.body().as(ErrorMessageDto.class);
        logger.info("[ERROR MESSAGE]:\n{}", error);
        assertUnauthorizedError(error, softAssert);
        softAssert.assertAll();
    }

    @Test
    public void loginNegativeTest_emptyPassword_401() {
        UserLombok user = TestDataFactory.userWithoutPassword();
        logger.info("Login user (empty password): {}", user);

        Response response = login(user);
        SoftAssert softAssert = new SoftAssert();
        assertStatusCode(response, 401, softAssert);

        ErrorMessageDto error = response.body().as(ErrorMessageDto.class);
        logger.info("[ERROR MESSAGE]:\n{}", error);
        assertUnauthorizedError(error, softAssert);
        softAssert.assertAll();
    }
}
