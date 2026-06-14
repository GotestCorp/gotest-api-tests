package br.com.gotest.apitests.model.bulk;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BulkTestCaseDefaults {
    private String testType     = "SCENARIO";
    private String status       = "IN_PROGRESS";
    private String priority     = "MEDIUM";
    private String executionType = "MANUAL";
}
