package br.com.gotest.apitests.functional;

import br.com.gotest.apitests.base.BaseTest;
import br.com.gotest.apitests.builder.FolderRequestBuilder;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@Epic("Functional Tests")
@Feature("Nodes")
@Story("Criar pasta")
public class CreateFolderTest extends BaseTest {

    private String projectId;
    private String createdFolderId;

    @BeforeClass
    public void resolveProjectId() {
        projectId = config.defaultProjectId();
        if (projectId == null || projectId.isBlank()) {
            throw new SkipException("default.project.id não configurado — pulando classe");
        }
    }

    @AfterMethod(alwaysRun = true)
    public void cleanup() {
        if (createdFolderId != null) {
            log.info("[Cleanup] Excluindo pasta criada no teste: {}", createdFolderId);
            nodeClient.deleteNode(projectId, createdFolderId);
            createdFolderId = null;
        }
    }

    @Test(description = "Criar pasta com nome válido retorna 200/201")
    @Description("Cenário feliz com nome aleatório único")
    public void validName_shouldCreateFolder() {
        Response response = nodeClient.createFolder(projectId, FolderRequestBuilder.valid());
        assertThat(response.getStatusCode()).isIn(200, 201);
        createdFolderId = response.jsonPath().getString("id");
        assertThat(createdFolderId).isNotBlank();
    }

    @Test(description = "Criar pasta com nome vazio retorna 400/422")
    @Description("Cenário negativo: nome obrigatório")
    public void emptyName_shouldReturn400() {
        Response response = nodeClient.createFolder(projectId, FolderRequestBuilder.invalidEmptyName());
        assertThat(response.getStatusCode()).isIn(400, 422);
    }

    @Test(description = "Criar pasta em projeto inexistente retorna 403/404")
    @Description("Cenário negativo: projectId inválido")
    public void nonExistingProject_shouldReturn404() {
        Response response = nodeClient.createFolder(
                "00000000-0000-0000-0000-000000000000",
                FolderRequestBuilder.valid());
        assertThat(response.getStatusCode()).isIn(403, 404);
    }
}
