package com.fiap.tddmock.controller;

import com.fiap.tddmock.model.Mensagem;
import com.fiap.tddmock.service.MensagemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("mensagens")
@RequiredArgsConstructor
public class MensagemController {

    private final MensagemService service;

    @PostMapping
    public ResponseEntity<Mensagem> registrar(Mensagem mensagem){
        var mensagemRegistrada = service.registrarMensagem(mensagem);
        return new ResponseEntity<>(mensagemRegistrada, HttpStatus.CREATED);
    }
}
