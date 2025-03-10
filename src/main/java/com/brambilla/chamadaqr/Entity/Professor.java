package com.brambilla.chamadaqr.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "Professor")
public class Professor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String email;
    private String senha;

    @OneToMany(mappedBy = "professorResponsavel")
    private List<Turma> turmas;

    @OneToMany(mappedBy = "professor")
    private List<Chamada> chamadas;
}
