package com.gestion.materiel.model;

import jakarta.persistence.*;
import java.util.Set;
import java.util.HashSet;
import com.gestion.materiel.model.Marque;

@Entity
public class TypeMateriel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    @ManyToMany(mappedBy = "types")
    private Set<Marque> marques = new HashSet<>();

    // Getters et setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public Set<Marque> getMarques() { return marques; }
    public void setMarques(Set<Marque> marques) { this.marques = marques; }
} 