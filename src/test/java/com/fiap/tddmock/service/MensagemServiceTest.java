package com.fiap.tddmock.service;

import com.fiap.tddmock.exception.MensagemNotFoundException;
import com.fiap.tddmock.model.Mensagem;
import com.fiap.tddmock.repository.MensagemRepository;
import com.fiap.tddmock.utils.MensagemHelper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.AssertionErrors;

import javax.swing.*;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static com.fiap.tddmock.utils.MensagemHelper.gerarMensagem;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class MensagemServiceTest {

    @Mock
    private MensagemRepository repo;

    private MensagemService service;

    AutoCloseable mock;

    @BeforeEach
    void setup() {
        mock = MockitoAnnotations.openMocks(this);
        service = new MensagemServiceImpl(repo);
    }

    @AfterEach
    void tearDown() throws Exception {
        mock.close();
    }

    @Test
    void devePermitirRegistrarMensagem() {

        //Arrange
        var mensagem = gerarMensagem();
        when(repo.save(ArgumentMatchers.any(Mensagem.class))).thenAnswer(i -> i.getArgument(0));

        //Act
        var mensagemRegistrada = service.registrarMensagem(mensagem);

        //Assert
        assertThat(mensagemRegistrada)
                .isInstanceOf(Mensagem.class)
                .isNotNull();

        assertThat(mensagemRegistrada.getConteudo()).isEqualTo(mensagem.getConteudo());
        assertThat(mensagemRegistrada.getUsuario()).isEqualTo(mensagem.getUsuario());
        assertThat(mensagemRegistrada.getId()).isNotNull();

        verify(repo, times(1)).save(any(Mensagem.class));
    }

    @Test
    void devePermitirBuscarMensagem() {

        //Arrange
        var id = UUID.fromString("fda8113f-0b2c-4361-b6bc-24f81d038f45");
        var mensagem = gerarMensagem();
        mensagem.setId(id);

        when(repo.findById(id)).thenReturn(Optional.of(mensagem));

        //Act
        var mensagemRetornada = service.buscarMensagem(id);

        //Assert
        assertThat(mensagemRetornada).isEqualTo(mensagem);
        verify(repo, times(1)).findById(any(UUID.class));
    }

    @Test
    void deveGerarExcecao_QuandoBuscarMensagem_IdNaoExiste() {

        //Arrange
        var id = UUID.fromString("f218127a-3dc8-450e-8df4-e54498de28e4");
        var mensagem = gerarMensagem();
        mensagem.setId(id);

        when(repo.findById(id)).thenReturn(Optional.empty());

        //Assert
        assertThatThrownBy(() -> service.buscarMensagem(id))
                .isInstanceOf(MensagemNotFoundException.class)
                .hasMessage("Registro não encontrado");

        verify(repo, times(1)).findById(id);
    }

    @Test
    void devePermitirAlterarMensagem() {

        //Arrange
        var id = UUID.fromString("62dc5107-880a-42cc-8d5e-ad56b4baa5e6");
        var mensagemAntiga = gerarMensagem();
        mensagemAntiga.setId(id);

        var mensagemNova = Mensagem.builder().id(mensagemAntiga.getId())
                .usuario(mensagemAntiga.getUsuario()).build();
        mensagemNova.setConteudo("ABCD 12345");

        when(repo.findById(id)).thenReturn(Optional.of(mensagemAntiga));
        when(repo.save(mensagemNova)).thenAnswer(i -> i.getArgument(0));

        //Act
        var mensagemRetornada = service.alterarMensagem(id, mensagemNova);


        //Assert
        assertThat(mensagemRetornada).isInstanceOf(Mensagem.class).isNotNull();
        assertThat(mensagemRetornada.getId()).isEqualTo(mensagemNova.getId());
        assertThat(mensagemRetornada.getUsuario()).isEqualTo(mensagemNova.getUsuario());
        assertThat(mensagemRetornada.getConteudo()).isEqualTo(mensagemNova.getConteudo());

        verify(repo, times(1)).findById(any(UUID.class));
        verify(repo, times(1)).save(any(Mensagem.class));

    }

    @Test
    void deveGerarExcecao_QuandoAlterarMensagem_IdNaoExiste() {
        //Arrange
        var id = UUID.fromString("cefc9542-2fc6-4a15-b4fb-c4260538ac6e");
        var mensagemNova = gerarMensagem();
        mensagemNova.setId(id);

        when(repo.findById(id)).thenReturn(Optional.empty());

        //Act & Assert
        assertThatThrownBy(() -> service.alterarMensagem(id, mensagemNova))
                .isInstanceOf(MensagemNotFoundException.class)
                .hasMessage("Registro não encontrado");

        verify(repo, times(1)).findById(any(UUID.class));
        verify(repo, never()).save(any(Mensagem.class));
    }

    @Test
    void deveGerarExcecao_QuandoAlterarMensagem_IdMensagemNovaEstaDiferente() {
        //Arrange
        var id = UUID.fromString("691af1e8-7fae-4c35-b021-833067f8200e");
        var mensagemAntiga = gerarMensagem();
        mensagemAntiga.setId(id);

        var mensagemNova = Mensagem.builder().id(UUID.fromString("8655747b-57e2-4935-aadb-920327d8ee00"))
                .conteudo("Teste 123").build();

        when(repo.findById(id)).thenReturn(Optional.of(mensagemAntiga));

        //Act & Assert
        assertThatThrownBy(() -> service.alterarMensagem(id, mensagemNova))
                .isInstanceOf(MensagemNotFoundException.class)
                .hasMessage("Mensagem atualizada não apresenta o ID correto.");

        verify(repo, times(1)).findById(any(UUID.class));
        verify(repo, never()).save(any(Mensagem.class));
    }


    @Test
    void devePermitirRemoverMensagem() {

        //Arrange
        var id = UUID.fromString("54f7cf60-bf92-4d4b-aeb1-8a0d6f991a8d");
        var mensagem = gerarMensagem();
        mensagem.setId(id);

        when(repo.findById(id)).thenReturn(Optional.of(mensagem));
        doNothing().when(repo).deleteById(id);

        //Act
        var mensagemFoiRemovida = service.removerMensagem(id);

        assertThat(mensagemFoiRemovida).isTrue();

        verify(repo, times(1)).findById(any(UUID.class));
        verify(repo, times(1)).deleteById(any(UUID.class));
    }

    @Test
    void deveGerarExcecao_QuandoRemoverMensagem_IdNaoExiste() {
        //Arrange
        var id = UUID.fromString("ef67b2a7-f100-4b76-8305-33b8e585b3f3");

        when(repo.findById(id)).thenReturn(Optional.empty());

        //Act & Assert
        assertThatThrownBy(() -> service.removerMensagem(id))
                .isInstanceOf(MensagemNotFoundException.class)
                .hasMessage("Registro não encontrado");

        verify(repo, times(1)).findById(any(UUID.class));
        verify(repo, never()).deleteById(any(UUID.class));
    }

    @Test
    void devePermitirListarMensagem() {

        //Arrange
        Page<Mensagem> page = new PageImpl<>(
                Arrays.asList(gerarMensagem(), gerarMensagem(), gerarMensagem()));

        when(repo.listarMensagens(any(Pageable.class))).thenReturn(page);

        //Act
        var resultadoObtido = service.listarMensagens(Pageable.unpaged());

        //Assert
        assertThat(resultadoObtido).hasSize(3);
        assertThat(resultadoObtido.getContent()).asList()
                .allSatisfy(mensagem -> {
                    assertThat(mensagem).isNotNull().isInstanceOf(Mensagem.class);
                });

        verify(repo, times(1)).listarMensagens(any(Pageable.class));
    }

}
