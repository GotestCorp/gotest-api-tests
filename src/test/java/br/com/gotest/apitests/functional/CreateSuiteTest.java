package br.com.gotest.apitests.functional;

import br.com.gotest.apitests.base.BaseTest;
import br.com.gotest.apitests.builder.SuiteRequestBuilder;
import br.com.gotest.apitests.model.request.SuiteRequest;
import br.com.gotest.apitests.model.response.Suite;
import br.com.gotest.apitests.util.DataFaker;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@Epic("Functional Tests")
@Feature("Suites")
@Story("Criar suite")
public class CreateSuiteTest extends BaseTest {

    private String projectId;
    private String createdSuiteId;

    @BeforeClass
    public void resolveProjectId() {
        projectId = config.defaultProjectId();
        if (projectId == null || projectId.isBlank()) {
            throw new SkipException("default.project.id não configurado — pulando classe");
        }
    }

    @AfterMethod(alwaysRun = true)
    public void cleanup() {
        if (createdSuiteId != null) {
            log.info("[Cleanup] Excluindo suite criada no teste: {}", createdSuiteId);
            nodeClient.deleteNode(projectId, createdSuiteId);
            createdSuiteId = null;
        }
    }

    @Test(description = "Criar suite com payload válido retorna 200/201 e devolve a suite criada")
    @Description("Cenário feliz com payload mínimo válido (apenas name)")
    public void validPayload_shouldCreateSuite() {
        SuiteRequest payload = SuiteRequestBuilder.aSuite()
                .withName("Suite " + DataFaker.shortUuid())
                .build();

        Response response = suiteClient.create(projectId, payload);

        assertThat(response.getStatusCode()).isIn(200, 201);

        Suite created = response.as(Suite.class);
        createdSuiteId = created.getId();
        assertThat(createdSuiteId).isNotBlank();
        assertThat(created.getName()).isEqualTo(payload.getName());
    }

    @Test(description = "Criar suite com nome vazio retorna 400")
    @Description("Cenário negativo: validação de campo obrigatório")
    public void emptyName_shouldReturn400() {
        Response response = suiteClient.create(projectId, SuiteRequestBuilder.invalidEmptyName());
        assertThat(response.getStatusCode()).isIn(400, 422);
    }

    @Test(description = "Criar suite com nome null retorna 400")
    @Description("Cenário negativo: nome ausente")
    public void nullName_shouldReturn400() {
        Response response = suiteClient.create(projectId, SuiteRequestBuilder.invalidNullName());
        assertThat(response.getStatusCode()).isIn(400, 422);
    }

    @Test(description = "Criar suite num projeto inexistente retorna 404")
    @Description("Cenário negativo: projectId inválido")
    public void nonExistingProject_shouldReturn404() {
        String fakeProjectId = "00000000-0000-0000-0000-000000000000";
        Response response = suiteClient.create(fakeProjectId, SuiteRequestBuilder.valid());
        assertThat(response.getStatusCode()).isIn(403, 404);
    }
}
