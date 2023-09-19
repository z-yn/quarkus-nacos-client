package com.github.alex.quarkus.nacos.service.discovery.stork.kotlin.it;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class QuarkusNacosServiceDiscoveryStorkKotlinResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
                .when().get("/quarkus-nacos-service-discovery-stork-kotlin")
                .then()
                .statusCode(200)
                .body(is("Hello quarkus-nacos-service-discovery-stork-kotlin"));
    }
}
