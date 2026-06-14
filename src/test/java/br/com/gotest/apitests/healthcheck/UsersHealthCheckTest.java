package br.com.gotest.apitests.healthcheck;

import br.com.gotest.apitests.base.BaseTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("HealthCheck")
@Feature("Users API")
public class UsersHealthCheckTest extends BaseTest {

    private static final long SLA_MS = 5000;

    @Test(description = "GET /users/me responde dentro do SLA")
    public void getMe_shouldRespondWithinSla() {
        Response response = userClient.getMe();
        assertThat(response.getStatusCode()).isIn(200, 401, 403);
        assertThat(response.getTime()).isLessThan(SLA_MS);
    }

    @Test(description = "GET /users/notifications/recent responde dentro do SLA")
    public void getRecentNotifications_shouldRespondWithinSla() {
        Response response = userClient.getRecentNotifications(10, "UNREAD");
        assertThat(response.getStatusCode()).isIn(200, 401, 403);
        assertThat(response.getTime()).isLessThan(SLA_MS);
    }

    @Test(description = "GET /users/notifications/unread/accountant responde dentro do SLA")
    public void getUnreadNotificationsCount_shouldRespondWithinSla() {
        Response response = userClient.getUnreadNotificationsCount();
        assertThat(response.getStatusCode()).isIn(200, 401, 403);
        assertThat(response.getTime()).isLessThan(SLA_MS);
    }

    @Test(description = "GET /users/me/execution-preferences responde dentro do SLA")
    public void getExecutionPreferences_shouldRespondWithinSla() {
        Response response = userClient.getExecutionPreferences();
        assertThat(response.getStatusCode()).isIn(200, 401, 403);
        assertThat(response.getTime()).isLessThan(SLA_MS);
    }
}
