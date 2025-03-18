package com.gestion.materiel.repository;

import com.gestion.materiel.model.LigneDemande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LigneDemandeRepository extends JpaRepository<LigneDemande, Long> {
}
