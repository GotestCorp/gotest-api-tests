package br.com.gotest.apitests.functional;

import br.com.gotest.apitests.base.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * <h2>Functional Tests — Navegação de Nós (pastas e suites)</h2>
 *
 * <p>A árvore de nós do projeto (pastas/suites) não tinha cobertura. Aqui
 * exercitamos as rotas de leitura usadas pela tela de testes, incluindo a nova
 * {@code POST /nodes/filter} (que substituiu o antigo {@code GET /nodes?page=}).
 */
@Epic("Functional Tests")
@Feature("Nodes")
@Story("Navegar pela árvore de nós do projeto")
public class NodesListTest extends BaseTest {

    private String projectId;

    @BeforeClass
    public void resolveProjectId() {
        projectId = config.defaultProjectId();
        if (projectId == null || projectId.isBlank()) {
            throw new SkipException("default.project.id não configurado — pulando classe");
        }
    }

    @Test(description = "GET /projects/{id}/nodes/all retorna 200 e uma lista")
    @Description("Cenário positivo: todos os nós do projeto")
    public void listAllNodes_shouldReturn200() {
        Response response = nodeClient.getAllNodes(projectId);

        assertThat(response.getStatusCode()).isEqualTo(200);
        assertThat(response.jsonPath().getList("$"))
                .as("Resposta de /nodes/all deveria ser uma lista")
                .isNotNull();
    }

    @Test(description = "GET /projects/{id}/nodes/folders retorna 200")
    @Description("Cenário positivo: listagem de pastas")
    public void listFolders_shouldReturn200() {
        Response response = nodeClient.getFolders(projectId);
        assertThat(response.getStatusCode()).isEqualTo(200);
    }

    @Test(description = "GET /projects/{id}/nodes/suites retorna 200")
    @Description("Cenário positivo: listagem de suites")
    public void listSuites_shouldReturn200() {
        Response response = nodeClient.getSuites(projectId);
        assertThat(response.getStatusCode()).isEqualTo(200);
    }

    @Test(description = "GET /projects/{id}/nodes/suites/nextIdentifier retorna 200")
    @Description("Cenário positivo: próximo identificador de suite")
    public void nextSuiteIdentifier_shouldReturn200() {
        Response response = nodeClient.getSuitesNextIdentifier(projectId);
        assertThat(response.getStatusCode()).isEqualTo(200);
    }

    @Test(description = "POST /projects/{id}/nodes/filter retorna 200 e estrutura paginada")
    @Description("Cenário positivo: filtro paginado de nós (substituiu o GET ?page= legado)")
    public void filterNodes_shouldReturn200() {
        Response response = nodeClient.filterDefault(projectId);

        assertThat(response.getStatusCode()).isEqualTo(200);
        Object content = response.jsonPath().get("content");
        assertThat(content)
                .as("Resposta de /nodes/filter deveria ter o campo 'content'")
                .isNotNull();
    }
}
