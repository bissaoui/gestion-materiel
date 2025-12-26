package com.gestion.materiel.Dto;

import java.time.LocalDate;

public class BesoinExprimeRequest {
    private Long agentId;
    private Long typeMaterielId;
    private LocalDate dateBesoin;
    private String raison;
    private String observation;

    public BesoinExprimeRequest() {
    }

    public BesoinExprimeRequest(Long agentId, Long typeMaterielId, LocalDate dateBesoin, String raison, String observation) {
        this.agentId = agentId;
        this.typeMaterielId = typeMaterielId;
        this.dateBesoin = dateBesoin;
        this.raison = raison;
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

    public String getRaison() {
        return raison;
    }

    public void setRaison(String raison) {
        this.raison = raison;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }
}

