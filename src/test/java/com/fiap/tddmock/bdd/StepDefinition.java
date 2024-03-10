package com.fiap.tddmock.bdd;

import com.fiap.tddmock.model.Mensagem;
import com.fiap.tddmock.utils.MensagemHelper;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static com.fiap.tddmock.utils.MensagemHelper.*;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class StepDefinition {

    private Response response;

    private Mensagem mensagemResposta;

    private final String ENDPOINT_API_MENSAGENS = "http://localhost:8080/mensagens";

    @Quando("registrar uma nova mensagem")
    public void registrar_uma_nova_mensagem() {

        var mensagemRequest = gerarMensagem();

        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(mensagemRequest)
            .when()
                .post(ENDPOINT_API_MENSAGENS);
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
}
