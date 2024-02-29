package com.fiap.tddmock.controller;

import com.fiap.tddmock.exception.MensagemNotFoundException;
import com.fiap.tddmock.model.Mensagem;
import com.fiap.tddmock.service.MensagemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("mensagens")
@RequiredArgsConstructor
public class MensagemController {

    private final MensagemService service;

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Mensagem> registrar(@RequestBody Mensagem mensagem) {
        var mensagemRegistrada = service.registrarMensagem(mensagem);
        return new ResponseEntity<>(mensagemRegistrada, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscar(@PathVariable String id) {

        try {
            var mensagem = service.buscarMensagem(UUID.fromString(id));
            return new ResponseEntity<>(mensagem, HttpStatus.OK);
        } catch (MensagemNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> alterar(@RequestBody Mensagem mensagem) {

        try {
            var mensagemAlterada = service.alterarMensagem(mensagem.getId(), mensagem);
            return new ResponseEntity<>(mensagemAlterada, HttpStatus.ACCEPTED);
        } catch (MensagemNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping(value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> remover(@PathVariable String id) {

        try {
            var isRemovido = service.removerMensagem(UUID.fromString(id));
            return new ResponseEntity<>("Mensagem Removida", HttpStatus.OK);
        } catch (MensagemNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
