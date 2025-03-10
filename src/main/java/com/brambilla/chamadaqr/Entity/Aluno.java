package com.brambilla.chamadaqr.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "Aluno")
public class Aluno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String nome;
    @NotNull
    private Long ra;
    private Long ultimoIpAcesso;
    private Boolean suspenso;
    private Integer nivelAlerta;
    private String senha;


    @ManyToMany(mappedBy = "alunos")
    private List<Turma> turmas;
}
