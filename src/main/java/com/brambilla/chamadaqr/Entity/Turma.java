package com.brambilla.chamadaqr.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "Turma")
public class Turma {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "professorResponsavel", nullable = false)
    private Professor professorResponsavel;

    private String curso;

    private Long qtdAlunos;
    private String semestre;
    private String ano;


    @ManyToMany
    @JoinTable(
            name = "Turma_Aluno",
            joinColumns = @JoinColumn(name = "turma_id"),
            inverseJoinColumns = @JoinColumn(name = "aluno_id")
    )
    private List<Aluno> alunos;
}