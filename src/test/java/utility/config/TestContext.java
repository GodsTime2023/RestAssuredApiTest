package utility.config;

import io.cucumber.core.internal.com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.datatable.DataTable;
import io.restassured.response.Response;

import java.nio.charset.StandardCharsets;
import  java.util.Base64;

import java.util.HashMap;
import java.util.Map;

public class TestContext {
    private Response response;
    Map<String, String> headers;
    private String userName;
    private String password;
    private String userId;
    private String token;

    // Get from response
    public Response getResponse() {
        return response;
    }
    public String setUserIdFromResponse(String key) {
        return response.then().extract().path(key);
    }

    //Save to response
    public void setResponse(Response response) {
        this.response = response;
        this.userId = setUserIdFromResponse("userID") != null ? this.userId = setUserIdFromResponse("userID") : this.userId;
    }

    public Map<String, String> AddHeaders(String contentType, Boolean withAuth) {
        headers = new HashMap<>();
        headers.put("Content-Type", contentType);

        if (withAuth.equals(true)) {
            String jwtToken = getToken();

            if (jwtToken != null && !jwtToken.isEmpty()) {
                headers.put("Authorization", "Bearer " + jwtToken);
                System.out.println("DEBUG: Authorization Header: Bearer " + jwtToken);
            } else {
                System.err.println("FATAL: Auth requested (true), but token is NULL or EMPTY.");
            }
        }

        return headers;
    }

    // Deserialize DataTable to Map
    public Map<String, String> GetTableData(DataTable dataTable) {
        return new HashMap<>(dataTable.asMap(String.class, String.class));
    }

    // Convert the requestBody map to a JSON string
    public String WriteValueAsString(Map<String, String> requestBody) throws Exception {
        return new ObjectMapper().writeValueAsString(requestBody);
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getUserName() {
        return userName;
    }

    public void setPassword(String password) { this.password = password; }
    public String getPassword() {
        return password;
    }

    public void setUserId(String userId) { this.userId = userId; }

    public String getUserId() {
        return userId;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return this.token;
    }

    public static class Isbn {
        public String isbn;

        public Isbn() {}

        public Isbn(String isbn) {
            this.isbn = isbn;
        }
    }
}