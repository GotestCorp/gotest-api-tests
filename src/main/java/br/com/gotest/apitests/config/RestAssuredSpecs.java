package br.com.gotest.apitests.config;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import lombok.experimental.UtilityClass;

import static org.apache.http.params.CoreConnectionPNames.CONNECTION_TIMEOUT;
import static org.apache.http.params.CoreConnectionPNames.SO_TIMEOUT;

/**
 * Fábrica das specifications globais.
 *
 * <p>Ideia: cada client herda dessas specs ao invés de cada teste configurar
 * baseUri, headers, timeouts etc. do zero. Mantém os testes enxutos.
 */
@UtilityClass
public class RestAssuredSpecs {

    private static final ConfigManager CONFIG = ConfigManager.getInstance();

    /**
     * Spec base com baseUri, content-type, bearer token, timeouts e logging.
     * Use no setUp dos clients ou diretamente no given() quando precisar.
     */
    public static RequestSpecification baseRequestSpec() {
        RequestSpecBuilder builder = new RequestSpecBuilder()
                .setBaseUri(CONFIG.baseUrl())
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .setConfig(timeoutsConfig());

        String token = CONFIG.authToken();
        if (token != null && !token.isBlank()) {
            builder.addHeader("Authorization", "Bearer " + token);
        }

        if (CONFIG.logEnabled()) {
            builder.addFilter(new RequestLoggingFilter());
            builder.addFilter(new ResponseLoggingFilter());
        }

        // Allure attachment automático nas requisições
        builder.addFilter(new AllureRestAssured());

        return builder.build();
    }

    /**
     * Resposta esperada para 2xx genérico — útil pra encurtar asserts.
     */
    public static ResponseSpecification okResponseSpec() {
        return new ResponseSpecBuilder()
                .expectStatusCode(org.hamcrest.Matchers.allOf(
                        org.hamcrest.Matchers.greaterThanOrEqualTo(200),
                        org.hamcrest.Matchers.lessThan(300)))
                .expectContentType(ContentType.JSON)
                .build();
    }

    public static ResponseSpecification createdResponseSpec() {
        return new ResponseSpecBuilder()
                .expectStatusCode(201)
                .expectContentType(ContentType.JSON)
                .build();
    }

    public static ResponseSpecification notFoundResponseSpec() {
        return new ResponseSpecBuilder()
                .expectStatusCode(404)
                .build();
    }

    private static RestAssuredConfig timeoutsConfig() {
        return RestAssured.config().httpClient(
                HttpClientConfig.httpClientConfig()
                        .setParam(CONNECTION_TIMEOUT, CONFIG.connectionTimeout())
                        .setParam(SO_TIMEOUT, CONFIG.responseTimeout())
        );
    }
}
