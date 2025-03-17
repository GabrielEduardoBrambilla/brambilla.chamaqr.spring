package com.brambilla.chamadaqr.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "Aluno")
public class Aluno {
    //Revisar regras de negocio, trazer validations para a entidades as que são possiveis
    //Fazer handler globla
    //Add validations em todas as entidades
    //Fazer endpoints completo para todos os metodos das enti
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
    @NotNull
    private String senha;


    @ManyToMany(mappedBy = "alunos")
    @JsonIgnoreProperties("alunos")
    private List<Turma> turma;
}
