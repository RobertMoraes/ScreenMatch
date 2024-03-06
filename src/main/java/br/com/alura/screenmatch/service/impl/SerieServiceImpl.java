package br.com.alura.screenmatch.service.impl;

import br.com.alura.screenmatch.dto.EpisodioDTO;
import br.com.alura.screenmatch.dto.SerieDTO;
import br.com.alura.screenmatch.enums.Categoria;
import br.com.alura.screenmatch.model.Serie;
import br.com.alura.screenmatch.repository.SerieRepository;
import br.com.alura.screenmatch.service.SerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
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

    @Override
    public List<SerieDTO> obterLancamentos() {
//        return converteDados(repository.lancamentosMaisRecentes());
        return converteDados(repository.findTop5ByOrderByEpisodiosDataLancamentoDesc());
    }

    @Override
    public SerieDTO obterPorId(UUID id) {
        Optional<Serie> serie = repository.findById(id);
        if(serie.isPresent()){
            Serie s = serie.get();
            return new SerieDTO(s.getId(),
                    s.getTitulo(),
                    s.getAtores(),
                    s.getGenero(),
                    s.getSinopse(),
                    s.getTotalTemporadas(),
                    s.getAvaliacao(),
                    s.getPoster());
        }
        return null;
    }

    @Override
    public List<EpisodioDTO> obterTodasTemporadas(UUID id) {
        Optional<Serie> serie = repository.findById(id);
        if(serie.isPresent()){
            Serie s = serie.get();
            return s.getEpisodios().stream()
                    .map(e -> new EpisodioDTO(e.getTemporada(), e.getTitulo(), e.getNumeroEpisodio()))
                    .collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public List<EpisodioDTO> obterTemporadasPorNumero(UUID id, Long nrTemporada) {
        return repository.obterEpisodiosPorTemporada(id, nrTemporada)
                .stream()
                .map(e -> new EpisodioDTO(e.getTemporada(), e.getTitulo(), e.getNumeroEpisodio()))
                .collect(Collectors.toList());
    }

    @Override
    public List<SerieDTO> filtrarPorCategoria(String nmGenero) {
        Categoria categoria = Categoria.fromPortugues(nmGenero);
        return converteDados(repository.findByGenero(categoria));
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
