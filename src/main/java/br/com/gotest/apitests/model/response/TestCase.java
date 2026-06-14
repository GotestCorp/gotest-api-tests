package br.com.gotest.apitests.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TestCase {

    private String id;
    private String testType;
    private String title;
    private String status;
    private String priority;
    private String executionType;
    private String suiteId;
    private String projectId;
    private String createdAt;
    private String updatedAt;
}
