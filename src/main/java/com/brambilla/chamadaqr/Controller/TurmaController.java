package com.brambilla.chamadaqr.Controller;

import com.brambilla.chamadaqr.Entity.Aluno;
import com.brambilla.chamadaqr.Entity.Professor;
import com.brambilla.chamadaqr.Entity.Turma;
import com.brambilla.chamadaqr.Service.AlunoService;
import com.brambilla.chamadaqr.Service.ProfessorService;
import com.brambilla.chamadaqr.Service.TurmaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/turmas")
@CrossOrigin(origins = "*")
public class TurmaController {

    @Autowired
    private TurmaService turmaService;
    @Autowired
    private ProfessorService professorService;
    @Autowired
    private AlunoService alunoService;

    @PutMapping("update/{id}")
    public ResponseEntity<?> updateTurma(@PathVariable Long id, @RequestBody Turma turmaDetails) {

        Optional<Turma> turmaExist = turmaService.getTurmaById(id);

        if (turmaExist.isPresent()) {
            Turma turma = turmaExist.get();

            turma.setCurso(turmaDetails.getCurso());
            turma.setQtdAlunos(turmaDetails.getQtdAlunos());
            turma.setSemestre(turmaDetails.getSemestre());

            // Handle ProfessorResponsavel relationship
            if (turmaDetails.getProfessorResponsavel() != null) {
                Optional<Professor> professorOpt = professorService.getProfessorById(
                        turmaDetails.getProfessorResponsavel().getId()
                );
                if (professorOpt.isPresent()) {
                    turma.setProfessorResponsavel(professorOpt.get());
                } else {
                    return ResponseEntity.badRequest().body("Professor responsável não encontrado.");
                }
            }

            // Handle Alunos relationship
            if (turmaDetails.getAlunos() != null && !turmaDetails.getAlunos().isEmpty()) {
                List<Aluno> alunos = alunoService.getAllByIds(
                        turmaDetails.getAlunos().stream()
                                .map(Aluno::getId)
                                .toList()
                );
                turma.setAlunos(alunos);
            } else {
                turma.setAlunos(List.of()); // clear if none provided
            }

            Turma updatedTurma = turmaService.saveTurma(turma);
            return ResponseEntity.ok(updatedTurma);
        } else {
            return ResponseEntity.status(404).body("Turma não encontrada.");
        }
    }


    @GetMapping("/findAll")
    public ResponseEntity<?> getAllTurmas() {

        List<Turma> turmas = turmaService.getAllTurmas();
        return ResponseEntity.ok(turmas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTurmaById(@PathVariable Long id) {

        Optional<Turma> turma = turmaService.getTurmaById(id);
        if (turma.isEmpty()) {
            return ResponseEntity.status(404).body("Turma não encontrada.");
        }
        return ResponseEntity.ok(turma);
    }

    @GetMapping("/curso/{curso}")
    public ResponseEntity<?> getTurmasByCurso(@PathVariable String curso) {

        List<Turma> turmas = turmaService.getTurmasByCurso(curso);
        return turmas.isEmpty() ? ResponseEntity.status(404).body("Nenhuma turma encontrada para este curso.") : ResponseEntity.ok(turmas);
    }

    @GetMapping("/qtd-alunos/{qtdAlunos}")
    public ResponseEntity<?> getTurmasByQtdAlunos(@PathVariable Long qtdAlunos) {

        List<Turma> turmas = turmaService.getTurmasByQtdAlunos(qtdAlunos);
        return turmas.isEmpty() ? ResponseEntity.status(404).body("Nenhuma turma encontrada com essa quantidade de alunos.") : ResponseEntity.ok(turmas);
    }


    @PostMapping("/save")
    public ResponseEntity<?> createTurma(@RequestBody Turma turma) {

        Turma savedTurma = turmaService.saveTurma(turma);
        return ResponseEntity.ok(savedTurma);
    }

    @DeleteMapping("deleteById/{id}")
    public ResponseEntity<?> deleteTurma(@PathVariable Long id) {

        if (turmaService.getTurmaById(id).isPresent()) {
            turmaService.deleteTurma(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(404).body("Turma não encontrada.");
        }
    }
}
