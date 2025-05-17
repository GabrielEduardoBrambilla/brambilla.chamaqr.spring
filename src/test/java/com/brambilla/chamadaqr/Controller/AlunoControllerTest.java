package com.brambilla.chamadaqr.Controller;


import com.brambilla.chamadaqr.Entity.Aluno;
import com.brambilla.chamadaqr.Service.AlunoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AlunoController.class)
public class AlunoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AlunoService alunoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /alunos/findAll - retorna lista de alunos")
    void getAllAlunos_shouldReturnList() throws Exception {
        Aluno aluno1 = new Aluno();
        aluno1.setId(1L);
        aluno1.setNome("João");
        aluno1.setRa(123L);
        aluno1.setSenha("123456");

        List<Aluno> alunos = Arrays.asList(aluno1);
        Mockito.when(alunoService.getAllAlunos()).thenReturn(alunos);

        mockMvc.perform(get("/alunos/findAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("João"));
    }

    @Test
    @DisplayName("GET /alunos/{id} - retorna aluno existente")
    void getAlunoById_shouldReturnAluno() throws Exception {
        Aluno aluno = new Aluno();
        aluno.setId(1L);
        aluno.setNome("Maria");
        aluno.setRa(456L);
        aluno.setSenha("abcdef");

        Mockito.when(alunoService.getAlunoById(1L)).thenReturn(Optional.of(aluno));

        mockMvc.perform(get("/alunos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Maria")); // Usando Optional no controller
    }

    @Test
    @DisplayName("GET /alunos/{id} - retorna erro se aluno não existir")
    void getAlunoById_shouldReturnNotFound() throws Exception {
        Mockito.when(alunoService.getAlunoById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/alunos/99"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Aluno não encontrado."));
    }

    @Test
    @DisplayName("GET /alunos/ByNivelAlerta - retorna alunos por nível")
    void getByNivelAlerta_shouldReturnAlunos() throws Exception {
        Aluno aluno = new Aluno();
        aluno.setId(2L);
        aluno.setNome("Carlos");
        aluno.setRa(789L);
        aluno.setSenha("senha123");

        Mockito.when(alunoService.findByAlertLevel(2)).thenReturn(List.of(aluno));

        mockMvc.perform(get("/alunos/ByNivelAlerta")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Carlos"));
    }

    @Test
    @DisplayName("POST /alunos/save - salva novo aluno com sucesso")
    void createAluno_shouldSaveAluno() throws Exception {
        Aluno aluno = new Aluno();
        aluno.setId(3L);
        aluno.setNome("Pedro");
        aluno.setRa(111L);
        aluno.setSenha("senha321");

        Mockito.when(alunoService.existsByRa(111L)).thenReturn(false);
        Mockito.when(alunoService.saveAluno(any(Aluno.class))).thenReturn(aluno);

        mockMvc.perform(post("/alunos/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(aluno)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Pedro"));
    }

    @Test
    @DisplayName("POST /alunos/save - retorna erro se RA já existe")
    void createAluno_shouldReturnBadRequestIfRaExists() throws Exception {
        Aluno aluno = new Aluno();
        aluno.setNome("Joana");
        aluno.setRa(222L);
        aluno.setSenha("senha456");

        Mockito.when(alunoService.existsByRa(222L)).thenReturn(true);

        mockMvc.perform(post("/alunos/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(aluno)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("RA já existe meu amigo. Fala com a secretaria que deu caquinha."));
    }

    @Test
    @DisplayName("DELETE /alunos/{id} - deleta aluno com sucesso")
    void deleteAluno_shouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/alunos/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /alunos/exist/{id} - verifica existência por RA")
    void existAluno_shouldReturnTrueOrFalse() throws Exception {
        Mockito.when(alunoService.existAluno(100L)).thenReturn(true);

        mockMvc.perform(get("/alunos/exist/100").param("ra", "100"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}

