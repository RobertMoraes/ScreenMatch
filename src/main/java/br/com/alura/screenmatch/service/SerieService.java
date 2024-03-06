package br.com.alura.screenmatch.service;

import br.com.alura.screenmatch.dto.EpisodioDTO;
import br.com.alura.screenmatch.dto.SerieDTO;

import java.util.List;
import java.util.UUID;

public interface SerieService {
    List<SerieDTO> obterTodasAsSeries();

    List<SerieDTO> obterTop5Series();

    List<SerieDTO> obterLancamentos();

    SerieDTO obterPorId(UUID id);

    List<EpisodioDTO> obterTodasTemporadas(UUID id);

    List<EpisodioDTO> obterTemporadasPorNumero(UUID id, Long nrTemporada);

    List<SerieDTO> filtrarPorCategoria(String nmGenero);
}
