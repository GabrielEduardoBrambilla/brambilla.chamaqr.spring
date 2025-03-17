package com.brambilla.chamadaqr.Repository;

import com.brambilla.chamadaqr.Entity.Aluno;
import com.brambilla.chamadaqr.Entity.Presenca;
import com.brambilla.chamadaqr.Entity.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long> {
    Optional<Professor> findByNome(String nome);
    Optional<Professor> findByEmail(String email);

}