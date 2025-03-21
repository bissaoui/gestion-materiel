package com.gestion.materiel.Dto;

import com.gestion.materiel.model.Article;
import com.gestion.materiel.model.Demande;
import com.gestion.materiel.model.LigneDemande;
import jakarta.persistence.*;

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

    public String getCodeArticle() {
        return codeArticle;
    }

    public void setCodeArticle(String codeArticle) {
        this.codeArticle = codeArticle;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public String getUnite() {
        return unite;
    }

    public void setUnite(String unite) {
        this.unite = unite;
    }
}
