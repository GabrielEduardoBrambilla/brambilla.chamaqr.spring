package com.brambilla.chamadaqr.Service;

import com.brambilla.chamadaqr.Entity.Aluno;
import com.brambilla.chamadaqr.Entity.Professor;
import com.brambilla.chamadaqr.Entity.Turma;
import com.brambilla.chamadaqr.Repository.AlunoRepository;
import com.brambilla.chamadaqr.Repository.ProfessorRepository;
import com.brambilla.chamadaqr.Repository.TurmaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TurmaServiceTest {

    @InjectMocks
    private TurmaService turmaService;

    @Mock
    private TurmaRepository turmaRepository;

    @Mock
    private ProfessorRepository professorRepository;

    @Mock
    private AlunoRepository alunoRepository;

    private Professor professor;
    private Aluno aluno;
    private Turma turma;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        professor = new Professor();
        professor.setId(1L);
        professor.setNome("Prof. Ana");
        professor.setEmail("ana@escola.com");
        professor.setSenha("123456");

        aluno = new Aluno();
        aluno.setId(10L);
        aluno.setNome("Aluno Teste");
        aluno.setRa(123L);
        aluno.setSenha("senha123");

        turma = new Turma();
        turma.setId(1L);
        turma.setCurso("Engenharia");
        turma.setQtdAlunos(30L);
        turma.setSemestre("2024.1");
        turma.setProfessorResponsavel(professor);
        turma.setAlunos(List.of(aluno));
    }

    @Test
    @DisplayName("saveTurma salva turma com relacionamentos válidos")
    void testSaveTurmaComRelacionamentos() {
        when(professorRepository.findById(1L)).thenReturn(Optional.of(professor));
        when(alunoRepository.findById(10L)).thenReturn(Optional.of(aluno));
        when(turmaRepository.save(any(Turma.class))).thenReturn(turma);

        Turma saved = turmaService.saveTurma(turma);

        assertNotNull(saved);
        verify(turmaRepository).save(turma);
    }

    @Test
    @DisplayName("saveTurma lança exceção se professor não existe")
    void testSaveTurmaProfessorInvalido() {
        when(professorRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> turmaService.saveTurma(turma));
        assertTrue(ex.getMessage().contains("Professor not found"));
    }

    @Test
    @DisplayName("saveTurma lança exceção se aluno não existe")
    void testSaveTurmaAlunoInvalido() {
        when(professorRepository.findById(1L)).thenReturn(Optional.of(professor));
        when(alunoRepository.findById(10L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> turmaService.saveTurma(turma));
        assertTrue(ex.getMessage().contains("Aluno not found"));
    }

    @Test
    @DisplayName("getAllTurmas retorna lista")
    void testGetAllTurmas() {
        when(turmaRepository.findAll()).thenReturn(List.of(turma));

        List<Turma> result = turmaService.getAllTurmas();

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("getTurmaById retorna turma existente")
    void testGetTurmaById() {
        when(turmaRepository.findById(1L)).thenReturn(Optional.of(turma));

        Optional<Turma> result = turmaService.getTurmaById(1L);

        assertTrue(result.isPresent());
        assertEquals("Engenharia", result.get().getCurso());
    }

    @Test
    @DisplayName("getTurmasByCurso retorna turmas do curso")
    void testGetTurmasByCurso() {
        when(turmaRepository.findByCurso("Engenharia")).thenReturn(List.of(turma));

        List<Turma> result = turmaService.getTurmasByCurso("Engenharia");

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("getTurmasByQtdAlunos retorna turmas com determinada quantidade")
    void testGetTurmasByQtdAlunos() {
        when(turmaRepository.findByQtdAlunos(30L)).thenReturn(List.of(turma));

        List<Turma> result = turmaService.getTurmasByQtdAlunos(30L);

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("deleteTurma remove turma")
    void testDeleteTurma() {
        doNothing().when(turmaRepository).deleteById(1L);

        turmaService.deleteTurma(1L);

        verify(turmaRepository).deleteById(1L);
    }
}
