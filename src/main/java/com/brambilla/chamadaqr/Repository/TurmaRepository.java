package com.brambilla.chamadaqr.Repository;

import com.brambilla.chamadaqr.Entity.Turma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TurmaRepository extends JpaRepository<Turma, Long> {
}