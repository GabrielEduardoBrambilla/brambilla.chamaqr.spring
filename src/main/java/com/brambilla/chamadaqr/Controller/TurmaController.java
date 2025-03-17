package com.brambilla.chamadaqr.Controller;

import com.brambilla.chamadaqr.Entity.Turma;
import com.brambilla.chamadaqr.Service.TurmaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/turmas")
public class TurmaController {

    @Autowired
    private TurmaService turmaService;

    @GetMapping
    public ResponseEntity<?> getAllTurmas() {
        try {
            List<Turma> turmas = turmaService.getAllTurmas();
            return ResponseEntity.ok(turmas);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao buscar as turmas.");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTurmaById(@PathVariable Long id) {
        try {
            Optional<Turma> turma = turmaService.getTurmaById(id);
            if(turma.isEmpty()){
                return ResponseEntity.status(404).body("Turma não encontrada.");
            }
            return ResponseEntity.ok(turma);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao buscar a turma.");
        }
    }

    @GetMapping("/curso/{curso}")
    public ResponseEntity<?> getTurmasByCurso(@PathVariable String curso) {
        try {
            List<Turma> turmas = turmaService.getTurmasByCurso(curso);
            return turmas.isEmpty() ? ResponseEntity.status(404).body("Nenhuma turma encontrada para este curso.") : ResponseEntity.ok(turmas);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao buscar turmas pelo curso.");
        }
    }

    @GetMapping("/qtd-alunos/{qtdAlunos}")
    public ResponseEntity<?> getTurmasByQtdAlunos(@PathVariable Long qtdAlunos) {
        try {
            List<Turma> turmas = turmaService.getTurmasByQtdAlunos(qtdAlunos);
            return turmas.isEmpty() ? ResponseEntity.status(404).body("Nenhuma turma encontrada com essa quantidade de alunos.") : ResponseEntity.ok(turmas);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao buscar turmas por quantidade de alunos.");
        }
    }


    @PostMapping
    public ResponseEntity<?> createTurma(@RequestBody Turma turma) {
        try {
            if (turma == null || turma.getProfessorResponsavel() == null) { // Substitua 'getNome()' pelo campo necessário para validação
                return ResponseEntity.badRequest().body("Dados inválidos para criação da turma.");
            }

            Turma savedTurma = turmaService.saveTurma(turma);
            return ResponseEntity.ok(savedTurma);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao criar turma.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTurma(@PathVariable Long id) {
        try {
            if (turmaService.getTurmaById(id).isPresent()) {
                turmaService.deleteTurma(id);
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.status(404).body("Turma não encontrada.");
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao deletar turma.");
        }
    }
}
