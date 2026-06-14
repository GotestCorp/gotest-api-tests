package br.com.gotest.apitests.model.bulk;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Representa um grupo de casos de teste a serem criados em bulk dentro de uma
 * mesma suite. Exemplo de uso:
 *
 * <pre>
 * BulkTestCaseGroup group = new BulkTestCaseGroup();
 * group.setProjectId("uuid-projeto");
 * group.setSuiteId("uuid-suite");
 * group.setCases(List.of(
 *     new BulkTestCaseDefinition("Login com sucesso", null, null, "HIGH", null),
 *     new BulkTestCaseDefinition("Login com senha inválida", null, null, null, null)
 * ));
 * TestCaseFixture.createBulk(List.of(group));
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BulkTestCaseGroup {
    private String projectId;
    private String suiteId;
    /** Valores padrão aplicados a todos os casos do grupo quando o campo individual for nulo. */
    private BulkTestCaseDefaults defaults;
    private List<BulkTestCaseDefinition> cases;
}
