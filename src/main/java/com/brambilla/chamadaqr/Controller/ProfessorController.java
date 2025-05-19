package com.brambilla.chamadaqr.Controller;

import com.brambilla.chamadaqr.Entity.Professor;
import com.brambilla.chamadaqr.Service.ProfessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/professores")
@CrossOrigin(origins = "*")
public class ProfessorController {

    @Autowired
    private ProfessorService professorService;

    @GetMapping("/findAll")
    public ResponseEntity<?> getAllProfessores() {

        List<Professor> professores = professorService.getAllProfessores();
        return ResponseEntity.ok(professores);
    }

    @GetMapping("findById/{id}")
    public ResponseEntity<?> getProfessorById(@PathVariable Long id) {

        Optional<Professor> professor = professorService.getProfessorById(id);
        if (professor.isEmpty()) {
            return ResponseEntity.status(404).body("Professor não encontrado.");
        }
        return ResponseEntity.ok(professor);
    }

    @GetMapping("/nome/{nome}")
    @PreAuthorize("hasRole('PROFESSOR')")
    public ResponseEntity<?> getProfessorByNome(@PathVariable String nome) {

        Optional<Professor> professor = professorService.getProfessorByNome(nome);
        if (professor.isEmpty()) {
            return ResponseEntity.status(404).body("Professor não encontrado pelo nome.");
        }
        return ResponseEntity.ok(professor);
    }


    @GetMapping("/email/{email}")
    public ResponseEntity<?> getProfessorByEmail(@PathVariable String email) {

        Optional<Professor> professor = professorService.getProfessorByEmail(email);
        if (professor.isEmpty()) {
            return ResponseEntity.status(404).body("Professor não encontrado pelo email.");
        }
        return ResponseEntity.ok(professor);
    }


    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('PROFESSOR')")
    public ResponseEntity<?> updateProfessor(@RequestBody Professor professor, @PathVariable Long id) {

        Optional<Professor> p = professorService.getProfessorById(id);
        if(p.isEmpty()){
            return ResponseEntity.status(404).body("Professor não encontrado.");
        }
        Professor savedProfessor = professorService.saveProfessor(professor);
        return ResponseEntity.ok(savedProfessor);
    }

    @PostMapping("/save")
    @PreAuthorize("hasRole('PROFESSOR')")
    public ResponseEntity<?> createProfessor(@RequestBody Professor professor) {

        if (professor == null || professor.getNome() == null || professor.getEmail() == null || professor.getSenha() == null) {
            return ResponseEntity.badRequest().body("Dados do professor são inválidos.");
        }

        Professor savedProfessor = professorService.saveProfessor(professor);
        return ResponseEntity.ok(savedProfessor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProfessor(@PathVariable Long id, @RequestBody Professor professorDetails) {

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
    }

    @DeleteMapping("/deleteById/{id}")
    @PreAuthorize("hasRole('PROFESSOR')")
    public ResponseEntity<?> deleteProfessor(@PathVariable Long id) {

        if (professorService.getProfessorById(id).isPresent()) {
            professorService.deleteProfessor(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(404).body("Professor não encontrado.");
        }
    }
}
