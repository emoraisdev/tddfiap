package com.fiap.tddmock.service;

import com.fiap.tddmock.exception.MensagemNotFoundException;
import com.fiap.tddmock.model.Mensagem;
import com.fiap.tddmock.repository.MensagemRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static com.fiap.tddmock.utils.MensagemHelper.gerarMensagem;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
@ActiveProfiles("test")
class MensagemServiceIT {

    @Autowired
    private MensagemRepository repo;

    @Autowired
    private MensagemService service;

    @BeforeEach
    void setup() {
        service = new MensagemServiceImpl(repo);
    }

    @AfterEach
    void tearDown() throws Exception {
    }

    @Nested
    class RegistrarMensagem {
        @Test
        void devePermitirRegistrarMensagem() {

            //Arrange
            var mensagem = gerarMensagem();

            //Act
            var mensagemRegistrada = service.registrarMensagem(mensagem);

            //Assert
            assertThat(mensagemRegistrada).isInstanceOf(Mensagem.class).isNotNull();
            assertThat(mensagemRegistrada.getConteudo()).isEqualTo(mensagem.getConteudo());
            assertThat(mensagemRegistrada.getUsuario()).isEqualTo(mensagem.getUsuario());
            assertThat(mensagemRegistrada.getId()).isNotNull();
            assertThat(mensagemRegistrada.getDataCriacao()).isNotNull();
            assertThat(mensagemRegistrada.getGostei()).isZero();
        }
    }

    @Nested
    class BuscarMensagem {

        @Test
        void devePermitirBuscarMensagem() {

            //Arrange
            var id = UUID.fromString("89608d39-3416-45dd-8652-4719bb98352b");

            //Act
            var mensagemRetornada = service.buscarMensagem(id);

            //Assert
            assertThat(mensagemRetornada).isNotNull().isInstanceOf(Mensagem.class);
            assertThat(mensagemRetornada.getId()).isNotNull()
                    .isEqualTo(UUID.fromString("89608d39-3416-45dd-8652-4719bb98352b"));
            assertThat(mensagemRetornada.getUsuario()).isNotNull()
                    .isEqualTo("Adam");
            assertThat(mensagemRetornada.getConteudo()).isNotNull()
                    .isEqualTo("Conteúdo da mensagem 01");
            assertThat(mensagemRetornada.getConteudo()).isNotNull();
            assertThat(mensagemRetornada.getGostei()).isZero();
        }

        @Test
        void deveGerarExcecao_QuandoBuscarMensagem_IdNaoExiste() {

            //Arrange
            var id = UUID.fromString("f218127a-3dc8-450e-8df4-e54498de28e4");

            //Assert
            assertThatThrownBy(() -> service.buscarMensagem(id))
                    .isInstanceOf(MensagemNotFoundException.class)
                    .hasMessage("Registro não encontrado");
        }
    }

    @Nested
    class AlterarMensagem {
        @Test
        void devePermitirAlterarMensagem() {

            //Arrange
            var id = UUID.fromString("1e9d37f1-148c-49f6-b248-fecfe71e0333");
            var mensagemNova = gerarMensagem();
            mensagemNova.setId(id);

            //Act
            var mensagemRetornada = service.alterarMensagem(id, mensagemNova);

            //Assert
            assertThat(mensagemRetornada).isInstanceOf(Mensagem.class).isNotNull();
            assertThat(mensagemRetornada.getId()).isEqualTo(mensagemNova.getId());
            assertThat(mensagemRetornada.getConteudo()).isEqualTo(mensagemNova.getConteudo());
            assertThat(mensagemRetornada.getUsuario()).isNotEqualTo(mensagemNova.getUsuario());

        }

        @Test
        void deveGerarExcecao_QuandoAlterarMensagem_IdNaoExiste() {
            //Arrange
            var id = UUID.fromString("cefc9542-2fc6-4a15-b4fb-c4260538ac6e");
            var mensagemNova = gerarMensagem();
            mensagemNova.setId(id);

            //Act & Assert
            assertThatThrownBy(() -> service.alterarMensagem(id, mensagemNova))
                    .isInstanceOf(MensagemNotFoundException.class)
                    .hasMessage("Registro não encontrado");
        }

        @Test
        void deveGerarExcecao_QuandoAlterarMensagem_IdMensagemNovaEstaDiferente() {
            //Arrange
            var id = UUID.fromString("1e9d37f1-148c-49f6-b248-fecfe71e0333");
            var mensagemNova = gerarMensagem();
            mensagemNova.setId(UUID.fromString("9adadf48-2d33-4b28-85fd-e06f52f15f9e"));

            //Act & Assert
            assertThatThrownBy(() -> service.alterarMensagem(id, mensagemNova))
                    .isInstanceOf(MensagemNotFoundException.class)
                    .hasMessage("Mensagem atualizada não apresenta o ID correto.");

        }
    }

    @Nested
    class removerMensagem {
        @Test
        void devePermitirRemoverMensagem() {

            //Arrange
            var id = UUID.fromString("1960d23e-52f9-4251-8605-b396791685f2");

            //Act
            var mensagemFoiRemovida = service.removerMensagem(id);

            assertThat(mensagemFoiRemovida).isTrue();
        }

        @Test
        void deveGerarExcecao_QuandoRemoverMensagem_IdNaoExiste() {
            //Arrange
            var id = UUID.fromString("cefc9542-2fc6-4a15-b4fb-c4260538ac6e");

            //Act & Assert
            assertThatThrownBy(() -> service.removerMensagem(id))
                    .isInstanceOf(MensagemNotFoundException.class)
                    .hasMessage("Registro não encontrado");
        }
    }

    @Nested
    class ListarMensagem {

        @Test
        void devePermitirListarMensagem() {

            //Arrange
            //Act
            var listaMensagens = service.listarMensagens(Pageable.unpaged());

            //Assert
            assertThat(listaMensagens).hasSize(3);
            assertThat(listaMensagens.getContent()).asList()
                    .allSatisfy(mensagem -> {
                        assertThat(mensagem).isNotNull().isInstanceOf(Mensagem.class);
                    });
        }
    }

}
