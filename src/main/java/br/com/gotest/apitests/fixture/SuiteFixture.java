package br.com.gotest.apitests.fixture;

import br.com.gotest.apitests.builder.SuiteRequestBuilder;
import br.com.gotest.apitests.client.NodeClient;
import br.com.gotest.apitests.model.request.SuiteRequest;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * Cria e remove suites em massa.
 */
@Slf4j
public class SuiteFixture {

    private static final NodeClient CLIENT = new NodeClient();

    private SuiteFixture() {}

    public static String create(String projectId, SuiteRequest payload) {
        Response response = CLIENT.createSuite(projectId, payload);
        int status = response.getStatusCode();
        if (status != 200 && status != 201) {
            throw new FixtureException("Falha ao criar suite. Status: " + status + " | Body: " + response.getBody().asString());
        }
        String id = response.jsonPath().getString("id");
        log.info("[SuiteFixture] Criada: id={} name={}", id, payload.getName());
        return id;
    }

    public static String create(String projectId, String name) {
        return create(projectId, SuiteRequestBuilder.aSuite().withName(name).build());
    }

    public static String create(String projectId, String name, String parentFolderId) {
        return create(projectId, SuiteRequestBuilder.aSuite()
                .withName(name)
                .withParentFolderId(parentFolderId)
                .build());
    }

    public static List<String> createMany(String projectId, int count) {
        List<String> ids = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            ids.add(create(projectId, SuiteRequestBuilder.valid()));
        }
        log.info("[SuiteFixture] {} suite(s) criada(s) no projeto {}.", count, projectId);
        return ids;
    }

    public static void delete(String projectId, String suiteId) {
        Response response = CLIENT.deleteNode(projectId, suiteId);
        int status = response.getStatusCode();
        if (status >= 400 && status != 404) {
            log.warn("[SuiteFixture] Falha ao excluir suite {}. Status: {}", suiteId, status);
        } else {
            log.info("[SuiteFixture] Excluída: id={}", suiteId);
        }
    }

    public static void deleteMany(String projectId, List<String> suiteIds) {
        suiteIds.forEach(id -> delete(projectId, id));
        log.info("[SuiteFixture] {} suite(s) excluída(s).", suiteIds.size());
    }
}
