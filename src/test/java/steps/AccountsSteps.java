package steps;

import io.cucumber.core.internal.com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.core.internal.com.fasterxml.jackson.databind.JsonNode;
import io.cucumber.core.internal.com.fasterxml.jackson.databind.ObjectMapper;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
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
        var objectMapper = new ObjectMapper();

        Map<String, String> stringMap = testContext.GetTableData(dataTable);
        Map<String, Object> requestBody = new HashMap<>(stringMap);

        if (requestBody.containsKey("userId") && requestBody.get("userId").equals("{0}")) {
            requestBody.put("userId", testContext.getUserId());
        }

        if (requestBody.containsKey("collectionOfIsbns") && requestBody.get("collectionOfIsbns") instanceof String) {
            String isbnsString = (String) requestBody.get("collectionOfIsbns");

            JsonNode collectionOfIsbnsNode = objectMapper.readTree(isbnsString);

            requestBody.put("collectionOfIsbns", collectionOfIsbnsNode);
        }

        // --- FINAL SERIALIZATION AND API CALL ---
        String finalJsonBody = objectMapper.writeValueAsString(requestBody);

        System.out.println("DEBUG: FINAL REQUEST BODY TO BOOKSTORE: " + finalJsonBody);

        Response response = ApiClient.sendRequest(
                method,
                endpoint,
                testContext.AddHeaders("application/json", true),
                finalJsonBody);

        testContext.setResponse(response);
    }

    @And("I generate user token")
    public void iGenerateUserToken() throws JsonProcessingException {
        // --- 1. Define Request Body using stored credentials ---
        Map<String, String> tokenRequestBody = new HashMap<>();
        tokenRequestBody.put("userName", testContext.getUserName());
        tokenRequestBody.put("password", testContext.getPassword());

        String finalRequestBody = new ObjectMapper().writeValueAsString(tokenRequestBody);

        // --- 2. Send the request to the GenerateToken endpoint ---
        Response response = ApiClient.sendRequest(
                "POST",
                "/Account/v1/GenerateToken",
                // No authorization header needed for this endpoint
                testContext.AddHeaders("application/json", false),
                finalRequestBody
        );

        // --- 3. Set the response so the next Then step can validate the status
        testContext.setResponse(response);

        // --- 4. Validate and Extract the Token ---
        if (response.statusCode() != 200) {
            throw new RuntimeException("Failed to generate token. Status: " + response.statusCode() +
                    ". Body: " + response.getBody().asString());
        }

        // 4. Extract the Bearer Token (JWT) from the NEW response
        String jwtToken = response.jsonPath().getString("token");

        // 5. Store the JWT in the context
        testContext.setToken(jwtToken);

        System.out.println("DEBUG: Token Set in Context: " + jwtToken);
    }

    @When("I create a new user with credentials")
    public void iCreateANewUserWithCredentials(DataTable dataTable) throws JsonProcessingException {
        var requestBody = testContext.GetTableData(dataTable);

        // --- 1. Generate Unique Credentials ---
        String uniqueUserName = requestBody.get("userName") + System.currentTimeMillis();
        String uniquePassword = requestBody.get("password");
        requestBody.put("userName", uniqueUserName);

        testContext.setUserName(uniqueUserName);
        testContext.setPassword(uniquePassword);

        // --- 2. Send the User Creation Request ---
        Response response = ApiClient.sendRequest(
                "POST",
                "/Account/v1/User",
                testContext.AddHeaders("application/json", false),
                new ObjectMapper().writeValueAsString(requestBody));

        testContext.setResponse(response);

        // --- 3. Extract and Store the userId immediately ---
        if (response.statusCode() == 201) {
            String extractedUserId = response.jsonPath().getString("userID");
            testContext.setUserId(extractedUserId);
        } else {
            System.err.println("User creation failed. Status: " + response.getStatusCode() + " Body: " + response.getBody().asString());
        }
    }
}
