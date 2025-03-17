package com.brambilla.chamadaqr.Controller;

import com.brambilla.chamadaqr.Entity.Professor;
import com.brambilla.chamadaqr.Service.ProfessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/professores")
public class ProfessorController {

    @Autowired
    private ProfessorService professorService;

    @GetMapping
    public ResponseEntity<?> getAllProfessores() {
        try {
            List<Professor> professores = professorService.getAllProfessores();
            return ResponseEntity.ok(professores);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao buscar professores.");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProfessorById(@PathVariable Long id) {
        try {
            Optional<Professor> professor = professorService.getProfessorById(id);
            if(professor.isEmpty()){
                return ResponseEntity.status(404).body("Professor não encontrado.");
            }
            return ResponseEntity.ok(professor);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao buscar professor.");
        }
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<?> getProfessorByNome(@PathVariable String nome) {
        try {
            Optional<Professor> professor = professorService.getProfessorByNome(nome);
            if(professor.isEmpty()){
                return ResponseEntity.status(404).body("Professor não encontrado pelo nome.");
            }
            return ResponseEntity.ok(professor);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao buscar professor pelo nome.");
        }
    }

    // 🔍 Get professor by Email
    @GetMapping("/email/{email}")
    public ResponseEntity<?> getProfessorByEmail(@PathVariable String email) {
        try {
            Optional<Professor> professor = professorService.getProfessorByEmail(email);
            if(professor.isEmpty()){
                return ResponseEntity.status(404).body("Professor não encontrado pelo email.");
            }
            return ResponseEntity.ok(professor);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao buscar professor pelo email.");
        }
    }


    @PostMapping
    public ResponseEntity<?> createProfessor(@RequestBody Professor professor) {
        try {
            if (professor == null || professor.getNome() == null || professor.getEmail() == null || professor.getSenha() == null) {
                return ResponseEntity.badRequest().body("Dados do professor são inválidos.");
            }

            Professor savedProfessor = professorService.saveProfessor(professor);
            return ResponseEntity.ok(savedProfessor);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao criar professor.");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProfessor(@PathVariable Long id, @RequestBody Professor professorDetails) {
        try {
            Optional<Professor> existingProfessor = professorService.getProfessorById(id);

            if (existingProfessor.isPresent()) {
                Professor professor = existingProfessor.get();
                professor.setNome(professorDetails.getNome());
                professor.setEmail(professorDetails.getEmail());
                professor.setSenha(professorDetails.getSenha());

                Professor updatedProfessor = professorService.saveProfessor(professor);
                return ResponseEntity.ok(updatedProfessor);
            } else {
                return ResponseEntity.status(404).body("Professor não encontrado.");
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao atualizar professor.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProfessor(@PathVariable Long id) {
        try {
            if (professorService.getProfessorById(id).isPresent()) {
                professorService.deleteProfessor(id);
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.status(404).body("Professor não encontrado.");
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao deletar professor.");
        }
    }
}
