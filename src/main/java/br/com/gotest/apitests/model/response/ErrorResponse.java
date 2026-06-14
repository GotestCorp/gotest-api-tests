package br.com.gotest.apitests.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Estrutura genérica de erro. Ajuste conforme o formato real que a API retornar
 * (RFC 7807 / Problem Details é o mais comum hoje).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorResponse {

    private String type;
    private String title;
    private Integer status;
    private String detail;
    private String instance;
    private List<String> errors;
}
