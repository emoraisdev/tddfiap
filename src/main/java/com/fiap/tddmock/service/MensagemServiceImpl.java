package com.fiap.tddmock.service;

import com.fiap.tddmock.exception.MensagemNotFoundException;
import com.fiap.tddmock.model.Mensagem;
import com.fiap.tddmock.repository.MensagemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MensagemServiceImpl implements MensagemService{

    private final MensagemRepository repo;

    @Override
    public Mensagem registrarMensagem(Mensagem mensagem) {

        mensagem.setId(UUID.randomUUID());
        return repo.save(mensagem);
    }

    @Override
    public Mensagem buscarMensagem(UUID id) {
        return repo.findById(id).orElseThrow(() -> new MensagemNotFoundException("Registro não encontrado"));
    }

    @Override
    public Mensagem alterarMensagem(UUID id, Mensagem mensagemAlterada) {

        var mensagem = buscarMensagem(id);

        if (!mensagem.getId().equals(mensagemAlterada.getId())) {
            throw new MensagemNotFoundException("Mensagem atualizada não apresenta o ID correto.");
        }
        mensagem.setConteudo(mensagemAlterada.getConteudo());

        return repo.save(mensagem);
    }

    @Override
    public boolean removerMensagem(UUID id) {

        buscarMensagem(id);
        repo.deleteById(id);

        return true;
    }

    @Override
    public Page<Mensagem> listarMensagens(Pageable pageable) {
        return repo.listarMensagens(pageable);
    }
}
