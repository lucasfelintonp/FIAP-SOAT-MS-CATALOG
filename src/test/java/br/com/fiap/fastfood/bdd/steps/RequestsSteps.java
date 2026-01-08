package br.com.fiap.fastfood.bdd.steps;

import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

public class RequestsSteps {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TestContext context;

    @Quando("eu realizar um requisicao GET para {string}")
    public void euRealizarUmRequisicaoGETPara(String endpoint) {
        ResponseEntity<String> response = restTemplate.getForEntity(endpoint, String.class);
        context.setResponse(response);
    }

    @Entao("deve retornar status {int}")
    public void deveRetornarStatus(int statusEsperado) {
        assertThat(context.getResponse().getStatusCode().value()).isEqualTo(statusEsperado);
    }

    @Entao("deve retornar um JSON com o schema {string}")
    public void deveRetornarUmJSONComOSchema(String nomeSchema) {
        assertThat(context.getResponse().getBody()).isNotNull();
    }
}
