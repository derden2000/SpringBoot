package pro.antonshu.market.soap;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;

class ProductsEndPointTest {

    @Test
    void postGetAll() {
        String request = "<x:Envelope xmlns:x=\"http://schemas.xmlsoap.org/soap/envelope/\" \n" +
                "xmlns:gre=\"http://market.antonshu.pro/soap/products\">\n" +
                "    <x:Header/>\n" +
                "    <x:Body>\n" +
                "        <gre:getAllProductsRequest/>\n" +
                "    </x:Body>\n" +
                "</x:Envelope>";
        System.out.println(request);

        Response response = given().
                contentType("text/xml").
                accept("application/json, text/plain, */*").
                body(request).
                when().
                post("https://antonshu.pro:8023/app/ws").
                then().
                extract().response();

        response.getBody().prettyPrint();

        response.then().
                statusCode(200).
                and().
                body(containsString("Panasonic RP-HF410"));
    }

    @Test
    void postGetAllByCategory() {
        String request = "<x:Envelope xmlns:x=\"http://schemas.xmlsoap.org/soap/envelope/\" \n" +
                "xmlns:gre=\"http://market.antonshu.pro/soap/products\">\n" +
                "    <x:Header/>\n" +
                "    <x:Body>\n" +
                "        <gre:getProductsByCategoryRequest>\n" +
                "            <gre:category>Standard</gre:category>\n" +
                "        </gre:getProductsByCategoryRequest>\n" +
                "    </x:Body>\n" +
                "</x:Envelope>";
        System.out.println(request);

        Response response = given().
                contentType("text/xml").
                accept("application/json, text/plain, */*").
                body(request).
                when().
                post("https://antonshu.pro:8023/app/ws").
                then().
                extract().response();

        response.getBody().prettyPrint();

        response.then().
                statusCode(200).
                and().
                body(not(containsString("Professional")));
    }
}