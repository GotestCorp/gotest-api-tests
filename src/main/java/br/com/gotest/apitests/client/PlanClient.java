package br.com.gotest.apitests.client;

import br.com.gotest.apitests.endpoint.Endpoints;
import br.com.gotest.apitests.model.request.FilterRequest;
import br.com.gotest.apitests.model.request.PlanRelationsRequest;
import br.com.gotest.apitests.model.request.PlanRequest;
import io.qameta.allure.Step;
import io.restassured.response.Response;

public class PlanClient extends BaseApiClient {

    @Step("POST /projects/{projectId}/plans/filter - listar planos paginado/filtrado")
    public Response filter(String projectId, FilterRequest filter) {
        return givenRequest()
                .body(filter)
                .when()
                .post(Endpoints.projectPlansFilter(projectId))
                .then().extract().response();
    }

    @Step("POST /projects/{projectId}/plans/filter - primeira página (filtro padrão)")
    public Response filterDefault(String projectId) {
        return filter(projectId, FilterRequest.defaultFilter());
    }

    @Step("GET /projects/{projectId}/plans/all - todos os planos")
    public Response listAll(String projectId, String query) {
        return givenRequest()
                .queryParam("query", query)
                .when()
                .get(Endpoints.projectPlansAll(projectId))
                .then().extract().response();
    }

    @Step("POST /projects/{projectId}/plans/validate-name - validar nome do plano")
    public Response validateName(String projectId, String name) {
        return givenRequest()
                .body("{\"name\": \"" + name + "\"}")
                .when()
                .post(Endpoints.projectPlansValidateName(projectId))
                .then().extract().response();
    }

    @Step("POST /projects/{projectId}/plans - criar plano")
    public Response create(String projectId, PlanRequest payload) {
        return givenRequest()
                .body(payload)
                .when()
                .post(Endpoints.projectPlans(projectId))
                .then().extract().response();
    }

    @Step("GET /projects/{projectId}/plans/{planId}/dashboard - dashboard do plano")
    public Response getDashboard(String projectId, String planId) {
        return givenRequest()
                .when()
                .get(Endpoints.projectPlanDashboard(projectId, planId))
                .then().extract().response();
    }

    @Step("GET /projects/{projectId}/plans/{planId}/executions/check - verificar execuções")
    public Response checkExecutions(String projectId, String planId) {
        return givenRequest()
                .when()
                .get(Endpoints.projectPlanExecutionsCheck(projectId, planId))
                .then().extract().response();
    }

    @Step("GET /projects/{projectId}/plans/{planId}/charts/last - último gráfico")
    public Response getChartsLast(String projectId, String planId) {
        return givenRequest()
                .when()
                .get(Endpoints.projectPlanChartsLast(projectId, planId))
                .then().extract().response();
    }

    @Step("GET /projects/{projectId}/plans/{planId}/charts/recent - gráficos recentes")
    public Response getChartsRecent(String projectId, String planId) {
        return givenRequest()
                .when()
                .get(Endpoints.projectPlanChartsRecent(projectId, planId))
                .then().extract().response();
    }

    @Step("GET /projects/{projectId}/plans/{planId}/flat-entities - entidades planas paginado")
    public Response getFlatEntities(String projectId, String planId, int page, int size, String sortBy) {
        return givenRequest()
                .queryParam("page", page)
                .queryParam("size", size)
                .queryParam("sortBy", sortBy)
                .when()
                .get(Endpoints.projectPlanFlatEntities(projectId, planId))
                .then().extract().response();
    }

    @Step("POST /projects/{projectId}/plans/relations - associar entidades ao plano")
    public Response addRelations(String projectId, PlanRelationsRequest payload) {
        return givenRequest()
                .body(payload)
                .when()
                .post(Endpoints.projectPlansRelations(projectId))
                .then().extract().response();
    }

    @Step("GET /projects/{projectId}/plans/{planId}/folder/{folderId} - testes de uma pasta no plano")
    public Response getFolderTests(String projectId, String planId, String folderId, int page, int size, String sortBy) {
        return givenRequest()
                .queryParam("page", page)
                .queryParam("size", size)
                .queryParam("sortBy", sortBy)
                .when()
                .get(Endpoints.projectPlanFolder(projectId, planId, folderId))
                .then().extract().response();
    }

    @Step("GET /projects/{projectId}/plans/{planId}/suites/{suiteId}/tests - testes de uma suite no plano")
    public Response getSuiteTests(String projectId, String planId, String suiteId, int page, int size, String sortBy) {
        return givenRequest()
                .queryParam("page", page)
                .queryParam("size", size)
                .queryParam("sortBy", sortBy)
                .when()
                .get(Endpoints.projectPlanSuiteTests(projectId, planId, suiteId))
                .then().extract().response();
    }

    @Step("DELETE /projects/{projectId}/plans/{planId} - excluir plano")
    public Response delete(String projectId, String planId) {
        return givenRequest()
                .when()
                .delete(Endpoints.deletePlan(projectId, planId))
                .then().extract().response();
    }
}
