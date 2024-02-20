package com.fiap.tddmock.service;

import com.fiap.tddmock.exception.MensagemNotFoundException;
import com.fiap.tddmock.model.Mensagem;
import com.fiap.tddmock.repository.MensagemRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.AssertionErrors;

import javax.swing.*;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class MensagemServiceTest {

    @Mock
    private MensagemRepository repo;

    private MensagemService service;

    AutoCloseable mock;

    @BeforeEach
    void setup(){
        mock = MockitoAnnotations.openMocks(this);
        service = new MensagemServiceImpl(repo);
    }

    @AfterEach
    void tearDown() throws Exception {
        mock.close();
    }

    @Test
    void devePermitirRegistrarMensagem(){

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
    void devePermitirBuscarMensagem(){

        //Arrange
        var id = UUID.randomUUID();
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
    void deveGerarExcecao_QuandoBuscarMensagem_IdNaoExiste(){

        //Arrange
        var id = UUID.randomUUID();
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
    void devePermitirAlterarMensagem(){

        //Arrange
        var id = UUID.randomUUID();
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
    void deveGerarExcecao_QuandoAlterarMensagem_IdNaoExiste(){
        //Arrange
        var id = UUID.randomUUID();
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
    void deveGerarExcecao_QuandoAlterarMensagem_IdMensagemNovaEstaDiferente(){
        //Arrange
        var id = UUID.randomUUID();
        var mensagemAntiga = gerarMensagem();
        mensagemAntiga.setId(id);

        var mensagemNova = Mensagem.builder().id(UUID.randomUUID()).conteudo("Teste 123").build();

        when(repo.findById(id)).thenReturn(Optional.of(mensagemAntiga));

        //Act & Assert
        assertThatThrownBy(() -> service.alterarMensagem(id, mensagemNova))
                .isInstanceOf(MensagemNotFoundException.class)
                .hasMessage("Mensagem atualizada não apresenta o ID correto.");

        verify(repo, times(1)).findById(any(UUID.class));
        verify(repo, never()).save(any(Mensagem.class));
    }


    @Test
    void devePermitirRemoverMensagem(){
        AssertionErrors.fail("Teste não implementado.");
    }

    @Test
    void devePermitirListarMensagem(){
        AssertionErrors.fail("Teste não implementado.");
    }

    private Mensagem gerarMensagem() {
        return Mensagem.builder().usuario("Maria").conteudo("Conteúdo teste").build();
    }
}
