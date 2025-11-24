package utility.config;

import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
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
        RequestSpecification request = requestSpecification.headers(headers);

        if (!"GET".equalsIgnoreCase(method) && body != null) {
            request = request.body(body);
        }
        Response response;

        switch (method.toUpperCase()) {
            case "GET":
                response = request.get(endpoint);
                break;
            case "POST":
                response = request.post(endpoint);
                break;
            case "PUT":
                response = request.put(endpoint);
                break;
            default:
                throw new IllegalArgumentException("Unsupported HTTP method: " + method);
        }

        return response.then().using().defaultParser(Parser.JSON).extract().response();
    }
}