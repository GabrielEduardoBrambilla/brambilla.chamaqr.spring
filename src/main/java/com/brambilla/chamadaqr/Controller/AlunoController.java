package com.brambilla.chamadaqr.Controller;

import com.brambilla.chamadaqr.Entity.Aluno;
import com.brambilla.chamadaqr.Entity.Turma;
import com.brambilla.chamadaqr.Service.AlunoService;
import com.brambilla.chamadaqr.Service.TurmaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alunos")
public class AlunoController {
    @Autowired
    private AlunoService alunoService;

    @GetMapping
    public ResponseEntity<List<Aluno>> getAllAlunos() {
        return ResponseEntity.ok(alunoService.getAllAlunos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Aluno> getAlunoById(@PathVariable Long id) {
        return alunoService.getAlunoById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createAluno(@RequestBody Aluno aluno) {
        if (alunoService.existsByRa(aluno.getRa())) {
            return ResponseEntity.badRequest().body("RA já existe meu amigo. Fala com a secretaria que deu caquinha.");
        }

        if (aluno.getSenha() == null || aluno.getSenha().length() < 6) {
            return ResponseEntity.badRequest().body("Senha tem q ter pelo menos 6 characters");
        }

        return ResponseEntity.ok(alunoService.saveAluno(aluno));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAluno(@PathVariable Long id) {
        alunoService.deleteAluno(id);
        return ResponseEntity.noContent().build();
    }
}