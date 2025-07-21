package com.gestion.materiel.Dto;

import com.gestion.materiel.model.LigneDemande;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LigneDemandeDto {
        private Long id;
        private int quantite;
        private String codeArticle;
        private String observation;
        private String designation;
        private String unite;

    public LigneDemandeDto(LigneDemande ligneDemande) {
        this.designation = ligneDemande.getArticle().getDesignation();
        this.id = ligneDemande.getId();
        this.observation = ligneDemande.getObservation();
        this.codeArticle = ligneDemande.getArticle().getCode();
        this.quantite = ligneDemande.getQuantite();
        this.unite = ligneDemande.getArticle().getUnite();
    }

}
