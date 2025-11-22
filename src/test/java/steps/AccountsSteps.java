package steps;

import io.cucumber.core.internal.com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.core.internal.com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.When;
import utility.config.ApiClient;
import utility.config.TestContext;

import java.util.HashMap;
import java.util.Map;

public class AccountsSteps {
    private final TestContext testContext;

    public AccountsSteps(TestContext testContext) {
        this.testContext = testContext;
    }

    @When("I send a {string} request to {string} with body")
    public void iSendARequestToWithBody(String method, String endpoint, DataTable dataTable) throws JsonProcessingException {

        Map<String, String> requestBody = new HashMap<>(dataTable.asMap(String.class, String.class));
        if (requestBody.containsKey("userName")) {
            String uniqueUserName = requestBody.get("userName") + System.currentTimeMillis();
            requestBody.put("userName", uniqueUserName);
        }

        // Convert the requestBody map to a JSON string
        String jsonBody = new ObjectMapper().writeValueAsString(requestBody);

        // Send the request with the JSON body
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        testContext.setResponse(ApiClient.sendRequest(method, endpoint, headers, jsonBody));
    }
}
