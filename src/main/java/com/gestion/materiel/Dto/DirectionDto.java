package com.gestion.materiel.Dto;

import com.gestion.materiel.model.Direction;

import java.util.List;
import java.util.stream.Collectors;

public class DirectionDto {
    private Long id;
    private String libelle;
    private String abreviation;
    private List<Long> agentsId; // On ne garde que l'ID de la direction

    public DirectionDto(Direction direction) {
        this.id = direction.getId();
        this.libelle = direction.getLibelle();
        this.abreviation = direction.getAbreviation();
        this.agentsId = direction.getAgents()
                .stream()
                .map(agent -> agent.getId()) // On récupère uniquement les IDs des agents
                .collect(Collectors.toList());

    }

    public String getAbreviation() {
        return abreviation;
    }

    public void setAbreviation(String abreviation) {
        this.abreviation = abreviation;
    }

    public List<Long> getAgentsId() {
        return agentsId;
    }

    public void setAgentsId(List<Long> agentsId) {
        this.agentsId = agentsId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }
}
