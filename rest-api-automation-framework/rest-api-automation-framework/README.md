# REST API Automation Testing Framework

API test automation framework built with **Java, REST Assured, TestNG and Maven**.
It tests the public [JSONPlaceholder](https://jsonplaceholder.typicode.com) REST API.

## Features
- Covers GET, POST, PUT, PATCH and DELETE
- Validates status codes, response body, response time and **JSON Schema**
- **Data-driven** tests with TestNG `@DataProvider`
- Request/response mapping with **POJOs** (Jackson)
- Reusable layers: config, request specification, models, base test
- Base URL and limits configurable from `config.properties` or the command line
- **GitHub Actions** CI runs the suite on every push

## Tech Stack
Java 17, Maven, REST Assured 5, TestNG 7, Jackson, Hamcrest

## Project Structure
```
src/main/java/com/api/
  config/ConfigManager.java        -> reads config.properties (+ CLI overrides)
  models/Post.java                 -> POJO for request/response bodies
  utils/RequestSpecFactory.java    -> common request specification
src/main/resources/config.properties
src/test/java/com/api/
  base/BaseTest.java               -> shared setup
  tests/PostsApiTest.java          -> CRUD, schema, data-driven, negative tests
  tests/UsersApiTest.java          -> users and nested resource tests
src/test/resources/schemas/        -> JSON schema files
testng.xml                         -> suite definition
.github/workflows/maven.yml        -> CI pipeline
```

## How to Run
Prerequisites: JDK 17+ and Maven.

```bash
# run the whole suite
mvn clean test

# run a single class
mvn clean test -Dtest=PostsApiTest

# point to a different base URL
mvn clean test -Dbase.url=https://your-api.com
```

Reports are generated in `target/surefire-reports/`.

## Note
JSONPlaceholder is a fake API: POST, PUT, PATCH and DELETE return realistic responses but do not
actually change data on the server.
