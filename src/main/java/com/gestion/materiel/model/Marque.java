package com.gestion.materiel.model;

import jakarta.persistence.*;
import java.util.Set;
import java.util.HashSet;

@Entity
public class Marque {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    @ManyToMany
    @JoinTable(
        name = "marque_type",
        joinColumns = @JoinColumn(name = "marque_id"),
        inverseJoinColumns = @JoinColumn(name = "type_materiel_id")
    )
    private Set<TypeMateriel> types = new HashSet<>();

    // Getters et setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public Set<TypeMateriel> getTypes() { return types; }
    public void setTypes(Set<TypeMateriel> types) { this.types = types; }
} 