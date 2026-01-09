package com.gestion.materiel.Dto;

public class MarcheSpecificationRequest {
    
    private Long typeMaterielId;
    private Integer quantite;
    
    public MarcheSpecificationRequest() {
    }
    
    public MarcheSpecificationRequest(Long typeMaterielId, Integer quantite) {
        this.typeMaterielId = typeMaterielId;
        this.quantite = quantite;
    }
    
    public Long getTypeMaterielId() {
        return typeMaterielId;
    }
    
    public void setTypeMaterielId(Long typeMaterielId) {
        this.typeMaterielId = typeMaterielId;
    }
    
    public Integer getQuantite() {
        return quantite;
    }
    
    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }
}
