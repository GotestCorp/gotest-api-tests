package br.com.gotest.apitests.client;

import br.com.gotest.apitests.endpoint.Endpoints;
import io.qameta.allure.Step;
import io.restassured.response.Response;

public class AccessManagementClient extends BaseApiClient {

    @Step("GET /accessManagement/listRoles - listar papéis disponíveis")
    public Response listRoles() {
        return givenRequest()
                .when()
                .get(Endpoints.ACCESS_MANAGEMENT_LIST_ROLES)
                .then().extract().response();
    }
}
