package com.brambilla.chamadaqr.Controller;

import com.brambilla.chamadaqr.Entity.Aluno;
import com.brambilla.chamadaqr.Entity.Professor;
import com.brambilla.chamadaqr.Entity.Turma;
import com.brambilla.chamadaqr.Service.AlunoService;
import com.brambilla.chamadaqr.Service.ProfessorService;
import com.brambilla.chamadaqr.Service.TurmaService;
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

@WebMvcTest(TurmaController.class)
public class TurmaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TurmaService turmaService;

    @MockBean
    private ProfessorService professorService;

    @MockBean
    private AlunoService alunoService;

    @Autowired
    private ObjectMapper objectMapper;

    private Turma createMockTurma() {
        Turma turma = new Turma();
        turma.setId(1L);
        turma.setCurso("Engenharia");
        turma.setQtdAlunos(30L);
        turma.setSemestre("2024.1");

        Professor professor = new Professor();
        professor.setId(1L);
        professor.setNome("Prof. João");
        professor.setEmail("joao@exemplo.com");
        professor.setSenha("123456");
        turma.setProfessorResponsavel(professor);

        return turma;
    }

    @Test
    @DisplayName("GET /turmas/findAll - retorna todas as turmas")
    void getAllTurmas() throws Exception {
        Mockito.when(turmaService.getAllTurmas()).thenReturn(List.of(createMockTurma()));

        mockMvc.perform(get("/turmas/findAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].curso").value("Engenharia"));
    }

    @Test
    @DisplayName("GET /turmas/{id} - retorna turma por ID")
    void getTurmaById() throws Exception {
        Mockito.when(turmaService.getTurmaById(1L)).thenReturn(Optional.of(createMockTurma()));

        mockMvc.perform(get("/turmas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.curso").value("Engenharia"));
    }

    @Test
    @DisplayName("GET /turmas/{id} - turma não encontrada")
    void getTurmaByIdNotFound() throws Exception {
        Mockito.when(turmaService.getTurmaById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/turmas/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Turma não encontrada."));
    }

    @Test
    @DisplayName("GET /turmas/curso/{curso} - retorna turmas por curso")
    void getTurmasByCurso() throws Exception {
        Mockito.when(turmaService.getTurmasByCurso("Engenharia")).thenReturn(List.of(createMockTurma()));

        mockMvc.perform(get("/turmas/curso/Engenharia"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].curso").value("Engenharia"));
    }

    @Test
    @DisplayName("GET /turmas/qtd-alunos/{qtdAlunos} - retorna turmas por quantidade de alunos")
    void getTurmasByQtdAlunos() throws Exception {
        Mockito.when(turmaService.getTurmasByQtdAlunos(30L)).thenReturn(List.of(createMockTurma()));

        mockMvc.perform(get("/turmas/qtd-alunos/30"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].qtdAlunos").value(30));
    }

    @Test
    @DisplayName("POST /turmas/save - cria turma com sucesso")
    void createTurma() throws Exception {
        Turma turma = createMockTurma();
        Mockito.when(turmaService.saveTurma(any(Turma.class))).thenReturn(turma);

        mockMvc.perform(post("/turmas/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(turma)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.curso").value("Engenharia"));
    }

    @Test
    @DisplayName("PUT /turmas/update/{id} - atualiza turma com sucesso")
    void updateTurma() throws Exception {
        Turma turma = createMockTurma();
        Professor professor = turma.getProfessorResponsavel();

        Mockito.when(turmaService.getTurmaById(1L)).thenReturn(Optional.of(turma));
        Mockito.when(professorService.getProfessorById(1L)).thenReturn(Optional.of(professor));
        Mockito.when(alunoService.getAllByIds(any())).thenReturn(List.of());
        Mockito.when(turmaService.saveTurma(any())).thenReturn(turma);

        mockMvc.perform(put("/turmas/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(turma)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.curso").value("Engenharia"));
    }

    @Test
    @DisplayName("PUT /turmas/update/{id} - turma não encontrada")
    void updateTurmaNotFound() throws Exception {
        Mockito.when(turmaService.getTurmaById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/turmas/update/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createMockTurma())))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Turma não encontrada."));
    }

    @Test
    @DisplayName("DELETE /turmas/deleteById/{id} - remove turma com sucesso")
    void deleteTurma() throws Exception {
        Mockito.when(turmaService.getTurmaById(1L)).thenReturn(Optional.of(createMockTurma()));
        doNothing().when(turmaService).deleteTurma(1L);

        mockMvc.perform(delete("/turmas/deleteById/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /turmas/deleteById/{id} - turma não encontrada")
    void deleteTurmaNotFound() throws Exception {
        Mockito.when(turmaService.getTurmaById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/turmas/deleteById/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Turma não encontrada."));
    }
}
