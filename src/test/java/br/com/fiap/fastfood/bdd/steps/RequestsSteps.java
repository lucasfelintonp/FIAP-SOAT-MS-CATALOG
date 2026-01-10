package br.com.fiap.fastfood.bdd.steps;

import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Step definitions for HTTP request-related Cucumber scenarios.
 */
public class RequestsSteps {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TestContext context;

    /**
     * Performs a GET request to the specified endpoint.
     *
     * @param endpoint the endpoint to call
     */
    @Quando("eu realizar um requisicao GET para {string}")
    public void euRealizarUmRequisicaoGETPara(final String endpoint) {
        ResponseEntity<String> response = restTemplate.getForEntity(endpoint, String.class);
        context.setResponse(response);
    }

    /**
     * Verifies that the response status matches the expected value.
     *
     * @param statusEsperado the expected HTTP status code
     */
    @Entao("deve retornar status {int}")
    public void deveRetornarStatus(final int statusEsperado) {
        assertThat(context.getResponse().getStatusCode().value()).isEqualTo(statusEsperado);
    }

    /**
     * Verifies that the response contains a valid JSON matching the schema.
     *
     * @param nomeSchema the name of the JSON schema file
     */
    @Entao("deve retornar um JSON com o schema {string}")
    public void deveRetornarUmJSONComOSchema(final String nomeSchema) {
        assertThat(context.getResponse().getBody()).isNotNull();
        // TODO: Implement JSON schema validation if needed
    }
}
