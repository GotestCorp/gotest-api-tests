package br.com.gotest.apitests.contract;

import br.com.gotest.apitests.base.BaseTest;
import br.com.gotest.apitests.builder.SuiteRequestBuilder;
import br.com.gotest.apitests.model.request.SuiteRequest;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.testng.annotations.Test;

@Epic("Contract Tests")
@Feature("Schema do recurso Suites")
public class SuitesContractTest extends BaseTest {

    @Test(description = "Response de POST /projects/{id}/nodes/suites respeita o schema",
          enabled = false /* habilite quando default.project.id estiver configurado */)
    @Description("Valida estrutura JSON do response de criação de suite")
    public void createSuite_shouldMatchSchema() {
        SuiteRequest payload = SuiteRequestBuilder.valid();
        Response response = suiteClient.create(config.defaultProjectId(), payload);

        response.then()
                .statusCode(org.hamcrest.Matchers.anyOf(
                        org.hamcrest.Matchers.equalTo(200),
                        org.hamcrest.Matchers.equalTo(201)))
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/suite-schema.json"));
    }
}
