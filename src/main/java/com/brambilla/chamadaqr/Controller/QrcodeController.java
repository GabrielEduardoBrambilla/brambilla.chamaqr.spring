package com.brambilla.chamadaqr.Controller;

import com.brambilla.chamadaqr.Entity.Qrcode;
import com.brambilla.chamadaqr.Service.QrcodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/qrcode")
@CrossOrigin(origins = "*")
public class QrcodeController {

    @Autowired
    private QrcodeService qrCodeService;

    @GetMapping
    public ResponseEntity<?> getAllQRCodes() {
        List<Qrcode> qrCodes = qrCodeService.getAllQRCodes();
        return ResponseEntity.ok(qrCodes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getQrcodeById(@PathVariable Long id) {
        Optional<Qrcode> qrCode = qrCodeService.getQRCodeById(id);
        if (qrCode.isEmpty()) {
            return ResponseEntity.status(404).body("QR Code não encontrado.");
        }
        return ResponseEntity.ok(qrCode);
    }

    @GetMapping("/createdAt/{createdAt}")
    @PreAuthorize("hasRole('PROFESSOR')")
    public ResponseEntity<?> getQRCodeByCreatedAt(@PathVariable String createdAt) {
        List<Qrcode> qrCodes = qrCodeService.getQRCodeByCreatedAt(createdAt);

        return qrCodes.isEmpty() ? ResponseEntity.status(404).body("Nenhum QR Code encontrado.") : ResponseEntity.ok(qrCodes);
    }

    @GetMapping("/hash/{hash}")
    public ResponseEntity<?> getQRCodeByHash(@PathVariable String hash) {
        Optional<Qrcode> qrCode = qrCodeService.getQRCodeByHash(hash);
        if (qrCode.isEmpty()) {
            return ResponseEntity.status(404).body("QR Code não encontrado pelo hash.");
        }

        return ResponseEntity.ok(qrCode);
    }


    @PostMapping
    @PreAuthorize("hasRole('PROFESSOR')")
    public ResponseEntity<?> createQrcode(@RequestBody Qrcode qrCode) {
        if (qrCode == null || qrCode.getHash() == null) {
            return ResponseEntity.badRequest().body("Dados inválidos para criação do QR Code.");
        }

        Qrcode savedQrCode = qrCodeService.saveQRCode(qrCode);
        return ResponseEntity.ok(savedQrCode);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('PROFESSOR')")
    public ResponseEntity<?> deleteQRCode(@PathVariable Long id) {
        if (qrCodeService.getQRCodeById(id).isPresent()) {
            qrCodeService.deleteQRCode(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(404).body("QR Code não encontrado.");
        }
    }
}
