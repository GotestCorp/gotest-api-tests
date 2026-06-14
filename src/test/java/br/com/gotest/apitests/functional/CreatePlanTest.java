package br.com.gotest.apitests.functional;

import br.com.gotest.apitests.base.BaseTest;
import br.com.gotest.apitests.builder.PlanRequestBuilder;
import br.com.gotest.apitests.model.response.Plan;
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
@Feature("Plans")
@Story("Criar plano de teste")
public class CreatePlanTest extends BaseTest {

    private String projectId;
    private String createdPlanId;

    @BeforeClass
    public void resolveProjectId() {
        projectId = config.defaultProjectId();
        if (projectId == null || projectId.isBlank()) {
            throw new SkipException("default.project.id não configurado — pulando classe");
        }
    }

    @AfterMethod(alwaysRun = true)
    public void cleanup() {
        if (createdPlanId != null) {
            log.info("[Cleanup] Excluindo plano criado no teste: {}", createdPlanId);
            planClient.delete(projectId, createdPlanId);
            createdPlanId = null;
        }
    }

    @Test(description = "Criar plano com payload válido retorna 200/201")
    @Description("Cenário feliz com name e description")
    public void validPayload_shouldCreatePlan() {
        Response response = planClient.create(projectId, PlanRequestBuilder.valid());

        assertThat(response.getStatusCode()).isIn(200, 201);

        Plan created = response.as(Plan.class);
        createdPlanId = created.getId();
        assertThat(createdPlanId).isNotBlank();
    }

    @Test(description = "Criar plano com nome vazio retorna 400/422")
    @Description("Cenário negativo: nome obrigatório")
    public void emptyName_shouldReturn400() {
        Response response = planClient.create(projectId, PlanRequestBuilder.invalidEmptyName());
        assertThat(response.getStatusCode()).isIn(400, 422);
    }

    @Test(description = "Criar plano em projeto inexistente retorna 403/404")
    @Description("Cenário negativo: projectId inválido")
    public void nonExistingProject_shouldReturn404() {
        Response response = planClient.create(
                "00000000-0000-0000-0000-000000000000",
                PlanRequestBuilder.valid());
        assertThat(response.getStatusCode()).isIn(403, 404);
    }

    @Test(description = "Listar planos do projeto retorna 200")
    @Description("Cenário positivo: listagem paginada de planos via POST /plans/filter")
    public void listPlans_shouldReturn200() {
        Response response = planClient.filterDefault(projectId);
        assertThat(response.getStatusCode()).isEqualTo(200);
    }
}
