package com.brambilla.chamadaqr.Repository;

import com.brambilla.chamadaqr.Entity.Aluno;
import com.brambilla.chamadaqr.Entity.Chamada;
import com.brambilla.chamadaqr.Entity.Presenca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PresencaRepository extends JpaRepository<Presenca, Long> {
    @Query("SELECT p FROM Presenca p WHERE p.aluno.ra = :ra")
    List<Presenca> findByAlunoRa(@Param("ra") Long ra);

    @Query("SELECT p FROM Presenca p WHERE p.chamada.id = :id")
    List<Presenca> findByChamadaId(@Param("id") Long id);
}