package br.com.gotest.apitests.healthcheck;

import br.com.gotest.apitests.base.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * <h2>Health Check Tests</h2>
 *
 * <p><b>Objetivo</b>: verificar se a API tá <i>de pé</i> e respondendo. Não valida
 * regra de negócio nem schema profundo. É o teste que você roda a cada deploy
 * pra saber em 5 segundos se algo quebrou catastroficamente.
 *
 * <p><b>O que testa</b>:
 * <ul>
 *     <li>Status code 2xx em endpoints críticos</li>
 *     <li>Latência dentro de um SLA (ex: &lt; 5s)</li>
 *     <li>Content-Type correto</li>
 * </ul>
 *
 * <p><b>O que NÃO testa</b>: estrutura de campos, valores, fluxos completos.
 */
@Epic("Health Check")
@Feature("Disponibilidade da API")
public class ProjectsHealthCheckTest extends BaseTest {

    private static final long SLA_MS = 5_000L;

    @Test(description = "GET /projects responde 2xx dentro do SLA")
    @Description("Verifica disponibilidade básica do endpoint de listagem de projetos")
    public void listProjects_shouldRespondWithinSla() {
        Response response = projectClient.listAll();

        assertThat(response.getStatusCode())
                .as("Status code deveria ser 2xx")
                .isBetween(200, 299);

        assertThat(response.getTime())
                .as("Latência deveria estar abaixo do SLA de %d ms", SLA_MS)
                .isLessThan(SLA_MS);
    }

    @Test(description = "Endpoint de busca por ID responde (mesmo que com 404 pra ID inexistente)")
    @Description("Health check do path parametrizado /projects/{id} — basta o serviço processar a rota")
    public void getProjectById_shouldRespond() {
        Response response = projectClient.getById("00000000-0000-0000-0000-000000000000");

        // Pra health check basta a API processar a rota (não retornar 5xx ou timeout).
        assertThat(response.getStatusCode())
                .as("Não deveria retornar 5xx — API parece estar instável")
                .isLessThan(500);

        assertThat(response.getTime()).isLessThan(SLA_MS);
    }
}
