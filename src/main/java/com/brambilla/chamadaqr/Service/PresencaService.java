package com.brambilla.chamadaqr.Service;

import com.brambilla.chamadaqr.Entity.Presenca;
import com.brambilla.chamadaqr.Repository.PresencaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PresencaService {
    @Autowired
    private PresencaRepository presencaRepository;

    public List<Presenca> getAllPresencas() {
        return presencaRepository.findAll();
    }

    public Optional<Presenca> getPresencaById(Long id) {
        return presencaRepository.findById(id);
    }

    public Presenca savePresenca(Presenca presenca) {
        return presencaRepository.save(presenca);
    }

    public void deletePresenca(Long id) {
        presencaRepository.deleteById(id);
    }
}
