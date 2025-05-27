package com.brambilla.chamadaqr.Service;

import com.brambilla.chamadaqr.Entity.Professor;
import com.brambilla.chamadaqr.Repository.ProfessorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class ProfessorServiceTest {

    @Mock
    private ProfessorRepository professorRepository;

    @InjectMocks
    private ProfessorService professorService;

    private Professor sampleProf;

    @BeforeEach
    void setUp() {
        sampleProf = new Professor();
        sampleProf.setId(7L);
        sampleProf.setNome("Ana Souza");
        sampleProf.setEmail("ana@uni.com");
        sampleProf.setSenha("senhaSegura");
    }

    @Test
    @DisplayName("getAllProfessores() → retorna lista vinda do repositório")
    void testGetAllProfessores() {
        when(professorRepository.findAll()).thenReturn(List.of(sampleProf));

        List<Professor> result = professorService.getAllProfessores();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(sampleProf, result.get(0));
        verify(professorRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("getProfessorById() → retorna Optional quando existe")
    void testGetProfessorByIdFound() {
        when(professorRepository.findById(7L)).thenReturn(Optional.of(sampleProf));

        Optional<Professor> result = professorService.getProfessorById(7L);

        assertTrue(result.isPresent());
        assertEquals(sampleProf, result.get());
        verify(professorRepository).findById(7L);
    }

    @Test
    @DisplayName("getProfessorById() → retorna Optional.empty quando não existe")
    void testGetProfessorByIdNotFound() {
        when(professorRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Professor> result = professorService.getProfessorById(99L);

        assertFalse(result.isPresent());
        verify(professorRepository).findById(99L);
    }

    @Test
    @DisplayName("saveProfessor() → delega ao repositório e retorna entidade salva")
    void testSaveProfessor() {
        when(professorRepository.save(sampleProf)).thenReturn(sampleProf);

        Professor result = professorService.saveProfessor(sampleProf);

        assertSame(sampleProf, result);
        verify(professorRepository).save(sampleProf);
    }

    @Test
    @DisplayName("deleteProfessor() → delega exclusão ao repositório")
    void testDeleteProfessor() {
        doNothing().when(professorRepository).deleteById(7L);

        professorService.deleteProfessor(7L);

        verify(professorRepository).deleteById(7L);
    }

    @Test
    @DisplayName("getProfessorByNome() → retorna Optional quando encontra por nome")
    void testGetProfessorByNomeFound() {
        when(professorRepository.findByNome("Ana Souza"))
                .thenReturn(Optional.of(sampleProf));

        Optional<Professor> result = professorService.getProfessorByNome("Ana Souza");

        assertTrue(result.isPresent());
        assertEquals(sampleProf, result.get());
        verify(professorRepository).findByNome("Ana Souza");
    }

    @Test
    @DisplayName("getProfessorByNome() → Optional.empty quando não encontra por nome")
    void testGetProfessorByNomeNotFound() {
        when(professorRepository.findByNome("Inexistente"))
                .thenReturn(Optional.empty());

        Optional<Professor> result = professorService.getProfessorByNome("Inexistente");

        assertFalse(result.isPresent());
        verify(professorRepository).findByNome("Inexistente");
    }

    @Test
    @DisplayName("getProfessorByEmail() → retorna Optional quando encontra por email")
    void testGetProfessorByEmailFound() {
        when(professorRepository.findByEmail("ana@uni.com"))
                .thenReturn(Optional.of(sampleProf));

        Optional<Professor> result = professorService.getProfessorByEmail("ana@uni.com");

        assertTrue(result.isPresent());
        assertEquals(sampleProf, result.get());
        verify(professorRepository).findByEmail("ana@uni.com");
    }

    @Test
    @DisplayName("getProfessorByEmail() → Optional.empty quando não encontra por email")
    void testGetProfessorByEmailNotFound() {
        when(professorRepository.findByEmail("x@x.com"))
                .thenReturn(Optional.empty());

        Optional<Professor> result = professorService.getProfessorByEmail("x@x.com");

        assertFalse(result.isPresent());
        verify(professorRepository).findByEmail("x@x.com");
    }
}
