package com.gestion.materiel.repository;

import com.gestion.materiel.model.Modele;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ModeleRepository extends JpaRepository<Modele, Long> {
    List<Modele> findByMarqueId(Long marqueId);
} 