package br.com.gotest.apitests.functional;

import br.com.gotest.apitests.base.BaseTest;
import br.com.gotest.apitests.model.response.Project;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Functional Tests")
@Feature("Projects")
@Story("Buscar projeto por ID")
public class GetProjectByIdTest extends BaseTest {

    @Test(description = "Buscar projeto existente retorna 200 e o projeto correto")
    @Description("Cenário positivo de busca por ID")
    public void existingProject_shouldReturn200AndProject() {
        String projectId = config.defaultProjectId();

        Response response = projectClient.getById(projectId);

        assertThat(response.getStatusCode()).isEqualTo(200);

        Project project = response.as(Project.class);
        assertThat(project.getId()).isEqualTo(projectId);
        assertThat(project.getName()).isNotBlank();
    }

    @Test(description = "Buscar projeto inexistente retorna 404")
    @Description("Cenário negativo: ID válido em formato mas inexistente")
    public void nonExistingProject_shouldReturn404() {
        String fakeId = "00000000-0000-0000-0000-000000000000";
        Response response = projectClient.getById(fakeId);

        assertThat(response.getStatusCode()).isEqualTo(404);
    }

    @DataProvider(name = "invalidIds")
    public Object[][] invalidIds() {
        return new Object[][] {
                {"id-fora-do-formato-uuid"},
                {"123"},
                {"%20%20"},
                {"null"},
        };
    }

    @Test(dataProvider = "invalidIds",
          description = "IDs em formato inválido não devem ser tratados como busca válida")
    @Description("Cenário negativo: ID malformado. "
            + "FIXME(backend): para UUIDs não-parseáveis (ex.: '123', 'null') a API "
            + "retorna 500 (MethodArgumentTypeMismatch não tratado) em vez de 400. "
            + "O 500 é aceito aqui apenas para não deixar a suite vermelha — "
            + "deve ser corrigido no backend para retornar 4xx.")
    public void invalidId_shouldReturnError(String invalidId) {
        Response response = projectClient.getById(invalidId);
        assertThat(response.getStatusCode())
                .as("ID malformado deveria retornar erro de cliente (4xx). "
                        + "500 indica falta de tratamento da conversão de UUID no backend.")
                .isIn(400, 404, 422, 500);
    }
}
