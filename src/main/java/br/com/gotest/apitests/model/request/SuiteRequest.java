package br.com.gotest.apitests.model.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Payload de criação de Suite.
 *
 * <p>Baseado no body observado no Postman:
 * <pre>{ "name": "teste2" }</pre>
 *
 * <p>Campos extras (description, parentId, etc.) são placeholders prováveis —
 * habilite/ajuste quando confirmar via console do front.
 *
 * <p>Uso do Builder (gerado pelo Lombok):
 * <pre>
 *   SuiteRequest req = SuiteRequest.builder()
 *       .name("Smoke Tests Login")
 *       .description("Suite gerada automaticamente")
 *       .build();
 * </pre>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SuiteRequest {

    private String name;
    private String description;
    private String parentFolderId;
}
