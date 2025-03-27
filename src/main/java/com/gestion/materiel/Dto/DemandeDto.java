package com.gestion.materiel.Dto;

import com.gestion.materiel.model.Demande;


import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DemandeDto {
    private Long id;
    private Date date;
    private Long AgentID ;
    private String agentNom ;
    private String agentPrenom ;
    private String departement;
    private String departementName;
    private String direction;
    private String directionName;
    private String service;
    private String serviceName;
    private boolean validation;
    private List<LigneDemandeDto> lignes;



    public DemandeDto(Demande demande) {
        AgentID = demande.getAgent().getId();
        agentNom = demande.getAgent().getUsername();
        agentPrenom = demande.getAgent().getNom();
        this.date = demande.getDate();
        this.id = demande.getId();
        this.validation = demande.isValidation();
        this.departement = Optional.ofNullable(demande.getAgent())
                .map(agent -> agent.getDepartement())
                .map(departement -> departement.getAbreviation())
                .orElse(null);
        this.departementName = Optional.ofNullable(demande.getAgent())
                .map(agent -> agent.getDepartement())
                .map(departement -> departement.getLibelle())
                .orElse(null);

        this.direction = Optional.ofNullable(demande.getAgent())
                .map(agent -> agent.getDirection())
                .map(direction -> direction.getAbreviation())
                .orElse(null);
        this.directionName = Optional.ofNullable(demande.getAgent())
                .map(agent -> agent.getDirection())
                .map(direction -> direction.getLibelle())
                .orElse(null);

        this.serviceName = Optional.ofNullable(demande.getAgent())
                .map(agent -> agent.getService())
                .map(service -> service.getLibelle())
                .orElse(null);
        this.service = Optional.ofNullable(demande.getAgent())
                .map(agent -> agent.getService())
                .map(service -> service.getAbreviation())
                .orElse(null);

        this.lignes =demande.getLignes().stream().map(LigneDemandeDto::new).collect(Collectors.toList());

    }

    public String getAgentPrenom() {
        return agentPrenom;
    }

    public void setAgentPrenom(String agentPrenom) {
        agentPrenom = agentPrenom;
    }

    public String getDepartementName() {
        return departementName;
    }

    public void setDepartementName(String departementName) {
        this.departementName = departementName;
    }

    public String getDirectionName() {
        return directionName;
    }

    public void setDirectionName(String directionName) {
        this.directionName = directionName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getDepartement() {
        return departement;
    }

    public void setDepartement(String departement) {
        this.departement = departement;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public Long getAgentID() {
        return AgentID;
    }

    public void setAgentID(Long agentID) {
        AgentID = agentID;
    }

    public String getAgentNom() {
        return agentNom;
    }

    public void setAgentNom(String agentName) {
        agentNom = agentName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<LigneDemandeDto> getLignes() {
        return lignes;
    }

    public void setLignes(List<LigneDemandeDto> lignes) {
        this.lignes = lignes;
    }

    public boolean isValidation() {
        return validation;
    }

    public void setValidation(boolean validation) {
        this.validation = validation;
    }
}
