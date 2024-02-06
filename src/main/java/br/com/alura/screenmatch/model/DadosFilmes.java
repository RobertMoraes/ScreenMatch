package br.com.alura.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosFilmes(@JsonAlias("Title") String titulo,
                          @JsonAlias("Runtime") String duracao,
                          @JsonAlias("Year") String ano,
                          @JsonAlias("imdbRating") String avaliacao) {
}
