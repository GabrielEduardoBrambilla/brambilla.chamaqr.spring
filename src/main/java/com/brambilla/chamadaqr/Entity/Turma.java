package com.brambilla.chamadaqr.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @JsonIgnoreProperties("turmas")
    private Professor professorResponsavel;

    private String curso;

    @NotNull(message = "Quantidade de alunos na sala é obrigatoria")
    @Min(0)
    @Max(50)
    private Long qtdAlunos;
    @NotBlank(message = "Semestre é obrigatorio, em string ")
    private String semestre;

    private String ano;


    @ManyToMany
    @JoinTable(
            name = "Turma_Aluno",
            joinColumns = @JoinColumn(name = "turma_id"),
            inverseJoinColumns = @JoinColumn(name = "aluno_id")
    )
    @JsonIgnoreProperties("turma")
    private List<Aluno> alunos;
}