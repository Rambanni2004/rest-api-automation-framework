package com.api.base;

import com.api.config.ConfigManager;
import com.api.utils.RequestSpecFactory;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;

public class BaseTest {

    protected RequestSpecification spec;
    protected long maxResponseTimeMs;

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        // Print full request/response only when an assertion fails
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        spec = RequestSpecFactory.getBaseSpec();
        maxResponseTimeMs = ConfigManager.getLong("response.time.limit.ms");
    }
}
