package com.gestion.materiel.Dto;

import com.gestion.materiel.model.BesoinExprime;
import com.gestion.materiel.model.StatutBesoin;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class BesoinExprimeDTO {
    private Long id;
    private Long agentId;
    private String agentNom;
    private String agentUsername;
    private Long typeMaterielId;
    private String typeMaterielNom;
    private LocalDate dateBesoin;
    private String raison;
    private String observation;
    private StatutBesoin statut;
    private LocalDateTime dateCreation;
    private LocalDateTime dateValidation;
    private LocalDateTime dateVisa;
    private LocalDateTime dateDecision;
    private Long validateurId;
    private String validateurNom;
    private Long viseurId;
    private String viseurNom;
    private Long decideurId;
    private String decideurNom;

    public BesoinExprimeDTO() {
    }

    public BesoinExprimeDTO(BesoinExprime besoin) {
        this.id = besoin.getId();
        this.agentId = besoin.getAgent() != null ? besoin.getAgent().getId() : null;
        this.agentNom = besoin.getAgent() != null ? besoin.getAgent().getNom() : null;
        this.agentUsername = besoin.getAgent() != null ? besoin.getAgent().getUsername() : null;
        this.typeMaterielId = besoin.getTypeMateriel() != null ? besoin.getTypeMateriel().getId() : null;
        this.typeMaterielNom = besoin.getTypeMateriel() != null ? besoin.getTypeMateriel().getNom() : null;
        this.dateBesoin = besoin.getDateBesoin();
        this.raison = besoin.getRaison();
        this.observation = besoin.getObservation();
        this.statut = besoin.getStatut();
        this.dateCreation = besoin.getDateCreation();
        this.dateValidation = besoin.getDateValidation();
        this.dateVisa = besoin.getDateVisa();
        this.dateDecision = besoin.getDateDecision();
        this.validateurId = besoin.getValidateur() != null ? besoin.getValidateur().getId() : null;
        this.validateurNom = besoin.getValidateur() != null ? besoin.getValidateur().getNom() : null;
        this.viseurId = besoin.getViseur() != null ? besoin.getViseur().getId() : null;
        this.viseurNom = besoin.getViseur() != null ? besoin.getViseur().getNom() : null;
        this.decideurId = besoin.getDecideur() != null ? besoin.getDecideur().getId() : null;
        this.decideurNom = besoin.getDecideur() != null ? besoin.getDecideur().getNom() : null;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public String getAgentNom() {
        return agentNom;
    }

    public void setAgentNom(String agentNom) {
        this.agentNom = agentNom;
    }

    public String getAgentUsername() {
        return agentUsername;
    }

    public void setAgentUsername(String agentUsername) {
        this.agentUsername = agentUsername;
    }

    public Long getTypeMaterielId() {
        return typeMaterielId;
    }

    public void setTypeMaterielId(Long typeMaterielId) {
        this.typeMaterielId = typeMaterielId;
    }

    public String getTypeMaterielNom() {
        return typeMaterielNom;
    }

    public void setTypeMaterielNom(String typeMaterielNom) {
        this.typeMaterielNom = typeMaterielNom;
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

    public StatutBesoin getStatut() {
        return statut;
    }

    public void setStatut(StatutBesoin statut) {
        this.statut = statut;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public LocalDateTime getDateValidation() {
        return dateValidation;
    }

    public void setDateValidation(LocalDateTime dateValidation) {
        this.dateValidation = dateValidation;
    }

    public LocalDateTime getDateVisa() {
        return dateVisa;
    }

    public void setDateVisa(LocalDateTime dateVisa) {
        this.dateVisa = dateVisa;
    }

    public LocalDateTime getDateDecision() {
        return dateDecision;
    }

    public void setDateDecision(LocalDateTime dateDecision) {
        this.dateDecision = dateDecision;
    }

    public Long getValidateurId() {
        return validateurId;
    }

    public void setValidateurId(Long validateurId) {
        this.validateurId = validateurId;
    }

    public String getValidateurNom() {
        return validateurNom;
    }

    public void setValidateurNom(String validateurNom) {
        this.validateurNom = validateurNom;
    }

    public Long getViseurId() {
        return viseurId;
    }

    public void setViseurId(Long viseurId) {
        this.viseurId = viseurId;
    }

    public String getViseurNom() {
        return viseurNom;
    }

    public void setViseurNom(String viseurNom) {
        this.viseurNom = viseurNom;
    }

    public Long getDecideurId() {
        return decideurId;
    }

    public void setDecideurId(Long decideurId) {
        this.decideurId = decideurId;
    }

    public String getDecideurNom() {
        return decideurNom;
    }

    public void setDecideurNom(String decideurNom) {
        this.decideurNom = decideurNom;
    }
}

