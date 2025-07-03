package api_tests;

import dto.UserLombok;
import io.restassured.response.Response;
import manager.AuthenticationController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import utils.BaseAPI;
import utils.TestDataFactory;
import utils.TestNGListener;

@Listeners(TestNGListener.class)
public class RestRegistrationTests extends AuthenticationController implements BaseAPI {

    private static final Logger logger = LoggerFactory.getLogger(RestRegistrationTests.class);

    @Test
    public void registrationPositiveTest_200() {
        UserLombok user = TestDataFactory.validUser();
        logger.info("Registering new user: {}", user);
        Response response = register(user);

        int statusCode = response.getStatusCode();
        String responseBody = response.getBody().asString();
        logger.info("[RESPONSE] Status Code: {}", statusCode);
        logger.debug("[RESPONSE] Body:\n{}", responseBody);

        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(responseBody.contains("success")
                || responseBody.contains("ok"), "Unexpected body content");
    }

    @Test
    public void registrationNegativeTest_wrongEmail_400() {
        UserLombok user = TestDataFactory.invalidEmailNoAtSymbol();
        logger.info("Registering new user (wrong email): {}", user);
        Response response = register(user);

        int statusCode = response.getStatusCode();
        String responseBody = response.getBody().asString();
        logger.info("[RESPONSE] Status Code: {}", statusCode);
        logger.debug("[RESPONSE] Body:\n{}", responseBody);

        Assert.assertEquals(statusCode, 400,
                "Expected status code 400 for invalid email format");
    }

    @Test
    public void registrationNegativeTest_userAlreadyExists_409() {
        UserLombok user = TestDataFactory.validUser();
        logger.info("Registering user Already Exists: {}", user);
        register(user);
        Response response = register(user);

        int statusCode = response.getStatusCode();
        String responseBody = response.getBody().asString();
        logger.info("[RESPONSE] Status Code: {}", statusCode);
        logger.debug("[RESPONSE] Body:\n{}", responseBody);

        Assert.assertEquals(statusCode, 409,
                "Expected status 409 for duplicate registration");
    }

    private Response register(UserLombok user) {
        return requestRegLogin(user, REGISTRATION_URL);
    }
}
