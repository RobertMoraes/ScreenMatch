package br.com.alura.screenmatch.service;

public interface ConverteDados {
    <T> T converter(String json, Class<T> classe);
}
