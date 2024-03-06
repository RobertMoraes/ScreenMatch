package br.com.alura.screenmatch.controller;

import br.com.alura.screenmatch.dto.FraseDTO;
import br.com.alura.screenmatch.service.FraseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FraseController {
    @Autowired
    private FraseService service;

    @GetMapping("/series/frases")
    private FraseDTO obterFraseAleatorio(){
        return service.obterFraseAleatoria();
    }
}
