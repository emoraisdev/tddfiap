package com.fiap.tddmock.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fiap.tddmock.AppTddMock;
import com.fiap.tddmock.config.ApplicationConfig;
import com.fiap.tddmock.exception.MensagemNotFoundException;
import com.fiap.tddmock.model.Mensagem;
import com.fiap.tddmock.service.MensagemService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

import static com.fiap.tddmock.utils.MensagemHelper.asJsonString;
import static com.fiap.tddmock.utils.MensagemHelper.gerarMensagem;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class MensagemControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MensagemService service;

    AutoCloseable mock;

    @BeforeEach
    void setup() {
        mock = MockitoAnnotations.openMocks(this);
        MensagemController controller = new MensagemController(service);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .addFilter((request, response, chain) -> {
                    response.setCharacterEncoding("UTF-8");
                    chain.doFilter(request, response);
                })
                .build();
    }

    @AfterEach
    void tearDown() throws Exception {
        mock.close();
    }

    @Nested
    class RegistrarMensagem {

        @Test
        void devePermitirRegistrarMensagem() throws Exception {
            //Arrange
            var mensagem = gerarMensagem();
            when(service.registrarMensagem(any(Mensagem.class))).thenAnswer(i -> i.getArgument(0));

            //Act

            mockMvc.perform(post("/mensagens")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(mensagem)))
                    .andExpect(status().isCreated());

            verify(service, times(1)).registrarMensagem(any(Mensagem.class));
        }

        @Test
        void deveGerarExcecao_QuandoRegistrarMensagem_PayloadXML() throws Exception {

            //Act
            mockMvc.perform(post("/mensagens")
                            .contentType(MediaType.APPLICATION_XML)
                            .content("<menssagem><usuario>Ana</usuario>" +
                                    "<conteudo>Conteudo 123</conteudo></menssagem>"))
                    .andExpect(status().isUnsupportedMediaType());

            verify(service, never()).registrarMensagem(any(Mensagem.class));
        }
    }

    @Nested
    class BuscarMensagem {

        @Test
        void devePermitirBuscarMensagem() throws Exception {

            //Arrange
            var id = "fda8113f-0b2c-4361-b6bc-24f81d038f45";
            var mensagem = gerarMensagem();
            when(service.buscarMensagem(any(UUID.class))).thenReturn(mensagem);

            //Act
            mockMvc.perform(get("/mensagens/{id}", id))
                    .andExpect(status().isOk());

            verify(service, times(1)).buscarMensagem(any(UUID.class));
        }

        @Test
        void deveGerarExcecao_QuandoBuscarMensagem_IdNaoExiste() throws Exception {

            //Arrange
            var id = UUID.fromString("fda8113f-0b2c-4361-b6bc-24f81d038f45");
            when(service.buscarMensagem(id)).thenThrow(new MensagemNotFoundException("Registro não encontrado"));

            //Act
            mockMvc.perform(get("/mensagens/{id}", id))
                    .andExpect(status().isBadRequest());

            verify(service, times(1)).buscarMensagem(id);
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

            when(service.alterarMensagem(id, mensagem)).thenAnswer(i -> i.getArgument(1));

            //Act
            mockMvc.perform(put("/mensagens")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(mensagem)))
                    .andExpect(status().isAccepted());

            verify(service, times(1)).alterarMensagem(id, mensagem);
        }

        @Test
        void deveGerarExcecao_QuandoAlterarMensagem_IdNaoExiste() throws Exception {

            //Arrange
            var id = UUID.fromString("150f80ba-5f1c-4e93-9cac-080e0bbf8ef0");
            var mensagem = gerarMensagem();
            mensagem.setId(id);

            var mensagemErro = "Registro não encontrado";

            when(service.alterarMensagem(id, mensagem))
                    .thenThrow(new MensagemNotFoundException(mensagemErro));

            //Act
            mockMvc.perform(put("/mensagens")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(mensagem)))
//                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(mensagemErro));

            verify(service, times(1)).alterarMensagem(id, mensagem);
        }

        @Test
        void deveGerarExcecao_QuandoAlterarMensagem_AlterarPayloadComXML() throws Exception {
            //Act
            mockMvc.perform(put("/mensagens")
                            .contentType(MediaType.APPLICATION_XML)
                            .content("<menssagem><id>150f80ba-5f1c-4e93-9cac-080e0bbf8ef0</id>" +
                                    "<usuario>Ana</usuario>" +
                                    "<conteudo>Conteudo 123</conteudo></menssagem>"))
                    .andExpect(status().isUnsupportedMediaType());

            verify(service, never()).alterarMensagem(any(UUID.class), any(Mensagem.class));
        }
    }

    @Nested
    class RemoverMensagem {

        @Test
        void devePermitirRemoverMensagem() throws Exception {
            //Arrange
            var id = UUID.fromString("150f80ba-5f1c-4e93-9cac-080e0bbf8ef0");

            when(service.removerMensagem(id)).thenReturn(true);

            //Act
            mockMvc.perform(delete("/mensagens/{id}", id.toString())
                            .contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isOk())
                    .andExpect(content().string("Mensagem Removida"));

            verify(service, times(1)).removerMensagem(id);
        }

        @Test
        void deveGerarExcecao_QuandoRemoverMensagem_IdNaoExiste() throws Exception {
            //Arrange
            var id = UUID.fromString("150f80ba-5f1c-4e93-9cac-080e0bbf8ef0");

            var mensagemErro = "Registro não encontrado";
            when(service.removerMensagem(id)).thenThrow(new MensagemNotFoundException(mensagemErro));

            //Act
            mockMvc.perform(delete("/mensagens/{id}", id.toString())
                            .contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(mensagemErro));

            verify(service, times(1)).removerMensagem(id);
        }
    }

    @Nested
    class ListarMensagem {

        @Test
        void devePermitirListarMensagem() throws Exception {

            //Arrange
            var page = new PageImpl<>(Arrays.asList(gerarMensagem()), PageRequest.of(0, 10), 1);

            when(service.listarMensagens(any(Pageable.class)))
                    .thenReturn(page);

            //Act
            mockMvc.perform(get("/mensagens")
                            .param("page", "0")
                            .param("size", "10")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content", not(empty())))
                    .andExpect(jsonPath("$.totalPages").value(1))
                    .andExpect(jsonPath("$.totalElements").value(1));

            verify(service, times(1)).listarMensagens(any(Pageable.class));

        }

        @Test
        void devePermitirListarMensagem_QuandoNaoInformadoPaginacao() throws Exception {

            //Arrange
            var page = new PageImpl<>(Arrays.asList(gerarMensagem()), PageRequest.of(0, 10), 1);

            when(service.listarMensagens(any(Pageable.class)))
                    .thenReturn(page);

            //Act
            mockMvc.perform(get("/mensagens"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content", not(empty())))
                    .andExpect(jsonPath("$.totalPages").value(1))
                    .andExpect(jsonPath("$.totalElements").value(1));

            verify(service, times(1)).listarMensagens(any(Pageable.class));

        }

    }
}
