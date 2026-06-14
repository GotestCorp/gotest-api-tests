package br.com.gotest.apitests.functional;

import br.com.gotest.apitests.base.BaseTest;
import br.com.gotest.apitests.model.response.Project;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * <h2>Functional Tests — Listagem de Projetos</h2>
 *
 * <p><b>Objetivo</b>: validar regra de negócio. Aqui sim verificamos
 * tipos, valores, presença de campos obrigatórios, etc.
 */
@Epic("Functional Tests")
@Feature("Projects")
@Story("Listar projetos")
public class ListProjectsTest extends BaseTest {

    @Test(description = "GET /projects retorna 200 e uma lista de projetos")
    @Description("Cenário positivo: usuário autenticado consegue listar seus projetos")
    public void shouldReturn200AndListOfProjects() {
        Response response = projectClient.listAll();

        assertThat(response.getStatusCode()).isEqualTo(200);

        // Desserializa a resposta. Ajuste se a API envelopa em { "data": [...] }.
        List<Project> projects = response.jsonPath().getList("$", Project.class);

        assertThat(projects)
                .as("A lista de projetos não deveria ser nula")
                .isNotNull();
    }

    @Test(description = "Cada projeto retornado possui id e nome não-nulos",
          enabled = false /* habilite quando a conta tiver projetos */)
    @Description("Valida que campos críticos vêm preenchidos")
    public void eachProjectShouldHaveIdAndName() {
        List<Project> projects = projectClient.listAll().jsonPath().getList("$", Project.class);

        assertThat(projects).isNotEmpty();
        assertThat(projects).allSatisfy(p -> {
            assertThat(p.getId()).as("id não pode ser nulo").isNotBlank();
            assertThat(p.getName()).as("name não pode ser nulo").isNotBlank();
        });
    }

    @Test(description = "Sem token, GET /projects deveria retornar 401")
    @Description("Cenário negativo de autenticação")
    public void withoutToken_shouldReturn401() {
        // Aqui usamos given() direto pra construir uma chamada SEM o token,
        // ignorando a spec base. Útil pra cenários onde a auth é o ponto do teste.
        Response response = io.restassured.RestAssured
                .given()
                    .baseUri(config.baseUrl())
                    .accept("application/json")
                .when()
                    .get("/projects");

        assertThat(response.getStatusCode())
                .as("Esperado 401 sem token")
                .isIn(401, 403);
    }
}
