package com.gestion.materiel.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Marche {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private LocalDate date;

    @Column(name = "date_ordre_service")
    private LocalDate dateOrdreService;

    @Column(name = "delai_execution")
    private Integer delaiExecution; // in days

    @Column(name = "date_reception_provisoire")
    private LocalDate dateReceptionProvisoire;

    @Column(name = "date_reception_definitive")
    private LocalDate dateReceptionDefinitive;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_marche")
    private TypeMarche typeMarche;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prestataire_id")
    private Prestataire prestataire;

    @OneToMany(mappedBy = "marche")
    private Set<Materiel> materiels = new HashSet<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public Set<Materiel> getMateriels() { return materiels; }
    public void setMateriels(Set<Materiel> materiels) { this.materiels = materiels; }

    public LocalDate getDateOrdreService() { return dateOrdreService; }
    public void setDateOrdreService(LocalDate dateOrdreService) { this.dateOrdreService = dateOrdreService; }

    public Integer getDelaiExecution() { return delaiExecution; }
    public void setDelaiExecution(Integer delaiExecution) { this.delaiExecution = delaiExecution; }

    public LocalDate getDateReceptionProvisoire() { return dateReceptionProvisoire; }
    public void setDateReceptionProvisoire(LocalDate dateReceptionProvisoire) { this.dateReceptionProvisoire = dateReceptionProvisoire; }

    public LocalDate getDateReceptionDefinitive() { return dateReceptionDefinitive; }
    public void setDateReceptionDefinitive(LocalDate dateReceptionDefinitive) { this.dateReceptionDefinitive = dateReceptionDefinitive; }

    public TypeMarche getTypeMarche() { return typeMarche; }
    public void setTypeMarche(TypeMarche typeMarche) { this.typeMarche = typeMarche; }

    public Prestataire getPrestataire() { return prestataire; }
    public void setPrestataire(Prestataire prestataire) { this.prestataire = prestataire; }
}





