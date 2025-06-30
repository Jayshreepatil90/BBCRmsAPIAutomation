package stepdefinitions;

import base.BaseTest;
import com.qa.constants.AuthType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static io.restassured.path.json.JsonPath.from;

public class APIDemoDefinitions {

    private Response response;
    private String responseBody;
    protected  BaseTest baseTest;


    public APIDemoDefinitions(){
        baseTest = new BaseTest();
    }

    @Given("I send a request to the URL to get episode details")
    public void i_send_a_request_to_the_url_to_get_episode_details() {
        response =  baseTest.restClient.get("https://testapi.io/api/RMSTest/ibltest","https://testapi.io/api/RMSTest/ibltest",null,null, AuthType.NO_AUTH,ContentType.JSON );
        responseBody = response.getBody().asString();
    }


    @Then("the response will return status code")
    public void the_response_will_return_status_code(List<Map<String, String>> StepData) {
        for(Map<String, String> s: StepData) {
            Assert.assertEquals(response.statusCode(), Integer.parseInt(s.get("statusCode")));
        }
    }

    @Then("the response time should be less than {int} milliseconds")
    public void the_response_time_should_be_less_than(int maxTimeMs) {
        long responseTime = response.getTime(); // in milliseconds
        System.out.println("Response time: " + responseTime + "ms");
        Assert.assertTrue(responseTime < maxTimeMs,"Expected response time under " + maxTimeMs + " ms but got " + responseTime + " ms");
    }

    @Then("all items in {string} should have a non-empty {string}")
    public void all_items_in_should_have_a_non_empty(String arrayName, String key) {
        List<String> values  = from(responseBody).getList( arrayName + "." + key);

        for(String val : values ){
            Assert.assertNotNull(val,"Value is null");
            Assert.assertFalse(val.trim().isEmpty(), "Value is empty");
        }

    }

    @Then("all items in {string} should have {string} equal to {string}")
    public void all_items_in_should_have_equal_to(String arrayPath, String nestedPath, String expectedValue) {

        List<String> types = from(responseBody).getList(arrayPath + "." + nestedPath);

        for (String type : types) {
            Assert.assertEquals(type,expectedValue,"Invalid episode type");
        }
    }

    @Then("only one episode in the list should have {string} as true")
    public void only_one_episode_live(String jsonPath) {
        List<Boolean> liveFlags = from(responseBody).getList("schedule.elements.episode.live");
        long count = liveFlags.stream().filter(Boolean::booleanValue).count();
        Assert.assertEquals(count,1,"More than one episode is live");
    }


    @Then("each item in {string} should have {string} before {string}")
    public void validate_dates_order(String arrayName, String startKey, String endKey) {
        List<String> startDates = from(responseBody).getList(arrayName+"."+ startKey);
        List<String> endDates = from(responseBody).getList(arrayName+"."+ endKey);

        Assert.assertEquals(startDates.size(), endDates.size(),"Mismatched start and end date count");

        for (int i = 0; i < startDates.size(); i++) {
            String start = startDates.get(i);
            String end = endDates.get(i);

            Assert.assertNotNull(start,"Start date is null");
            Assert.assertNotNull(end,"End date is null");

            Instant startInstant = Instant.parse(start);
            Instant endInstant = Instant.parse(end);

            Assert.assertTrue(startInstant.isBefore(endInstant),"Start date is not before end date for item");
        }
    }


}