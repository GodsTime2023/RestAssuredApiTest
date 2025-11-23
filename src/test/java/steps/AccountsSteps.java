package steps;

import io.cucumber.core.internal.com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.core.internal.com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.When;
import models.UserAccount;
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
        var requestBody = testContext.GetTableData(dataTable);
        if (requestBody.containsKey("userName")) {
            String uniqueUserName = requestBody.get("userName") + System.currentTimeMillis();
            requestBody.put("userName", uniqueUserName);
            System.out.println(uniqueUserName);
        }

        String jsonBody = new ObjectMapper().writeValueAsString(requestBody);

        testContext.setResponse(ApiClient.sendRequest(method, endpoint, testContext.AddHeaders("application/json"), jsonBody));
    }
}
