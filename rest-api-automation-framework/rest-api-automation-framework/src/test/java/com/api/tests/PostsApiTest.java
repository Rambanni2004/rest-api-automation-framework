package com.api.tests;

import com.api.base.BaseTest;
import com.api.models.Post;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

public class PostsApiTest extends BaseTest {

    @DataProvider(name = "postIds")
    public Object[][] postIds() {
        return new Object[][]{{1}, {25}, {50}, {100}};
    }

    @Test(groups = {"smoke", "regression"}, description = "GET /posts returns all 100 posts")
    public void getAllPosts_returns100Posts() {
        given().spec(spec)
        .when().get("/posts")
        .then()
                .statusCode(200)
                .contentType("application/json; charset=utf-8")
                .body("size()", equalTo(100))
                .time(lessThan(maxResponseTimeMs));
    }

    @Test(groups = {"smoke", "regression"}, description = "GET /posts/1 returns correct post")
    public void getPostById_returnsExpectedPost() {
        given().spec(spec)
        .when().get("/posts/1")
        .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("userId", equalTo(1))
                .body("title", not(emptyOrNullString()))
                .body("body", not(emptyOrNullString()));
    }

    @Test(dataProvider = "postIds", groups = "regression", description = "Data-driven GET by id")
    public void getPostById_dataDriven(int id) {
        given().spec(spec)
        .when().get("/posts/{id}", id)
        .then()
                .statusCode(200)
                .body("id", equalTo(id));
    }

    @Test(groups = "regression", description = "Response matches JSON schema")
    public void getPost_matchesJsonSchema() {
        given().spec(spec)
        .when().get("/posts/1")
        .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/post-schema.json"));
    }

    @Test(groups = "regression", description = "Query parameter filters posts by userId")
    public void getPosts_filterByUserId() {
        given().spec(spec).queryParam("userId", 1)
        .when().get("/posts")
        .then()
                .statusCode(200)
                .body("size()", equalTo(10))
                .body("userId", everyItem(equalTo(1)));
    }

    @Test(groups = "regression", description = "Non-existent post returns 404")
    public void getPost_nonExistent_returns404() {
        given().spec(spec)
        .when().get("/posts/9999")
        .then()
                .statusCode(404);
    }

    @Test(groups = {"smoke", "regression"}, description = "POST /posts creates a post (deserialized to POJO)")
    public void createPost_returns201AndCreatedPost() {
        Post request = new Post(1, "Automation Title", "Created by REST Assured");

        Post created = given().spec(spec).body(request)
        .when().post("/posts")
        .then()
                .statusCode(201)
                .extract().as(Post.class);

        Assert.assertNotNull(created.getId(), "Created post should have an id");
        Assert.assertEquals(created.getTitle(), request.getTitle());
        Assert.assertEquals(created.getBody(), request.getBody());
        Assert.assertEquals(created.getUserId(), request.getUserId());
    }

    @Test(groups = "regression", description = "PUT /posts/1 replaces the post")
    public void updatePost_withPut_returns200() {
        Post update = new Post(1, "Updated Title", "Updated Body");
        update.setId(1);

        given().spec(spec).body(update)
        .when().put("/posts/1")
        .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("title", equalTo("Updated Title"))
                .body("body", equalTo("Updated Body"));
    }

    @Test(groups = "regression", description = "PATCH /posts/1 updates only the title")
    public void updatePost_withPatch_returns200() {
        given().spec(spec).body(Map.of("title", "Patched Title"))
        .when().patch("/posts/1")
        .then()
                .statusCode(200)
                .body("title", equalTo("Patched Title"))
                .body("id", equalTo(1));
    }

    @Test(groups = "regression", description = "DELETE /posts/1 returns 200")
    public void deletePost_returns200() {
        given().spec(spec)
        .when().delete("/posts/1")
        .then()
                .statusCode(200);
    }
}
