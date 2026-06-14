package br.com.gotest.apitests.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TestExecution {

    private String id;
    private String status;
    private String executionTime;
    private String testId;
    private String executionId;
    private String createdAt;
    private String updatedAt;
}
