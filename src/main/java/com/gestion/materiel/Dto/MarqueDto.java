package com.gestion.materiel.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
public class MarqueDto {
    // Getters et setters
    private Long id;
    private String nom;
    @JsonProperty(value = "typeMaterielIds", access = JsonProperty.Access.WRITE_ONLY)
    private Set<Long> typeMaterielIds;

    @JsonProperty("typeIds")
    public Set<Long> getTypeIds() {
        return typeMaterielIds;
    }

    @JsonProperty("typeIds")
    public void setTypeIds(Set<Long> typeIds) {
        this.typeMaterielIds = typeIds;
    }

    public Set<Long> getTypeMaterielIds() {
        return typeMaterielIds;
    }

    public void setTypeMaterielIds(Set<Long> typeMaterielIds) {
        this.typeMaterielIds = typeMaterielIds;
    }
}