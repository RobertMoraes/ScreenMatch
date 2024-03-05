package br.com.alura.screenmatch.dto;

import br.com.alura.screenmatch.enums.Categoria;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.UUID;

public record SerieDTO(UUID id,
                       String titulo,
                       String atores,
                       Categoria genero,
                       String sinopse,
                       Integer totalTemporadas,
                       Double avaliacao,
                       String poster) {

}
