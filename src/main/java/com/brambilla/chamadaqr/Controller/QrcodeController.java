package com.brambilla.chamadaqr.Controller;

import com.brambilla.chamadaqr.Entity.Aluno;
import com.brambilla.chamadaqr.Entity.Qrcode;
import com.brambilla.chamadaqr.Service.AlunoService;
import com.brambilla.chamadaqr.Service.QrcodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/qrcodes")
public class QrcodeController {
    @Autowired
    private QrcodeService qrCodeService;

    @GetMapping
    public ResponseEntity<List<Qrcode>> getAllQRCodes() {
        return ResponseEntity.ok(qrCodeService.getAllQRCodes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Qrcode> getQrcodeById(@PathVariable Long id) {
        return qrCodeService.getQRCodeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Qrcode> createQrcode(@RequestBody Qrcode qrCode) {
        return ResponseEntity.ok(qrCodeService.saveQRCode(qrCode));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQRCode(@PathVariable Long id) {
        qrCodeService.deleteQRCode(id);
        return ResponseEntity.noContent().build();
    }
}
