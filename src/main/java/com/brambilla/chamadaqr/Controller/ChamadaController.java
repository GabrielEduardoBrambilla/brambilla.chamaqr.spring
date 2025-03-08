package com.brambilla.chamadaqr.Controller;

import com.brambilla.chamadaqr.Entity.Aluno;
import com.brambilla.chamadaqr.Entity.Chamada;
import com.brambilla.chamadaqr.Service.AlunoService;
import com.brambilla.chamadaqr.Service.ChamadaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chamadas")
public class ChamadaController {
    @Autowired
    private ChamadaService chamadaService;

    @GetMapping
    public ResponseEntity<List<Chamada>> getAllChamadas() {
        return ResponseEntity.ok(chamadaService.getAllChamadas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Chamada> getChamadaById(@PathVariable Long id) {
        return chamadaService.getChamadaById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Chamada> createChamada(@RequestBody Chamada chamada) {
        return ResponseEntity.ok(chamadaService.saveChamada(chamada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChamada(@PathVariable Long id) {
        chamadaService.deleteChamada(id);
        return ResponseEntity.noContent().build();
    }
}
