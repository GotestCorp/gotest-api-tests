package br.com.gotest.apitests.fixture;

import br.com.gotest.apitests.config.ConfigManager;
import br.com.gotest.apitests.model.bulk.BulkTestCaseDefaults;
import br.com.gotest.apitests.model.bulk.BulkTestCaseDefinition;
import br.com.gotest.apitests.model.bulk.BulkTestCaseGroup;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Ponto de entrada CLI para operações de setup/teardown de dados de teste.
 *
 * <p>Execute via Maven sem rodar a suite de testes:
 * <pre>
 *   mvn compile exec:java -Dexec.args="create-projects 5"
 *   mvn compile exec:java -Dexec.args="create-full-project"
 *   mvn compile exec:java -Dexec.args="delete-projects id1,id2,id3"
 *   mvn compile exec:java -Dexec.args="create-test-cases 10"
 *   mvn compile exec:java -Dexec.args="create-test-cases-json src/test/resources/bulk-test-cases-sample.json"
 *   mvn compile exec:java -Dexec.args="create-test-cases-bulk proj-id suite-id Caso1|Caso2|Caso3"
 * </pre>
 */
@Slf4j
public class DataSetup {

    private static final ConfigManager CONFIG = ConfigManager.getInstance();

    public static void main(String[] args) {
        if (args.length == 0) {
            printHelp();
            return;
        }

        String command = args[0];
        switch (command) {
            case "create-projects":
                runCreateProjects(parseCount(args, 1, 1));
                break;
            case "delete-projects":
                runDeleteProjects(parseIds(args, 1));
                break;
            case "create-suites":
                runCreateSuites(parseCount(args, 1, 1));
                break;
            case "delete-suites":
                runDeleteSuites(parseIds(args, 1));
                break;
            case "create-folders":
                runCreateFolders(parseCount(args, 1, 1));
                break;
            case "delete-folders":
                runDeleteFolders(parseIds(args, 1));
                break;
            case "create-plans":
                runCreatePlans(parseCount(args, 1, 1));
                break;
            case "delete-plans":
                runDeletePlans(parseIds(args, 1));
                break;
            case "create-test-cases":
                runCreateTestCases(parseCount(args, 1, 1));
                break;
            case "create-test-cases-json":
                runCreateTestCasesFromJson(parseRequired(args, 1, "Informe o caminho do arquivo JSON."));
                break;
            case "create-test-cases-bulk":
                runCreateTestCasesBulkInline(args);
                break;
            case "create-full-project":
                runCreateFullProject();
                break;
            default:
                log.error("Comando desconhecido: '{}'. Use sem argumentos para ver a ajuda.", command);
                printHelp();
        }
    }

    // ---- Comandos ----

    private static void runCreateProjects(int count) {
        log.info("Criando {} projeto(s)...", count);
        List<String> ids = ProjectFixture.createMany(count);
        log.info("IDs criados: {}", ids);
    }

    private static void runDeleteProjects(List<String> ids) {
        log.info("Excluindo {} projeto(s): {}", ids.size(), ids);
        ProjectFixture.deleteMany(ids);
    }

    private static void runCreateSuites(int count) {
        String projectId = requireProjectId();
        log.info("Criando {} suite(s) no projeto {}...", count, projectId);
        List<String> ids = SuiteFixture.createMany(projectId, count);
        log.info("IDs criados: {}", ids);
    }

    private static void runDeleteSuites(List<String> ids) {
        String projectId = requireProjectId();
        log.info("Excluindo {} suite(s): {}", ids.size(), ids);
        SuiteFixture.deleteMany(projectId, ids);
    }

    private static void runCreateFolders(int count) {
        String projectId = requireProjectId();
        log.info("Criando {} pasta(s) no projeto {}...", count, projectId);
        List<String> ids = FolderFixture.createMany(projectId, count);
        log.info("IDs criados: {}", ids);
    }

    private static void runDeleteFolders(List<String> ids) {
        String projectId = requireProjectId();
        log.info("Excluindo {} pasta(s): {}", ids.size(), ids);
        FolderFixture.deleteMany(projectId, ids);
    }

    private static void runCreatePlans(int count) {
        String projectId = requireProjectId();
        log.info("Criando {} plano(s) no projeto {}...", count, projectId);
        List<String> ids = PlanFixture.createMany(projectId, count);
        log.info("IDs criados: {}", ids);
    }

    private static void runDeletePlans(List<String> ids) {
        String projectId = requireProjectId();
        log.info("Excluindo {} plano(s): {}", ids.size(), ids);
        PlanFixture.deleteMany(projectId, ids);
    }

    private static void runCreateTestCases(int count) {
        String projectId = requireProjectId();
        String suiteId = requireSuiteId();
        log.info("Criando {} caso(s) de teste na suite {}...", count, suiteId);
        List<String> ids = TestCaseFixture.createMany(projectId, suiteId, count);
        log.info("IDs criados: {}", ids);
    }

    /**
     * Lê um arquivo JSON com grupos de casos de teste e os cria via API.
     *
     * <p>Exemplo: {@code create-test-cases-json src/test/resources/bulk-test-cases-sample.json}
     */
    private static void runCreateTestCasesFromJson(String filePath) {
        log.info("Criando casos de teste a partir do arquivo: {}", filePath);
        try {
            List<String> ids = TestCaseFixture.createFromJson(filePath);
            log.info("Total criado: {} caso(s). IDs: {}", ids.size(), ids);
        } catch (IOException e) {
            throw new FixtureException("Falha ao ler arquivo JSON: " + e.getMessage(), e);
        }
    }

    /**
     * Cria casos de teste informando projectId, suiteId e títulos diretamente na linha de comando.
     *
     * <p>Formato: {@code create-test-cases-bulk <projectId> <suiteId> Título 1|Título 2|Título 3}
     * <p>Prioridade/status/type opcionais podem ser acrescentados como pares chave=valor separados
     * por vírgula logo após o título:
     * {@code "Login com sucesso,priority=HIGH|Login inválido,status=DRAFT"}
     */
    private static void runCreateTestCasesBulkInline(String[] args) {
        if (args.length < 4) {
            throw new IllegalArgumentException(
                    "Uso: create-test-cases-bulk <projectId> <suiteId> \"Caso1|Caso2|...\"\n" +
                    "  Atributos opcionais por caso (separados por vírgula após o título):\n" +
                    "    priority=HIGH|MEDIUM|LOW\n" +
                    "    status=IN_PROGRESS|DRAFT|DEPRECATED\n" +
                    "    testType=SCENARIO|STEP_BY_STEP\n" +
                    "    executionType=MANUAL|AUTOMATED"
            );
        }

        String projectId = args[1];
        String suiteId   = args[2];
        String titlesRaw = args[3];

        String[] parts = titlesRaw.split("\\|");
        List<BulkTestCaseDefinition> definitions = new ArrayList<>();

        for (String part : parts) {
            String[] tokens = part.split(",");
            String title = tokens[0].trim();
            BulkTestCaseDefinition def = new BulkTestCaseDefinition();
            def.setTitle(title);
            for (int i = 1; i < tokens.length; i++) {
                String[] kv = tokens[i].trim().split("=", 2);
                if (kv.length == 2) {
                    switch (kv[0].trim().toLowerCase()) {
                        case "priority":      def.setPriority(kv[1].trim());      break;
                        case "status":        def.setStatus(kv[1].trim());        break;
                        case "testtype":      def.setTestType(kv[1].trim());      break;
                        case "executiontype": def.setExecutionType(kv[1].trim()); break;
                    }
                }
            }
            definitions.add(def);
        }

        BulkTestCaseGroup group = new BulkTestCaseGroup(projectId, suiteId, new BulkTestCaseDefaults(), definitions);
        log.info("Criando {} caso(s) inline na suite {}...", definitions.size(), suiteId);
        List<String> ids = TestCaseFixture.createBulk(List.of(group));
        log.info("IDs criados: {}", ids);
    }

    private static void runCreateFullProject() {
        log.info("Criando estrutura completa de projeto...");

        String projectId = ProjectFixture.create(
                br.com.gotest.apitests.builder.ProjectRequestBuilder.valid());
        log.info("Projeto: {}", projectId);

        for (int f = 1; f <= 3; f++) {
            String folderId = FolderFixture.create(projectId, "Pasta " + f);
            log.info("  Pasta {}: {}", f, folderId);

            for (int s = 1; s <= 2; s++) {
                String suiteId = SuiteFixture.create(projectId, "Suite " + f + "." + s, folderId);
                log.info("    Suite {}.{}: {}", f, s, suiteId);

                List<String> caseIds = TestCaseFixture.createMany(projectId, suiteId, 5);
                log.info("      Casos: {}", caseIds);
            }
        }

        List<String> planIds = PlanFixture.createMany(projectId, 2);
        log.info("Planos: {}", planIds);
        log.info("Estrutura criada com sucesso. projectId={}", projectId);
    }

    // ---- Helpers ----

    private static String requireProjectId() {
        String id = CONFIG.defaultProjectId();
        if (id == null || id.isBlank()) {
            throw new IllegalStateException(
                    "default.project.id não configurado. Defina em config.properties ou via -Ddefault.project.id=<uuid>");
        }
        return id;
    }

    private static String requireSuiteId() {
        String id = CONFIG.defaultSuiteId();
        if (id == null || id.isBlank()) {
            throw new IllegalStateException(
                    "default.suite.id não configurado. Defina em config.properties ou via -Ddefault.suite.id=<uuid>");
        }
        return id;
    }

    private static int parseCount(String[] args, int index, int fallback) {
        if (args.length > index) {
            try {
                return Integer.parseInt(args[index]);
            } catch (NumberFormatException e) {
                log.warn("Quantidade inválida '{}', usando {}.", args[index], fallback);
            }
        }
        return fallback;
    }

    private static List<String> parseIds(String[] args, int index) {
        if (args.length <= index) {
            throw new IllegalArgumentException("Informe pelo menos um ID. Ex: delete-projects id1,id2");
        }
        return Arrays.asList(args[index].split(","));
    }

    private static String parseRequired(String[] args, int index, String errorMsg) {
        if (args.length <= index || args[index].isBlank()) {
            throw new IllegalArgumentException(errorMsg);
        }
        return args[index];
    }

    private static void printHelp() {
        System.out.println(
            "goTest API Tests — DataSetup\n" +
            "================================\n" +
            "Uso: mvn compile exec:java -Dexec.args=\"<comando> [args]\"\n" +
            "\n" +
            "Projetos:\n" +
            "  create-projects   <n>          Cria N projetos com dados aleatórios\n" +
            "  delete-projects   <id1,id2...> Exclui projetos pelos IDs\n" +
            "\n" +
            "Suites / Pastas:\n" +
            "  create-suites     <n>          Cria N suites no projeto padrão\n" +
            "  delete-suites     <id1,id2...> Exclui suites (usa projeto padrão)\n" +
            "  create-folders    <n>          Cria N pastas no projeto padrão\n" +
            "  delete-folders    <id1,id2...> Exclui pastas\n" +
            "\n" +
            "Planos:\n" +
            "  create-plans      <n>          Cria N planos no projeto padrão\n" +
            "  delete-plans      <id1,id2...> Exclui planos\n" +
            "\n" +
            "Casos de Teste:\n" +
            "  create-test-cases <n>          Cria N casos na suite padrão (nomes aleatórios)\n" +
            "  create-test-cases-json <path>  Cria casos a partir de um arquivo JSON\n" +
            "                                 Ex: create-test-cases-json src/test/resources/bulk-test-cases-sample.json\n" +
            "  create-test-cases-bulk <projectId> <suiteId> \"Caso1|Caso2,priority=HIGH|Caso3\"\n" +
            "                                 Cria casos inline com títulos separados por |\n" +
            "                                 Atributos opcionais por caso: priority, status, testType, executionType\n" +
            "\n" +
            "Estrutura Completa:\n" +
            "  create-full-project            Cria projeto + 3 pastas + 2 suites/pasta + 5 casos/suite + 2 planos\n" +
            "\n" +
            "Configuração (config.properties ou parâmetros Maven):\n" +
            "  -Ddefault.project.id=<uuid>    Projeto padrão para comandos que precisam\n" +
            "  -Ddefault.suite.id=<uuid>      Suite padrão para create-test-cases\n" +
            "  -DbaseUrl=<url>                Sobrescreve a URL base da API\n"
        );
    }
}
