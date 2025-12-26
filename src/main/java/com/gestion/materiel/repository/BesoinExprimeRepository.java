package com.gestion.materiel.repository;

import com.gestion.materiel.model.BesoinExprime;
import com.gestion.materiel.model.StatutBesoin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BesoinExprimeRepository extends JpaRepository<BesoinExprime, Long> {
    
    // Find by statut with pagination
    Page<BesoinExprime> findByStatut(StatutBesoin statut, Pageable pageable);
    
    // Find by agent ID with pagination
    Page<BesoinExprime> findByAgentId(Long agentId, Pageable pageable);
    
    // Find by statut and agent ID with pagination
    Page<BesoinExprime> findByStatutAndAgentId(StatutBesoin statut, Long agentId, Pageable pageable);
    
    // Find by agent ID ordered by date creation desc
    List<BesoinExprime> findByAgentIdOrderByDateCreationDesc(Long agentId);
    
    // Find by statut (without pagination)
    List<BesoinExprime> findByStatut(StatutBesoin statut);
    
    // Custom queries for hierarchical filtering
    @Query("SELECT b FROM BesoinExprime b WHERE b.agent.direction.id = :directionId AND b.statut = :statut")
    List<BesoinExprime> findByDirectionIdAndStatut(@Param("directionId") Long directionId, @Param("statut") StatutBesoin statut);
    
    @Query("SELECT b FROM BesoinExprime b WHERE b.agent.departement.id = :departementId AND b.statut = :statut")
    List<BesoinExprime> findByDepartementIdAndStatut(@Param("departementId") Long departementId, @Param("statut") StatutBesoin statut);
    
    @Query("SELECT b FROM BesoinExprime b WHERE b.agent.service.id = :serviceId AND b.statut = :statut")
    List<BesoinExprime> findByServiceIdAndStatut(@Param("serviceId") Long serviceId, @Param("statut") StatutBesoin statut);
    
    // Find besoins à valider (statut CRÉÉ) for a specific direction
    @Query("SELECT b FROM BesoinExprime b WHERE b.agent.direction.id = :directionId AND b.statut = 'CRÉÉ'")
    Page<BesoinExprime> findBesoinsAValiderByDirection(@Param("directionId") Long directionId, Pageable pageable);
    
    // Find besoins à valider (statut CRÉÉ) for a specific departement
    @Query("SELECT b FROM BesoinExprime b WHERE b.agent.departement.id = :departementId AND b.statut = 'CRÉÉ'")
    Page<BesoinExprime> findBesoinsAValiderByDepartement(@Param("departementId") Long departementId, Pageable pageable);
    
    // Find besoins à valider (statut CRÉÉ) for a specific service
    @Query("SELECT b FROM BesoinExprime b WHERE b.agent.service.id = :serviceId AND b.statut = 'CRÉÉ'")
    Page<BesoinExprime> findBesoinsAValiderByService(@Param("serviceId") Long serviceId, Pageable pageable);
}

