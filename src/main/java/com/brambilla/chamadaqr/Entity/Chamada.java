package com.brambilla.chamadaqr.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;

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
    @JsonIgnoreProperties({"chamadas", "turmas"})
    @ToString.Exclude
    private Professor professor;
    @ManyToOne
    @JoinColumn(name = "id_turma", nullable = false)
    @JsonIgnoreProperties("alunos")
    @ToString.Exclude
    private Turma turma;
    @NotNull(message = "A data de criação é obrigatoria")
    private LocalDateTime createdAt;
    private Long qtdAlunos;


    private LocalDateTime updatedAt;
    private Long qtdQrs;
    private Long status;
    private Long intervaloQr;

}