package br.com.gotest.apitests.client;

import br.com.gotest.apitests.config.RestAssuredSpecs;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

/**
 * Classe base pra todos os clients. Encapsula a montagem da
 * {@link RequestSpecification} já com baseUri, headers e auth.
 *
 * <p>Subclasses só precisam declarar os métodos do recurso (listAll, getById...)
 * sem se preocupar com setup HTTP.
 */
public abstract class BaseApiClient {

    /**
     * Ponto de entrada pra qualquer chamada — já vem com spec base aplicada.
     * Sobrescreva headers/params específicos no próprio método do client se precisar.
     */
    protected RequestSpecification givenRequest() {
        return given().spec(RestAssuredSpecs.baseRequestSpec());
    }
}
