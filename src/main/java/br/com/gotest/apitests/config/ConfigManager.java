package br.com.gotest.apitests.config;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Singleton para acesso centralizado às configurações da suite.
 *
 * <p>Ordem de precedência (do mais forte pro mais fraco):
 * <ol>
 *     <li>System property (-Dchave=valor)</li>
 *     <li>Variável de ambiente</li>
 *     <li>config.properties</li>
 * </ol>
 *
 * <p>Útil quando você quer rodar a mesma suite contra ambientes diferentes
 * sem mudar código: {@code mvn test -DbaseUrl=https://staging.api.com}.
 */
@Slf4j
public final class ConfigManager {

    private static final String CONFIG_FILE = "config.properties";
    private static final ConfigManager INSTANCE = new ConfigManager();

    private final Properties properties = new Properties();

    private ConfigManager() {
        loadProperties();
    }

    public static ConfigManager getInstance() {
        return INSTANCE;
    }

    private void loadProperties() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                log.warn("Arquivo {} não encontrado no classpath. Confiando em system properties / env vars.", CONFIG_FILE);
                return;
            }
            properties.load(input);
            log.info("Configurações carregadas de {}", CONFIG_FILE);
        } catch (IOException e) {
            throw new IllegalStateException("Falha ao carregar " + CONFIG_FILE, e);
        }
    }

    /**
     * Busca propriedade respeitando precedência: system property → env var → arquivo.
     */
    public String get(String key) {
        String fromSys = System.getProperty(key);
        if (fromSys != null && !fromSys.isBlank()) return fromSys;

        String envKey = key.toUpperCase().replace('.', '_');
        String fromEnv = System.getenv(envKey);
        if (fromEnv != null && !fromEnv.isBlank()) return fromEnv;

        return properties.getProperty(key);
    }

    public String get(String key, String defaultValue) {
        String value = get(key);
        return (value == null || value.isBlank()) ? defaultValue : value;
    }

    public int getInt(String key, int defaultValue) {
        String value = get(key);
        try {
            return value == null ? defaultValue : Integer.parseInt(value);
        } catch (NumberFormatException e) {
            log.warn("Valor inválido para {}: '{}'. Usando default {}", key, value, defaultValue);
            return defaultValue;
        }
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        String value = get(key);
        return value == null ? defaultValue : Boolean.parseBoolean(value);
    }

    // Atalhos para configs mais usadas
    public String baseUrl()        { return get("baseUrl"); }
    public String authToken()      { return get("auth.token"); }
    public String defaultProjectId() { return get("default.project.id"); }
    public String defaultSuiteId()   { return get("default.suite.id"); }
    public int connectionTimeout() { return getInt("timeout.connection", 10_000); }
    public int responseTimeout()   { return getInt("timeout.response", 30_000); }
    public boolean logEnabled()    { return getBoolean("log.enabled", true); }
}
