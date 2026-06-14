package br.com.gotest.apitests.listener;

import lombok.extern.slf4j.Slf4j;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * Listener simples que loga ciclo de vida de cada teste.
 * Plugado via testng.xml.
 */
@Slf4j
public class TestListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        log.info("▶  INICIANDO: {}.{}", result.getTestClass().getRealClass().getSimpleName(),
                result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        log.info("✓  PASSOU: {} ({}ms)", result.getMethod().getMethodName(),
                result.getEndMillis() - result.getStartMillis());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        log.error("✗  FALHOU: {} — {}", result.getMethod().getMethodName(),
                result.getThrowable() != null ? result.getThrowable().getMessage() : "sem mensagem");
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        log.warn("∼  PULADO: {}", result.getMethod().getMethodName());
    }
}
