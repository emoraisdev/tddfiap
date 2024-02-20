package com.fiap.tddmock.service;

import com.fiap.tddmock.model.Mensagem;

import java.util.List;
import java.util.UUID;

public interface MensagemService {

    Mensagem registrarMensagem(Mensagem mensagem);

    Mensagem buscarMensagem(UUID id);

    Mensagem alterarMensagem(UUID id, Mensagem mensagemAlterada);

    boolean removerMensagem(UUID id);

    List<Mensagem> listarMensagem();
}
