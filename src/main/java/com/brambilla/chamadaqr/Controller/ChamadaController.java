package com.brambilla.chamadaqr.Controller;

import com.brambilla.chamadaqr.Entity.Chamada;
import com.brambilla.chamadaqr.Entity.Turma;
import com.brambilla.chamadaqr.Service.ChamadaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/chamadas")
@CrossOrigin(origins = "*")
public class ChamadaController {

    @Autowired
    private ChamadaService chamadaService;

    @GetMapping
    public ResponseEntity<List<Chamada>> getAllChamadas() {
        List<Chamada> chamadas = chamadaService.getAllChamadas();
        return ResponseEntity.ok(chamadas);

    }

    @GetMapping("/findByTurmaId/{id}")
    public ResponseEntity<?> getChamadaByTurmaId(@PathVariable Long id) {
        List<Chamada> chamada = chamadaService.findChamadasByTurmaId(id);
        return ResponseEntity.ok(chamada);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Chamada> getChamadaById(@PathVariable Long id) {

        Optional<Chamada> chamada = chamadaService.getChamadaById(id);
        return chamada.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/qtd/{qtdQrs}")
    public ResponseEntity<?> getChamadasByQtdQrs(@PathVariable Long qtdQrs) {

        List<Chamada> chamadas = chamadaService.getChamadasByQtdQrs(qtdQrs);
        return chamadas.isEmpty() ? ResponseEntity.status(404).body("Nenhuma chamada encontrada.") : ResponseEntity.ok(chamadas);
    }

    @GetMapping("/last-month")
    public ResponseEntity<?> getChamadasFromLastMonth() {

        List<Chamada> chamadas = chamadaService.getChamadasFromLastMonth();
        return chamadas.isEmpty() ? ResponseEntity.status(404).body("Nenhuma chamada do último mês encontrada.") : ResponseEntity.ok(chamadas);
    }

    @PostMapping("/save")
    public ResponseEntity<?> createChamada(@RequestBody Chamada chamada) {

        if (chamada == null) {
            return ResponseEntity.badRequest().body("Os dados da chamada são inválidos.");
        }
        Chamada novaChamada = chamadaService.saveChamada(chamada);
        return ResponseEntity.ok(novaChamada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChamada(@PathVariable Long id) {

        chamadaService.deleteChamada(id);
        return ResponseEntity.noContent().build();
    }
}
