package com.brambilla.chamadaqr.Repository;

import com.brambilla.chamadaqr.Entity.Presenca;
import com.brambilla.chamadaqr.Entity.Turma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TurmaRepository extends JpaRepository<Turma, Long> {
    List<Turma> findByCurso(String curso);
    List<Turma> findByQtdAlunos(Long qtdAlunos);

}