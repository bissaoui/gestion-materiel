package com.gestion.materiel.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ModeleDto {
    // Getters et setters
    private Long id;
    private String nom;
    @JsonProperty("marqueId")
    private Long marqueId;
    private Long typeMaterielId;

    public Long getTypeMaterielId() { return typeMaterielId; }
    public void setTypeMaterielId(Long typeMaterielId) { this.typeMaterielId = typeMaterielId; }
}