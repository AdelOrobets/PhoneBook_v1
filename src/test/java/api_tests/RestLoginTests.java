package api_tests;

import dto.UserLombok;
import io.restassured.response.Response;
import manager.AuthenticationController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import utils.TestDataFactory;
import utils.TestNGListener;

@Listeners(TestNGListener.class)
public class RestLoginTests extends AuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(RestLoginTests.class);

    @Test
    public void loginPositiveTest_200() {
        UserLombok user = TestDataFactory.validUser();
        logger.info("Login new user: {}", user);
        register(user);
        Response response = login(user);

        int statusCode = response.getStatusCode();
        logger.info("[RESPONSE] Status Code: {}", statusCode);

        Assert.assertEquals(statusCode, 200,
                "Expected status code 200 on successful login");
    }

    @Test
    public void loginNegativeTest_wrongEmail_401() {
        UserLombok user = TestDataFactory.invalidEmailNoAtSymbol();
        logger.info("Login user (wrong email): {}", user);
        Response response = login(user);

        int statusCode = response.getStatusCode();
        logger.info("[RESPONSE] Status Code: {}", statusCode);

        Assert.assertEquals(statusCode, 401,
                "Expected status code 401 for invalid email format");
    }

    private void register(UserLombok user) {
        requestRegLogin(user, REGISTRATION_URL);
    }

    private Response login(UserLombok user) {
        return requestRegLogin(user, LOGIN_URL);
    }
}
