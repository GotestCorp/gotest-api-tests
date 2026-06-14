package br.com.gotest.apitests.builder;

import br.com.gotest.apitests.model.request.ProjectRequest;
import br.com.gotest.apitests.util.DataFaker;

public final class ProjectRequestBuilder {

    private String name;
    private String identifier;
    private String description;

    private ProjectRequestBuilder() {}

    public static ProjectRequestBuilder aProject() {
        return new ProjectRequestBuilder();
    }

    public static ProjectRequest valid() {
        String uid = DataFaker.shortUuid().toUpperCase();
        return aProject()
                .withName("Projeto Auto " + uid)
                .withIdentifier("PA" + uid.substring(0, Math.min(3, uid.length())))
                .withDescription(DataFaker.sentence())
                .build();
    }

    public static ProjectRequest invalidEmptyName() {
        return aProject().withName("").withIdentifier("TST").build();
    }

    public static ProjectRequest invalidNullName() {
        return aProject().withName(null).withIdentifier("TST").build();
    }

    public ProjectRequestBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ProjectRequestBuilder withIdentifier(String identifier) {
        this.identifier = identifier;
        return this;
    }

    public ProjectRequestBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public ProjectRequest build() {
        return ProjectRequest.builder()
                .name(name)
                .identifier(identifier)
                .description(description)
                .build();
    }
}
