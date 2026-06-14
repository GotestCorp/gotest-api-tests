package br.com.gotest.apitests.functional;

import br.com.gotest.apitests.base.BaseTest;
import br.com.gotest.apitests.model.response.User;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Functional Tests")
@Feature("Users")
@Story("Perfil do usuário")
public class GetUserMeTest extends BaseTest {

    @Test(description = "GET /users/me retorna 200 e o perfil do usuário autenticado")
    @Description("Cenário positivo: usuário autenticado obtém seu perfil")
    public void shouldReturn200AndUserProfile() {
        Response response = userClient.getMe();

        assertThat(response.getStatusCode()).isEqualTo(200);

        User user = response.as(User.class);
        assertThat(user.getId()).isNotBlank();
        assertThat(user.getEmail()).isNotBlank();
    }

    @Test(description = "Sem token, GET /users/me retorna 401/403")
    @Description("Cenário negativo: requisição não autenticada")
    public void withoutToken_shouldReturn401() {
        Response response = io.restassured.RestAssured
                .given()
                    .baseUri(config.baseUrl())
                    .accept("application/json")
                .when()
                    .get("/users/me");

        assertThat(response.getStatusCode()).isIn(401, 403);
    }
}
