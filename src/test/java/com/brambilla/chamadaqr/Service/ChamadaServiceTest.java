package com.brambilla.chamadaqr.Service;

import com.brambilla.chamadaqr.Entity.Chamada;
import com.brambilla.chamadaqr.Repository.ChamadaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ChamadaServiceTest {

    @Autowired
    private ChamadaService chamadaService;

    @MockBean
    private ChamadaRepository chamadaRepository;

    @Test
    @DisplayName("Cena 01 - Deve retornar todas as chamadas")
    void cenario01() {
        Chamada c1 = new Chamada();
        Chamada c2 = new Chamada();

        when(chamadaRepository.findAll()).thenReturn(List.of(c1, c2));

        List<Chamada> chamadas = chamadaService.getAllChamadas();
        assertEquals(2, chamadas.size());
    }

    @Test
    @DisplayName("Cena 02 - Deve salvar chamada")
    void cenario02() {
        Chamada chamada = new Chamada();
        chamada.setQtdQrs(5L);

        when(chamadaRepository.save(any(Chamada.class))).thenReturn(chamada);

        Chamada saved = chamadaService.saveChamada(chamada);
        assertEquals(5L, saved.getQtdQrs());
    }

    @Test
    @DisplayName("Cena 03 - Deve buscar chamada por ID")
    void cenario03() {
        Chamada chamada = new Chamada();
        chamada.setId(1L);

        when(chamadaRepository.findById(1L)).thenReturn(Optional.of(chamada));

        Optional<Chamada> result = chamadaService.getChamadaById(1L);
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    @DisplayName("Cena 04 - Deve deletar chamada")
    void cenario04() {
        doNothing().when(chamadaRepository).deleteById(1L);

        chamadaService.deleteChamada(1L);
        verify(chamadaRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Cena 05 - Deve buscar chamadas do último mês")
    void cenario05() {
        when(chamadaRepository.findChamadasFromLastMonth(any(), any()))
                .thenReturn(List.of(new Chamada()));

        List<Chamada> chamadas = chamadaService.getChamadasFromLastMonth();
        assertEquals(1, chamadas.size());
    }
}
