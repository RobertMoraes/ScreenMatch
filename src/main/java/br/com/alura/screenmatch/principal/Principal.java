package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.DadosEpisodios;
import br.com.alura.screenmatch.model.DadosFilmes;
import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.service.ConsumerApiImpl;
import br.com.alura.screenmatch.service.ConverteDadosImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.FileWriter;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private final Scanner teclado = new Scanner(System.in);
    private final ConsumerApiImpl consumerApi = new ConsumerApiImpl();
    private final ConverteDadosImpl converteDados = new ConverteDadosImpl();

    private ObjectMapper objectMapper = new ObjectMapper();
    private final String ENDERECO_API = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=92860590";

    public void exibeMenu() {
        String outputPath = "src/main/resources/output/";
        System.out.println("Digite o nome da série para buscar: ");
        var nomeSerie = teclado.nextLine().replaceAll(" ", "+");
        var json = consumerApi.obterDados(ENDERECO_API + nomeSerie + API_KEY);
        var tituloArquivo = nomeSerie.replaceAll("\\+", "");
        try {
            FileWriter escritor = new FileWriter(outputPath + tituloArquivo + "titulo.json");
            escritor.write(json);
            escritor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Map<String, String> map = new HashMap<>();
        map.putAll(converteDados.converter(json, Map.class));

        if (map.get("Type").equals("series") && !map.get("totalSeasons").equals("N/A")) {
            DadosSerie dados = converteDados.converter(json, DadosSerie.class);
            System.out.println("dados: " + dados);
            List<DadosTemporada> lsDadosTemporada = new ArrayList<>();
            for (int i = 1; i <= dados.totalTemporadas(); i++) {
                json = consumerApi.obterDados(ENDERECO_API + nomeSerie + "&season=" + i + API_KEY);
                DadosTemporada dadosTemporada = converteDados.converter(json, DadosTemporada.class);
                lsDadosTemporada.add(dadosTemporada);
            }
            System.out.println("Lista dados temporada: ");
            try {
                objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
                String jsonConvertido = objectMapper.writeValueAsString(lsDadosTemporada);
                FileWriter escritor = new FileWriter(outputPath + tituloArquivo + "temporadas.json");
                escritor.write(jsonConvertido);
                escritor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            lsDadosTemporada.forEach(System.out::println);

            System.out.println("Episodios: ");
            lsDadosTemporada.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));

            List<DadosEpisodios> dadosEpisodios = lsDadosTemporada.stream()
                    .flatMap(k -> k.episodios().stream())
                    .collect(Collectors.toList());

            System.out.println("Episodios:::::::::::::: ");
            dadosEpisodios.forEach(System.out::println);

            System.out.println("Episodios::::::::::::::TOP 5");
            dadosEpisodios.stream()
                    .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
                    .sorted(Comparator.comparing(DadosEpisodios::avaliacao).reversed())
                    .limit(5)
                    .forEach(System.out::println);


        } else {
            DadosFilmes dados = converteDados.converter(json, DadosFilmes.class);
            System.out.println(dados);
        }

    }
}
