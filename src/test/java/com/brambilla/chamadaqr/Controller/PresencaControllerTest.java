package com.brambilla.chamadaqr.Controller;

import com.brambilla.chamadaqr.Entity.Presenca;
import com.brambilla.chamadaqr.Service.PresencaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PresencaControllerTest {

    private PresencaController controller;
    private PresencaService service;

    @BeforeEach
    void setUp() {
        service = mock(PresencaService.class);
        controller = new PresencaController();
        ReflectionTestUtils.setField(controller, "presencaService", service);
    }

    private Presenca buildPresenca() {
        Presenca p = new Presenca();
        p.setId(50L);
        // apenas IDs para aluno e chamada
        var aluno = new com.brambilla.chamadaqr.Entity.Aluno();
        aluno.setId(1L);
        p.setAluno(aluno);
        var chamada = new com.brambilla.chamadaqr.Entity.Chamada();
        chamada.setId(2L);
        p.setChamada(chamada);
        p.setPresente(true);
        return p;
    }

    @Test
    @DisplayName("GET /presenca → 200 + lista de presenças")
    void testGetAllPresencas() {
        Presenca p = buildPresenca();
        when(service.getAllPresencas()).thenReturn(List.of(p));

        ResponseEntity<List<Presenca>> resp = controller.getAllPresencas();
        assertEquals(200, resp.getStatusCodeValue());
        List<Presenca> body = resp.getBody();
        assertNotNull(body);
        assertEquals(1, body.size());
        assertEquals(50L, body.get(0).getId());
    }

    @Test
    @DisplayName("GET /presenca/{id} → 200 quando existe")
    void testGetByIdFound() {
        Presenca p = buildPresenca();
        when(service.getPresencaById(50L)).thenReturn(Optional.of(p));

        ResponseEntity<Presenca> resp = controller.getPresencaById(50L);
        assertEquals(200, resp.getStatusCodeValue());
        assertEquals(50L, resp.getBody().getId());
    }

    @Test
    @DisplayName("GET /presenca/{id} → 404 quando não existe")
    void testGetByIdNotFound() {
        when(service.getPresencaById(99L)).thenReturn(Optional.empty());

        ResponseEntity<Presenca> resp = controller.getPresencaById(99L);
        assertEquals(404, resp.getStatusCodeValue());
        assertNull(resp.getBody());
    }

    @Test
    @DisplayName("GET /presenca/aluno/{ra} → 200 quando há resultados")
    void testGetByAlunoFound() {
        Presenca p = buildPresenca();
        when(service.getPresencasByAlunoRa(123L)).thenReturn(List.of(p));

        ResponseEntity<?> resp = controller.getPresencasByAluno(123L);
        assertEquals(200, resp.getStatusCodeValue());
        @SuppressWarnings("unchecked")
        List<Presenca> list = (List<Presenca>) resp.getBody();
        assertEquals(1, list.size());
    }

    @Test
    @DisplayName("GET /presenca/aluno/{ra} → 404 quando vazio")
    void testGetByAlunoNotFound() {
        when(service.getPresencasByAlunoRa(123L)).thenReturn(List.of());

        ResponseEntity<?> resp = controller.getPresencasByAluno(123L);
        assertEquals(404, resp.getStatusCodeValue());
        assertEquals("Nenhuma presença encontrada para este RA.", resp.getBody());
    }

    @Test
    @DisplayName("GET /presenca/chamada/{id} → 200 quando há resultados")
    void testGetByChamadaFound() {
        Presenca p = buildPresenca();
        when(service.getPresencasByChamadaId(2L)).thenReturn(List.of(p));

        ResponseEntity<?> resp = controller.getPresencasByChamada(2L);
        assertEquals(200, resp.getStatusCodeValue());
        @SuppressWarnings("unchecked")
        List<Presenca> list = (List<Presenca>) resp.getBody();
        assertEquals(1, list.size());
    }

    @Test
    @DisplayName("GET /presenca/chamada/{id} → 404 quando vazio")
    void testGetByChamadaNotFound() {
        when(service.getPresencasByChamadaId(2L)).thenReturn(List.of());

        ResponseEntity<?> resp = controller.getPresencasByChamada(2L);
        assertEquals(404, resp.getStatusCodeValue());
        assertEquals("Nenhuma presença encontrada para esta chamada.", resp.getBody());
    }

    @Test
    @DisplayName("POST /presenca → 200 e retorna entidade salva")
    void testCreatePresenca() {
        Presenca p = buildPresenca();
        when(service.savePresenca(p)).thenReturn(p);

        ResponseEntity<?> resp = controller.createPresenca(p);
        assertEquals(200, resp.getStatusCodeValue());
        assertSame(p, resp.getBody());
    }

    @Test
    @DisplayName("DELETE /presenca/{id} → 204 quando existe")
    void testDeleteWhenExists() {
        Presenca p = buildPresenca();
        when(service.getPresencaById(50L)).thenReturn(Optional.of(p));
        doNothing().when(service).deletePresenca(50L);

        ResponseEntity<Void> resp = controller.deletePresenca(50L);
        assertEquals(204, resp.getStatusCodeValue());
        assertNull(resp.getBody());
        verify(service).deletePresenca(50L);
    }

    @Test
    @DisplayName("DELETE /presenca/{id} → 404 quando não existe")
    void testDeleteWhenNotExists() {
        when(service.getPresencaById(99L)).thenReturn(Optional.empty());

        ResponseEntity<Void> resp = controller.deletePresenca(99L);
        assertEquals(404, resp.getStatusCodeValue());
        assertNull(resp.getBody());
        verify(service, never()).deletePresenca(anyLong());
    }
}
