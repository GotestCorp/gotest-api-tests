package br.com.gotest.apitests.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representação de um Projeto no response da API.
 *
 * <p>Como não há Swagger, esses campos são uma suposição razoável para uma
 * plataforma de gestão de testes. {@code @JsonIgnoreProperties(ignoreUnknown = true)}
 * garante que campos extras retornados não quebram a desserialização.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Project {

    private String id;
    private String name;
    private String identifier;
    private String description;
    private String createdAt;
    private String updatedAt;
    private String ownerId;
}
