package com.jorgemonteiro.usermanager.steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

/**
 * Step definitions for the authentication filter feature.
 */
public class AuthSteps {

    private Response response;

    @When("a request is made to {string} without the auth header")
    public void requestWithoutAuthHeader(String path) {
        response = RestAssured.given()
                .when()
                .get(path);
    }

    @When("a request is made to {string} with email {string}")
    public void requestWithEmail(String path, String email) {
        response = RestAssured.given()
                .header("X-Auth-Request-Email", email)
                .when()
                .get(path);
    }

    @Then("the response status is {int}")
    public void responseStatusIs(int status) {
        assertThat(response.statusCode(), equalTo(status));
    }

    @And("the response body contains {string}")
    public void responseBodyContains(String text) {
        assertThat(response.body().asString(), containsString(text));
    }
}
