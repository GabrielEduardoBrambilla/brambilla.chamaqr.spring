package com.brambilla.chamadaqr.Service;

import com.brambilla.chamadaqr.Entity.Turma;
import com.brambilla.chamadaqr.Repository.TurmaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TurmaService {
    @Autowired
    private TurmaRepository turmaRepository;

    public List<Turma> getAllTurmas() {
        return turmaRepository.findAll();
    }

    public Optional<Turma> getTurmaById(Long id) {
        return turmaRepository.findById(id);
    }

    public Turma saveTurma(Turma turma) {
        return turmaRepository.save(turma);
    }

    public void deleteTurma(Long id) {
        turmaRepository.deleteById(id);
    }
}