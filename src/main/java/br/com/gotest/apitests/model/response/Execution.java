package br.com.gotest.apitests.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Execution {

    private String id;
    private String name;
    private String status;
    private String planId;
    private String projectId;
    private String createdAt;
    private String updatedAt;
}
