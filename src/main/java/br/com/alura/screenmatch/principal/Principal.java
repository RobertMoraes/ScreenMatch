package br.com.alura.screenmatch.principal;

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
        Optional<Serie> serieBuscada = repository.findByTituloContainingIgnoreCase(nomeSerie);

        if (serieBuscada.isPresent()) {
            System.out.println("Dados da seírie: " + serieBuscada.get());
        } else {
            System.out.println("Seírie não encontrada");
        }
    }

}
