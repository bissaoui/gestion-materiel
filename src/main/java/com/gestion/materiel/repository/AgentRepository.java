package com.gestion.materiel.repository;
import com.gestion.materiel.model.Agent;
import com.gestion.materiel.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AgentRepository extends JpaRepository<Agent, Long> {
    Optional<Agent> findAgentByCIN(String cin);
    
    /**
     * Trouve un chef de service dans un service donné
     */
    @Query("SELECT a FROM Agent a WHERE a.service.id = :serviceId " +
           "AND a.poste IS NOT NULL " +
           "AND (LOWER(a.poste) LIKE '%chef de service%' OR LOWER(a.poste) LIKE '%chef service%')")
    List<Agent> findChefServiceByServiceId(@Param("serviceId") Long serviceId);
    
    /**
     * Trouve un chef de département dans un département donné
     */
    @Query("SELECT a FROM Agent a WHERE a.departement.id = :departementId " +
           "AND a.poste IS NOT NULL " +
           "AND (LOWER(a.poste) LIKE '%chef de département%' OR LOWER(a.poste) LIKE '%chef département%')")
    List<Agent> findChefDepartementByDepartementId(@Param("departementId") Long departementId);
    
    /**
     * Trouve un directeur dans une direction donnée
     */
    @Query("SELECT a FROM Agent a WHERE a.direction.id = :directionId " +
           "AND a.poste IS NOT NULL " +
           "AND LOWER(a.poste) LIKE '%directeur%'")
    List<Agent> findDirecteurByDirectionId(@Param("directionId") Long directionId);
    
    /**
     * Trouve un administrateur
     */
    List<Agent> findByRole(Role role);
}
