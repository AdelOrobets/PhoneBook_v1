package manager;

import dto.ContactLombok;
import dto.TokenDto;
import dto.UserLombok;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.BeforeMethod;
import utils.BaseAPI;

import static io.restassured.RestAssured.given;
import static utils.PropertiesReader.getProperty;

public class ContactController implements BaseAPI {

    protected TokenDto tokenDto;

    @BeforeMethod(alwaysRun = true)
    public void login() {
        String email = getProperty("test_data.properties", "valid.email");
        String password = getProperty("test_data.properties", "valid.password");

        UserLombok user = new UserLombok(email, password);
        Response response = new AuthenticationController().requestRegLogin(user, LOGIN_URL);

        int statusCode = response.getStatusCode();
        if (statusCode == 200) {
            tokenDto = response.body().as(TokenDto.class);
        } else {
            throw new RuntimeException("Login failed before suite started");
        }
    }

    // POST request to create new contact
    protected Response addNewContactRequest(ContactLombok contact, TokenDto token) {
        return given()
                .baseUri(getProperty("config.properties", "baseUrlBackend"))
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header("Authorization", token.getToken())
                .body(contact)
                .post(ADD_NEW_CONTACT_URL)
                .thenReturn();
    }

   // GET request to retrieve all contacts
    public Response getAllUserContacts() {
        return given()
                .baseUri(getProperty("config.properties", "baseUrlBackend"))
                .accept(ContentType.JSON)
                .header("Authorization", tokenDto.getToken())
                .get(ADD_NEW_CONTACT_URL)
                .thenReturn();
    }

    // PUT request to update contact
    public Response updateContactRequest(ContactLombok contact, TokenDto token) {
        return given()
                .baseUri(getProperty("config.properties", "baseUrlBackend"))
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header("Authorization", token.getToken())
                .body(contact)
                .put(ADD_NEW_CONTACT_URL)
                .thenReturn();
    }

    // DELETE request by Id
    protected Response deleteContactByIdRequest(String id, TokenDto token) {
        return given()
                .baseUri(getProperty("config.properties", "baseUrlBackend"))
                .header("Authorization", token.getToken())
                .delete(ADD_NEW_CONTACT_URL + "/" + id)
                .thenReturn();
    }
}
