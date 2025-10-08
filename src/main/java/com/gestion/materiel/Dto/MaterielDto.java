package com.gestion.materiel.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Setter
@Getter
public class MaterielDto {
    // Getters et setters
    private Long id;
    private String numeroSerie;
    @JsonProperty("typeMaterielId")
    private Long typeMaterielId;
    @JsonProperty("marqueId")
    private Long marqueId;
    @JsonProperty("modeleId")
    private Long modeleId;
    @JsonProperty("agentId")
    private Long agentId;
    @JsonProperty("marcherId")
    private Long marcherId;
    private LocalDateTime dateAffectation;

    public LocalDateTime getDateAffectation() { return dateAffectation; }
    public void setDateAffectation(LocalDateTime dateAffectation) { this.dateAffectation = dateAffectation; }
}