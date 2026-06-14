package br.com.gotest.apitests.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Suite {

    private String id;
    private String name;
    private String description;
    private String projectId;
    private String parentFolderId;
    private String createdAt;
    private String updatedAt;
}
