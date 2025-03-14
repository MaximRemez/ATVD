package org.example.lab3;

import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import org.apache.hc.core5.http.HttpStatus;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class PetAndStoreTest {
    private static final String BASE_URL = "https://petstore.swagger.io/v2";
    private static final String STORE_ORDER = "/store/order", STORE_ORDER_ID = STORE_ORDER + "/{orderId}";
    private static final String PET = "/pet";

    private int orderId;
    private int petId;

    @BeforeClass
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
        RestAssured.defaultParser = Parser.JSON;
        RestAssured.requestSpecification = new RequestSpecBuilder().setContentType(ContentType.JSON).build();
        RestAssured.responseSpecification = new ResponseSpecBuilder().build();
    }

    @Test
    public void verifyCreateOrder() {
        Map<String, ?> body = Map.of(
                "id", Faker.instance().number().randomDigitNotZero(),
                "petId", Faker.instance().number().numberBetween(1, 100),
                "quantity", Faker.instance().number().numberBetween(1, 5),
                "shipDate", "2025-03-15T10:00:00.000Z",
                "status", "placed",
                "complete", true
        );

        orderId = given().body(body)
                .post(STORE_ORDER)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .path("id");
    }

    @Test(dependsOnMethods = "verifyCreateOrder")
    public void verifyGetOrder() {
        given().pathParam("orderId", orderId)
                .get(STORE_ORDER_ID)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("id", equalTo(orderId));
    }

    @Test(dependsOnMethods = "verifyGetOrder")
    public void verifyDeleteOrder() {
        given().pathParam("orderId", orderId)
                .delete(STORE_ORDER_ID)
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void verifyUpdatePet() {
        petId = Faker.instance().number().numberBetween(1, 100);
        Map<String, ?> body = Map.of(
                "id", petId,
                "name", Faker.instance().dog().name(),
                "status", "available"
        );

        given().body(body)
                .put(PET)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("id", equalTo(petId))
                .body("status", equalTo("available"));
    }
}
