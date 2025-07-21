package com.gestion.materiel.Dto;

import com.gestion.materiel.model.Departement;
import com.gestion.materiel.model.Service;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
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

}
