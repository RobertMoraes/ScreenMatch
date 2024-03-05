package br.com.alura.screenmatch.service;

import br.com.alura.screenmatch.dto.SerieDTO;

import java.util.List;

public interface SerieService {
    List<SerieDTO> obterTodasAsSeries();

    List<SerieDTO> obterTop5Series();
}
