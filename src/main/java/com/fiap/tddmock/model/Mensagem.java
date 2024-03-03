package com.fiap.tddmock.model;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
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
	private LocalDateTime dataCriacao = LocalDateTime.now();

	@Builder.Default
	private int gostei = 0;
}
