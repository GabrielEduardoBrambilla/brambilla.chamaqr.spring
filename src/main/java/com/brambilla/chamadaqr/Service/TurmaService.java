package com.brambilla.chamadaqr.Service;

import com.brambilla.chamadaqr.Entity.Aluno;
import com.brambilla.chamadaqr.Entity.Professor;
import com.brambilla.chamadaqr.Entity.Turma;
import com.brambilla.chamadaqr.Repository.AlunoRepository;
import com.brambilla.chamadaqr.Repository.ProfessorRepository;
import com.brambilla.chamadaqr.Repository.TurmaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TurmaService {
    @Autowired
    private TurmaRepository turmaRepository;
    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private AlunoRepository alunoRepository;


    public List<Turma> getAllTurmas() {
        return turmaRepository.findAll();
    }

    public Optional<Turma> getTurmaById(Long id) {
        return turmaRepository.findById(id);
    }
    public List<Turma> getTurmasByCurso(String curso) {
        return turmaRepository.findByCurso(curso);
    }

    public List<Turma> getTurmasByQtdAlunos(Long qtdAlunos) {
        return turmaRepository.findByQtdAlunos(qtdAlunos);
    }


    @Transactional
    public Turma saveTurma(Turma turma) {
        if (turma.getProfessorResponsavel() != null) {
            Long professorId = turma.getProfessorResponsavel().getId();
            Professor professor = professorRepository.findById(professorId)
                    .orElseThrow(() -> new RuntimeException("Professor not found with ID: " + professorId));
            turma.setProfessorResponsavel(professor);
        }

        if (turma.getAlunos() != null && !turma.getAlunos().isEmpty()) {
            List<Aluno> alunos = turma.getAlunos().stream()
                    .map(aluno -> alunoRepository.findById(aluno.getId())
                            .orElseThrow(() -> new RuntimeException("Aluno not found with ID: " + aluno.getId())))
                    .collect(Collectors.toList());
            turma.setAlunos(alunos);
        }

        return turmaRepository.save(turma);
    }

    public void deleteTurma(Long id) {
        turmaRepository.deleteById(id);
    }
}