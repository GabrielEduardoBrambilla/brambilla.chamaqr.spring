package com.brambilla.chamadaqr.Controller;

import com.brambilla.chamadaqr.Entity.Presenca;
import com.brambilla.chamadaqr.Service.PresencaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/presenca")
@CrossOrigin(origins = "*")
public class PresencaController {

    @Autowired
    private PresencaService presencaService;

    @GetMapping
    public ResponseEntity<List<Presenca>> getAllPresencas() {
        List<Presenca> presencas = presencaService.getAllPresencas();
        return ResponseEntity.ok(presencas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Presenca> getPresencaById(@PathVariable Long id) {
        Optional<Presenca> presenca = presencaService.getPresencaById(id);
        return presenca.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/aluno/{ra}")
    public ResponseEntity<?> getPresencasByAluno(@PathVariable Long ra) {

        List<Presenca> presencas = presencaService.getPresencasByAlunoRa(ra);
        return presencas.isEmpty() ? ResponseEntity.status(404).body("Nenhuma presença encontrada para este RA.") : ResponseEntity.ok(presencas);
    }

    @GetMapping("/chamada/{id}")
    public ResponseEntity<?> getPresencasByChamada(@PathVariable Long id) {

        List<Presenca> presencas = presencaService.getPresencasByChamadaId(id);
        return presencas.isEmpty() ? ResponseEntity.status(404).body("Nenhuma presença encontrada para esta chamada.") : ResponseEntity.ok(presencas);
    }


    @PostMapping
    public ResponseEntity<?> createPresenca(@RequestBody Presenca presenca) {

        Presenca savedPresenca = presencaService.savePresenca(presenca);
        return ResponseEntity.ok(savedPresenca);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePresenca(@PathVariable Long id) {
        if (presencaService.getPresencaById(id).isPresent()) {
            presencaService.deletePresenca(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
