package pro.antonshu.market.rest;

import io.restassured.response.Response;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringCase;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RestApiControllerTest {

    @Test
    @Order(1)
    void getAllProducts() {
        when()
                .get("https://antonshu.pro:8023/app/api/v1/products")
                .then()
                .statusCode(200)
                .body("[0].id", equalTo(1))
                .and()
                .body("[0].title", equalToIgnoringCase("Paypal Headphones 10"))
                .extract()
                .response()
                .prettyPrint();
    }

    @Test
    @Order(2)
    void getProductById() {
        when()
                .get("https://antonshu.pro:8023/app/api/v1/products/1")
                .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .and()
                .body("title", equalToIgnoringCase("Paypal Headphones 10"))
                .extract()
                .response()
                .prettyPrint();
    }

    @Test
    @Order(3)
    void updateProduct() {
        JSONObject categoryJson = new JSONObject();
        categoryJson.put("id", 2);
        categoryJson.put("title", "Professional");
        JSONObject productJson = new JSONObject();
        productJson.put("id", 5);
        productJson.put("title", "Apple Airpods 2");
        productJson.put("description", null);
        productJson.put("category", categoryJson);
        productJson.put("price", 9999);

        Response response = given().
                contentType("application/json").
                accept("application/json, text/plain, */*").
                body(productJson.toJSONString()).
                when().
                put("https://antonshu.pro:8023/app/api/v1/products").
                then().extract().response();

        response.getBody().prettyPrint();

        response.then().
                statusCode(200).
                and().
                body("id", equalTo(5)).
                and().
                body("price", equalTo(9999));

    }

    @Test
    @Order(4)
    void updateProductRollback() {
        JSONObject categoryJson = new JSONObject();
        categoryJson.put("id", 2);
        categoryJson.put("title", "Professional");
        JSONObject productJson = new JSONObject();
        productJson.put("id", 5);
        productJson.put("title", "Apple Airpods 2");
        productJson.put("description", null);
        productJson.put("category", categoryJson);
        productJson.put("price", 9250);

        Response response = given().
                contentType("application/json").
                accept("application/json, text/plain, */*").
                body(productJson.toJSONString()).
                when().
                put("https://antonshu.pro:8023/app/api/v1/products").
                then().
                extract().
                response();

        response.getBody().prettyPrint();

        response.then().
                statusCode(200).
                and().
                body("id", equalTo(5)).
                and().
                body("price", equalTo(9250));


    }

    @Test
    @Order(5)
    void saveNewProduct() {
        JSONObject categoryJson = new JSONObject();
        categoryJson.put("id", 2);
        categoryJson.put("title", "Professional");
        JSONObject productJson = new JSONObject();
        productJson.put("id", 24);
        productJson.put("title", "Super Product");
        productJson.put("description", null);
        productJson.put("category", categoryJson);
        productJson.put("price", 9999);

        Response response = given().
                contentType("application/json").
                accept("application/json, text/plain, */*").
                body(productJson.toJSONString()).
                when().
                post("https://antonshu.pro:8023/app/api/v1/products").
                then().
                extract().response();

        response.getBody().prettyPrint();

        response.then().
                statusCode(201).
                and().
                body("title", equalTo("Super Product")).
                and().
                body("price", equalTo(9999));
    }

    @Test
    @Order(6)
    void delProductById() {
        Response response = given().
                contentType("text/plain").
                accept("application/json, text/plain, */*").
                body("25").
                when().
                delete("https://antonshu.pro:8023/app/api/v1/products").
                then()
                .extract()
                .response();

        response
                .getBody()
                .prettyPrint();

        response
                .then()
                .statusCode(200)
                .and()
                .body(equalTo("OK"));
    }
}