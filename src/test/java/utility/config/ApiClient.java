package utility.config;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

public class ApiClient {
    private static RequestSpecification requestSpecification;

    public static void initializeApi() {
        RestAssured.baseURI = ConfigReader.getProperty("bookstore.api.baseurl");
        requestSpecification = RestAssured.given();
    }

    public static Response sendRequest(String method, String endpoint, Map<String, String> headers, Object body) {
        switch (method.toUpperCase()) {
            case "GET":
                return requestSpecification.headers(headers).get(endpoint);
            case "POST":
                return requestSpecification.headers(headers).body(body).post(endpoint);
            case "PUT":
                return requestSpecification.headers(headers).body(body).put(endpoint);
            default:
                throw new IllegalArgumentException("Unsupported HTTP method: " + method);
        }
    }
}