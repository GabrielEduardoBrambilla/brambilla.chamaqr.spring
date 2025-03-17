package com.brambilla.chamadaqr.Repository;

import com.brambilla.chamadaqr.Entity.Chamada;
import com.brambilla.chamadaqr.Entity.Presenca;
import com.brambilla.chamadaqr.Entity.Turma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChamadaRepository extends JpaRepository<Chamada, Long> {

    List<Chamada> findByQtdQrs(Long qtdQrs);
    @Query("SELECT c FROM Chamada c WHERE c.createdAt >= :startOfLastMonth AND c.createdAt < :startOfThisMonth")
    List<Chamada> findChamadasFromLastMonth(
            @Param("startOfLastMonth") LocalDateTime startOfLastMonth,
            @Param("startOfThisMonth") LocalDateTime startOfThisMonth
    );
}