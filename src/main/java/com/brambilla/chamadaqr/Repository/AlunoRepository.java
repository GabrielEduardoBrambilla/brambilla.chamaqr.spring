package com.brambilla.chamadaqr.Repository;

import com.brambilla.chamadaqr.Entity.Aluno;
import com.brambilla.chamadaqr.Entity.Turma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long> {
    Optional<Aluno> findByRa(Long ra);
    List<Aluno> findByUltimoIpAcesso(Long ultimoIpAcesso);
    boolean existsByRa(Long ra);

    @Query("SELECT a FROM Aluno a WHERE a.nivelAlerta >= :nivel")
    List<Aluno> findByAlertLevel(@Param("nivel") Integer nivel);

}