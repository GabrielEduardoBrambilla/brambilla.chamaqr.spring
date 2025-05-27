package com.brambilla.chamadaqr.Controller;

import com.brambilla.chamadaqr.Entity.Professor;
import com.brambilla.chamadaqr.Service.ProfessorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProfessorControllerTest {

    private ProfessorController controller;
    private ProfessorService service;

    @BeforeEach
    void setUp() {
        service = mock(ProfessorService.class);
        controller = new ProfessorController();
        ReflectionTestUtils.setField(controller, "professorService", service);
    }

    private Professor buildProfessor() {
        Professor p = new Professor();
        p.setId(10L);
        p.setNome("Maria Silva");
        p.setEmail("maria@uni.edu");
        p.setSenha("segredo");
        return p;
    }

    @Test
    @DisplayName("GET /professores/findAll → 200 + lista de professores")
    void testGetAllProfessores() {
        Professor p = buildProfessor();
        when(service.getAllProfessores()).thenReturn(List.of(p));

        ResponseEntity<?> resp = controller.getAllProfessores();
        assertEquals(200, resp.getStatusCodeValue());
        @SuppressWarnings("unchecked")
        List<Professor> list = (List<Professor>) resp.getBody();
        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals("Maria Silva", list.get(0).getNome());
    }

    @Test
    @DisplayName("GET /professores/findById/{id} → 200 quando existe")
    void testGetByIdFound() {
        Professor p = buildProfessor();
        when(service.getProfessorById(10L)).thenReturn(Optional.of(p));

        ResponseEntity<?> resp = controller.getProfessorById(10L);
        assertEquals(200, resp.getStatusCodeValue());
        // controller retorna Optional dentro do body
        Optional<?> opt = (Optional<?>) resp.getBody();
        assertTrue(opt.isPresent());
        assertEquals(p, opt.get());
    }

    @Test
    @DisplayName("GET /professores/findById/{id} → 404 quando não existe")
    void testGetByIdNotFound() {
        when(service.getProfessorById(99L)).thenReturn(Optional.empty());

        ResponseEntity<?> resp = controller.getProfessorById(99L);
        assertEquals(404, resp.getStatusCodeValue());
        assertEquals("Professor não encontrado.", resp.getBody());
    }

    @Test
    @DisplayName("GET /professores/nome/{nome} → 200 quando existe")
    void testGetByNomeFound() {
        Professor p = buildProfessor();
        when(service.getProfessorByNome("Maria Silva")).thenReturn(Optional.of(p));

        ResponseEntity<?> resp = controller.getProfessorByNome("Maria Silva");
        assertEquals(200, resp.getStatusCodeValue());
        Optional<?> opt = (Optional<?>) resp.getBody();
        assertTrue(opt.isPresent());
        assertEquals(p, opt.get());
    }

    @Test
    @DisplayName("GET /professores/nome/{nome} → 404 quando não existe")
    void testGetByNomeNotFound() {
        when(service.getProfessorByNome("NãoExiste")).thenReturn(Optional.empty());

        ResponseEntity<?> resp = controller.getProfessorByNome("NãoExiste");
        assertEquals(404, resp.getStatusCodeValue());
        assertEquals("Professor não encontrado pelo nome.", resp.getBody());
    }

    @Test
    @DisplayName("GET /professores/email/{email} → 200 quando existe")
    void testGetByEmailFound() {
        Professor p = buildProfessor();
        when(service.getProfessorByEmail("maria@uni.edu")).thenReturn(Optional.of(p));

        ResponseEntity<?> resp = controller.getProfessorByEmail("maria@uni.edu");
        assertEquals(200, resp.getStatusCodeValue());
        Optional<?> opt = (Optional<?>) resp.getBody();
        assertTrue(opt.isPresent());
        assertEquals(p, opt.get());
    }

    @Test
    @DisplayName("GET /professores/email/{email} → 404 quando não existe")
    void testGetByEmailNotFound() {
        when(service.getProfessorByEmail("x@x.com")).thenReturn(Optional.empty());

        ResponseEntity<?> resp = controller.getProfessorByEmail("x@x.com");
        assertEquals(404, resp.getStatusCodeValue());
        assertEquals("Professor não encontrado pelo email.", resp.getBody());
    }

    @Test
    @DisplayName("POST /professores/save → 400 quando corpo inválido")
    void testCreateProfessorBadRequest() {
        // corpo nulo
        ResponseEntity<?> resp1 = controller.createProfessor(null);
        assertEquals(400, resp1.getStatusCodeValue());
        assertEquals("Dados do professor são inválidos.", resp1.getBody());

        // faltando campos
        Professor incomplete = new Professor();
        ResponseEntity<?> resp2 = controller.createProfessor(incomplete);
        assertEquals(400, resp2.getStatusCodeValue());
    }

    @Test
    @DisplayName("POST /professores/save → 200 e retorna entidade salva")
    void testCreateProfessorSuccess() {
        Professor p = buildProfessor();
        when(service.saveProfessor(p)).thenReturn(p);

        ResponseEntity<?> resp = controller.createProfessor(p);
        assertEquals(200, resp.getStatusCodeValue());
        assertSame(p, resp.getBody());
    }

    @Test
    @DisplayName("PUT /professores/update/{id} → 404 quando não existe")
    void testUpdateNotFoundFirstVariant() {
        when(service.getProfessorById(5L)).thenReturn(Optional.empty());

        ResponseEntity<?> resp = controller.updateProfessor(new Professor(), 5L);
        assertEquals(404, resp.getStatusCodeValue());
        assertEquals("Professor não encontrado.", resp.getBody());
    }

    @Test
    @DisplayName("PUT /professores/update/{id} → 200 e salva")
    void testUpdateSuccessFirstVariant() {
        Professor existing = buildProfessor();
        Professor updated = buildProfessor();
        updated.setNome("Novo Nome");

        when(service.getProfessorById(10L)).thenReturn(Optional.of(existing));
        when(service.saveProfessor(updated)).thenReturn(updated);

        ResponseEntity<?> resp = controller.updateProfessor(updated, 10L);
        assertEquals(200, resp.getStatusCodeValue());
        assertEquals("Novo Nome", ((Professor) resp.getBody()).getNome());
    }

    @Test
    @DisplayName("PUT /professores/{id} → 404 quando não existe (segunda variante)")
    void testUpdateNotFoundSecondVariant() {
        when(service.getProfessorById(7L)).thenReturn(Optional.empty());

        ResponseEntity<?> resp = controller.updateProfessor(7L, buildProfessor());
        assertEquals(404, resp.getStatusCodeValue());
        assertEquals("Professor não encontrado.", resp.getBody());
    }

    @Test
    @DisplayName("PUT /professores/{id} → 200 e atualiza (segunda variante)")
    void testUpdateSuccessSecondVariant() {
        Professor existing = buildProfessor();
        Professor details = new Professor();
        details.setNome("Outro Nome");
        details.setEmail("outro@x.com");
        details.setSenha("nova");

        when(service.getProfessorById(10L)).thenReturn(Optional.of(existing));
        when(service.saveProfessor(any(Professor.class))).thenAnswer(inv -> inv.getArgument(0));

        ResponseEntity<?> resp = controller.updateProfessor(10L, details);
        assertEquals(200, resp.getStatusCodeValue());
        Professor body = (Professor) resp.getBody();
        assertEquals("Outro Nome", body.getNome());
        assertEquals("outro@x.com", body.getEmail());
    }

    @Test
    @DisplayName("DELETE /professores/deleteById/{id} → 204 quando existe")
    void testDeleteWhenExists() {
        when(service.getProfessorById(10L)).thenReturn(Optional.of(buildProfessor()));
        doNothing().when(service).deleteProfessor(10L);

        ResponseEntity<?> resp = controller.deleteProfessor(10L);
        assertEquals(204, resp.getStatusCodeValue());
    }

    @Test
    @DisplayName("DELETE /professores/deleteById/{id} → 404 quando não existe")
    void testDeleteWhenNotFound() {
        when(service.getProfessorById(99L)).thenReturn(Optional.empty());

        ResponseEntity<?> resp = controller.deleteProfessor(99L);
        assertEquals(404, resp.getStatusCodeValue());
        assertEquals("Professor não encontrado.", resp.getBody());
    }
}
