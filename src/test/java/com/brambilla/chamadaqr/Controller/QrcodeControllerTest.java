package com.brambilla.chamadaqr.Controller;

import com.brambilla.chamadaqr.Entity.Qrcode;
import com.brambilla.chamadaqr.Service.QrcodeService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(QrcodeController.class)
public class QrcodeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QrcodeService qrCodeService;

    @Autowired
    private ObjectMapper objectMapper;

    private Qrcode createMockQrcode() {
        Qrcode qrcode = new Qrcode();
        qrcode.setId(1L);
        qrcode.setHash("abc123hash");
        qrcode.setCreatedAt("2024-05-01");
        return qrcode;
    }

    @Test
    @DisplayName("GET /qrcode - retorna todos os QR Codes")
    void getAllQRCodes() throws Exception {
        Mockito.when(qrCodeService.getAllQRCodes()).thenReturn(List.of(createMockQrcode()));

        mockMvc.perform(get("/qrcode"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].hash").value("abc123hash"));
    }

    @Test
    @DisplayName("GET /qrcode/{id} - retorna QR Code por ID")
    void getQrcodeById() throws Exception {
        Mockito.when(qrCodeService.getQRCodeById(1L)).thenReturn(Optional.of(createMockQrcode()));

        mockMvc.perform(get("/qrcode/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hash").value("abc123hash"));
    }

    @Test
    @DisplayName("GET /qrcode/{id} - QR Code não encontrado")
    void getQrcodeByIdNotFound() throws Exception {
        Mockito.when(qrCodeService.getQRCodeById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/qrcode/999"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("QR Code não encontrado."));
    }


    @Test
    @DisplayName("GET /qrcode/createdAt/{createdAt} - retorna QR Codes por data")
    void getQRCodeByCreatedAt() throws Exception {
        Mockito.when(qrCodeService.getQRCodeByCreatedAt("2024-05-01"))
                .thenReturn(List.of(createMockQrcode()));

        mockMvc.perform(get("/qrcode/createdAt/2024-05-01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].hash").value("abc123hash"));
    }

    @Test
    @DisplayName("GET /qrcode/createdAt/{createdAt} - nenhum encontrado")
    void getQRCodeByCreatedAtNotFound() throws Exception {
        Mockito.when(qrCodeService.getQRCodeByCreatedAt("2023-01-01"))
                .thenReturn(List.of());

        mockMvc.perform(get("/qrcode/createdAt/2023-01-01"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Nenhum QR Code encontrado."));
    }

    @Test
    @DisplayName("GET /qrcode/hash/{hash} - retorna QR Code por hash")
    void getQRCodeByHash() throws Exception {
        Mockito.when(qrCodeService.getQRCodeByHash("abc123hash"))
                .thenReturn(Optional.of(createMockQrcode()));

        mockMvc.perform(get("/qrcode/hash/abc123hash"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.createdAt").value("2024-05-01"));
    }

    @Test
    @DisplayName("GET /qrcode/hash/{hash} - hash não encontrado")
    void getQRCodeByHashNotFound() throws Exception {
        Mockito.when(qrCodeService.getQRCodeByHash("inexistente"))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/qrcode/hash/inexistente"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("QR Code não encontrado pelo hash."));
    }

    @Test
    @DisplayName("POST /qrcode - cria QR Code com sucesso")
    void createQRCode() throws Exception {
        Qrcode qr = createMockQrcode();
        Mockito.when(qrCodeService.saveQRCode(any(Qrcode.class))).thenReturn(qr);

        mockMvc.perform(post("/qrcode")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(qr)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hash").value("abc123hash"));
    }

    @Test
    @DisplayName("POST /qrcode - dados inválidos")
    void createQRCodeInvalid() throws Exception {
        mockMvc.perform(post("/qrcode")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Dados inválidos para criação do QR Code."));
    }

    @Test
    @DisplayName("DELETE /qrcode/{id} - remove QR Code")
    void deleteQRCodeSuccess() throws Exception {
        Mockito.when(qrCodeService.getQRCodeById(1L)).thenReturn(Optional.of(createMockQrcode()));
        doNothing().when(qrCodeService).deleteQRCode(1L);

        mockMvc.perform(delete("/qrcode/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /qrcode/{id} - QR Code não encontrado")
    void deleteQRCodeNotFound() throws Exception {
        Mockito.when(qrCodeService.getQRCodeById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/qrcode/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("QR Code não encontrado."));
    }
}
