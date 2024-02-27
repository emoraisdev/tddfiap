package com.fiap.tddmock.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fiap.tddmock.model.Mensagem;

public abstract class MensagemHelper {

    public static Mensagem gerarMensagem() {
        return Mensagem.builder().usuario("Maria").conteudo("Conteúdo teste").build();
    }

    public static String asJsonString(final Object object){

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
