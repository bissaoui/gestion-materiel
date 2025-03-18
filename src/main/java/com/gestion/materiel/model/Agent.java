package com.gestion.materiel.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

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

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nom;

    private String poste;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne
    @JoinColumn(name = "direction_id", nullable = true)
    private Direction direction;

    @ManyToOne
    @JoinColumn(name = "departement_id", nullable = true)
    private Departement departement;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = true)
    private Service service;
}
enum Role {
    ADMIN, USER
}