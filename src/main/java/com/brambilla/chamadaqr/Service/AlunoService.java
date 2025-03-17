package com.brambilla.chamadaqr.Service;

import com.brambilla.chamadaqr.Entity.Aluno;
import com.brambilla.chamadaqr.Entity.Turma;
import com.brambilla.chamadaqr.Repository.AlunoRepository;
import com.brambilla.chamadaqr.Repository.TurmaRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AlunoService {
    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private TurmaRepository turmaRepository;

    public List<Aluno> getAllAlunos() {
        return alunoRepository.findAll();
    }

    public Optional<Aluno> getAlunoById(Long id) {
        return alunoRepository.findById(id);
    }

    @Transactional
    public Aluno saveAluno(Aluno aluno) {
        if (aluno.getTurma() != null) {
            List<Turma> turmas = aluno.getTurma().stream()
                    .map(t -> turmaRepository.findById(t.getId()).orElseThrow(() -> new RuntimeException("Turma not found")))
                    .collect(Collectors.toList());
            aluno.setTurma(turmas);
        }
        validateAluno(aluno);
        return alunoRepository.save(aluno);
    }

    private void validateAluno(Aluno aluno) {
        if (aluno.getNome() == null || aluno.getNome().trim().isEmpty()) {
            throw new ValidationException("Nome do aluno não pode estar vazio.");
        }

        if (aluno.getRa() == null || aluno.getRa() <= 0) {
            throw new ValidationException("RA do aluno deve ser um número válido.");
        }

        if (alunoRepository.existsByRa(aluno.getRa())) {
            throw new ValidationException("RA já cadastrado. Utilize um RA único.");
        }

        if (aluno.getSenha() == null || aluno.getSenha().length() < 6) {
            throw new ValidationException("A senha deve ter pelo menos 6 caracteres.");
        }
    }

    public boolean existAluno(Long ra){
        return alunoRepository.existsByRa(ra);
    }

    public List<Aluno> findByAlertLevel(Integer nivel){
        List<Aluno> alunoRetornado = alunoRepository.findByAlertLevel(nivel);

        return alunoRetornado;
    }

    public boolean existsByRa(Long ra) {
        return alunoRepository.existsByRa(ra);
    }

    public void deleteAluno(Long id) {
        alunoRepository.deleteById(id);
    }
}