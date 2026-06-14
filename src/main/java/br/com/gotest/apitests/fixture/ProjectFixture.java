package br.com.gotest.apitests.fixture;

import br.com.gotest.apitests.builder.ProjectRequestBuilder;
import br.com.gotest.apitests.client.ProjectClient;
import br.com.gotest.apitests.model.request.ProjectRequest;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * Cria e remove projetos em massa para setup/teardown de dados de teste.
 *
 * <p>Uso típico:
 * <pre>
 *   List&lt;String&gt; ids = ProjectFixture.createMany(5);
 *   // ... testes ...
 *   ProjectFixture.deleteMany(ids);
 * </pre>
 */
@Slf4j
public class ProjectFixture {

    private static final ProjectClient CLIENT = new ProjectClient();

    private ProjectFixture() {}

    public static String create(ProjectRequest payload) {
        Response response = CLIENT.create(payload);
        int status = response.getStatusCode();
        if (status != 200 && status != 201) {
            throw new FixtureException("Falha ao criar projeto. Status: " + status + " | Body: " + response.getBody().asString());
        }
        String id = response.jsonPath().getString("id");
        log.info("[ProjectFixture] Criado: id={} name={}", id, payload.getName());
        return id;
    }

    public static String create(String name, String identifier) {
        return create(ProjectRequestBuilder.aProject()
                .withName(name)
                .withIdentifier(identifier)
                .build());
    }

    public static List<String> createMany(int count) {
        List<String> ids = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            ids.add(create(ProjectRequestBuilder.valid()));
        }
        log.info("[ProjectFixture] {} projeto(s) criado(s).", count);
        return ids;
    }

    public static void delete(String projectId) {
        Response response = CLIENT.delete(projectId);
        int status = response.getStatusCode();
        if (status >= 400 && status != 404) {
            log.warn("[ProjectFixture] Falha ao excluir projeto {}. Status: {}", projectId, status);
        } else {
            log.info("[ProjectFixture] Excluído: id={}", projectId);
        }
    }

    public static void deleteMany(List<String> projectIds) {
        projectIds.forEach(ProjectFixture::delete);
        log.info("[ProjectFixture] {} projeto(s) excluído(s).", projectIds.size());
    }
}
