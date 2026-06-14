package br.com.gotest.apitests.healthcheck;

import br.com.gotest.apitests.base.BaseTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("HealthCheck")
@Feature("Access Management API")
public class AccessManagementHealthCheckTest extends BaseTest {

    private static final long SLA_MS = 5000;

    @Test(description = "GET /accessManagement/listRoles responde dentro do SLA")
    public void listRoles_shouldRespondWithinSla() {
        Response response = accessManagementClient.listRoles();
        assertThat(response.getStatusCode()).isIn(200, 401, 403);
        assertThat(response.getTime()).isLessThan(SLA_MS);
    }
}
