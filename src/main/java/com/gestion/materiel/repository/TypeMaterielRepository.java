package com.gestion.materiel.repository;

import com.gestion.materiel.model.TypeMateriel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TypeMaterielRepository extends JpaRepository<TypeMateriel, Long> {
    boolean existsByNomIgnoreCase(String nom);
} 