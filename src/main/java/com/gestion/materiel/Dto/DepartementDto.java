package com.gestion.materiel.Dto;

import com.gestion.materiel.model.Departement;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DepartementDto {
    private Long id;
    private String libelle;
    private String abreviation;
    private Long directionId;

    public DepartementDto(Departement departement) {
        this.id = departement.getId();
        this.libelle = departement.getLibelle();
        this.abreviation = departement.getAbreviation();
        this.directionId = departement.getDirection().getId();
    }

    // Getters et Setters


}
