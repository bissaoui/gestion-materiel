package com.gestion.materiel.repository;

import com.gestion.materiel.model.Marque;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MarqueRepository extends JpaRepository<Marque, Long> {
    boolean existsByNomIgnoreCase(String nom);
} 