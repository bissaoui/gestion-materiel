package com.gestion.materiel.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "besoins_exprimes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BesoinExprime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "agent_id", nullable = false)
    @JsonIgnore
    private Agent agent;

    @ManyToOne
    @JoinColumn(name = "type_materiel_id", nullable = false)
    private TypeMateriel typeMateriel;

    @Column(name = "date_besoin", nullable = false)
    private LocalDate dateBesoin;

    @Column(name = "motif", nullable = false, length = 1000)
    private String motif;

    @Column(name = "observation", length = 2000)
    private String observation;

    @Column(name = "date_affectation_ancien")
    private LocalDate dateAffectationAncien;

    @Column(name = "numero_serie_ancien", length = 100)
    private String numeroSerieAncien;

    @Column(name = "marque_ancien", length = 100)
    private String marqueAncien;

    @Column(name = "modele_ancien", length = 100)
    private String modeleAncien;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false, length = 20)
    private StatutBesoin statut = StatutBesoin.CRÉÉ;

    @Column(name = "date_creation", nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    @Column(name = "date_validation")
    private LocalDateTime dateValidation;

    @Column(name = "date_visa")
    private LocalDateTime dateVisa;

    @Column(name = "date_decision")
    private LocalDateTime dateDecision;

    @ManyToOne
    @JoinColumn(name = "validateur_id")
    @JsonIgnore
    private Agent validateur;

    @ManyToOne
    @JoinColumn(name = "viseur_id")
    @JsonIgnore
    private Agent viseur;

    @ManyToOne
    @JoinColumn(name = "decideur_id")
    @JsonIgnore
    private Agent decideur;

    @PrePersist
    protected void onCreate() {
        dateCreation = LocalDateTime.now();
    }
}

