package com.bigtreetc.sample.micronaut.aws.lambda.controller;

import static org.hamcrest.Matchers.notNullValue;

import io.lettuce.core.RedisClient;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.restassured.specification.RequestSpecification;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@MicronautTest(transactional = false)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthControllerTest {

  @Inject RedisClient redisClient;

  @BeforeAll
  void beforeAll() {
    redisClient.connect();
  }

  @Test
  @DisplayName("認証が通った場合はアクセストークンが返ること")
  void testAuth(RequestSpecification spec) {
    spec.given()
        .formParam("username", "test@example.com")
        .formParam("password", "passw0rd")
        .when()
        .post("/api/auth/login")
        .then()
        .statusCode(200)
        .body("username", notNullValue())
        .body("access_token", notNullValue());
  }
}
