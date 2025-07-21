package com.gestion.materiel.repository;

import com.gestion.materiel.model.Materiel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaterielRepository extends JpaRepository<Materiel, Long> {
    boolean existsByNumeroSerie(String numeroSerie);
} 