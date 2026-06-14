package br.com.gotest.apitests.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Plan {

    private String id;
    private String name;
    private String description;
    private String projectId;
    private String createdAt;
    private String updatedAt;
}
