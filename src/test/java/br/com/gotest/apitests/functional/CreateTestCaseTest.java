package br.com.gotest.apitests.functional;

import br.com.gotest.apitests.base.BaseTest;
import br.com.gotest.apitests.builder.TestCaseRequestBuilder;
import br.com.gotest.apitests.model.response.TestCase;
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
@Feature("Tests")
@Story("Criar caso de teste")
public class CreateTestCaseTest extends BaseTest {

    private String projectId;
    private String suiteId;
    private String createdTestCaseId;

    @BeforeClass
    public void resolveIds() {
        projectId = config.defaultProjectId();
        suiteId = config.get("default.suite.id");
        if (projectId == null || projectId.isBlank() || suiteId == null || suiteId.isBlank()) {
            throw new SkipException("default.project.id ou default.suite.id não configurados — pulando classe");
        }
    }

    @AfterMethod(alwaysRun = true)
    public void cleanup() {
        if (createdTestCaseId != null) {
            log.info("[Cleanup] Excluindo caso de teste criado no teste: {}", createdTestCaseId);
            testCaseClient.delete(projectId, suiteId, createdTestCaseId);
            createdTestCaseId = null;
        }
    }

    @Test(description = "Criar caso de teste com payload válido retorna 200/201")
    @Description("Cenário feliz com todos os campos obrigatórios")
    public void validPayload_shouldCreateTestCase() {
        Response response = testCaseClient.create(projectId, suiteId, TestCaseRequestBuilder.valid());

        assertThat(response.getStatusCode()).isIn(200, 201);

        TestCase created = response.as(TestCase.class);
        createdTestCaseId = created.getId();
        assertThat(createdTestCaseId).isNotBlank();
    }

    @Test(description = "Criar caso de teste com título vazio retorna 400/422")
    @Description("Cenário negativo: título obrigatório")
    public void emptyTitle_shouldReturn400() {
        Response response = testCaseClient.create(projectId, suiteId, TestCaseRequestBuilder.invalidEmptyTitle());
        assertThat(response.getStatusCode()).isIn(400, 422);
    }

    @Test(description = "Criar caso de teste em suite inexistente retorna 403/404")
    @Description("Cenário negativo: suiteId inválido")
    public void nonExistingSuite_shouldReturn404() {
        Response response = testCaseClient.create(
                projectId,
                "00000000-0000-0000-0000-000000000000",
                TestCaseRequestBuilder.valid());
        assertThat(response.getStatusCode()).isIn(403, 404);
    }
}
