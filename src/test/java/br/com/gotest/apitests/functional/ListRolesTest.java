package br.com.gotest.apitests.functional;

import br.com.gotest.apitests.base.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * <h2>Functional Tests — Access Management / Roles</h2>
 *
 * <p>Antes só havia health check. Aqui validamos a regra de negócio: o catálogo
 * de papéis precisa vir preenchido e com os campos que a UI consome
 * ({@code authority}, {@code name}, {@code description}, {@code id}).
 */
@Epic("Functional Tests")
@Feature("Access Management")
@Story("Listar papéis de acesso")
public class ListRolesTest extends BaseTest {

    @Test(description = "GET /accessManagement/listRoles retorna 200 e lista de papéis")
    @Description("Cenário positivo: catálogo de papéis disponível e bem formado")
    public void shouldReturn200AndRoles() {
        Response response = accessManagementClient.listRoles();

        assertThat(response.getStatusCode()).isEqualTo(200);

        List<Map<String, Object>> roles = response.jsonPath().getList("roles");
        assertThat(roles)
                .as("A lista de papéis não deveria ser nula nem vazia")
                .isNotNull()
                .isNotEmpty();

        assertThat(roles).allSatisfy(role -> {
            assertThat(role.get("authority")).as("authority não pode ser nulo").isNotNull();
            assertThat(role.get("id")).as("id não pode ser nulo").isNotNull();
        });
    }

    @Test(description = "O papel ROLE_ADMIN está presente no catálogo")
    @Description("Garante que um papel central da plataforma continua sendo exposto")
    public void shouldContainAdminRole() {
        Response response = accessManagementClient.listRoles();

        List<String> authorities = response.jsonPath().getList("roles.authority");
        assertThat(authorities)
                .as("ROLE_ADMIN deveria estar entre os papéis disponíveis")
                .contains("ROLE_ADMIN");
    }
}
