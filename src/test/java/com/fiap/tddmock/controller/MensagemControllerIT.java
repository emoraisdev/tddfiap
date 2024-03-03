package com.fiap.tddmock.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.tddmock.AppTddMock;
import com.fiap.tddmock.exception.MensagemNotFoundException;
import com.fiap.tddmock.model.Mensagem;
import com.fiap.tddmock.service.MensagemService;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.UUID;

import static com.fiap.tddmock.utils.MensagemHelper.asJsonString;
import static com.fiap.tddmock.utils.MensagemHelper.gerarMensagem;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MensagemControllerIT {

    @Autowired
    MensagemService service;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @AfterEach
    void tearDown() throws Exception {

    }

    @Nested
    class RegistrarMensagem {

        @Test
        void devePermitirRegistrarMensagem() throws Exception {

            var mensagem = gerarMensagem();

            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(mensagem)
//                    .log().all()
                    .when()
                    .post("/mensagens")
                    .then()
//                    .log().all()
                    .statusCode(HttpStatus.CREATED.value());
        }

        @Test
        void deveGerarExcecao_QuandoRegistrarMensagem_PayloadXML() throws Exception {

            String payload = "<menssagem><usuario>Ana</usuario><conteudo>Conteudo 123</conteudo></menssagem>";

            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(payload)
                    .when()
                    .post("/mensagens")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("$", hasKey("timestamp"))
                    .body("$", hasKey("status"))
                    .body("$", hasKey("error"))
                    .body("$", hasKey("path"))
                    .body("error", equalTo("Bad Request"))
                    .body("path", equalTo("/mensagens"));
        }
    }

    @Nested
    class BuscarMensagem {

        @Test
        void devePermitirBuscarMensagem() throws Exception {

            var id = "89608d39-3416-45dd-8652-4719bb98352b";
            when()
                    .get("/mensagens/{id}", id)
                    .then()
                    .statusCode(HttpStatus.OK.value());
        }

        @Test
        void deveGerarExcecao_QuandoBuscarMensagem_IdNaoExiste() throws Exception {

            var id = "89608d39-3416-45dd-8652-4719bb98352";
            when()
                    .get("/mensagens/{id}", id)
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }
    }

    @Nested
    class AlterarMensagem {

        @Test
        void devePermitirAlterarMensagem() throws Exception {

            //Arrange
            var id = UUID.fromString("150f80ba-5f1c-4e93-9cac-080e0bbf8ef0");
            var mensagem = gerarMensagem();
            mensagem.setId(id);
        }

        @Test
        void deveGerarExcecao_QuandoAlterarMensagem_IdNaoExiste() throws Exception {

            //Arrange
            var id = UUID.fromString("150f80ba-5f1c-4e93-9cac-080e0bbf8ef0");
            var mensagem = gerarMensagem();
            mensagem.setId(id);
        }

        @Test
        void deveGerarExcecao_QuandoAlterarMensagem_AlterarPayloadComXML() throws Exception {
            //Act
        }
    }

    @Nested
    class RemoverMensagem {

        @Test
        void devePermitirRemoverMensagem() throws Exception {
            //Arrange
            var id = UUID.fromString("150f80ba-5f1c-4e93-9cac-080e0bbf8ef0");

        }

        @Test
        void deveGerarExcecao_QuandoRemoverMensagem_IdNaoExiste() throws Exception {
            //Arrange
            var id = UUID.fromString("150f80ba-5f1c-4e93-9cac-080e0bbf8ef0");
        }
    }

    @Nested
    class ListarMensagem {

        @Test
        void devePermitirListarMensagem() throws Exception {

            //Arrange
            var page = new PageImpl<>(Arrays.asList(gerarMensagem()), PageRequest.of(0, 10), 1);

        }

        @Test
        void devePermitirListarMensagem_QuandoNaoInformadoPaginacao() throws Exception {

            //Arrange
            var page = new PageImpl<>(Arrays.asList(gerarMensagem()), PageRequest.of(0, 10), 1);

        }

    }

}
