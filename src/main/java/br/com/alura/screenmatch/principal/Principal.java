package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.enums.Categoria;
import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.model.Serie;
import br.com.alura.screenmatch.repository.SerieRepository;
import br.com.alura.screenmatch.service.impl.ConsumerApiImpl;
import br.com.alura.screenmatch.service.impl.ConverteDadosImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileWriter;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private final Scanner teclado = new Scanner(System.in);
    private final ConsumerApiImpl consumerApi = new ConsumerApiImpl();
    private final ConverteDadosImpl converteDados = new ConverteDadosImpl();
    private ObjectMapper objectMapper = new ObjectMapper();
    private List<DadosSerie> dadosSeries = new ArrayList<>();
    private SerieRepository repository;
    private List<Serie> series;
    private Optional<Serie> serieBusca;

    public Principal(SerieRepository repository) {
        this.repository = repository;
    }

    public void exibeMenu() {
        var opcao = -1;
        while (opcao != 0) {
            var menu = """
                        1 - Buscar Séries
                        2 - Buscar Episodios
                        3 - Listar Seíries
                        4 - Buscar por Titulo
                        5 - Buscar serie por ator
                        6 - Top 5 seíries
                        7 - Buscar por genero
                        8 - Filtrar séries
                        9 - Filtrar episódio
                        10 - Top 5 Episódios Por Serie
                        11 - Buscar episódios por data
                        0 - Sair
                    """;
            System.out.println(menu);
            opcao = teclado.nextInt();
            teclado.nextLine();
            switch (opcao) {
                case 1 -> this.buscaSeries();
                case 2 -> this.buscaEpisodios();
                case 3 -> this.listarSeriesBuscadas();
                case 4 -> this.buscaSeriesPorTitulo();
                case 5 -> this.buscarSeriesPorAtor();
                case 6 -> this.topCincoSeries();
                case 7 -> this.buscarSeriesPorGenero();
                case 8 -> this.buscarSeriesPorFiltro();
                case 9 -> this.buscarEpisodioPorTrecho();
                case 10 -> this.topEpisodiosPorSerie();
                case 11 -> this.buscarEpisodioPorData();
                case 0 -> System.out.println("Saindo...");
                default -> System.out.println("Opcão inválida");
            }
        }
    }

    private void buscaSeries() {
        DadosSerie dados = getDadosSerie();
        Serie serie = new Serie(dados);
        repository.save(serie);
        System.out.println(dados);
    }

    private DadosSerie getDadosSerie() {
        String outputPath = "src/main/resources/output/";
        System.out.println("Digite o nome da seírie: ");
        var nomeSerie = teclado.nextLine();
        var json = consumerApi.obterDados(System.getenv("API_KEY") + nomeSerie.replaceAll(" ", "+"));
        try {
            FileWriter escritor = new FileWriter(outputPath + nomeSerie.replaceAll(" ", "_") + "titulo.json");
            escritor.write(json);
            escritor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        DadosSerie dados = converteDados.converter(json, DadosSerie.class);
        return dados;
    }

    private void buscaEpisodios() {

        listarSeriesBuscadas();
        System.out.println("Digite o nome da seírie: ");
        var nomeSerie = teclado.nextLine();
        Optional<Serie> first = repository.findByTituloContainingIgnoreCase(nomeSerie);
        if (first.isPresent()) {
            var serieEncontrada = first.get();
            List<DadosTemporada> temporadas = new ArrayList<>();

            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                var json = consumerApi.obterDados(System.getenv("API_KEY") + serieEncontrada.getTitulo().replaceAll(" ", "+") + "&Season=" + i);
                DadosTemporada dadosTemporada = converteDados.converter(json, DadosTemporada.class);
                temporadas.add(dadosTemporada);
            }
            temporadas.forEach(System.out::println);

            List<Episodio> episodios = temporadas.stream()
                    .flatMap(t -> t.episodios().stream()
                            .map(e -> new Episodio(t.numero(), e)))
                    .collect(Collectors.toList());
            serieEncontrada.setEpisodios(episodios);
            repository.save(serieEncontrada);
        } else {
            System.out.println("Seírie não encontrada");
        }

    }

    private void listarSeriesBuscadas() {
        series = repository.findAll();
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }

    private void buscaSeriesPorTitulo() {
        System.out.println("Digite o nome da seírie: ");
        var nomeSerie = teclado.nextLine();
        serieBusca = repository.findByTituloContainingIgnoreCase(nomeSerie);

        if (serieBusca.isPresent()) {
            System.out.println("Dados da seírie: " + serieBusca.get());
        } else {
            System.out.println("Seírie não encontrada");
        }
    }

    private void buscarSeriesPorAtor() {
        System.out.println("Digite o nome do ator: ");
        var nomeAtor = teclado.nextLine();
        System.out.println("Digite a avaliação: ");
        var avaliacao = teclado.nextDouble();
        List<Serie> seriesEncontradas = repository.findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(nomeAtor, avaliacao);
        System.out.println("Seíries em que "+ nomeAtor + " trabalhou: ");
        seriesEncontradas.forEach(serie -> System.out.println(serie.getTitulo() + " - " + serie.getGenero() + " - " + serie.getAvaliacao()));
    }

    private void topCincoSeries() {
        List<Serie> topCincoSeries = repository.findTop5ByOrderByAvaliacaoDesc();
        topCincoSeries.forEach(serie -> System.out.println(serie.getTitulo() + " - " + serie.getGenero() + " - " + serie.getAvaliacao()));
    }

    private void buscarSeriesPorGenero() {
        System.out.println("Selecione o genero: ");
        var nomeGenero = teclado.nextLine();
        Categoria categoria = Categoria.fromPortugues(nomeGenero);
        List<Serie> seriesPorGenero = repository.findByGenero(categoria);
        System.out.println("Seíries do genero " + nomeGenero);
        seriesPorGenero.forEach(System.out::println);
    }


    private void buscarSeriesPorFiltro() {
        System.out.println("Filtrar séries até quantas temporadas? ");
        int totalTemporadas = Integer.parseInt(teclado.nextLine());
        System.out.println("Com avaliação a partir de que valor? ");
        double avaliacao = Double.parseDouble(teclado.nextLine());
//        List<Serie> filtroSeries = repository.findByTotalTemporadasLessThanEqualAndAvaliacaoGreaterThanEqual(totalTemporadas, avaliacao);
        List<Serie> filtroSeries = repository.seriesPorTemporadaEAvaliacao(totalTemporadas, avaliacao);
        System.out.println("*** Séries filtradas ***");
        filtroSeries.forEach(s ->
                System.out.println(s.getTitulo() + "  - avaliação: " + s.getAvaliacao()));
    }

    private void buscarEpisodioPorTrecho() {
        System.out.println("Digite o nome do episódio para busca? ");
        var trechoEpisodio = teclado.nextLine();
        List<Episodio> lsRetornoEpisodios = repository.episodiosPorTrecho(trechoEpisodio);
        lsRetornoEpisodios.forEach(e -> System.out.printf("Série: %s ::: Temporada %s - Episódio %s - %s\n",
                e.getSerie().getTitulo(), e.getTemporada(), e.getNumeroEpisodio(), e.getTitulo()));
    }


    private void topEpisodiosPorSerie() {
        buscaSeriesPorTitulo();
        if(serieBusca.isPresent()){
            Serie serie = serieBusca.get();
            List<Episodio> topEpisodios = repository.topEpisodiosPorSerie(serie);
            topEpisodios.forEach(e -> System.out.printf("Série: %s ::: Temporada %s - Episódio %s - %s - Avaliação %s\n",
                    e.getSerie().getTitulo(), e.getTemporada(), e.getNumeroEpisodio(), e.getTitulo(), e.getAvaliacao()));
        }
    }

    private void buscarEpisodioPorData(){
       buscaSeriesPorTitulo();
       if(serieBusca.isPresent()){
           Serie serie = serieBusca.get();
           System.out.println("Digite o ano limite de lançamento: ");
           int anoLancamento = Integer.parseInt(teclado.nextLine());

           List<Episodio> episodiosAno = repository.episodioPorSerieEAno(serie, anoLancamento);

           episodiosAno.forEach(System.out::println);
       }
    }
}
