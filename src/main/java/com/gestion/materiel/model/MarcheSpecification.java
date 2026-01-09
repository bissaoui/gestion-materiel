package com.gestion.materiel.model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "marche_specification")
public class MarcheSpecification {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "marche_id", nullable = false)
    private Marche marche;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_materiel_id", nullable = false)
    private TypeMateriel typeMateriel;
    
    @Column(nullable = false)
    private Integer quantite;
    
    public MarcheSpecification() {
    }
    
    public MarcheSpecification(Marche marche, TypeMateriel typeMateriel, Integer quantite) {
        this.marche = marche;
        this.typeMateriel = typeMateriel;
        this.quantite = quantite;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Marche getMarche() {
        return marche;
    }
    
    public void setMarche(Marche marche) {
        this.marche = marche;
    }
    
    public TypeMateriel getTypeMateriel() {
        return typeMateriel;
    }
    
    public void setTypeMateriel(TypeMateriel typeMateriel) {
        this.typeMateriel = typeMateriel;
    }
    
    public Integer getQuantite() {
        return quantite;
    }
    
    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MarcheSpecification that = (MarcheSpecification) o;
        return Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
