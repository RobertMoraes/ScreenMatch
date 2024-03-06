package br.com.alura.screenmatch.service.impl;

import br.com.alura.screenmatch.dto.FraseDTO;
import br.com.alura.screenmatch.model.Frase;
import br.com.alura.screenmatch.repository.FraseRepository;
import br.com.alura.screenmatch.service.FraseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FraseServiceImpl implements FraseService {

    @Autowired
    private FraseRepository fraseRepository;

    @Override
    public FraseDTO obterFraseAleatoria() {
        Frase frase = fraseRepository.buscarFraseAleatoria();
        return new FraseDTO(frase.getTitulo(), frase.getFrase(), frase.getPersonagem(), frase.getPoster());
    }
}
