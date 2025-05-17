package com.brambilla.chamadaqr.Controller;

import com.brambilla.chamadaqr.Entity.Professor;
import com.brambilla.chamadaqr.Service.ProfessorService;
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

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProfessorController.class)
public class ProfessorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProfessorService professorService;

    @Autowired
    private ObjectMapper objectMapper;

    private Professor createProfessorMock() {
        Professor p = new Professor();
        p.setId(1L);
        p.setNome("Fulano");
        p.setEmail("fulano@teste.com");
        p.setSenha("123456");
        return p;
    }

    @Test
    @DisplayName("GET /professores/findAll - deve retornar lista de professores")
    void getAllProfessores() throws Exception {
        Mockito.when(professorService.getAllProfessores()).thenReturn(List.of(createProfessorMock()));

        mockMvc.perform(get("/professores/findAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Fulano"));
    }

    @Test
    @DisplayName("GET /professores/findById/{id} - deve retornar professor existente")
    void getProfessorById() throws Exception {
        Mockito.when(professorService.getProfessorById(1L)).thenReturn(Optional.of(createProfessorMock()));

        mockMvc.perform(get("/professores/findById/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Fulano"));
    }

    @Test
    @DisplayName("GET /professores/findById/{id} - deve retornar erro 404")
    void getProfessorByIdNotFound() throws Exception {
        Mockito.when(professorService.getProfessorById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/professores/findById/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Professor não encontrado."));
    }

    @Test
    @DisplayName("GET /professores/nome/{nome} - deve retornar professor pelo nome")
    void getProfessorByNome() throws Exception {
        Mockito.when(professorService.getProfessorByNome("Fulano")).thenReturn(Optional.of(createProfessorMock()));

        mockMvc.perform(get("/professores/nome/Fulano"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("fulano@teste.com"));
    }

    @Test
    @DisplayName("GET /professores/email/{email} - deve retornar professor pelo email")
    void getProfessorByEmail() throws Exception {
        Mockito.when(professorService.getProfessorByEmail("fulano@teste.com")).thenReturn(Optional.of(createProfessorMock()));

        mockMvc.perform(get("/professores/email/fulano@teste.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Fulano"));
    }

    @Test
    @DisplayName("POST /professores/save - cria professor com sucesso")
    void createProfessorSuccess() throws Exception {
        Professor p = createProfessorMock();
        Mockito.when(professorService.saveProfessor(any(Professor.class))).thenReturn(p);

        mockMvc.perform(post("/professores/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(p)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Fulano"));
    }

    @Test
    @DisplayName("POST /professores/save - retorna erro se campos obrigatórios estiverem faltando")
    void createProfessorInvalid() throws Exception {
        Professor p = new Professor(); // vazio

        mockMvc.perform(post("/professores/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(p)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Dados do professor são inválidos."));
    }

    @Test
    @DisplayName("PUT /professores/{id} - atualiza professor com sucesso")
    void updateProfessor() throws Exception {
        Professor existing = createProfessorMock();
        Professor updated = createProfessorMock();
        updated.setNome("Atualizado");

        Mockito.when(professorService.getProfessorById(1L)).thenReturn(Optional.of(existing));
        Mockito.when(professorService.saveProfessor(any(Professor.class))).thenReturn(updated);

        mockMvc.perform(put("/professores/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Atualizado"));
    }

    @Test
    @DisplayName("PUT /professores/{id} - retorna erro se professor não encontrado")
    void updateProfessorNotFound() throws Exception {
        Mockito.when(professorService.getProfessorById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/professores/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createProfessorMock())))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Professor não encontrado."));
    }

    @Test
    @DisplayName("DELETE /professores/deleteById/{id} - remove professor com sucesso")
    void deleteProfessor() throws Exception {
        Mockito.when(professorService.getProfessorById(1L)).thenReturn(Optional.of(createProfessorMock()));
        Mockito.doNothing().when(professorService).deleteProfessor(1L);

        mockMvc.perform(delete("/professores/deleteById/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /professores/deleteById/{id} - retorna erro se professor não encontrado")
    void deleteProfessorNotFound() throws Exception {
        Mockito.when(professorService.getProfessorById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/professores/deleteById/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Professor não encontrado."));
    }
}
