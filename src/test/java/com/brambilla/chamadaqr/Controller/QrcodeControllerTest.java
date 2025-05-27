package com.brambilla.chamadaqr.Controller;

import com.brambilla.chamadaqr.Entity.Qrcode;
import com.brambilla.chamadaqr.Service.QrcodeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class QrcodeControllerTest {

    private QrcodeController controller;
    private QrcodeService service;

    @BeforeEach
    void setUp() {
        service = mock(QrcodeService.class);
        controller = new QrcodeController();
        ReflectionTestUtils.setField(controller, "qrCodeService", service);
    }

    private Qrcode buildQrcode() {
        Qrcode q = new Qrcode();
        q.setId(77L);
        q.setHash("abc123");
        q.setCreatedAt("2025-05-26T20:00");
        return q;
    }

    @Test
    @DisplayName("GET /qrcode → 200 + lista completa")
    void testGetAllQRCodes() {
        Qrcode q = buildQrcode();
        when(service.getAllQRCodes()).thenReturn(List.of(q));

        ResponseEntity<?> resp = controller.getAllQRCodes();
        assertEquals(200, resp.getStatusCodeValue());
        @SuppressWarnings("unchecked")
        List<Qrcode> list = (List<Qrcode>) resp.getBody();
        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals(77L, list.get(0).getId());
    }

    @Test
    @DisplayName("GET /qrcode/{id} → 200 quando existe")
    void testGetByIdFound() {
        Qrcode q = buildQrcode();
        when(service.getQRCodeById(77L)).thenReturn(Optional.of(q));

        ResponseEntity<?> resp = controller.getQrcodeById(77L);
        assertEquals(200, resp.getStatusCodeValue());
        Optional<?> opt = (Optional<?>) resp.getBody();
        assertTrue(opt.isPresent());
        assertEquals(q, opt.get());
    }

    @Test
    @DisplayName("GET /qrcode/{id} → 404 quando não existe")
    void testGetByIdNotFound() {
        when(service.getQRCodeById(1L)).thenReturn(Optional.empty());

        ResponseEntity<?> resp = controller.getQrcodeById(1L);
        assertEquals(404, resp.getStatusCodeValue());
        assertEquals("QR Code não encontrado.", resp.getBody());
    }

    @Test
    @DisplayName("GET /qrcode/createdAt/{data} → 200 quando há resultados")
    void testGetByCreatedAtFound() {
        Qrcode q = buildQrcode();
        when(service.getQRCodeByCreatedAt("2025-05-26T20:00")).thenReturn(List.of(q));

        ResponseEntity<?> resp = controller.getQRCodeByCreatedAt("2025-05-26T20:00");
        assertEquals(200, resp.getStatusCodeValue());
        @SuppressWarnings("unchecked")
        List<Qrcode> list = (List<Qrcode>) resp.getBody();
        assertEquals(1, list.size());
    }

    @Test
    @DisplayName("GET /qrcode/createdAt/{data} → 404 quando vazio")
    void testGetByCreatedAtNotFound() {
        when(service.getQRCodeByCreatedAt("x")).thenReturn(List.of());

        ResponseEntity<?> resp = controller.getQRCodeByCreatedAt("x");
        assertEquals(404, resp.getStatusCodeValue());
        assertEquals("Nenhum QR Code encontrado.", resp.getBody());
    }

    @Test
    @DisplayName("GET /qrcode/hash/{hash} → 200 quando existe")
    void testGetByHashFound() {
        Qrcode q = buildQrcode();
        when(service.getQRCodeByHash("abc123")).thenReturn(Optional.of(q));

        ResponseEntity<?> resp = controller.getQRCodeByHash("abc123");
        assertEquals(200, resp.getStatusCodeValue());
        Optional<?> opt = (Optional<?>) resp.getBody();
        assertTrue(opt.isPresent());
        assertEquals(q, opt.get());
    }

    @Test
    @DisplayName("GET /qrcode/hash/{hash} → 404 quando não existe")
    void testGetByHashNotFound() {
        when(service.getQRCodeByHash("zzz")).thenReturn(Optional.empty());

        ResponseEntity<?> resp = controller.getQRCodeByHash("zzz");
        assertEquals(404, resp.getStatusCodeValue());
        assertEquals("QR Code não encontrado pelo hash.", resp.getBody());
    }

    @Test
    @DisplayName("POST /qrcode → 400 quando corpo inválido")
    void testCreateBadRequest() {
        // corpo nulo
        ResponseEntity<?> r1 = controller.createQrcode(null);
        assertEquals(400, r1.getStatusCodeValue());
        assertEquals("Dados inválidos para criação do QR Code.", r1.getBody());

        // hash nulo
        Qrcode incomplete = new Qrcode();
        ResponseEntity<?> r2 = controller.createQrcode(incomplete);
        assertEquals(400, r2.getStatusCodeValue());
    }

    @Test
    @DisplayName("POST /qrcode → 200 e retorna QR salvo")
    void testCreateSuccess() {
        Qrcode q = buildQrcode();
        when(service.saveQRCode(q)).thenReturn(q);

        ResponseEntity<?> resp = controller.createQrcode(q);
        assertEquals(200, resp.getStatusCodeValue());
        assertSame(q, resp.getBody());
    }

    @Test
    @DisplayName("DELETE /qrcode/{id} → 204 quando existe")
    void testDeleteWhenExists() {
        Qrcode q = buildQrcode();
        when(service.getQRCodeById(77L)).thenReturn(Optional.of(q));
        doNothing().when(service).deleteQRCode(77L);

        ResponseEntity<?> resp = controller.deleteQRCode(77L);
        assertEquals(204, resp.getStatusCodeValue());
    }

    @Test
    @DisplayName("DELETE /qrcode/{id} → 404 quando não existe")
    void testDeleteNotFound() {
        when(service.getQRCodeById(99L)).thenReturn(Optional.empty());

        ResponseEntity<?> resp = controller.deleteQRCode(99L);
        assertEquals(404, resp.getStatusCodeValue());
        assertEquals("QR Code não encontrado.", resp.getBody());
    }
}
