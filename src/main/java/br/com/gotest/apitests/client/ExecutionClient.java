package br.com.gotest.apitests.client;

import br.com.gotest.apitests.endpoint.Endpoints;
import br.com.gotest.apitests.model.request.ExecutionUpdateRequest;
import br.com.gotest.apitests.model.request.TestExecutionUpdateRequest;
import io.qameta.allure.Step;
import io.restassured.response.Response;

public class ExecutionClient extends BaseApiClient {

    @Step("POST /projects/{projectId}/plans/{planId}/executions - criar execução")
    public Response create(String projectId, String planId) {
        return givenRequest()
                .body("{}")
                .when()
                .post(Endpoints.planExecutions(projectId, planId))
                .then().extract().response();
    }

    @Step("GET /projects/{projectId}/plans/executions/{executionId} - buscar execução por ID")
    public Response getById(String projectId, String executionId) {
        return givenRequest()
                .when()
                .get(Endpoints.executionById(projectId, executionId))
                .then().extract().response();
    }

    @Step("PUT /projects/{projectId}/plans/executions/{executionId}/initialize - inicializar execução")
    public Response initialize(String projectId, String executionId) {
        return givenRequest()
                .body("{}")
                .when()
                .put(Endpoints.executionInitialize(projectId, executionId))
                .then().extract().response();
    }

    @Step("PUT /projects/{projectId}/plans/executions/{executionId} - atualizar execução")
    public Response update(String projectId, String executionId, ExecutionUpdateRequest payload) {
        return givenRequest()
                .body(payload)
                .when()
                .put(Endpoints.executionById(projectId, executionId))
                .then().extract().response();
    }

    @Step("GET /projects/{projectId}/plans/test-executions/{testExecutionId} - buscar execução de teste por ID")
    public Response getTestExecutionById(String projectId, String testExecutionId) {
        return givenRequest()
                .when()
                .get(Endpoints.testExecutionById(projectId, testExecutionId))
                .then().extract().response();
    }

    @Step("PUT /projects/{projectId}/plans/test-executions/{testExecutionId} - atualizar execução de teste")
    public Response updateTestExecution(String projectId, String testExecutionId, TestExecutionUpdateRequest payload) {
        return givenRequest()
                .body(payload)
                .when()
                .put(Endpoints.testExecutionById(projectId, testExecutionId))
                .then().extract().response();
    }
}
