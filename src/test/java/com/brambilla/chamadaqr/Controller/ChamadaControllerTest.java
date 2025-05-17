package com.brambilla.chamadaqr.Controller;

import com.brambilla.chamadaqr.Entity.Chamada;
import com.brambilla.chamadaqr.Entity.Turma;
import com.brambilla.chamadaqr.Service.ChamadaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ChamadaController.class)
public class ChamadaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChamadaService chamadaService;

    @Autowired
    private ObjectMapper objectMapper;

    private Chamada createChamadaMock() {
        Chamada chamada = new Chamada();
        chamada.setId(1L);
        chamada.setCreatedAt(LocalDateTime.now());
        chamada.setQtdQrs(3L);
        chamada.setTurma(new Turma());
        return chamada;
    }

    @Test
    @DisplayName("GET /chamadas - retorna todas as chamadas")
    void getAllChamadas() throws Exception {
        Chamada chamada = createChamadaMock();
        Mockito.when(chamadaService.getAllChamadas()).thenReturn(List.of(chamada));

        mockMvc.perform(get("/chamadas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    @DisplayName("GET /chamadas/findByTurmaId/{id} - retorna chamadas por turma")
    void getChamadasByTurmaId() throws Exception {
        Chamada chamada = createChamadaMock();
        Mockito.when(chamadaService.findChamadasByTurmaId(1L)).thenReturn(List.of(chamada));

        mockMvc.perform(get("/chamadas/findByTurmaId/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    @DisplayName("GET /chamadas/{id} - retorna chamada por id")
    void getChamadaById() throws Exception {
        Chamada chamada = createChamadaMock();
        Mockito.when(chamadaService.getChamadaById(1L)).thenReturn(Optional.of(chamada));

        mockMvc.perform(get("/chamadas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @DisplayName("GET /chamadas/{id} - retorna 404 se chamada não encontrada")
    void getChamadaByIdNotFound() throws Exception {
        Mockito.when(chamadaService.getChamadaById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/chamadas/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /chamadas/qtd/{qtdQrs} - retorna chamadas por qtdQrs")
    void getChamadasByQtdQrs() throws Exception {
        Chamada chamada = createChamadaMock();
        Mockito.when(chamadaService.getChamadasByQtdQrs(3L)).thenReturn(List.of(chamada));

        mockMvc.perform(get("/chamadas/qtd/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].qtdQrs").value(3L));
    }

    @Test
    @DisplayName("GET /chamadas/qtd/{qtdQrs} - retorna 404 se nenhuma chamada encontrada")
    void getChamadasByQtdQrsNotFound() throws Exception {
        Mockito.when(chamadaService.getChamadasByQtdQrs(0L)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/chamadas/qtd/0"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Nenhuma chamada encontrada."));
    }

    @Test
    @DisplayName("GET /chamadas/last-month - retorna chamadas do último mês")
    void getChamadasFromLastMonth() throws Exception {
        Chamada chamada = createChamadaMock();
        Mockito.when(chamadaService.getChamadasFromLastMonth()).thenReturn(List.of(chamada));

        mockMvc.perform(get("/chamadas/last-month"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    @DisplayName("GET /chamadas/last-month - retorna 404 se nenhuma chamada encontrada")
    void getChamadasFromLastMonthEmpty() throws Exception {
        Mockito.when(chamadaService.getChamadasFromLastMonth()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/chamadas/last-month"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Nenhuma chamada do último mês encontrada."));
    }

    @Test
    @DisplayName("POST /chamadas/save - cria chamada com sucesso")
    void createChamadaSuccess() throws Exception {
        Chamada chamada = createChamadaMock();
        Mockito.when(chamadaService.saveChamada(any(Chamada.class))).thenReturn(chamada);

        mockMvc.perform(post("/chamadas/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(chamada)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @DisplayName("POST /chamadas/save - retorna erro se corpo estiver ausente")
    void createChamadaInvalid() throws Exception {
        mockMvc.perform(post("/chamadas/save")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Required request body is missing")));
    }

    @Test
    @DisplayName("DELETE /chamadas/{id} - deleta chamada com sucesso")
    void deleteChamada() throws Exception {
        doNothing().when(chamadaService).deleteChamada(1L);

        mockMvc.perform(delete("/chamadas/1"))
                .andExpect(status().isNoContent());
    }
}
