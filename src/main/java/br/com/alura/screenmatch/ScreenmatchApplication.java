package br.com.alura.screenmatch;

import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.service.ConsumerApiImpl;
import br.com.alura.screenmatch.service.ConverteDadosImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ScreenmatchApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        var consumerApi = new ConsumerApiImpl();
//        var json = consumerApi.obterDados("https://raw.githubusercontent.com/alura-cursos/imersao-java-2-api/main/MOCK_DATA.json");
        var json = consumerApi.obterDados("http://www.omdbapi.com/?apikey=92860590&t=Matrix");
        System.out.println(json);
        ConverteDadosImpl converteDados = new ConverteDadosImpl();
        DadosSerie dados = converteDados.converter(json, DadosSerie.class);
        System.out.println(dados);
    }
}
