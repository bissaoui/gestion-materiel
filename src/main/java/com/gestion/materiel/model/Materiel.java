package com.gestion.materiel.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
public class Materiel {
    // Getters et setters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numeroSerie;

    @ManyToOne
    @JoinColumn(name = "type_materiel_id")
    private TypeMateriel typeMateriel;

    @ManyToOne
    @JoinColumn(name = "marque_id")
    private Marque marque;

    @ManyToOne
    @JoinColumn(name = "modele_id")
    private Modele modele;

    @ManyToOne(optional = true)
    @JoinColumn(name = "agent_id", nullable = true)
    private Agent agent;

    private LocalDateTime dateAffectation;

    @ManyToOne
    @JoinColumn(name = "marche_id")
    private Marche marche;

    public Marche getMarche() { return marche; }
    public void setMarche(Marche marche) { this.marche = marche; }
}