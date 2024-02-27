package com.fiap.tddmock.controller;

import com.fiap.tddmock.model.Mensagem;
import com.fiap.tddmock.service.MensagemService;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.fiap.tddmock.utils.MensagemHelper.asJsonString;
import static com.fiap.tddmock.utils.MensagemHelper.gerarMensagem;
import static org.assertj.core.api.Assertions.assertThat;
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

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
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
                        .content(asJsonString(mensagem)))
                    .andExpect(status().isCreated());

            verify(service, times(1)).registrarMensagem(any(Mensagem.class));
        }
    }

    @Nested
    class BuscarMensagem {

        @Test
        void devePermitirBuscarMensagem() {

        }

        @Test
        void deveGerarExcecao_QuandoBuscarMensagem_IdNaoExiste() {

        }
    }

    @Nested
    class AlterarMensagem {

        @Test
        void devePermitirAlterarMensagem() {

        }

        @Test
        void deveGerarExcecao_QuandoAlterarMensagem_IdNaoExiste() {

        }

        @Test
        void deveGerarExcecao_QuandoAlterarMensagem_IdMensagemNovaEstaDiferente() {

        }
    }

    @Nested
    class RemoverMensagem {

        @Test
        void devePermitirRemoverMensagem() {

        }

        @Test
        void deveGerarExcecao_QuandoRemoverMensagem_IdNaoExiste() {

        }
    }

    @Nested
    class ListarMensagem {

        @Test
        void devePermitirListarMensagem() {

        }

    }
}
