package com.brambilla.chamadaqr.Controller;

import com.brambilla.chamadaqr.Entity.Aluno;
import com.brambilla.chamadaqr.Entity.Professor;
import com.brambilla.chamadaqr.Entity.Turma;
import com.brambilla.chamadaqr.Service.AlunoService;
import com.brambilla.chamadaqr.Service.ProfessorService;
import com.brambilla.chamadaqr.Service.TurmaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TurmaControllerTest {

    private TurmaController controller;
    private TurmaService turmaService;
    private ProfessorService professorService;
    private AlunoService alunoService;

    @BeforeEach
    void setUp() {
        turmaService = mock(TurmaService.class);
        professorService = mock(ProfessorService.class);
        alunoService = mock(AlunoService.class);

        controller = new TurmaController();
        ReflectionTestUtils.setField(controller, "turmaService", turmaService);
        ReflectionTestUtils.setField(controller, "professorService", professorService);
        ReflectionTestUtils.setField(controller, "alunoService", alunoService);
    }

    private Turma buildTurma() {
        Turma t = new Turma();
        t.setId(5L);
        t.setCurso("Engenharia");
        t.setQtdAlunos(30L);
        t.setSemestre("2025-1");
        return t;
    }

    private Professor buildProfessor() {
        Professor p = new Professor();
        p.setId(2L);
        p.setNome("Prof A");
        p.setEmail("a@uni");
        p.setSenha("123456");
        return p;
    }

    private Aluno buildAluno(Long id) {
        Aluno a = new Aluno();
        a.setId(id);
        a.setNome("Aluno"+id);
        a.setRa(1000L+id);
        a.setSenha("senha");
        return a;
    }

    @Test
    @DisplayName("PUT update/{id} → 404 se não existe")
    void testUpdateNotFound() {
        when(turmaService.getTurmaById(5L)).thenReturn(Optional.empty());

        ResponseEntity<?> resp = controller.updateTurma(5L, buildTurma());
        assertEquals(HttpStatusCode.valueOf(404), resp.getStatusCode());
        assertEquals("Turma não encontrada.", resp.getBody());
    }

    @Test
    @DisplayName("PUT update/{id} → 400 se professor não encontrado")
    void testUpdateProfessorNotFound() {
        Turma original = buildTurma();
        when(turmaService.getTurmaById(5L)).thenReturn(Optional.of(original));

        // tentativa de setar professor inexistente
        Turma detalhes = new Turma();
        Professor fake = new Professor(); fake.setId(99L);
        detalhes.setProfessorResponsavel(fake);

        when(professorService.getProfessorById(99L)).thenReturn(Optional.empty());

        ResponseEntity<?> resp = controller.updateTurma(5L, detalhes);
        assertEquals(400, resp.getStatusCodeValue());
        assertEquals("Professor responsável não encontrado.", resp.getBody());
    }

    @Test
    @DisplayName("PUT update/{id} → 200 com professor e alunos atualizados")
    void testUpdateSuccess() {
        Turma original = buildTurma();
        when(turmaService.getTurmaById(5L)).thenReturn(Optional.of(original));

        // detalhes com novo curso, professor e lista de alunos
        Turma detalhes = new Turma();
        detalhes.setCurso("Medicina");
        detalhes.setQtdAlunos(20L);
        detalhes.setSemestre("2025-2");

        Professor prof = buildProfessor();
        detalhes.setProfessorResponsavel(prof);
        when(professorService.getProfessorById(2L)).thenReturn(Optional.of(prof));

        Aluno a1 = buildAluno(10L);
        Aluno a2 = buildAluno(11L);
        detalhes.setAlunos(List.of(a1, a2));
        when(alunoService.getAllByIds(List.of(10L,11L))).thenReturn(List.of(a1,a2));

        // salvar devolve exatamente o objeto recebido
        when(turmaService.saveTurma(any(Turma.class))).thenAnswer(inv -> inv.getArgument(0));

        ResponseEntity<?> resp = controller.updateTurma(5L, detalhes);
        assertEquals(200, resp.getStatusCodeValue());

        Turma atualizado = (Turma) resp.getBody();
        assertEquals("Medicina", atualizado.getCurso());
        assertEquals(20L, atualizado.getQtdAlunos());
        assertEquals("2025-2", atualizado.getSemestre());
        assertSame(prof, atualizado.getProfessorResponsavel());
        assertEquals(2, atualizado.getAlunos().size());
    }

    @Test
    @DisplayName("GET /turmas/findAll → 200 + lista")
    void testGetAllTurmas() {
        Turma t = buildTurma();
        when(turmaService.getAllTurmas()).thenReturn(List.of(t));

        ResponseEntity<?> resp = controller.getAllTurmas();
        assertEquals(200, resp.getStatusCodeValue());
        @SuppressWarnings("unchecked")
        List<Turma> list = (List<Turma>) resp.getBody();
        assertEquals(1, list.size());
    }

    @Test
    @DisplayName("GET /turmas/{id} → 200 quando existe")
    void testGetByIdFound() {
        Turma t = buildTurma();
        when(turmaService.getTurmaById(5L)).thenReturn(Optional.of(t));

        ResponseEntity<?> resp = controller.getTurmaById(5L);
        assertEquals(200, resp.getStatusCodeValue());
        Optional<?> opt = (Optional<?>) resp.getBody();
        assertTrue(opt.isPresent());
        assertEquals(t, opt.get());
    }

    @Test
    @DisplayName("GET /turmas/{id} → 404 quando não existe")
    void testGetByIdNotFound() {
        when(turmaService.getTurmaById(5L)).thenReturn(Optional.empty());

        ResponseEntity<?> resp = controller.getTurmaById(5L);
        assertEquals(404, resp.getStatusCodeValue());
        assertEquals("Turma não encontrada.", resp.getBody());
    }

    @Test
    @DisplayName("GET /turmas/curso/{curso} → 200 quando há resultados")
    void testGetByCursoFound() {
        Turma t = buildTurma();
        when(turmaService.getTurmasByCurso("Engenharia")).thenReturn(List.of(t));

        ResponseEntity<?> resp = controller.getTurmasByCurso("Engenharia");
        assertEquals(200, resp.getStatusCodeValue());
        @SuppressWarnings("unchecked")
        List<Turma> list = (List<Turma>) resp.getBody();
        assertEquals(1, list.size());
    }

    @Test
    @DisplayName("GET /turmas/curso/{curso} → 404 quando vazio")
    void testGetByCursoNotFound() {
        when(turmaService.getTurmasByCurso("X")).thenReturn(List.of());

        ResponseEntity<?> resp = controller.getTurmasByCurso("X");
        assertEquals(404, resp.getStatusCodeValue());
        assertEquals("Nenhuma turma encontrada para este curso.", resp.getBody());
    }

    @Test
    @DisplayName("GET /turmas/qtdalunos/{qtd} → 200 quando há resultados")
    void testGetByQtdAlunosFound() {
        Turma t = buildTurma();
        when(turmaService.getTurmasByQtdAlunos(30L)).thenReturn(List.of(t));

        ResponseEntity<?> resp = controller.getTurmasByQtdAlunos(30L);
        assertEquals(200, resp.getStatusCodeValue());
        @SuppressWarnings("unchecked")
        List<Turma> list = (List<Turma>) resp.getBody();
        assertEquals(1, list.size());
    }

    @Test
    @DisplayName("GET /turmas/qtdalunos/{qtd} → 404 quando vazio")
    void testGetByQtdAlunosNotFound() {
        when(turmaService.getTurmasByQtdAlunos(0L)).thenReturn(List.of());

        ResponseEntity<?> resp = controller.getTurmasByQtdAlunos(0L);
        assertEquals(404, resp.getStatusCodeValue());
        assertEquals("Nenhuma turma encontrada com essa quantidade de alunos.", resp.getBody());
    }

    @Test
    @DisplayName("POST /turmas/save → 200 e retorna criada")
    void testCreateTurma() {
        Turma t = buildTurma();
        when(turmaService.saveTurma(t)).thenReturn(t);

        ResponseEntity<?> resp = controller.createTurma(t);
        assertEquals(200, resp.getStatusCodeValue());
        assertSame(t, resp.getBody());
    }

    @Test
    @DisplayName("DELETE deleteById/{id} → 204 quando existe")
    void testDeleteWhenExists() {
        when(turmaService.getTurmaById(5L)).thenReturn(Optional.of(buildTurma()));
        doNothing().when(turmaService).deleteTurma(5L);

        ResponseEntity<?> resp = controller.deleteTurma(5L);
        assertEquals(204, resp.getStatusCodeValue());
    }

    @Test
    @DisplayName("DELETE deleteById/{id} → 404 quando não existe")
    void testDeleteNotFound() {
        when(turmaService.getTurmaById(5L)).thenReturn(Optional.empty());

        ResponseEntity<?> resp = controller.deleteTurma(5L);
        assertEquals(404, resp.getStatusCodeValue());
        assertEquals("Turma não encontrada.", resp.getBody());
    }
}
