package com.brambilla.chamadaqr.Service;

import com.brambilla.chamadaqr.Entity.Professor;
import com.brambilla.chamadaqr.Repository.ProfessorRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ProfessorServiceTest {

    @Autowired
    private ProfessorService professorService;

    @MockBean
    private ProfessorRepository professorRepository;

    @Test
    @DisplayName("Cena 01 - Deve retornar todos os professores")
    void cenario01() {
        when(professorRepository.findAll()).thenReturn(List.of(new Professor(), new Professor()));

        List<Professor> result = professorService.getAllProfessores();

        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("Cena 02 - Deve retornar professor por ID")
    void cenario02() {
        Professor professor = new Professor();
        professor.setId(1L);
        professor.setNome("Carlos");

        when(professorRepository.findById(1L)).thenReturn(Optional.of(professor));

        Optional<Professor> result = professorService.getProfessorById(1L);

        assertTrue(result.isPresent());
        assertEquals("Carlos", result.get().getNome());
    }

    @Test
    @DisplayName("Cena 03 - Deve salvar professor")
    void cenario03() {
        Professor professor = new Professor();
        professor.setNome("Joana");
        professor.setEmail("joana@email.com");
        professor.setSenha("senha123");

        when(professorRepository.save(any(Professor.class))).thenReturn(professor);

        Professor saved = professorService.saveProfessor(professor);

        assertNotNull(saved);
        assertEquals("Joana", saved.getNome());
    }

    @Test
    @DisplayName("Cena 04 - Deve deletar professor por ID")
    void cenario04() {
        Long id = 1L;

        doNothing().when(professorRepository).deleteById(id);

        professorService.deleteProfessor(id);

        verify(professorRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("Cena 05 - Deve buscar professor por nome")
    void cenario05() {
        Professor professor = new Professor();
        professor.setNome("Ricardo");

        when(professorRepository.findByNome("Ricardo")).thenReturn(Optional.of(professor));

        Optional<Professor> result = professorService.getProfessorByNome("Ricardo");

        assertTrue(result.isPresent());
        assertEquals("Ricardo", result.get().getNome());
    }

    @Test
    @DisplayName("Cena 06 - Deve buscar professor por email")
    void cenario06() {
        Professor professor = new Professor();
        professor.setEmail("teste@email.com");

        when(professorRepository.findByEmail("teste@email.com")).thenReturn(Optional.of(professor));

        Optional<Professor> result = professorService.getProfessorByEmail("teste@email.com");

        assertTrue(result.isPresent());
        assertEquals("teste@email.com", result.get().getEmail());
    }
}

