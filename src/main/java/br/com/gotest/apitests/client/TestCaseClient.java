package br.com.gotest.apitests.client;

import br.com.gotest.apitests.endpoint.Endpoints;
import br.com.gotest.apitests.model.request.FilterRequest;
import br.com.gotest.apitests.model.request.TestCaseRequest;
import br.com.gotest.apitests.util.JsonUtils;
import io.qameta.allure.Step;
import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.response.Response;

import java.nio.charset.StandardCharsets;

public class TestCaseClient extends BaseApiClient {

    @Step("POST /projects/{projectId}/suites/{suiteId}/tests/filter - listar testes paginado/filtrado")
    public Response filter(String projectId, String suiteId, FilterRequest filter) {
        return givenRequest()
                .body(filter)
                .when()
                .post(Endpoints.suiteTestsFilter(projectId, suiteId))
                .then().extract().response();
    }

    @Step("GET /projects/{projectId}/suites/{suiteId}/tests - listar testes paginado")
    public Response list(String projectId, String suiteId, int page, int size, String query, String orderBy) {
        return givenRequest()
                .queryParam("page", page)
                .queryParam("size", size)
                .queryParam("query", query)
                .queryParam("orderBy", orderBy)
                .when()
                .get(Endpoints.suiteTests(projectId, suiteId))
                .then().extract().response();
    }

    @Step("GET /projects/{projectId}/suites/{suiteId}/tests/all - todos os testes")
    public Response listAll(String projectId, String suiteId) {
        return givenRequest()
                .when()
                .get(Endpoints.suiteTestsAll(projectId, suiteId))
                .then().extract().response();
    }

    @Step("GET /projects/{projectId}/suites/{suiteId}/tests/nextIdentifier - próximo identificador")
    public Response getNextIdentifier(String projectId, String suiteId) {
        return givenRequest()
                .when()
                .get(Endpoints.suiteTestsNextIdentifier(projectId, suiteId))
                .then().extract().response();
    }

    @Step("POST /projects/{projectId}/suites/{suiteId}/tests/validate-title - validar título")
    public Response validateTitle(String projectId, String suiteId, String title) {
        return givenRequest()
                .body("{\"title\": \"" + title + "\"}")
                .when()
                .post(Endpoints.suiteTestsValidateTitle(projectId, suiteId))
                .then().extract().response();
    }

    /**
     * Cria um caso de teste.
     *
     * <p>A API mudou (refatoração 2026-06): o endpoint passou a aceitar
     * {@code multipart/form-data} com uma parte {@code request} contendo o JSON
     * do caso de teste (e, opcionalmente, anexos). Enviar o corpo como JSON puro
     * agora resulta em {@code "Failed to parse multipart servlet request"}.
     */
    @Step("POST /projects/{projectId}/suites/{suiteId}/tests - criar teste (multipart)")
    public Response create(String projectId, String suiteId, TestCaseRequest payload) {
        return givenRequest()
                .contentType("multipart/form-data")
                .multiPart(new MultiPartSpecBuilder(JsonUtils.toJson(payload))
                        .controlName("request")
                        .mimeType("application/json")
                        .charset(StandardCharsets.UTF_8)
                        .build())
                .when()
                .post(Endpoints.suiteTests(projectId, suiteId))
                .then().extract().response();
    }

    @Step("GET /projects/{projectId}/suites/{suiteId}/tests/{testId} - buscar teste por ID")
    public Response getById(String projectId, String suiteId, String testId) {
        return givenRequest()
                .when()
                .get(Endpoints.suiteTestById(projectId, suiteId, testId))
                .then().extract().response();
    }

    @Step("POST /projects/{projectId}/suites/{suiteId}/tests/{testId}/ai/review - solicitar revisão IA")
    public Response requestAiReview(String projectId, String suiteId, String testId) {
        return givenRequest()
                .body("{}")
                .when()
                .post(Endpoints.suiteTestAiReview(projectId, suiteId, testId))
                .then().extract().response();
    }

    @Step("GET /projects/{projectId}/suites/{suiteId}/tests/{testId}/ai/reviews - listar revisões IA")
    public Response getAiReviews(String projectId, String suiteId, String testId) {
        return givenRequest()
                .when()
                .get(Endpoints.suiteTestAiReviews(projectId, suiteId, testId))
                .then().extract().response();
    }

    @Step("DELETE /projects/{projectId}/suites/{suiteId}/tests/{testId} - excluir caso de teste")
    public Response delete(String projectId, String suiteId, String testId) {
        return givenRequest()
                .when()
                .delete(Endpoints.deleteSuiteTest(projectId, suiteId, testId))
                .then().extract().response();
    }
}
