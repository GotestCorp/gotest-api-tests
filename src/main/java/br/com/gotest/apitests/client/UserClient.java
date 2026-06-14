package br.com.gotest.apitests.client;

import br.com.gotest.apitests.endpoint.Endpoints;
import io.qameta.allure.Step;
import io.restassured.response.Response;

public class UserClient extends BaseApiClient {

    @Step("GET /users/me - obter perfil do usuário autenticado")
    public Response getMe() {
        return givenRequest()
                .when()
                .get(Endpoints.USERS_ME)
                .then().extract().response();
    }

    @Step("GET /users/notifications/unread/accountant - contador de notificações não lidas")
    public Response getUnreadNotificationsCount() {
        return givenRequest()
                .when()
                .get(Endpoints.USERS_NOTIFICATIONS_UNREAD_ACCOUNTANT)
                .then().extract().response();
    }

    @Step("GET /users/notifications/recent - notificações recentes")
    public Response getRecentNotifications(int limit, String readStatus) {
        return givenRequest()
                .queryParam("limit", limit)
                .queryParam("readStatus", readStatus)
                .when()
                .get(Endpoints.USERS_NOTIFICATIONS_RECENT)
                .then().extract().response();
    }

    @Step("GET /users/me/execution-preferences - preferências de execução")
    public Response getExecutionPreferences() {
        return givenRequest()
                .when()
                .get(Endpoints.USERS_ME_EXECUTION_PREFERENCES)
                .then().extract().response();
    }
}
