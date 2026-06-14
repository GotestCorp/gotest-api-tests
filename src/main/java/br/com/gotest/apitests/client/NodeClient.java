package br.com.gotest.apitests.client;

import br.com.gotest.apitests.endpoint.Endpoints;
import br.com.gotest.apitests.model.request.FilterRequest;
import br.com.gotest.apitests.model.request.FolderRequest;
import br.com.gotest.apitests.model.request.SuiteRequest;
import io.qameta.allure.Step;
import io.restassured.response.Response;

public class NodeClient extends BaseApiClient {

    @Step("GET /projects/{projectId}/nodes/all - todos os nós do projeto")
    public Response getAllNodes(String projectId) {
        return givenRequest()
                .when()
                .get(Endpoints.projectNodesAll(projectId))
                .then().extract().response();
    }

    @Step("POST /projects/{projectId}/nodes/filter - listar nós paginado/filtrado")
    public Response filter(String projectId, FilterRequest filter) {
        return givenRequest()
                .body(filter)
                .when()
                .post(Endpoints.projectNodesFilter(projectId))
                .then().extract().response();
    }

    @Step("POST /projects/{projectId}/nodes/filter - primeira página (filtro enxuto)")
    public Response filterDefault(String projectId) {
        return filter(projectId, FilterRequest.simpleFilter());
    }

    @Step("GET /projects/{projectId}/nodes/all-for-selection - nós para seleção")
    public Response getAllNodesForSelection(String projectId) {
        return givenRequest()
                .when()
                .get(Endpoints.projectNodesAllForSelection(projectId))
                .then().extract().response();
    }

    @Step("GET /projects/{projectId}/nodes/folders - listar pastas")
    public Response getFolders(String projectId) {
        return givenRequest()
                .when()
                .get(Endpoints.projectNodesFolders(projectId))
                .then().extract().response();
    }

    @Step("POST /projects/{projectId}/nodes/folders - criar pasta")
    public Response createFolder(String projectId, FolderRequest payload) {
        return givenRequest()
                .body(payload)
                .when()
                .post(Endpoints.projectNodesFolders(projectId))
                .then().extract().response();
    }

    @Step("GET /projects/{projectId}/nodes/suites - listar suites")
    public Response getSuites(String projectId) {
        return givenRequest()
                .when()
                .get(Endpoints.projectNodesSuites(projectId))
                .then().extract().response();
    }

    @Step("GET /projects/{projectId}/nodes/suites/nextIdentifier - próximo identificador de suite")
    public Response getSuitesNextIdentifier(String projectId) {
        return givenRequest()
                .when()
                .get(Endpoints.projectNodesSuitesNextIdentifier(projectId))
                .then().extract().response();
    }

    @Step("POST /projects/{projectId}/nodes/suites - criar suite")
    public Response createSuite(String projectId, SuiteRequest payload) {
        return givenRequest()
                .body(payload)
                .when()
                .post(Endpoints.projectNodesSuites(projectId))
                .then().extract().response();
    }

    @Step("GET /projects/{projectId}/nodes - listar nós paginado")
    public Response getNodes(String projectId, int page, int size, String query) {
        return givenRequest()
                .queryParam("page", page)
                .queryParam("size", size)
                .queryParam("query", query)
                .when()
                .get(Endpoints.projectNodes(projectId))
                .then().extract().response();
    }

    @Step("GET /projects/{projectId}/nodes/{nodeId} - buscar nó por ID")
    public Response getNodeById(String projectId, String nodeId) {
        return givenRequest()
                .when()
                .get(Endpoints.projectNodeById(projectId, nodeId))
                .then().extract().response();
    }

    @Step("DELETE /projects/{projectId}/nodes/{nodeId} - excluir nó (pasta ou suite)")
    public Response deleteNode(String projectId, String nodeId) {
        return givenRequest()
                .when()
                .delete(Endpoints.deleteNode(projectId, nodeId))
                .then().extract().response();
    }
}
