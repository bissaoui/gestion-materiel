package com.gestion.materiel.Dto;

import java.time.LocalDate;

public class BesoinExprimeRequest {
    private Long agentId;
    private Long typeMaterielId;
    private LocalDate dateBesoin;
    private String motif;
    private String observation;
    private LocalDate dateAffectationAncien;
    private String numeroSerieAncien;
    private String marqueAncien;
    private String modeleAncien;

    public BesoinExprimeRequest() {
    }

    public BesoinExprimeRequest(Long agentId, Long typeMaterielId, LocalDate dateBesoin, String motif, String observation) {
        this.agentId = agentId;
        this.typeMaterielId = typeMaterielId;
        this.dateBesoin = dateBesoin;
        this.motif = motif;
        this.observation = observation;
    }

    // Getters and Setters
    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public Long getTypeMaterielId() {
        return typeMaterielId;
    }

    public void setTypeMaterielId(Long typeMaterielId) {
        this.typeMaterielId = typeMaterielId;
    }

    public LocalDate getDateBesoin() {
        return dateBesoin;
    }

    public void setDateBesoin(LocalDate dateBesoin) {
        this.dateBesoin = dateBesoin;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public LocalDate getDateAffectationAncien() {
        return dateAffectationAncien;
    }

    public void setDateAffectationAncien(LocalDate dateAffectationAncien) {
        this.dateAffectationAncien = dateAffectationAncien;
    }

    public String getNumeroSerieAncien() {
        return numeroSerieAncien;
    }

    public void setNumeroSerieAncien(String numeroSerieAncien) {
        this.numeroSerieAncien = numeroSerieAncien;
    }

    public String getMarqueAncien() {
        return marqueAncien;
    }

    public void setMarqueAncien(String marqueAncien) {
        this.marqueAncien = marqueAncien;
    }

    public String getModeleAncien() {
        return modeleAncien;
    }

    public void setModeleAncien(String modeleAncien) {
        this.modeleAncien = modeleAncien;
    }
}

