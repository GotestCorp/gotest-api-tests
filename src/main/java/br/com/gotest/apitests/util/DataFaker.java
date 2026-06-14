package br.com.gotest.apitests.util;

import com.github.javafaker.Faker;
import lombok.experimental.UtilityClass;

import java.util.Locale;
import java.util.UUID;

/**
 * Wrapper sobre JavaFaker pra gerar dados de teste de forma consistente.
 * Locale fixo em pt-BR pra nomes/textos parecerem nativos quando aparecerem em logs.
 */
@UtilityClass
public class DataFaker {

    private static final Faker FAKER = new Faker(new Locale("pt", "BR"));

    public static String shortUuid() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    public static String sentence() {
        return FAKER.lorem().sentence();
    }

    public static String lorem(int chars) {
        return FAKER.lorem().characters(chars);
    }

    public static String fullName() {
        return FAKER.name().fullName();
    }

    public static String email() {
        return FAKER.internet().emailAddress();
    }

    public static String randomUuid() {
        return UUID.randomUUID().toString();
    }
}
