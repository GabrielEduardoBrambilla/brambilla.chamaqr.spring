package com.brambilla.chamadaqr.Controller;

import com.brambilla.chamadaqr.Entity.Aluno;
import com.brambilla.chamadaqr.Entity.Chamada;
import com.brambilla.chamadaqr.Service.AlunoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/alunos")
public class AlunoController {

    @Autowired
    private AlunoService alunoService;

    @GetMapping
    public ResponseEntity<?> getAllAlunos() {
        try {
            List<Aluno> alunos = alunoService.getAllAlunos();
            return ResponseEntity.ok(alunos);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao buscar alunos.");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAlunoById(@PathVariable Long id) {
        try {
            Optional<Aluno> aluno = alunoService.getAlunoById(id);
            if (aluno.isEmpty()) {
                return ResponseEntity.badRequest().body("Aluno não encontrado.");
            }
            return ResponseEntity.ok(aluno);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao buscar aluno.");
        }
    }

    @GetMapping("/ByNivelAlerta")
    public ResponseEntity<?> findByAlertLevel(@RequestBody Integer nivel){
        try {
            List<Aluno> aluno = alunoService.findByAlertLevel(nivel);

            return ResponseEntity.ok(aluno);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao buscar por nivel de alerta.");
        }
    }
    @GetMapping("/exist/{id}")
    public ResponseEntity<?> findByAlertLevel(@RequestParam Long ra){
        try {
            boolean aluno = alunoService.existAluno(ra);

            if(aluno){
                return ResponseEntity.ok(aluno);
            }

            return (ResponseEntity<?>) ResponseEntity.notFound();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao buscar por aluno.");
        }
    }

    @PostMapping
    public ResponseEntity<?> createAluno(@RequestBody Aluno aluno) {
        try {
            if (alunoService.existsByRa(aluno.getRa())) {
                return ResponseEntity.badRequest().body("RA já existe meu amigo. Fala com a secretaria que deu caquinha.");
            }
            Aluno novoAluno = alunoService.saveAluno(aluno);
            return ResponseEntity.ok(novoAluno);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao criar aluno.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAluno(@PathVariable Long id) {
        try {
            alunoService.deleteAluno(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao deletar aluno.");
        }
    }


}
