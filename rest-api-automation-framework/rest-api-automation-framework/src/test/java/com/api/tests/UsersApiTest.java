package com.api.tests;

import com.api.base.BaseTest;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class UsersApiTest extends BaseTest {

    @Test(groups = {"smoke", "regression"}, description = "GET /users returns 10 users with valid emails")
    public void getAllUsers_returns10UsersWithEmails() {
        given().spec(spec)
        .when().get("/users")
        .then()
                .statusCode(200)
                .body("size()", equalTo(10))
                .body("email", everyItem(containsString("@")))
                .time(lessThan(maxResponseTimeMs));
    }

    @Test(groups = "regression", description = "GET /users/1 returns nested address and company objects")
    public void getUserById_returnsNestedObjects() {
        given().spec(spec)
        .when().get("/users/1")
        .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("name", not(emptyOrNullString()))
                .body("address.city", not(emptyOrNullString()))
                .body("company.name", not(emptyOrNullString()));
    }

    @Test(groups = "regression", description = "GET /posts/1/comments returns comments with valid emails")
    public void getCommentsForPost_returnsComments() {
        given().spec(spec)
        .when().get("/posts/1/comments")
        .then()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .body("postId", everyItem(equalTo(1)))
                .body("email", everyItem(containsString("@")));
    }

    @Test(groups = "regression", description = "Non-existent user returns 404")
    public void getUser_nonExistent_returns404() {
        given().spec(spec)
        .when().get("/users/9999")
        .then()
                .statusCode(404);
    }
}
