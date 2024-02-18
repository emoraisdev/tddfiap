package com.fiap.tddmock.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fiap.tddmock.model.Mensagem;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

public class MensagemRepositoryTest {

	@Mock
	private MensagemRepository repo;

	AutoCloseable openMocks;

	@BeforeEach
	void setup() {
		openMocks = MockitoAnnotations.openMocks(this);
	}

	@AfterEach
	void tearDown() throws Exception {
		openMocks.close();
	}

	@Test
	void devePermitirRegistrarMensagem() {

		var mensagem = gerarMensagem();

		when(repo.save(any(Mensagem.class))).thenReturn(mensagem);

		var mensagemArmazenada = repo.save(mensagem);

		assertThat(mensagemArmazenada).isNotNull().isEqualTo(mensagem);

		verify(repo, times(1)).save(any(Mensagem.class));

	}

	@Test
	void devePermitirExcluirMensagem() {
		//Arrange
		var id = UUID.randomUUID();
		doNothing().when(repo).deleteById(any(UUID.class));

		//Act
		repo.deleteById(id);

		//Assert
		verify(repo, times(1)).deleteById(any(UUID.class));

	}

	@Test
	void devePermitirBuscarMensagem() {
		//Arrange
		var id = UUID.randomUUID();
		var mensagem = gerarMensagem();
		mensagem.setId(id);

		when(repo.findById(any(UUID.class))).thenReturn(Optional.of(mensagem));

		//Act
		var mensagemRecebida = repo.findById(id);

		//Assert
		assertThat(mensagemRecebida).isPresent()
			.containsSame(mensagem);

		mensagemRecebida.ifPresent(m -> {
			assertThat(m.getId()).isEqualTo(mensagem.getId());
			assertThat(m.getConteudo()).isEqualTo(mensagem.getConteudo());
			assertThat(m.getUsuario()).isEqualTo(mensagem.getUsuario());
		});

		verify(repo, times(1)).findById(any(UUID.class));
	}

	@Test
	void devePermitirListarMensagens() {
		//Arrange

		var mensagem1 = gerarMensagem();
		var mensagem2 = gerarMensagem();
		var listaMensagens = Arrays.asList(mensagem1, mensagem2);

		when(repo.findAll()).thenReturn(listaMensagens);

		//Act
		var mensagensRecebidas = repo.findAll();

		//Assert
		assertThat(mensagensRecebidas)
				.isNotNull()
				.hasSize(2)
				.containsExactlyInAnyOrder(mensagem1, mensagem2);

		verify(repo, times(1)).findAll();
	}

	private Mensagem gerarMensagem() {
		return Mensagem.builder().usuario("Maria").conteudo("Conteúdo teste").build();
	}
}
