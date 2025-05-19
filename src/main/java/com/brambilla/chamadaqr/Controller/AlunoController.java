package com.brambilla.chamadaqr.Controller;

import com.brambilla.chamadaqr.Entity.Aluno;
import com.brambilla.chamadaqr.Entity.Chamada;
import com.brambilla.chamadaqr.Service.AlunoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/alunos")
@CrossOrigin(origins = "*")
public class AlunoController {

    @Autowired
    private AlunoService alunoService;

    @GetMapping("/findAll")
    public ResponseEntity<?> getAllAlunos() {
        List<Aluno> alunos = alunoService.getAllAlunos();
        return ResponseEntity.ok(alunos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAlunoById(@PathVariable Long id) {
        Optional<Aluno> aluno = alunoService.getAlunoById(id);
        if (aluno.isEmpty()) {
            return ResponseEntity.badRequest().body("Aluno não encontrado.");
        }
        return ResponseEntity.ok(aluno);
    }

    @GetMapping("/ByNivelAlerta")
    public ResponseEntity<?> findByAlertLevel(@RequestBody Integer nivel){
        List<Aluno> aluno = alunoService.findByAlertLevel(nivel);

        return ResponseEntity.ok(aluno);
    }
    @GetMapping("/exist/{id}")
    public ResponseEntity<?> findByAlertLevel(@RequestParam Long ra){
        // Verifica se o aluno existe
        boolean aluno = alunoService.existAluno(ra);

        if(aluno){
            return ResponseEntity.ok(aluno);
        }

        return (ResponseEntity<?>) ResponseEntity.notFound();
    }

    @PostMapping("/save")
    @PreAuthorize("hasRole('PROFESSOR')")
    public ResponseEntity<?> createAluno(@RequestBody Aluno aluno) {
        if (alunoService.existsByRa(aluno.getRa())) {
            return ResponseEntity.badRequest().body("RA já existe meu amigo. Fala com a secretaria que deu caquinha.");
        }
        Aluno novoAluno = alunoService.saveAluno(aluno);
        return ResponseEntity.ok(novoAluno);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('PROFESSOR')")
    public ResponseEntity<?> deleteAluno(@PathVariable Long id) {
            alunoService.deleteAluno(id);
            return ResponseEntity.noContent().build();
    }


}
