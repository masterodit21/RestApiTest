package org.example;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class RestAbsurd {
    private static String requestBodyUpdate = "{\n" +
            "  \"title\": \"tedy aditia dongs\",\n" +
            "  \"completed\": \"true\",\n" +
            "  \"userId\": \"5\",\n" +
            "  \"id\": \"5\" \n}";

    @BeforeSuite
    public void setup() {
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";
    }

    @Test
    public void getRequest() {
        ValidatableResponse validate = RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .get("/todos")
                .then();

        Response response = validate.extract().response();
        JsonPath jsonPathEvaluator = response.jsonPath();
        assertEquals(200, response.statusCode());
        assertEquals(jsonPathEvaluator.getList("").size(), 200);
        assertEquals("laboriosam mollitia et enim quasi adipisci quia provident illum", response.jsonPath().getString("title[4]"));
    }

    @Test
    public void getRequestWithQueryParam() {
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .headers("Authorization", "JWT todos")
                .param("users", "2")
                .when()
                .get("/todos")
                .then()
                .extract().response();

        assertEquals(200, response.statusCode());
        assertEquals("et porro tempora", response.jsonPath().getString("title[3]"));
    }

    @Test
    public void postRequest() {
        String jsonData = "{\r\n"
                + "    \"title\": \"tedy aditia\",\r\n"
                + "    \"completed\": \"true\",\r\n"
                + "    \"userId\": \"2\"\r\n"
                + "}";
        Response response = RestAssured.given()
                .header("Content-type", "application/json")
                .and()
                .body(jsonData)
                .when()
                .post("/todos")
                .then()
                .extract().response();

        assertEquals(201, response.statusCode());
        assertEquals("tedy aditia", response.jsonPath().getString("title"));
        assertEquals("true", response.jsonPath().getString("completed"));
        assertEquals("2", response.jsonPath().getString("userId"));
        assertEquals("201", response.jsonPath().getString("id"));
    }

    @Test
    public void putRequest() {
        Response response = RestAssured.given()
                .header("Content-type", "application/json")
                .and()
                .body(requestBodyUpdate)
                .when()
                .put("/todos/5")
                .then()
                .extract().response();

        assertEquals(200, response.statusCode());
        assertEquals("tedy aditia dongs", response.jsonPath().getString("title"));
        assertEquals("true", response.jsonPath().getString("completed"));
        assertEquals("5", response.jsonPath().getString("userId"));
        assertEquals("5", response.jsonPath().getString("id"));
    }

    @Test
    public void deleteRequest() {
        Response response = RestAssured.given()
                .header("Content-type", "application/json")
                .when()
                .delete("/todos/1")
                .then()
                .extract().response();

        assertEquals(200, response.statusCode());
    }
}

