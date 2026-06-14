package br.com.gotest.apitests.model.bulk;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BulkTestCaseDefinition {
    private String title;
    private String testType;     // se nulo, usa o default do grupo
    private String status;       // se nulo, usa o default do grupo
    private String priority;     // se nulo, usa o default do grupo
    private String executionType; // se nulo, usa o default do grupo
}
