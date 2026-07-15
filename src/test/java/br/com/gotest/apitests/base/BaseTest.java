package br.com.gotest.apitests.base;

import br.com.gotest.apitests.client.*;
import br.com.gotest.apitests.config.ConfigManager;
import io.restassured.RestAssured;
import io.restassured.filter.log.LogDetail;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

@Slf4j
public abstract class BaseTest {

    protected final ConfigManager config = ConfigManager.getInstance();

    protected final ProjectClient projectClient = new ProjectClient();
    protected final UserClient userClient = new UserClient();
    protected final NodeClient nodeClient = new NodeClient();
    protected final TestCaseClient testCaseClient = new TestCaseClient();
    protected final PlanClient planClient = new PlanClient();
    protected final ExecutionClient executionClient = new ExecutionClient();
    protected final AccessManagementClient accessManagementClient = new AccessManagementClient();

    // Mantido por retrocompatibilidade (aponta para NodeClient internamente)
    protected final SuiteClient suiteClient = new SuiteClient();

    @BeforeSuite(alwaysRun = true)
    public void globalSetUp() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails(LogDetail.ALL);
        validatePreconditions();
        log.info("Suite iniciada — baseUrl={}", config.baseUrl());
    }

    @BeforeTest(alwaysRun = true)
    public void beforeTest() {}

    private void validatePreconditions() {
        if (config.baseUrl() == null || config.baseUrl().isBlank()) {
            throw new IllegalStateException(
                    "baseUrl não configurado. Defina em config.properties ou via -DbaseUrl=...");
        }
        boolean hasStaticToken = config.get("auth.token") != null && !config.get("auth.token").isBlank();
        boolean hasCredentials = config.get("auth.email") != null && !config.get("auth.email").isBlank()
                && config.get("auth.password") != null && !config.get("auth.password").isBlank();
        if (!hasStaticToken && !hasCredentials) {
            log.warn("Nenhuma credencial configurada (auth.token ou auth.email/auth.password). "
                    + "Endpoints autenticados vão falhar com 401.");
        }
    }
}
