package com.brambilla.chamadaqr.Service;

import com.brambilla.chamadaqr.Entity.Professor;
import com.brambilla.chamadaqr.Repository.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProfessorService {
    @Autowired
    private ProfessorRepository professorRepository;

    public List<Professor> getAllProfessores() {
        return professorRepository.findAll();
    }

    public Optional<Professor> getProfessorById(Long id) {
        return professorRepository.findById(id);
    }

    public Professor saveProfessor(Professor professor) {
        return professorRepository.save(professor);
    }

    public void deleteProfessor(Long id) {
        professorRepository.deleteById(id);
    }

    public Optional<Professor> getProfessorByNome(String nome) {
        return professorRepository.findByNome(nome);
    }

    public Optional<Professor> getProfessorByEmail(String email) {
        return professorRepository.findByEmail(email);
    }
}