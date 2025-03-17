package com.brambilla.chamadaqr.Controller;

import com.brambilla.chamadaqr.Entity.Chamada;
import com.brambilla.chamadaqr.Service.ChamadaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/chamadas")
public class ChamadaController {

    @Autowired
    private ChamadaService chamadaService;

    @GetMapping
    public ResponseEntity<List<Chamada>> getAllChamadas() {
        try {
            List<Chamada> chamadas = chamadaService.getAllChamadas();
            return ResponseEntity.ok(chamadas);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Chamada> getChamadaById(@PathVariable Long id) {
        try {
            Optional<Chamada> chamada = chamadaService.getChamadaById(id);
            return chamada.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    @GetMapping("/qtd/{qtdQrs}")
    public ResponseEntity<?> getChamadasByQtdQrs(@PathVariable Long qtdQrs) {
        try {
            List<Chamada> chamadas = chamadaService.getChamadasByQtdQrs(qtdQrs);
            return chamadas.isEmpty() ? ResponseEntity.status(404).body("Nenhuma chamada encontrada.") : ResponseEntity.ok(chamadas);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao buscar chamadas.");
        }
    }

    @GetMapping("/last-month")
    public ResponseEntity<?> getChamadasFromLastMonth() {
        try {
            List<Chamada> chamadas = chamadaService.getChamadasFromLastMonth();
            return chamadas.isEmpty() ? ResponseEntity.status(404).body("Nenhuma chamada do último mês encontrada.") : ResponseEntity.ok(chamadas);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao buscar chamadas do último mês.");
        }
    }
    @PostMapping
    public ResponseEntity<?> createChamada(@RequestBody Chamada chamada) {
        try {
            if (chamada == null) {
                return ResponseEntity.badRequest().body("Os dados da chamada são inválidos.");
            }
            Chamada novaChamada = chamadaService.saveChamada(chamada);
            return ResponseEntity.ok(novaChamada);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao criar chamada.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChamada(@PathVariable Long id) {
        try {
            chamadaService.deleteChamada(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
