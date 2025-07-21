package com.gestion.materiel.Dto;

import com.gestion.materiel.model.Agent;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AgentDto {
    private Long id;
    private String nom;
    private String cin;
    private String poste;
    private Long directionId;
    private Long departementId;
    private Long serviceId;
    private String departementName;
    private String serviceName;
    private String directionName;

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

}
