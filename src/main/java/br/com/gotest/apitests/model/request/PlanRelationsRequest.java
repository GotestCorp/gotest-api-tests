package br.com.gotest.apitests.model.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlanRelationsRequest {

    private List<String> tests;
    private List<String> testIds;
    private List<String> suiteIds;
    private List<String> folderIds;
    private List<String> planIds;
}
