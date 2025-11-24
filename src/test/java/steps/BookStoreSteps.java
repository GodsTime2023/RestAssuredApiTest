package steps;

import io.cucumber.core.internal.com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import utility.config.ApiClient;
import models.BookResponse;
import utility.config.TestContext;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class BookStoreSteps {
    private final TestContext testContext;

    public BookStoreSteps(TestContext testContext) {
        this.testContext = testContext;
    }

    @Given("^I have access to the Bookstore API$")
    public void i_have_access_to_the_bookstore_api(){
        ApiClient.initializeApi();
    }

    @When("^I send a \"([^\"]*)\" request to \"([^\"]*)\"$")
    public void i_send_a_get_request_to_(String method, String endpoint){
        testContext.setResponse(ApiClient.sendRequest(
                method,
                endpoint,
                testContext.AddHeaders("application/json", false),
                null));
    }

    @And("I send a {string} request to {string} with {string}")
    public void iSendARequestToWith(String method, String endpoint, String body) {
        String isbn  = "";
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            BookResponse bookResponse = objectMapper.readValue(testContext.getResponse().getBody().asString(), BookResponse.class);
            isbn = bookResponse.getBooks().get(0).getIsbn();
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize response", e);
        }
        testContext.setResponse(ApiClient.sendRequest(method, endpoint + body + isbn, testContext.AddHeaders("application/json", false),null));
    }

    @Then("^I should receive a (\\d+) OK response$")
    public void i_should_receive_a_ok_response_(Integer statusCode) {
        assertEquals("Status code does not match!", statusCode.intValue(), testContext.getResponse().getStatusCode());
    }

    @Then("^the response should contain a list of books$")
    public void the_response_should_contain_a_list_of_books(){
        assertTrue("The response does not contain a list of books!", testContext.getResponse().jsonPath().getList("books").size() > 0);
    }

    @Then("^the response should contain the following$")
    public void the_response_should_contain_the_following(DataTable dataTable){// Convert DataTable to a Map
        Map<String, String> expectedData = dataTable.asMap(String.class, String.class);
        
        var actualData = testContext.getResponse().jsonPath().prettyPeek();

        // Assert each field in the expected data
        for (Map.Entry<String, String> entry : expectedData.entrySet()) {
            String key = entry.getKey();
            String expectedValue = entry.getValue();
            Object actualValue = actualData.get(key);

            assertEquals("Mismatch for key: " + key, expectedValue, actualValue.toString());
        }
    }
}