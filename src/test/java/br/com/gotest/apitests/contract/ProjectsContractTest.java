package br.com.gotest.apitests.contract;

import br.com.gotest.apitests.base.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.testng.annotations.Test;

/**
 * <h2>Contract Tests</h2>
 *
 * <p><b>Objetivo</b>: validar que a API respeita o <i>contrato</i> esperado —
 * ou seja, a estrutura do JSON (campos obrigatórios, tipos, formato).
 *
 * <p>Diferente do health check (que só vê se respondeu) e do funcional (que
 * exercita regras de negócio), aqui o foco é o <i>shape</i> do response.
 *
 * <p>Mecanismo: cada endpoint tem um schema JSON em
 * {@code src/test/resources/schemas/} validado com {@code json-schema-validator}.
 *
 * <p><b>Importante</b>: como a aplicação não tem Swagger, os schemas foram
 * inferidos. Atualize-os assim que tiver responses reais — eles são a fonte
 * da verdade do contrato.
 */
@Epic("Contract Tests")
@Feature("Schema do recurso Projects")
public class ProjectsContractTest extends BaseTest {

    @Test(description = "Response de GET /projects respeita o schema esperado")
    @Description("Valida estrutura JSON da listagem de projetos contra schema versionado")
    public void listProjects_shouldMatchSchema() {
        Response response = projectClient.listAll();

        response.then()
                .statusCode(200)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/projects-list-schema.json"));
    }

    @Test(description = "Response de GET /projects/{id} respeita o schema",
          enabled = false /* habilite quando tiver um ID válido em config */)
    @Description("Valida estrutura JSON de um projeto específico contra schema")
    public void getProjectById_shouldMatchSchema() {
        String projectId = config.defaultProjectId();
        Response response = projectClient.getById(projectId);

        response.then()
                .statusCode(200)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/project-schema.json"));
    }
}
