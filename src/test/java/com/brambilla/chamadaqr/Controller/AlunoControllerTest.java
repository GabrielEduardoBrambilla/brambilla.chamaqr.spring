package com.brambilla.chamadaqr.Controller;

import com.brambilla.chamadaqr.Entity.Aluno;
import com.brambilla.chamadaqr.Service.AlunoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AlunoControllerTest {

    private AlunoController controller;
    private AlunoService service;

    @BeforeEach
    void setUp() {
        service = mock(AlunoService.class);
        controller = new AlunoController();
        // inject mock into private field
        ReflectionTestUtils.setField(controller, "alunoService", service);
    }

    private Aluno sampleAluno() {
        Aluno a = new Aluno();
        a.setId(1L);
        a.setNome("João");
        a.setRa(12345L);
        a.setSenha("senha123");
        a.setNivelAlerta(2);
        a.setSuspenso(false);
        return a;
    }

    @Test
    @DisplayName("getAllAlunos → 200 + lista")
    void testGetAllAlunos() {
        Aluno aluno = sampleAluno();
        when(service.getAllAlunos()).thenReturn(List.of(aluno));

        ResponseEntity<?> resp = controller.getAllAlunos();
        assertEquals(200, resp.getStatusCodeValue());
        @SuppressWarnings("unchecked")
        List<Aluno> body = (List<Aluno>) resp.getBody();
        assertNotNull(body);
        assertEquals(1, body.size());
        assertEquals("João", body.get(0).getNome());
    }

    @Test
    @DisplayName("getAlunoById (existente) → 200 + aluno")
    void testGetAlunoByIdFound() {
        Aluno aluno = sampleAluno();
        when(service.getAlunoById(1L)).thenReturn(Optional.of(aluno));

        ResponseEntity<?> resp = controller.getAlunoById(1L);
        assertEquals(200, resp.getStatusCodeValue());
        Optional<Aluno> body = (Optional<Aluno>) resp.getBody();
    }

    @Test
    @DisplayName("getAlunoById (não existe) → 400 + mensagem")
    void testGetAlunoByIdNotFound() {
        when(service.getAlunoById(99L)).thenReturn(Optional.empty());

        ResponseEntity<?> resp = controller.getAlunoById(99L);
        assertEquals(400, resp.getStatusCodeValue());
        assertEquals("Aluno não encontrado.", resp.getBody());
    }

    @Test
    @DisplayName("findByAlertLevel → 200 + filtragem")
    void testFindByAlertLevel() {
        Aluno aluno = sampleAluno();
        aluno.setNivelAlerta(1);
        when(service.findByAlertLevel(1)).thenReturn(List.of(aluno));

        ResponseEntity<?> resp = controller.findByAlertLevel(1);
        assertEquals(200, resp.getStatusCodeValue());
        @SuppressWarnings("unchecked")
        List<Aluno> list = (List<Aluno>) resp.getBody();
        assertEquals(1, list.size());
        assertEquals(1, list.get(0).getNivelAlerta());
    }

    @Test
    @DisplayName("existAluno → 200 + true/false")
    void testExistAluno() {
        when(service.existAluno(123L)).thenReturn(true);

        ResponseEntity<?> resp = controller.existAluno(123L);
        assertEquals(200, resp.getStatusCodeValue());
        assertEquals(true, resp.getBody());
    }

    @Test
    @DisplayName("saveAluno (novo RA) → 200 + entidade salva")
    void testSaveAlunoSuccess() {
        Aluno aluno = sampleAluno();
        when(service.existsByRa(aluno.getRa())).thenReturn(false);
        when(service.saveAluno(aluno)).thenReturn(aluno);

        ResponseEntity<Aluno> resp = (ResponseEntity<Aluno>) controller.saveAluno(aluno);
        assertEquals(200, resp.getStatusCodeValue());
        assertEquals("João", resp.getBody().getNome());
    }

    @Test
    @DisplayName("saveAluno (RA duplicado) → 400 + mensagem customizada")
    void testSaveAlunoDuplicateRa() {
        Aluno aluno = sampleAluno();
        when(service.existsByRa(aluno.getRa())).thenReturn(true);

        ResponseEntity<Aluno> resp = (ResponseEntity<Aluno>) controller.saveAluno(aluno);
        assertEquals(400, resp.getStatusCodeValue());
        assertEquals("RA já existe meu amigo. Fala com a secretaria que deu caquinha.", resp.getBody());
    }

    @Test
    @DisplayName("deleteAluno → 204 No Content")
    void testDeleteAluno() {
        // no exception thrown
        doNothing().when(service).deleteAluno(1L);

        ResponseEntity<Void> resp = (ResponseEntity<Void>) controller.deleteAluno(1L);
        assertEquals(204, resp.getStatusCodeValue());
        assertNull(resp.getBody());
        verify(service).deleteAluno(1L);
    }
}