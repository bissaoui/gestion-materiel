package com.gestion.materiel.Dto;

import com.gestion.materiel.model.Agent;

public class AgentDto {
    private Long id;
    private String nom;
    private String cin;
    private String poste;
    private Long directionId; // On ne garde que l'ID de la direction
    private Long departementId; // On ne garde que l'ID de la direction
    private Long serviceId; // On ne garde que l'ID de la direction
    private String departementName; // On ne garde que l'ID de la direction
    private String serviceName; // On ne garde que l'ID de la direction
    private String directionName; // On ne garde que l'ID de la direction

    private String role;
    private String username;

    public AgentDto(Agent agent) {
        this.id = agent.getId();
        this.nom = agent.getNom();
        this.cin = agent.getCIN();
        this.poste = agent.getPoste();

        if (agent.getDirection() != null) {
            this.directionName = agent.getDirection().getLibelle();
            this.directionId = agent.getDirection().getId();
        }

        if (agent.getService() != null) {
            this.serviceName = agent.getService().getLibelle();
            this.serviceId = agent.getService().getId();
        }

        if (agent.getDepartement() != null) {
            this.departementName = agent.getDepartement().getLibelle();
            this.departementId = agent.getDepartement().getId();
        }

        this.role = agent.getRole() != null ? String.valueOf(agent.getRole()) : null;
        this.username = agent.getUsername();
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


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public String getPoste() {
        return poste;
    }

    public void setPoste(String poste) {
        this.poste = poste;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDirectionId() {
        return directionId;
    }

    public void setDirectionId(Long directionId) {
        this.directionId = directionId;
    }

    public Long getDepartementId() {
        return departementId;
    }

    public void setDepartementId(Long departementId) {
        this.departementId = departementId;
    }

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }
}
