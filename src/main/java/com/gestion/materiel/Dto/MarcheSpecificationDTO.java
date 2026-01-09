package com.gestion.materiel.Dto;

public class MarcheSpecificationDTO {
    
    private Long id;
    private Long marcheId;
    private Long typeMaterielId;
    private String typeMaterielNom;
    private Integer quantite;
    
    public MarcheSpecificationDTO() {
    }
    
    public MarcheSpecificationDTO(Long id, Long marcheId, Long typeMaterielId, String typeMaterielNom, Integer quantite) {
        this.id = id;
        this.marcheId = marcheId;
        this.typeMaterielId = typeMaterielId;
        this.typeMaterielNom = typeMaterielNom;
        this.quantite = quantite;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getMarcheId() {
        return marcheId;
    }
    
    public void setMarcheId(Long marcheId) {
        this.marcheId = marcheId;
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
    
    public Integer getQuantite() {
        return quantite;
    }
    
    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }
}
