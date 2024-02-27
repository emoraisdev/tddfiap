package com.fiap.tddmock.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.fiap.tddmock.model.Mensagem;
import org.springframework.data.jpa.repository.Query;


public interface MensagemRepository extends JpaRepository<Mensagem, UUID>{

    @Query("SELECT m FROM Mensagem m ORDER BY m.dataCriacao DESC")
    Page<Mensagem> listarMensagens(Pageable pageable);
}
