package br.com.gotest.apitests.client;

import br.com.gotest.apitests.endpoint.Endpoints;
import br.com.gotest.apitests.model.request.FilterRequest;
import br.com.gotest.apitests.model.request.ProjectRequest;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.List;
import java.util.Map;

public class ProjectClient extends BaseApiClient {

    @Step("POST /projects/filter - listar projetos paginados/filtrados")
    public Response filter(FilterRequest filter) {
        return givenRequest()
                .body(filter)
                .when()
                .post(Endpoints.PROJECTS_FILTER)
                .then().extract().response();
    }

    @Step("POST /projects/filter - primeira página (filtro padrão)")
    public Response filterDefault() {
        return filter(FilterRequest.defaultFilter());
    }

    @Step("GET /projects/all - listar todos os projetos sem paginação")
    public Response listAll() {
        return givenRequest()
                .when()
                .get(Endpoints.PROJECTS_ALL)
                .then().extract().response();
    }

    @Step("GET /projects/{projectId} - buscar projeto por ID")
    public Response getById(String projectId) {
        return givenRequest()
                .pathParam("projectId", projectId)
                .when()
                .get(Endpoints.PROJECTS + "/{projectId}")
                .then().extract().response();
    }

    @Step("POST /projects - criar projeto")
    public Response create(ProjectRequest payload) {
        return givenRequest()
                .body(payload)
                .when()
                .post(Endpoints.PROJECTS)
                .then().extract().response();
    }

    @Step("GET /projects/{projectId}/members - listar membros")
    public Response getMembers(String projectId) {
        return givenRequest()
                .when()
                .get(Endpoints.projectMembers(projectId))
                .then().extract().response();
    }

    @Step("GET /projects/{projectId}/members/filter - filtrar membros por nome")
    public Response getMembersFilter(String projectId, String name) {
        return givenRequest()
                .queryParam("name", name)
                .when()
                .get(Endpoints.projectMembersFilter(projectId))
                .then().extract().response();
    }

    @Step("GET /projects/{projectId}/members/suggestion - sugestão de membros")
    public Response getMembersSuggestion(String projectId) {
        return givenRequest()
                .when()
                .get(Endpoints.projectMembersSuggestion(projectId))
                .then().extract().response();
    }

    @Step("POST /projects/{projectId}/members/validate - validar membro por e-mail")
    public Response validateMember(String projectId, String email) {
        return givenRequest()
                .queryParam("email", email)
                .body("{}")
                .when()
                .post(Endpoints.projectMembersValidate(projectId))
                .then().extract().response();
    }

    @Step("POST /projects/{projectId}/members - adicionar membros")
    public Response addMembers(String projectId, List<Map<String, String>> members) {
        return givenRequest()
                .body(members)
                .when()
                .post(Endpoints.projectMembers(projectId))
                .then().extract().response();
    }

    @Step("GET /projects/{projectId}/plans/efficiency - eficiência dos planos")
    public Response getPlansEfficiency(String projectId) {
        return givenRequest()
                .when()
                .get(Endpoints.projectPlansEfficiency(projectId))
                .then().extract().response();
    }

    @Step("GET /projects/{projectId}/plans/summary - sumário dos planos")
    public Response getPlansSummary(String projectId) {
        return givenRequest()
                .when()
                .get(Endpoints.projectPlansSummary(projectId))
                .then().extract().response();
    }

    @Step("DELETE /projects/{projectId} - excluir projeto")
    public Response delete(String projectId) {
        return givenRequest()
                .when()
                .delete(Endpoints.deleteProject(projectId))
                .then().extract().response();
    }
}
