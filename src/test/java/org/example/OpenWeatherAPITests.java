package org.example;

import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class OpenWeatherAPITests {

    // Base URL setup
    static {
        RestAssured.baseURI = "https://api.openweathermap.org/data/2.5";
    }

    private final String API_KEY = "dd82f60a7a046240545f12a4ed407e54";  // Your real OpenWeather API key
    // Test Case 1: Validate Response Code
    @Test
    public void validateResponseCode() {
        given()
                .queryParam("q", "London")
                .queryParam("appid", API_KEY)
                .when()
                .get("/weather")
                .then()
                .statusCode(200) // Validates that the response code is 200 (OK)
                .log().all();
    }

    // Test Case 2: Validate Response Schema
    @Test
    public void validateResponseSchema() {
        given()
                .queryParam("q", "London")
                .queryParam("appid", API_KEY)
                .when()
                .get("/weather")
                .then()
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("weather-schema.json")); // Validates schema
    }

    // Test Case 3: Validate Response Data
    @Test
    public void validateResponseData() {
        given()
                .queryParam("q", "London")
                .queryParam("appid", API_KEY)
                .when()
                .get("/weather")
                .then()
                .body("name", equalTo("London")) // Validates the city name
                .body("main.temp", greaterThan(0f)) // Validates temperature is greater than 0
                .body("main.humidity", lessThanOrEqualTo(100)) // Validates humidity is between 0 and 100
                .log().all();
    }

    // Test Case 4: Validate Response Time
    @Test
    public void validateResponseTime() {
        given()
                .queryParam("q", "London")
                .queryParam("appid", API_KEY)
                .when()
                .get("/weather")
                .then()
                .time(lessThan(2000L)); // Validates that the response time is less than 2 seconds
    }
}
