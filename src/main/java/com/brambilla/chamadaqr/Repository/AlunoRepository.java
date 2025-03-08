package com.brambilla.chamadaqr.Repository;

import com.brambilla.chamadaqr.Entity.Aluno;
import com.brambilla.chamadaqr.Entity.Turma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long> {
    Optional<Aluno> FindByRa(Long ra);
    List<Aluno> FindByUltimoIpAcesso(Long ra);
    boolean existsByRa(Long ra);

}