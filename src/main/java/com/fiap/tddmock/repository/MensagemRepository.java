package com.fiap.tddmock.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fiap.tddmock.model.Mensagem;

public interface MensagemRepository extends JpaRepository<Mensagem, UUID>{

}
