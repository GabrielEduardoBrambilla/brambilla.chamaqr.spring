package com.brambilla.chamadaqr.Service;

import com.brambilla.chamadaqr.Entity.Qrcode;
import com.brambilla.chamadaqr.Repository.QrcodeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class QrcodeServiceTest {

    @InjectMocks
    private QrcodeService qrcodeService;

    @Mock
    private QrcodeRepository qrcodeRepository;

    private Qrcode qrcode;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        qrcode = new Qrcode();
        qrcode.setId(1L);
        qrcode.setHash("abc123");
        qrcode.setCreatedAt("2024-05-01");
    }

    @Test
    @DisplayName("getAllQRCodes retorna lista com QR Codes")
    void testGetAllQRCodes() {
        when(qrcodeRepository.findAll()).thenReturn(List.of(qrcode));

        List<Qrcode> result = qrcodeService.getAllQRCodes();

        assertEquals(1, result.size());
        assertEquals("abc123", result.get(0).getHash());
        verify(qrcodeRepository).findAll();
    }

    @Test
    @DisplayName("getQRCodeById retorna QR Code existente")
    void testGetQRCodeById_found() {
        when(qrcodeRepository.findById(1L)).thenReturn(Optional.of(qrcode));

        Optional<Qrcode> result = qrcodeService.getQRCodeById(1L);

        assertTrue(result.isPresent());
        assertEquals("abc123", result.get().getHash());
        verify(qrcodeRepository).findById(1L);
    }

    @Test
    @DisplayName("getQRCodeById retorna vazio se não encontrado")
    void testGetQRCodeById_notFound() {
        when(qrcodeRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Qrcode> result = qrcodeService.getQRCodeById(99L);

        assertFalse(result.isPresent());
        verify(qrcodeRepository).findById(99L);
    }

    @Test
    @DisplayName("saveQRCode salva e retorna o QR Code")
    void testSaveQRCode() {
        when(qrcodeRepository.save(qrcode)).thenReturn(qrcode);

        Qrcode saved = qrcodeService.saveQRCode(qrcode);

        assertNotNull(saved);
        assertEquals("abc123", saved.getHash());
        verify(qrcodeRepository).save(qrcode);
    }

    @Test
    @DisplayName("deleteQRCode remove QR Code pelo ID")
    void testDeleteQRCode() {
        doNothing().when(qrcodeRepository).deleteById(1L);

        qrcodeService.deleteQRCode(1L);

        verify(qrcodeRepository).deleteById(1L);
    }

    @Test
    @DisplayName("getQRCodeByCreatedAt retorna lista com QR Codes da data")
    void testGetQRCodeByCreatedAt() {
        when(qrcodeRepository.findByCreatedAt("2024-05-01")).thenReturn(List.of(qrcode));

        List<Qrcode> result = qrcodeService.getQRCodeByCreatedAt("2024-05-01");

        assertEquals(1, result.size());
        assertEquals("abc123", result.get(0).getHash());
        verify(qrcodeRepository).findByCreatedAt("2024-05-01");
    }

    @Test
    @DisplayName("getQRCodeByHash retorna QR Code se encontrado")
    void testGetQRCodeByHash_found() {
        when(qrcodeRepository.findByHash("abc123")).thenReturn(Optional.of(qrcode));

        Optional<Qrcode> result = qrcodeService.getQRCodeByHash("abc123");

        assertTrue(result.isPresent());
        assertEquals("abc123", result.get().getHash());
        verify(qrcodeRepository).findByHash("abc123");
    }

    @Test
    @DisplayName("getQRCodeByHash retorna vazio se não encontrado")
    void testGetQRCodeByHash_notFound() {
        when(qrcodeRepository.findByHash("xyz")).thenReturn(Optional.empty());

        Optional<Qrcode> result = qrcodeService.getQRCodeByHash("xyz");

        assertFalse(result.isPresent());
        verify(qrcodeRepository).findByHash("xyz");
    }
}
