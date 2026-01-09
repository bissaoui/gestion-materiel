package com.gestion.materiel.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Agent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String CIN;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String prenom;

    @Column(unique = true, nullable = true)
    private String email;

    @Column(unique = true, nullable = true)
    private String matricule;

    @Column(name = "date_naissance")
    private LocalDate dateNaissance;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private Sexe sexe;

    private String poste;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne
    @JoinColumn(name = "direction_id", nullable = true)
    @JsonIgnore
    private Direction direction;

    @ManyToOne
    @JoinColumn(name = "departement_id", nullable = true)
    @JsonIgnore
    private Departement departement;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = true)
    @JsonIgnore
    private Service service;
}
