package com.gestion.materiel.repository;

import com.gestion.materiel.model.MarcheSpecification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarcheSpecificationRepository extends JpaRepository<MarcheSpecification, Long> {
    
    List<MarcheSpecification> findByMarcheId(Long marcheId);
    
    void deleteByMarcheId(Long marcheId);
    
    boolean existsByMarcheIdAndTypeMaterielId(Long marcheId, Long typeMaterielId);
}
