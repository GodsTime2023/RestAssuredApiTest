package utility.config;

import io.cucumber.core.internal.com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.datatable.DataTable;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

public class TestContext {
    private Response response;
    Map<String, String> headers;

    // Get from response
    public Response getResponse() {
        return response;
    }

    //Save to response
    public void setResponse(Response response) {
        this.response = response;
    }

    // Add header type to request e.g. application/json
    public Map<String, String> AddHeaders(String headerType) {
        headers = new HashMap<>();
        headers.put("Content-Type", headerType);
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
}