package com.brambilla.chamadaqr.Controller;

import com.brambilla.chamadaqr.Entity.Qrcode;
import com.brambilla.chamadaqr.Service.QrcodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/qrcode")
public class QrcodeController {

    @Autowired
    private QrcodeService qrCodeService;

    @GetMapping
    public ResponseEntity<?> getAllQRCodes() {
        try {
            List<Qrcode> qrCodes = qrCodeService.getAllQRCodes();
            return ResponseEntity.ok(qrCodes);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao buscar os QR Codes.");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getQrcodeById(@PathVariable Long id) {
        try {
            Optional<Qrcode> qrCode = qrCodeService.getQRCodeById(id);
            if(qrCode.isEmpty()){
                ResponseEntity.status(404).body("QR Code não encontrado.");
            }
            return ResponseEntity.ok(qrCode);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao buscar o QR Code.");
        }
    }
    @GetMapping("/createdAt/{createdAt}")
    public ResponseEntity<?> getQRCodeByCreatedAt(@PathVariable String createdAt) {
        try {
            List<Qrcode> qrCodes = qrCodeService.getQRCodeByCreatedAt(createdAt);

            return qrCodes.isEmpty() ? ResponseEntity.status(404).body("Nenhum QR Code encontrado.") : ResponseEntity.ok(qrCodes);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao buscar QR Codes.");
        }
    }

    // 🔍 Get QRCode by Hash
    @GetMapping("/hash/{hash}")
    public ResponseEntity<?> getQRCodeByHash(@PathVariable String hash) {
        try {
            Optional<Qrcode> qrCode = qrCodeService.getQRCodeByHash(hash);
            if(qrCode.isEmpty()){
                return ResponseEntity.status(404).body("QR Code não encontrado pelo hash.");
            }

            return ResponseEntity.ok(qrCode);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao buscar QR Code pelo hash.");
        }
    }


    @PostMapping
    public ResponseEntity<?> createQrcode(@RequestBody Qrcode qrCode) {
        try {
            if (qrCode == null || qrCode.getHash() == null) {
                return ResponseEntity.badRequest().body("Dados inválidos para criação do QR Code.");
            }

            Qrcode savedQrCode = qrCodeService.saveQRCode(qrCode);
            return ResponseEntity.ok(savedQrCode);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao criar QR Code.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteQRCode(@PathVariable Long id) {
        try {
            if (qrCodeService.getQRCodeById(id).isPresent()) {
                qrCodeService.deleteQRCode(id);
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.status(404).body("QR Code não encontrado.");
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao deletar QR Code.");
        }
    }
}
