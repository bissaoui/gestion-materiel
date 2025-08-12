package com.gestion.materiel.repository;

import com.gestion.materiel.model.Departement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartementRepository extends JpaRepository<Departement, Long> {
    List<Departement> findByDirectionId(Long directionId);
}
