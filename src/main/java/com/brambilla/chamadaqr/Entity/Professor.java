package com.brambilla.chamadaqr.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Entity
@Data
@Table(name = "Professor")
public class Professor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "O nome do professor é obrigatorio")
    private String nome;
    @NotBlank(message = "Email de contato é obrigatorio")
    @Email
    private String email;
    @NotBlank(message = "Senha é obrigatoria")
    private String senha;

    @OneToMany(mappedBy = "professorResponsavel")
    @JsonIgnoreProperties("professor")
    @ToString.Exclude
    private List<Turma> turmas;

    @OneToMany(mappedBy = "professor")
    @JsonIgnoreProperties({"professor", "turma"})
    @ToString.Exclude
    private List<Chamada> chamadas;
}
