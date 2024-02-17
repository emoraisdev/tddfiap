package com.fiap.tddmock.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fiap.tddmock.model.Mensagem;

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
	void devePermitirAlterarMensagem() {
		Assertions.fail("não implementado");
	}

	@Test
	void devePermitirExcluirMensagem() {
		Assertions.fail("não implementado");
	}

	@Test
	void devePermitirBuscarMensagem() {
		Assertions.fail("não implementado");
	}

	private Mensagem gerarMensagem() {
		return Mensagem.builder().usuario("Maria").conteudo("Conteúdo teste").build();
	}
}
