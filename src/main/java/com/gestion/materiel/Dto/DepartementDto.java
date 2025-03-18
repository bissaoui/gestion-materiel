package com.gestion.materiel.Dto;

import com.gestion.materiel.model.Departement;

public class DepartementDto {
    private Long id;
    private String libelle;
    private String abreviation;
    private Long directionId; // On ne garde que l'ID de la direction

    public DepartementDto(Departement departement) {
        this.id = departement.getId();
        this.libelle = departement.getLibelle();
        this.abreviation = departement.getAbreviation();
        this.directionId = departement.getDirection().getId();
    }

    // Getters et Setters


    public String getAbreviation() {
        return abreviation;
    }

    public void setAbreviation(String abreviation) {
        this.abreviation = abreviation;
    }

    public Long getDirectionId() {
        return directionId;
    }

    public void setDirectionId(Long directionId) {
        this.directionId = directionId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }
}
