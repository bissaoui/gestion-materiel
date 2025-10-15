package com.gestion.materiel.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Prestataire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "raison_social", nullable = false)
    private String raisonSocial;

    @Column(name = "numero_tele")
    private String numeroTele;

    @OneToMany(mappedBy = "prestataire")
    private Set<Marche> marches = new HashSet<>();

    public Long getId() { 
        return id; 
    }
    
    public void setId(Long id) { 
        this.id = id; 
    }

    public String getRaisonSocial() { 
        return raisonSocial; 
    }
    
    public void setRaisonSocial(String raisonSocial) { 
        this.raisonSocial = raisonSocial; 
    }

    public String getNumeroTele() { 
        return numeroTele; 
    }
    
    public void setNumeroTele(String numeroTele) { 
        this.numeroTele = numeroTele; 
    }

    public Set<Marche> getMarches() { 
        return marches; 
    }
    
    public void setMarches(Set<Marche> marches) { 
        this.marches = marches; 
    }
}
