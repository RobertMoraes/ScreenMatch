package br.com.alura.screenmatch.service.impl;

import br.com.alura.screenmatch.service.ConverteDados;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConverteDadosImpl implements ConverteDados {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public <T> T converter(String json, Class<T> classe) {
        try {
            return mapper.readValue(json, classe);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
