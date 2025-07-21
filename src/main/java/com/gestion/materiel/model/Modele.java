package com.gestion.materiel.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Modele {
    // Getters et setters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    @ManyToOne
    @JoinColumn(name = "marque_id")
    private Marque marque;

    @ManyToOne
    @JoinColumn(name = "type_materiel_id")
    private TypeMateriel typeMateriel;

    public TypeMateriel getTypeMateriel() { return typeMateriel; }
    public void setTypeMateriel(TypeMateriel typeMateriel) { this.typeMateriel = typeMateriel; }
}