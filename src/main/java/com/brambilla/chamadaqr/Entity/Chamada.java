package com.brambilla.chamadaqr.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "A data da ultima atualização é obrigatoria")
    private LocalDateTime updatedAt;
    @NotNull(message = "A data de criação é obrigatoria")
    private LocalDateTime createdAt;

    @NotNull(message = "A quantidade total de Alunos presente é obrigatorio")
    private Long qtdAlunos;
    private Long status;
    @NotNull(message = "A quantidade de QR codes gerados é obrigatorio")
    private Long qtdQrs;
    private Long intervaloQr;

    @ManyToOne
    @JoinColumn(name = "id_turma", nullable = false)
    private Turma turma;
}