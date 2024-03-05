package br.com.alura.screenmatch.service.impl;

import br.com.alura.screenmatch.dto.SerieDTO;
import br.com.alura.screenmatch.model.Serie;
import br.com.alura.screenmatch.repository.SerieRepository;
import br.com.alura.screenmatch.service.SerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
class SerieServiceImpl implements SerieService {

    @Autowired
    private SerieRepository repository;

    public List<SerieDTO> obterTodasAsSeries() {
        return converteDados(repository.findAll());
    }

    @Override
    public List<SerieDTO> obterTop5Series() {
        return converteDados(repository.findTop5ByOrderByAvaliacaoDesc());
    }

    private List<SerieDTO> converteDados(List<Serie> series) {
        return series.stream()
                .map(serie -> new SerieDTO(serie.getId(),
                        serie.getTitulo(),
                        serie.getAtores(),
                        serie.getGenero(),
                        serie.getSinopse(),
                        serie.getTotalTemporadas(),
                        serie.getAvaliacao(),
                        serie.getPoster()))
                .collect(Collectors.toList());
    }
}
