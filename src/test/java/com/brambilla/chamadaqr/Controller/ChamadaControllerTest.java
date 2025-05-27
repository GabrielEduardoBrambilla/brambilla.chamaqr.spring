package com.brambilla.chamadaqr.Controller;

import com.brambilla.chamadaqr.Entity.Chamada;
import com.brambilla.chamadaqr.Service.ChamadaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ChamadaControllerTest {

    private ChamadaController controller;
    private ChamadaService service;

    @BeforeEach
    void setUp() {
        service = mock(ChamadaService.class);
        controller = new ChamadaController();
        ReflectionTestUtils.setField(controller, "chamadaService", service);
    }

    private Chamada sampleChamada() {
        Chamada c = new Chamada();
        c.setId(42L);
        c.setCreatedAt(LocalDateTime.of(2025,5,1,10,0));
        c.setQtdQrs(5L);
        return c;
    }

    @Test
    @DisplayName("GET /chamadas → 200 + lista completa")
    void testGetAllChamadas() {
        Chamada c = sampleChamada();
        when(service.getAllChamadas()).thenReturn(List.of(c));

        ResponseEntity<List<Chamada>> resp = controller.getAllChamadas();
        assertEquals(200, resp.getStatusCodeValue());
        List<Chamada> body = resp.getBody();
        assertNotNull(body);
        assertEquals(1, body.size());
        assertEquals(42L, body.get(0).getId());
    }

    @Test
    @DisplayName("GET /chamadas/findByTurmaId/{id} → 200 + filtragem por turma")
    void testGetByTurmaId() {
        Chamada c = sampleChamada();
        when(service.findChamadasByTurmaId(7L)).thenReturn(List.of(c));

        ResponseEntity<?> resp = controller.getChamadaByTurmaId(7L);
        assertEquals(200, resp.getStatusCodeValue());
        @SuppressWarnings("unchecked")
        List<Chamada> list = (List<Chamada>) resp.getBody();
        assertEquals(1, list.size());
    }

    @Test
    @DisplayName("GET /chamadas/{id} → 200 + encontrado")
    void testGetByIdFound() {
        Chamada c = sampleChamada();
        when(service.getChamadaById(42L)).thenReturn(Optional.of(c));

        ResponseEntity<Chamada> resp = controller.getChamadaById(42L);
        assertEquals(200, resp.getStatusCodeValue());
        assertEquals(42L, resp.getBody().getId());
    }

    @Test
    @DisplayName("GET /chamadas/{id} → 404 quando não encontrado")
    void testGetByIdNotFound() {
        when(service.getChamadaById(99L)).thenReturn(Optional.empty());

        ResponseEntity<Chamada> resp = controller.getChamadaById(99L);
        assertEquals(404, resp.getStatusCodeValue());
        assertNull(resp.getBody());
    }

    @Test
    @DisplayName("GET /chamadas/qtd/{qtdQrs} → 200 + lista não vazia")
    void testGetByQtdQrsFound() {
        Chamada c = sampleChamada();
        when(service.getChamadasByQtdQrs(5L)).thenReturn(List.of(c));

        ResponseEntity<?> resp = controller.getChamadasByQtdQrs(5L);
        assertEquals(200, resp.getStatusCodeValue());
        @SuppressWarnings("unchecked")
        List<Chamada> list = (List<Chamada>) resp.getBody();
        assertEquals(1, list.size());
    }

    @Test
    @DisplayName("GET /chamadas/qtd/{qtdQrs} → 404 quando lista vazia")
    void testGetByQtdQrsNotFound() {
        when(service.getChamadasByQtdQrs(0L)).thenReturn(List.of());

        ResponseEntity<?> resp = controller.getChamadasByQtdQrs(0L);
        assertEquals(404, resp.getStatusCodeValue());
        assertEquals("Nenhuma chamada encontrada.", resp.getBody());
    }

    @Test
    @DisplayName("GET /chamadas/last-month → 200 + encontrada")
    void testGetLastMonthFound() {
        Chamada c = sampleChamada();
        when(service.getChamadasFromLastMonth()).thenReturn(List.of(c));

        ResponseEntity<?> resp = controller.getChamadasFromLastMonth();
        assertEquals(200, resp.getStatusCodeValue());
        @SuppressWarnings("unchecked")
        List<Chamada> list = (List<Chamada>) resp.getBody();
        assertEquals(1, list.size());
    }

    @Test
    @DisplayName("GET /chamadas/last-month → 404 quando vazia")
    void testGetLastMonthNotFound() {
        when(service.getChamadasFromLastMonth()).thenReturn(List.of());

        ResponseEntity<?> resp = controller.getChamadasFromLastMonth();
        assertEquals(404, resp.getStatusCodeValue());
        assertEquals("Nenhuma chamada do último mês encontrada.", resp.getBody());
    }

    @Test
    @DisplayName("POST /chamadas/save → 200 + cria nova chamada")
    void testCreateChamadaSuccess() {
        Chamada c = sampleChamada();
        when(service.saveChamada(c)).thenReturn(c);

        ResponseEntity<?> resp = controller.createChamada(c);
        assertEquals(200, resp.getStatusCodeValue());
        assertSame(c, resp.getBody());
    }

    @Test
    @DisplayName("POST /chamadas/save → 400 quando body inválido (null)")
    void testCreateChamadaBadRequest() {
        ResponseEntity<?> resp = controller.createChamada(null);
        assertEquals(400, resp.getStatusCodeValue());
        assertEquals("Os dados da chamada são inválidos.", resp.getBody());
    }

    @Test
    @DisplayName("DELETE /chamadas/{id} → 204 No Content")
    void testDeleteChamada() {
        doNothing().when(service).deleteChamada(42L);

        ResponseEntity<Void> resp = controller.deleteChamada(42L);
        assertEquals(204, resp.getStatusCodeValue());
        assertNull(resp.getBody());
        verify(service).deleteChamada(42L);
    }
}
