package br.com.fiap.fastfood.bdd.steps;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * Context holder for sharing state between Cucumber steps.
 * This component stores the HTTP response to be shared across different step definitions.
 */
@Component
public final class TestContext {
    private ResponseEntity<String> response;

    /**
     * Gets the stored HTTP response.
     *
     * @return the response entity
     */
    public ResponseEntity<String> getResponse() {
        return response;
    }

    /**
     * Stores the HTTP response for later verification.
     *
     * @param response the response entity to store
     */
    public void setResponse(final ResponseEntity<String> response) {
        this.response = response;
    }
}
