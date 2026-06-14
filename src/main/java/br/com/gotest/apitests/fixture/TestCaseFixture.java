package br.com.gotest.apitests.fixture;

import br.com.gotest.apitests.builder.TestCaseRequestBuilder;
import br.com.gotest.apitests.client.TestCaseClient;
import br.com.gotest.apitests.model.bulk.BulkTestCaseDefaults;
import br.com.gotest.apitests.model.bulk.BulkTestCaseDefinition;
import br.com.gotest.apitests.model.bulk.BulkTestCaseGroup;
import br.com.gotest.apitests.model.request.TestCaseRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class TestCaseFixture {

    private static final TestCaseClient CLIENT = new TestCaseClient();
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private TestCaseFixture() {}

    // ---- Criação individual ----

    public static String create(String projectId, String suiteId, TestCaseRequest payload) {
        Response response = CLIENT.create(projectId, suiteId, payload);
        int status = response.getStatusCode();
        if (status != 200 && status != 201) {
            throw new FixtureException("Falha ao criar caso de teste. Status: " + status + " | Body: " + response.getBody().asString());
        }
        String id = response.jsonPath().getString("id");
        log.info("[TestCaseFixture] Criado: id={} title={}", id, payload.getTitle());
        return id;
    }

    public static String create(String projectId, String suiteId, String title) {
        return create(projectId, suiteId, TestCaseRequestBuilder.aTestCase()
                .withTestType("SCENARIO")
                .withTitle(title)
                .withStatus("IN_PROGRESS")
                .withPriority("MEDIUM")
                .withExecutionType("MANUAL")
                .build());
    }

    public static List<String> createMany(String projectId, String suiteId, int count) {
        List<String> ids = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            ids.add(create(projectId, suiteId, TestCaseRequestBuilder.valid()));
        }
        log.info("[TestCaseFixture] {} caso(s) criado(s) na suite {}.", count, suiteId);
        return ids;
    }

    // ---- Criação em bulk ----

    /**
     * Cria casos de teste em múltiplos destinos (projetos/suites diferentes) de uma vez.
     *
     * <p>Cada {@link BulkTestCaseGroup} representa um destino (projectId + suiteId) com
     * seus próprios casos e defaults. Os campos individuais de cada caso sobrescrevem
     * os defaults do grupo quando informados.
     *
     * <pre>
     * List&lt;String&gt; ids = TestCaseFixture.createBulk(List.of(
     *     new BulkTestCaseGroup("proj-1", "suite-A", null, List.of(
     *         new BulkTestCaseDefinition("Login com sucesso",    null, null, "HIGH",   null),
     *         new BulkTestCaseDefinition("Login inválido",       null, null, "MEDIUM", null)
     *     )),
     *     new BulkTestCaseGroup("proj-1", "suite-B", null, List.of(
     *         new BulkTestCaseDefinition("Cadastro de usuário",  null, null, "HIGH",   null)
     *     ))
     * ));
     * </pre>
     */
    public static List<String> createBulk(List<BulkTestCaseGroup> groups) {
        List<String> allIds = new ArrayList<>();
        for (BulkTestCaseGroup group : groups) {
            BulkTestCaseDefaults defs = group.getDefaults() != null
                    ? group.getDefaults()
                    : new BulkTestCaseDefaults();

            log.info("[TestCaseFixture] Grupo: projectId={} suiteId={} ({} caso(s))",
                    group.getProjectId(), group.getSuiteId(), group.getCases().size());

            for (BulkTestCaseDefinition def : group.getCases()) {
                TestCaseRequest req = TestCaseRequest.builder()
                        .testType(coalesce(def.getTestType(), defs.getTestType()))
                        .title(def.getTitle())
                        .status(coalesce(def.getStatus(), defs.getStatus()))
                        .priority(coalesce(def.getPriority(), defs.getPriority()))
                        .executionType(coalesce(def.getExecutionType(), defs.getExecutionType()))
                        .build();
                allIds.add(create(group.getProjectId(), group.getSuiteId(), req));
            }
        }
        log.info("[TestCaseFixture] Bulk concluído: {} caso(s) criado(s) no total.", allIds.size());
        return allIds;
    }

    /**
     * Lê um arquivo JSON e cria todos os casos de teste nele descritos.
     *
     * <p>O arquivo deve conter um array de {@link BulkTestCaseGroup}. Veja o
     * exemplo em {@code src/test/resources/bulk-test-cases-sample.json}.
     *
     * <pre>
     * // Caminho absoluto
     * TestCaseFixture.createFromJson("C:/dados/meus-casos.json");
     *
     * // Caminho relativo ao diretório de execução do Maven
     * TestCaseFixture.createFromJson("src/test/resources/bulk-test-cases-sample.json");
     * </pre>
     */
    public static List<String> createFromJson(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FixtureException("Arquivo não encontrado: " + file.getAbsolutePath());
        }
        List<BulkTestCaseGroup> groups = MAPPER.readValue(
                file,
                MAPPER.getTypeFactory().constructCollectionType(List.class, BulkTestCaseGroup.class)
        );
        log.info("[TestCaseFixture] JSON lido: {} grupo(s) em '{}'.", groups.size(), file.getAbsolutePath());
        return createBulk(groups);
    }

    // ---- Exclusão ----

    public static void delete(String projectId, String suiteId, String testId) {
        Response response = CLIENT.delete(projectId, suiteId, testId);
        int status = response.getStatusCode();
        if (status >= 400 && status != 404) {
            log.warn("[TestCaseFixture] Falha ao excluir caso {}. Status: {}", testId, status);
        } else {
            log.info("[TestCaseFixture] Excluído: id={}", testId);
        }
    }

    public static void deleteMany(String projectId, String suiteId, List<String> testIds) {
        testIds.forEach(id -> delete(projectId, suiteId, id));
        log.info("[TestCaseFixture] {} caso(s) excluído(s).", testIds.size());
    }

    // ---- Utilitário ----

    private static String coalesce(String value, String fallback) {
        return (value != null && !value.isBlank()) ? value : fallback;
    }
}
