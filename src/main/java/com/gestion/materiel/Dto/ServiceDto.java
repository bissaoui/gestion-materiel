package com.gestion.materiel.Dto;

import com.gestion.materiel.model.Departement;
import com.gestion.materiel.model.Service;

public class ServiceDto {
    private Long id;
    private String libelle;
    private String abreviation;
    private Long departementId;
    private String departementAbbreviation;

    public ServiceDto(Service service) {
        this.id = service.getId();
        this.libelle = service.getLibelle();
        this.abreviation = service.getAbreviation();
        this.departementId = service.getDepartement().getId();
        this.departementAbbreviation= service.getDepartement().getAbreviation();
    }

    public String getAbreviation() {
        return abreviation;
    }

    public void setAbreviation(String abreviation) {
        this.abreviation = abreviation;
    }

    public String getDepartementAbbreviation() {
        return departementAbbreviation;
    }

    public void setDepartementAbbreviation(String departementAbbreviation) {
        this.departementAbbreviation = departementAbbreviation;
    }

    public Long getDepartementId() {
        return departementId;
    }

    public void setDepartementId(Long departementId) {
        this.departementId = departementId;
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
