package br.com.gotest.apitests.model.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Body de paginação/filtro compartilhado pelos endpoints {@code POST .../filter}.
 *
 * <p>A partir da refatoração de 2026-06, a API substituiu os antigos
 * {@code GET ...?page=&size=&query=&orderBy=} por chamadas {@code POST .../filter}
 * com este corpo. O mesmo shape atende projects, plans, nodes e tests:
 * <pre>{@code
 * {"page":0,"size":10,"query":"","createdByName":[],"updatedByName":[],
 *  "orderBy":"CREATED_AT","direction":"DESC"}
 * }</pre>
 *
 * <p>{@code @JsonInclude(NON_NULL)} garante que campos não preenchidos (ex.: as
 * listas de nome em {@code nodes/filter}) simplesmente não vão no JSON.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FilterRequest {

    private Integer page;
    private Integer size;
    private String query;
    private List<String> createdByName;
    private List<String> updatedByName;
    private String orderBy;
    private String direction;

    /**
     * Filtro padrão "primeira página" usado por projects e plans
     * (inclui as listas vazias de createdByName/updatedByName, como o front faz).
     */
    public static FilterRequest defaultFilter() {
        return FilterRequest.builder()
                .page(0)
                .size(10)
                .query("")
                .createdByName(List.of())
                .updatedByName(List.of())
                .orderBy("CREATED_AT")
                .direction("DESC")
                .build();
    }

    /**
     * Filtro enxuto usado por nodes/filter (sem as listas de nome).
     */
    public static FilterRequest simpleFilter() {
        return FilterRequest.builder()
                .page(0)
                .size(10)
                .query("")
                .orderBy("CREATED_AT")
                .direction("DESC")
                .build();
    }
}
