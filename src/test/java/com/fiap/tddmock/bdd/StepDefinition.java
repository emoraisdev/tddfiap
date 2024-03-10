package com.fiap.tddmock.bdd;

import com.fiap.tddmock.model.Mensagem;
import com.fiap.tddmock.utils.MensagemHelper;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static com.fiap.tddmock.utils.MensagemHelper.*;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class StepDefinition {

    private Response response;

    private Mensagem mensagemResposta;

    private final String ENDPOINT_API_MENSAGENS = "http://localhost:8080/mensagens";

    @Quando("registrar uma nova mensagem")
    public Mensagem registrar_uma_nova_mensagem() {

        var mensagemRequest = gerarMensagem();

        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(mensagemRequest)
            .when()
                .post(ENDPOINT_API_MENSAGENS);

        return response.then().extract().as(Mensagem.class);
    }

    @Entao("a mensagem é registrada com sucesso")
    public void a_mensagem_é_registrada_com_sucesso() {

        response.then()
                .statusCode(HttpStatus.CREATED.value())
                .body(matchesJsonSchemaInClasspath("schema/mensagem.schema.json"));
    }

    @Entao("deve ser apresentada")
    public void deve_ser_apresentada() {
        response.then()
                .body(matchesJsonSchemaInClasspath("schema/mensagem.schema.json"));
    }

    @Dado("que uma mensagem já foi publicada")
    public void que_uma_mensagem_já_foi_publicada() {
        mensagemResposta = registrar_uma_nova_mensagem();
    }
    @Quando("efetuar a busca da mensagemr")
    public void efetuar_a_busca_da_mensagemr() {

        response = when()
                .get(ENDPOINT_API_MENSAGENS + "/{id}", mensagemResposta.getId());
    }
    @Entao("a mensagem é exibida com sucesso")
    public void a_mensagem_é_exibida_com_sucesso() {
        response.then()
                .body(matchesJsonSchemaInClasspath("schema/mensagem.schema.json"));
    }

    @Quando("efetuar a requisição para alterar a mensagemr")
    public void efetuar_a_requisição_para_alterar_a_mensagemr() {

        mensagemResposta.setConteudo("teste 987654");

        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(mensagemResposta)
            .when()
                .put(ENDPOINT_API_MENSAGENS);
    }

    @Entao("a mensagem é alterada com sucesso")
    public void a_mensagem_é_alterada_com_sucesso() {
        response.then()
                .statusCode(HttpStatus.ACCEPTED.value())
                .body(matchesJsonSchemaInClasspath("schema/mensagem.schema.json"));
    }

    @Quando("requisitar a remoção da mensagem")
    public void requisitar_a_remoção_da_mensagem() {

        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete(ENDPOINT_API_MENSAGENS + "/{id}", mensagemResposta.getId());

    }
    @Entao("a mensagem é removida com sucesso")
    public void a_mensagem_é_removida_com_sucesso() {

        response.then()
                .statusCode(HttpStatus.OK.value());
    }
}
