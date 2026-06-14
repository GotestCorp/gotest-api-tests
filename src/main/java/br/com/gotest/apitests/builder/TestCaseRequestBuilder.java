package br.com.gotest.apitests.builder;

import br.com.gotest.apitests.model.request.TestCaseRequest;
import br.com.gotest.apitests.util.DataFaker;

public final class TestCaseRequestBuilder {

    private String testType;
    private String title;
    private String status;
    private String priority;
    private String executionType;

    private TestCaseRequestBuilder() {}

    public static TestCaseRequestBuilder aTestCase() {
        return new TestCaseRequestBuilder();
    }

    public static TestCaseRequest valid() {
        return aTestCase()
                .withTestType("SCENARIO")
                .withTitle("Cenário Auto " + DataFaker.shortUuid())
                .withStatus("IN_PROGRESS")
                .withPriority("MEDIUM")
                .withExecutionType("MANUAL")
                .build();
    }

    public static TestCaseRequest invalidEmptyTitle() {
        return aTestCase()
                .withTestType("SCENARIO")
                .withTitle("")
                .withStatus("IN_PROGRESS")
                .withPriority("MEDIUM")
                .withExecutionType("MANUAL")
                .build();
    }

    public static TestCaseRequest invalidNullTitle() {
        return aTestCase()
                .withTestType("SCENARIO")
                .withTitle(null)
                .withStatus("IN_PROGRESS")
                .withPriority("MEDIUM")
                .withExecutionType("MANUAL")
                .build();
    }

    public TestCaseRequestBuilder withTestType(String testType) {
        this.testType = testType;
        return this;
    }

    public TestCaseRequestBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public TestCaseRequestBuilder withStatus(String status) {
        this.status = status;
        return this;
    }

    public TestCaseRequestBuilder withPriority(String priority) {
        this.priority = priority;
        return this;
    }

    public TestCaseRequestBuilder withExecutionType(String executionType) {
        this.executionType = executionType;
        return this;
    }

    public TestCaseRequest build() {
        return TestCaseRequest.builder()
                .testType(testType)
                .title(title)
                .status(status)
                .priority(priority)
                .executionType(executionType)
                .build();
    }
}
