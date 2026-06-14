package br.com.gotest.apitests.builder;

import br.com.gotest.apitests.model.request.PlanRequest;
import br.com.gotest.apitests.util.DataFaker;

public final class PlanRequestBuilder {

    private String name;
    private String description;

    private PlanRequestBuilder() {}

    public static PlanRequestBuilder aPlan() {
        return new PlanRequestBuilder();
    }

    public static PlanRequest valid() {
        return aPlan()
                .withName("Plano Auto " + DataFaker.shortUuid())
                .withDescription(DataFaker.sentence())
                .build();
    }

    public static PlanRequest invalidEmptyName() {
        return aPlan().withName("").build();
    }

    public static PlanRequest invalidNullName() {
        return aPlan().withName(null).build();
    }

    public PlanRequestBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public PlanRequestBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public PlanRequest build() {
        return PlanRequest.builder()
                .name(name)
                .description(description)
                .build();
    }
}
