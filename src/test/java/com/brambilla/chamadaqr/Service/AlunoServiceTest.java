package com.brambilla.chamadaqr.Service;

import com.brambilla.chamadaqr.Entity.Aluno;
import com.brambilla.chamadaqr.Entity.Turma;
import com.brambilla.chamadaqr.Repository.AlunoRepository;
import com.brambilla.chamadaqr.Repository.TurmaRepository;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AlunoServiceTest {

    @Autowired
    private AlunoService alunoService;

    @MockBean
    private AlunoRepository alunoRepository;

    @MockBean
    private TurmaRepository turmaRepository;

    @Test
    @DisplayName("Cena 01 - Deve salvar aluno com dados válidos")
    void cenario01() {
        Aluno aluno = new Aluno();
        aluno.setNome("João");
        aluno.setRa(123456L);
        aluno.setSenha("senha123");

        Turma turma = new Turma();
        turma.setId(1L);
        aluno.setTurma(List.of(turma));

        when(turmaRepository.findById(1L)).thenReturn(Optional.of(turma));
        when(alunoRepository.existsByRa(123456L)).thenReturn(false);
        when(alunoRepository.save(any(Aluno.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Aluno saved = alunoService.saveAluno(aluno);

        assertNotNull(saved);
        assertEquals("João", saved.getNome());
    }

    @Test
    @DisplayName("Cena 02 - Deve lançar exceção se nome for nulo")
    void cenario02() {
        Aluno aluno = new Aluno();
        aluno.setNome(null);
        aluno.setRa(123456L);
        aluno.setSenha("senha123");

        assertThrows(ValidationException.class, () -> alunoService.saveAluno(aluno));
    }

    @Test
    @DisplayName("Cena 03 - Deve lançar exceção se RA for inválido")
    void cenario03() {
        Aluno aluno = new Aluno();
        aluno.setNome("Maria");
        aluno.setRa(-5L);
        aluno.setSenha("senha123");

        assertThrows(ValidationException.class, () -> alunoService.saveAluno(aluno));
    }

    @Test
    @DisplayName("Cena 04 - Deve lançar exceção se RA já existir")
    void cenario04() {
        Aluno aluno = new Aluno();
        aluno.setNome("Carlos");
        aluno.setRa(123456L);
        aluno.setSenha("senha123");

        when(alunoRepository.existsByRa(123456L)).thenReturn(true);

        assertThrows(ValidationException.class, () -> alunoService.saveAluno(aluno));
    }

    @Test
    @DisplayName("Cena 05 - Deve lançar exceção se senha for muito curta")
    void cenario05() {
        Aluno aluno = new Aluno();
        aluno.setNome("Ana");
        aluno.setRa(123456L);
        aluno.setSenha("123");

        assertThrows(ValidationException.class, () -> alunoService.saveAluno(aluno));
    }

    @Test
    @DisplayName("Cena 06 - Deve retornar todos os alunos")
    void cenario06() {
        List<Aluno> lista = List.of(new Aluno(), new Aluno());
        when(alunoRepository.findAll()).thenReturn(lista);

        List<Aluno> result = alunoService.getAllAlunos();
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("Cena 07 - Deve deletar aluno por ID")
    void cenario07() {
        Long id = 1L;
        doNothing().when(alunoRepository).deleteById(id);

        alunoService.deleteAluno(id);
        verify(alunoRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("Cena 08 - Deve retornar aluno por ID")
    void cenario08() {
        Aluno aluno = new Aluno();
        aluno.setId(10L);

        when(alunoRepository.findById(10L)).thenReturn(Optional.of(aluno));
        Optional<Aluno> result = alunoService.getAlunoById(10L);

        assertTrue(result.isPresent());
        assertEquals(10L, result.get().getId());
    }

    @Test
    @DisplayName("Cena 09 - Deve retornar se RA existe")
    void cenario09() {
        when(alunoRepository.existsByRa(123L)).thenReturn(true);

        assertTrue(alunoService.existAluno(123L));
    }

    @Test
    @DisplayName("Cena 10 - Deve retornar alunos com determinado nível de alerta")
    void cenario10() {
        Aluno aluno = new Aluno();
        aluno.setNivelAlerta(2);

        when(alunoRepository.findByAlertLevel(2)).thenReturn(List.of(aluno));
        List<Aluno> result = alunoService.findByAlertLevel(2);

        assertEquals(1, result.size());
        assertEquals(2, result.get(0).getNivelAlerta());
    }
}
