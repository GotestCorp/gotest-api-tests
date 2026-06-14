package br.com.gotest.apitests.fixture;

import br.com.gotest.apitests.builder.FolderRequestBuilder;
import br.com.gotest.apitests.client.NodeClient;
import br.com.gotest.apitests.model.request.FolderRequest;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * Cria e remove pastas (nodes do tipo folder) em massa.
 */
@Slf4j
public class FolderFixture {

    private static final NodeClient CLIENT = new NodeClient();

    private FolderFixture() {}

    public static String create(String projectId, FolderRequest payload) {
        Response response = CLIENT.createFolder(projectId, payload);
        int status = response.getStatusCode();
        if (status != 200 && status != 201) {
            throw new FixtureException("Falha ao criar pasta. Status: " + status + " | Body: " + response.getBody().asString());
        }
        String id = response.jsonPath().getString("id");
        log.info("[FolderFixture] Criada: id={} name={}", id, payload.getName());
        return id;
    }

    public static String create(String projectId, String name) {
        return create(projectId, FolderRequestBuilder.aFolder().withName(name).build());
    }

    public static List<String> createMany(String projectId, int count) {
        List<String> ids = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            ids.add(create(projectId, FolderRequestBuilder.valid()));
        }
        log.info("[FolderFixture] {} pasta(s) criada(s) no projeto {}.", count, projectId);
        return ids;
    }

    public static void delete(String projectId, String nodeId) {
        Response response = CLIENT.deleteNode(projectId, nodeId);
        int status = response.getStatusCode();
        if (status >= 400 && status != 404) {
            log.warn("[FolderFixture] Falha ao excluir pasta {}. Status: {}", nodeId, status);
        } else {
            log.info("[FolderFixture] Excluída: id={}", nodeId);
        }
    }

    public static void deleteMany(String projectId, List<String> nodeIds) {
        nodeIds.forEach(id -> delete(projectId, id));
        log.info("[FolderFixture] {} pasta(s) excluída(s).", nodeIds.size());
    }
}
