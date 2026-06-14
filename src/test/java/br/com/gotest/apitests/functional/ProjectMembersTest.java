package br.com.gotest.apitests.functional;

import br.com.gotest.apitests.base.BaseTest;
import br.com.gotest.apitests.util.DataFaker;
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
 * <h2>Functional Tests — Membros do Projeto</h2>
 *
 * <p>Cobre os endpoints de membros, que antes não tinham nenhum teste:
 * listagem, sugestão, filtro por nome e validação de e-mail.
 *
 * <p><b>Importante</b>: os cenários de validação usam e-mails fake aleatórios
 * ({@link DataFaker#email()}). Nenhum e-mail real de gestor é usado, de propósito,
 * para não disparar notificações/convites a ninguém real.
 */
@Epic("Functional Tests")
@Feature("Project Members")
@Story("Gestão de membros do projeto")
public class ProjectMembersTest extends BaseTest {

    private String projectId;

    @BeforeClass
    public void resolveProjectId() {
        projectId = config.defaultProjectId();
        if (projectId == null || projectId.isBlank()) {
            throw new SkipException("default.project.id não configurado — pulando classe");
        }
    }

    @Test(description = "GET /projects/{id}/members retorna 200")
    @Description("Cenário positivo: listagem de membros do projeto")
    public void listMembers_shouldReturn200() {
        Response response = projectClient.getMembers(projectId);
        assertThat(response.getStatusCode()).isEqualTo(200);
    }

    @Test(description = "GET /projects/{id}/members/suggestion retorna 200")
    @Description("Cenário positivo: sugestões de membros para o projeto")
    public void suggestMembers_shouldReturn200() {
        Response response = projectClient.getMembersSuggestion(projectId);
        assertThat(response.getStatusCode()).isEqualTo(200);
    }

    @Test(description = "GET /projects/{id}/members/filter por nome aleatório retorna 200")
    @Description("Cenário positivo: filtro de membros por nome (não precisa haver match)")
    public void filterMembers_shouldReturn200() {
        Response response = projectClient.getMembersFilter(projectId, DataFaker.fullName());
        assertThat(response.getStatusCode()).isEqualTo(200);
    }

    @Test(description = "POST /projects/{id}/members/validate com e-mail fake retorna 2xx")
    @Description("Cenário de validação de e-mail de membro. Usa e-mail fake aleatório — "
            + "nunca um e-mail real, para não gerar convites/notificações indevidos.")
    public void validateMemberWithFakeEmail_shouldRespond() {
        String fakeEmail = DataFaker.email();
        Response response = projectClient.validateMember(projectId, fakeEmail);

        // A API pode aceitar (200) ou recusar (4xx) o e-mail; o que NÃO pode é dar 5xx.
        assertThat(response.getStatusCode())
                .as("Validação de membro não deveria estourar erro de servidor")
                .isLessThan(500);
    }
}
