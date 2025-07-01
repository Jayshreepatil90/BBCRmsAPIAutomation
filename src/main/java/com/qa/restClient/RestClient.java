package com.qa.restClient;

import static io.restassured.RestAssured.expect;
import static org.hamcrest.Matchers.*;

import java.io.File;
import java.util.Base64;
import java.util.Map;

import com.qa.constants.AuthType;
import com.qa.exceptions.FrameworkException;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import com.qa.utils.ConfigReader;


/**
 *
 * @author naveenautomationlabs
 *
 */
public class RestClient {

    //define Response Specs:
    private ResponseSpecification responseSpec200or404 = expect().statusCode(anyOf(equalTo(200), equalTo(404)));


    private RequestSpecification setupRequest(String baseUrl, AuthType authType, ContentType contentType) {

        RequestSpecification request =	RestAssured.given().log().all()
                .baseUri(baseUrl)
                .contentType(contentType)
                .accept(contentType);


        switch (authType) {
            case BEARER_TOKEN:
                request.header("Authorization", "Bearer "+ ConfigReader.get("bearerToken"));
                break;
            case CONTACTS_BEARER_TOKEN:
                request.header("Authorization", "Bearer "+ConfigReader.get("contacts_bearer_Token"));
                break;
            case OAUTH2:
                request.header("Authorization", "Bearer "+ generateOAuth2Token());
                break;
            case BASIC_AUTH:
                request.header("Authorization", "Basic " + generateBasicAuthToken());
                break;
            case API_KEY:
                request.header("x-api-key", ConfigReader.get("apiKey"));
                break;
            case NO_AUTH:
                System.out.println("Auth is not required...");
                break;
            default:
                System.out.println("This Auth is not supported...please pass the right AuthType...");
                throw new FrameworkException("NO AUTH SUPPORTED");
        }

        return request;

    }

    /**
     * this method is used to generate the Base64 encoded string
     * @return
     */
    private String generateBasicAuthToken() {
        String credentials = ConfigReader.get("basicUsername")+ ":" + ConfigReader.get("basicPassword");//admin:admin
        return Base64.getEncoder().encodeToString(credentials.getBytes());
    }


    //method to get OAuth2 token
    private String generateOAuth2Token() {
        return RestAssured.given()
                .formParam("client_id", ConfigReader.get("clientId"))
                .formParam("client_secret", ConfigReader.get("clientSecret"))
                .formParam("grant_type", ConfigReader.get("grantType"))
                .post(ConfigReader.get("tokenUrl"))
                .then()
                .extract()
                .path("access_token");
    }


    //**************************CRUD Methods*********************//

    /**
     * This method is used to call the GET APIs.
     * @param endPoint
     * @param queryParams
     * @param pathParams
     * @param authType
     * @param contentType
     * @return it returns the get api response
     */
    public Response get(String baseUrl, String endPoint, Map<String, String> queryParams,
                        Map<String, String> pathParams,
                        AuthType authType, ContentType contentType) {

        RequestSpecification request = setUpAuthAndContentType(baseUrl, authType, contentType);

        applyParams(request, queryParams, pathParams);

        Response response = request.get(endPoint).then().spec(responseSpec200or404).extract().response();
        response.prettyPrint();
        return response;
    }

    private RequestSpecification setUpAuthAndContentType(String baseUrl, AuthType authType, ContentType contentType) {
        return setupRequest(baseUrl, authType, contentType);
    }

    private void applyParams(RequestSpecification request, Map<String, String> queryParams, Map<String, String> pathParams) {
        if(queryParams!=null) {
            request.queryParams(queryParams);
        }
        if(pathParams!=null) {
            request.pathParams(pathParams);
        }
    }


}
