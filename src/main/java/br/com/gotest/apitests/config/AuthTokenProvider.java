package br.com.gotest.apitests.config;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * Resolve o Bearer token usado nas chamadas da suite.
 *
 * <p>Prioridade:
 * <ol>
 *     <li>{@code auth.token} explícito (override manual / cole um JWT já pronto)</li>
 *     <li>Login programático via {@code POST /auth/login} usando
 *     {@code auth.email} + {@code auth.password}, com o token resultante
 *     cacheado em memória pelo resto da execução</li>
 * </ol>
 *
 * <p>Os JWTs dessa API expiram em ~24h, então depender de um token fixo (secret
 * estático no CI) trava a pipeline todo dia. Fazendo login a cada execução,
 * o token nasce sempre fresco — sem renovação manual.
 *
 * <p>Nunca loga usuário, senha ou o token em si (nem via RestAssured request/response
 * logging), para não vazar credenciais em logs de CI.
 */
@Slf4j
public final class AuthTokenProvider {

    private static final ConfigManager CONFIG = ConfigManager.getInstance();
    private static final Object LOCK = new Object();

    private static volatile String cachedToken;

    private AuthTokenProvider() {
    }

    public static String getToken() {
        String staticToken = CONFIG.get("auth.token");
        if (staticToken != null && !staticToken.isBlank()) {
            return staticToken;
        }

        String token = cachedToken;
        if (token != null) {
            return token;
        }

        synchronized (LOCK) {
            if (cachedToken == null) {
                cachedToken = login();
            }
            return cachedToken;
        }
    }

    /**
     * Força um novo login no próximo {@link #getToken()}, descartando o token
     * cacheado. Útil se o token cacheado expirar no meio de uma suite muito longa.
     */
    public static void invalidate() {
        synchronized (LOCK) {
            cachedToken = null;
        }
    }

    private static String login() {
        String email = CONFIG.get("auth.email");
        String password = CONFIG.get("auth.password");

        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            log.warn("Nenhuma credencial configurada (auth.token ou auth.email/auth.password). " +
                    "Endpoints autenticados vão falhar com 401.");
            return null;
        }

        log.info("Autenticando via login programático (POST /auth/login)...");

        Map<String, Object> body = new HashMap<>();
        body.put("user", email);
        body.put("password", password);
        body.put("rememberMe", false);

        // Requisição "crua", sem os filtros de log/Allure da spec padrão:
        // o payload contém a senha e a resposta contém o accessToken.
        Response response = RestAssured.given()
                .baseUri(CONFIG.baseUrl())
                .contentType(ContentType.JSON)
                .body(body)
                .post("/auth/login");

        if (response.getStatusCode() != 200) {
            throw new IllegalStateException(
                    "Falha no login programático (status " + response.getStatusCode() + " em POST /auth/login). "
                            + "Verifique auth.email/auth.password (ou AUTH_EMAIL/AUTH_PASSWORD).");
        }

        String accessToken = response.jsonPath().getString("accessToken");
        if (accessToken == null || accessToken.isBlank()) {
            throw new IllegalStateException("Login retornou 200 mas sem accessToken no corpo da resposta.");
        }

        log.info("Login programático concluído com sucesso para {}.", email);
        return accessToken;
    }
}
