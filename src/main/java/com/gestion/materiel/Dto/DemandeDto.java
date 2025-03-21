package com.gestion.materiel.Dto;

import com.gestion.materiel.model.Demande;
import com.gestion.materiel.model.LigneDemande;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DemandeDto {
    private Long id;
    private Date date;
    private Long AgentID ;
    private String AgentName ;
    private String departement;
    private String direction;
    private String service;
    ;
    private List<LigneDemandeDto> lignes;

    public DemandeDto(Demande demande) {
        AgentID = demande.getAgent().getId();
        AgentName = demande.getAgent().getUsername();
        this.date = demande.getDate();
        this.id = demande.getId();
        this.departement = Optional.ofNullable(demande.getAgent())
                .map(agent -> agent.getDepartement())
                .map(departement -> departement.getAbreviation())
                .orElse(null);

        this.direction = Optional.ofNullable(demande.getAgent())
                .map(agent -> agent.getDirection())
                .map(direction -> direction.getAbreviation())
                .orElse(null);

        this.service = Optional.ofNullable(demande.getAgent())
                .map(agent -> agent.getService())
                .map(service -> service.getAbreviation())
                .orElse(null);
        this.lignes =demande.getLignes().stream().map(LigneDemandeDto::new).collect(Collectors.toList());

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

    public String getAgentName() {
        return AgentName;
    }

    public void setAgentName(String agentName) {
        AgentName = agentName;
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
}
