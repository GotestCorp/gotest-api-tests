package br.com.gotest.apitests.functional;

import br.com.gotest.apitests.base.BaseTest;
import br.com.gotest.apitests.builder.ProjectRequestBuilder;
import br.com.gotest.apitests.model.request.ProjectRequest;
import br.com.gotest.apitests.model.response.Project;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@Epic("Functional Tests")
@Feature("Projects")
@Story("Criar projeto")
public class CreateProjectTest extends BaseTest {

    private String createdProjectId;

    @AfterMethod(alwaysRun = true)
    public void cleanup() {
        if (createdProjectId != null) {
            log.info("[Cleanup] Excluindo projeto criado no teste: {}", createdProjectId);
            projectClient.delete(createdProjectId);
            createdProjectId = null;
        }
    }

    @Test(description = "Criar projeto com payload válido retorna 200/201 com o projeto criado")
    @Description("Cenário feliz: payload com name, identifier e description")
    public void validPayload_shouldCreateProject() {
        ProjectRequest payload = ProjectRequestBuilder.valid();

        Response response = projectClient.create(payload);

        assertThat(response.getStatusCode()).isIn(200, 201);

        Project created = response.as(Project.class);
        createdProjectId = created.getId();
        assertThat(createdProjectId).isNotBlank();
        assertThat(created.getName()).isEqualTo(payload.getName());
    }

    @Test(description = "Criar projeto sem nome retorna 400/422")
    @Description("Cenário negativo: campo obrigatório ausente")
    public void emptyName_shouldReturn400() {
        Response response = projectClient.create(ProjectRequestBuilder.invalidEmptyName());
        assertThat(response.getStatusCode()).isIn(400, 422);
    }

    @Test(description = "Criar projeto com nome nulo retorna 400/422")
    @Description("Cenário negativo: name null")
    public void nullName_shouldReturn400() {
        Response response = projectClient.create(ProjectRequestBuilder.invalidNullName());
        assertThat(response.getStatusCode()).isIn(400, 422);
    }

    @Test(description = "Sem token, criar projeto retorna 401/403")
    @Description("Cenário negativo: requisição não autenticada")
    public void withoutToken_shouldReturn401() {
        Response response = io.restassured.RestAssured
                .given()
                    .baseUri(config.baseUrl())
                    .contentType("application/json")
                    .accept("application/json")
                    .body(ProjectRequestBuilder.valid())
                .when()
                    .post("/projects");

        assertThat(response.getStatusCode()).isIn(401, 403);
    }
}
