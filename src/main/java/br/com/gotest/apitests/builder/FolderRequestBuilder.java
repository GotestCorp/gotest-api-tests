package br.com.gotest.apitests.builder;

import br.com.gotest.apitests.model.request.FolderRequest;
import br.com.gotest.apitests.util.DataFaker;

public final class FolderRequestBuilder {

    private String name;

    private FolderRequestBuilder() {}

    public static FolderRequestBuilder aFolder() {
        return new FolderRequestBuilder();
    }

    public static FolderRequest valid() {
        return aFolder()
                .withName("Pasta Auto " + DataFaker.shortUuid())
                .build();
    }

    public static FolderRequest invalidEmptyName() {
        return aFolder().withName("").build();
    }

    public static FolderRequest invalidNullName() {
        return aFolder().withName(null).build();
    }

    public FolderRequestBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public FolderRequest build() {
        return FolderRequest.builder()
                .name(name)
                .build();
    }
}
