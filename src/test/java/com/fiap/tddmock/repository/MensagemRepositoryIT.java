package com.fiap.tddmock.repository;

import com.fiap.tddmock.AppTddMock;
import com.fiap.tddmock.model.Mensagem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = AppTddMock.class)
@AutoConfigureTestDatabase
@Transactional
public class MensagemRepositoryIT {

    @Autowired
    private MensagemRepository repo;

    @Test
    void devePermitirCriarTabela(){
        var totalRegistros = repo.count();

        assertThat(totalRegistros).isGreaterThan(0);
    }

    @Test
    void devePermitirRegistrarMensagem(){
        //Arrange
        var id = UUID.randomUUID();
        var mensagem = gerarMensagem();
        mensagem.setId(id);

        //Act
        var mensagemRecebida = repo.save(mensagem);

        //Assert
        assertThat(mensagemRecebida)
                .isInstanceOf(Mensagem.class)
                .isNotNull();

        assertThat(mensagemRecebida.getId()).isEqualTo(id);
        assertThat(mensagemRecebida.getConteudo()).isEqualTo(mensagem.getConteudo());
        assertThat(mensagemRecebida.getUsuario()).isEqualTo(mensagem.getUsuario());

    }

    @Test
    void devePermitirBuscarMensagem(){
        //Arrange
        var id = UUID.fromString("89608d39-3416-45dd-8652-4719bb98352b");

        //Act
        var mensagemRecebida = repo.findById(id);

        //Assert
        assertThat(mensagemRecebida)
                .isPresent();

        mensagemRecebida.ifPresent(m -> {
            assertThat(m.getId()).isEqualTo(id);
        });

    }

    @Test
    void devePermitirRemoverMensagem(){
        //Arrange
        var id = UUID.fromString("1e9d37f1-148c-49f6-b248-fecfe71e0333");

        //Act
        repo.deleteById(id);
        var mensagemRemovida = repo.findById(id);

        //Assert
        assertThat(mensagemRemovida).isEmpty();
    }

    @Test
    void devePermitirListarMensagens(){

        var resultados = repo.findAll();

        assertThat(resultados).hasSizeGreaterThan(0);
    }

    private Mensagem gerarMensagem() {
        return Mensagem.builder().usuario("Maria").conteudo("Conteúdo teste").build();
    }

    private Mensagem criarMensagem(Mensagem mensagem) {
        return repo.save(mensagem);
    }
}
