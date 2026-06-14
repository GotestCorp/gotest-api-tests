package br.com.gotest.apitests.client;

import br.com.gotest.apitests.endpoint.Endpoints;
import br.com.gotest.apitests.model.request.SuiteRequest;
import io.qameta.allure.Step;
import io.restassured.response.Response;

/**
 * Client para o recurso /projects/{projectId}/nodes/suites.
 */
public class SuiteClient extends BaseApiClient {

    @Step("POST /projects/{projectId}/nodes/suites - criar suite")
    public Response create(String projectId, SuiteRequest payload) {
        return givenRequest()
                .pathParam("projectId", projectId)
                .body(payload)
                .when()
                .post(Endpoints.PROJECTS + "/{projectId}/nodes/suites")
                .then()
                .extract()
                .response();
    }

    @Step("POST /projects/{projectId}/nodes/suites - criar suite (raw body)")
    public Response createRaw(String projectId, String rawJsonBody) {
        return givenRequest()
                .pathParam("projectId", projectId)
                .body(rawJsonBody)
                .when()
                .post(Endpoints.PROJECTS + "/{projectId}/nodes/suites")
                .then()
                .extract()
                .response();
    }

    @Step("GET /projects/{projectId}/nodes/suites - listar suites do projeto")
    public Response listByProject(String projectId) {
        return givenRequest()
                .pathParam("projectId", projectId)
                .when()
                .get(Endpoints.PROJECTS + "/{projectId}/nodes/suites")
                .then()
                .extract()
                .response();
    }
}
