package com.gestion.materiel.model;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Departement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String libelle;

    @Column(nullable = false, unique = true)
    private String abreviation;


    @ManyToOne
    @JoinColumn(name = "direction_id")
    private Direction direction;

    @OneToMany(mappedBy = "departement")
    private List<Agent> agents;
}
