package com.brambilla.chamadaqr.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "Chamada")
public class Chamada {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_chamada")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_prof_respo", nullable = false)
    private Professor professor;

    private LocalDateTime updatedAt;  // Changed from String
    private LocalDateTime createdAt;

    private Long qtdAlunos;
    private Long status;
    private Long qtdQrs;
    private Long intervaloQr;

    @ManyToOne
    @JoinColumn(name = "id_turma", nullable = false)
    private Turma turma;
}