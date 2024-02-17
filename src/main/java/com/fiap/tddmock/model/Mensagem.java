package com.fiap.tddmock.model;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Mensagem {

	@Id
	private UUID id;
	
	@Column(nullable = false)
	@NotNull(message = "Usuário não pode ser vazio.")
	private String usuario;
	
	@Column(nullable = false)
	@NotNull(message = "Conteúdo não pode ser vazio.")
	private String conteudo;

	@Builder.Default
	private LocalDateTime dataCriacaoMensagem = LocalDateTime.now();
	
	@Builder.Default
	private int gostei = 0;
}
