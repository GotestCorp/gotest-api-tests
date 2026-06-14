package br.com.gotest.apitests.fixture;

import br.com.gotest.apitests.builder.PlanRequestBuilder;
import br.com.gotest.apitests.client.PlanClient;
import br.com.gotest.apitests.model.request.PlanRequest;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * Cria e remove planos de teste em massa.
 */
@Slf4j
public class PlanFixture {

    private static final PlanClient CLIENT = new PlanClient();

    private PlanFixture() {}

    public static String create(String projectId, PlanRequest payload) {
        Response response = CLIENT.create(projectId, payload);
        int status = response.getStatusCode();
        if (status != 200 && status != 201) {
            throw new FixtureException("Falha ao criar plano. Status: " + status + " | Body: " + response.getBody().asString());
        }
        String id = response.jsonPath().getString("id");
        log.info("[PlanFixture] Criado: id={} name={}", id, payload.getName());
        return id;
    }

    public static String create(String projectId, String name) {
        return create(projectId, PlanRequestBuilder.aPlan().withName(name).build());
    }

    public static List<String> createMany(String projectId, int count) {
        List<String> ids = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            ids.add(create(projectId, PlanRequestBuilder.valid()));
        }
        log.info("[PlanFixture] {} plano(s) criado(s) no projeto {}.", count, projectId);
        return ids;
    }

    public static void delete(String projectId, String planId) {
        Response response = CLIENT.delete(projectId, planId);
        int status = response.getStatusCode();
        if (status >= 400 && status != 404) {
            log.warn("[PlanFixture] Falha ao excluir plano {}. Status: {}", planId, status);
        } else {
            log.info("[PlanFixture] Excluído: id={}", planId);
        }
    }

    public static void deleteMany(String projectId, List<String> planIds) {
        planIds.forEach(id -> delete(projectId, id));
        log.info("[PlanFixture] {} plano(s) excluído(s).", planIds.size());
    }
}
