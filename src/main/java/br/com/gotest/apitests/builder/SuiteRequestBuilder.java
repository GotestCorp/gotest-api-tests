package br.com.gotest.apitests.builder;

import br.com.gotest.apitests.model.request.SuiteRequest;
import br.com.gotest.apitests.util.DataFaker;

/**
 * Builder customizado para {@link SuiteRequest}.
 *
 * <p>Por que existe se o Lombok já gera um? Dois motivos didáticos e práticos:
 * <ul>
 *     <li><b>Defaults inteligentes</b>: {@link #valid()} já devolve um payload
 *         válido com nome aleatório — útil quando o teste só quer "uma suite qualquer".</li>
 *     <li><b>Cenários nomeados</b>: {@link #invalidEmptyName()},
 *         {@link #invalidNullName()}, etc. encapsulam variações comuns para testes
 *         negativos sem poluir o teste com setup repetitivo.</li>
 * </ul>
 *
 * <p>Padrão Builder clássico, fluente, encadeável e imutável após {@link #build()}.
 *
 * <p>Uso:
 * <pre>
 *   SuiteRequest req = SuiteRequestBuilder.aSuite()
 *       .withName("Login Smoke")
 *       .withDescription("Casos críticos de login")
 *       .build();
 *
 *   // ou, pra testes negativos:
 *   SuiteRequest invalido = SuiteRequestBuilder.invalidEmptyName();
 * </pre>
 */
public final class SuiteRequestBuilder {

    private String name;
    private String description;
    private String parentFolderId;

    private SuiteRequestBuilder() {
        // Force uso dos factory methods
    }

    public static SuiteRequestBuilder aSuite() {
        return new SuiteRequestBuilder();
    }

    /**
     * Atalho: payload válido pronto pra uso, com nome único.
     */
    public static SuiteRequest valid() {
        return aSuite()
                .withName("Suite Auto " + DataFaker.shortUuid())
                .withDescription(DataFaker.sentence())
                .build();
    }

    public static SuiteRequest invalidEmptyName() {
        return aSuite().withName("").build();
    }

    public static SuiteRequest invalidNullName() {
        return aSuite().withName(null).build();
    }

    public static SuiteRequest invalidWhitespaceName() {
        return aSuite().withName("   ").build();
    }

    public static SuiteRequest invalidLongName() {
        return aSuite().withName(DataFaker.lorem(600)).build();
    }

    public SuiteRequestBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public SuiteRequestBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public SuiteRequestBuilder withParentFolderId(String parentFolderId) {
        this.parentFolderId = parentFolderId;
        return this;
    }

    public SuiteRequest build() {
        return SuiteRequest.builder()
                .name(name)
                .description(description)
                .parentFolderId(parentFolderId)
                .build();
    }
}
