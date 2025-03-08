package com.brambilla.chamadaqr.Service;

import com.brambilla.chamadaqr.Entity.Qrcode;
import com.brambilla.chamadaqr.Repository.QrcodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QrcodeService {
    @Autowired
    private QrcodeRepository qrCodeRepository;

    public List<Qrcode> getAllQRCodes() {
        return qrCodeRepository.findAll();
    }

    public Optional<Qrcode> getQRCodeById(Long id) {
        return qrCodeRepository.findById(id);
    }

    public Qrcode saveQRCode(Qrcode qrCode) {
        return qrCodeRepository.save(qrCode);
    }

    public void deleteQRCode(Long id) {
        qrCodeRepository.deleteById(id);
    }
}