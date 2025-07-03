package manager;

import dto.UserLombok;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import utils.BaseAPI;

import static io.restassured.RestAssured.given;

public class AuthenticationController implements BaseAPI {

    public Response requestRegLogin(UserLombok user, String url) {
        return given()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post(BASE_URL + url)
                .thenReturn()
                ;
    }
}