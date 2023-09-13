package com.github.alex.quarkus.nacos.client.it;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class QuarkusNacosClientResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
                .when().get("/quarkus-nacos-client")
                .then()
                .statusCode(200)
                .body(is("Hello quarkus-nacos-client"));
    }
}
