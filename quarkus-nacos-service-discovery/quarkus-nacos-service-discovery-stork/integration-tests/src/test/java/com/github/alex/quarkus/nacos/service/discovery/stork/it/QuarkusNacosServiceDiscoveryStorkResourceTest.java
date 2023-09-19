package com.github.alex.quarkus.nacos.service.discovery.stork.it;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class QuarkusNacosServiceDiscoveryStorkResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
                .when().get("/quarkus-nacos-service-discovery-stork")
                .then()
                .statusCode(200)
                .body(is("Hello quarkus-nacos-service-discovery-stork"));
    }
}
