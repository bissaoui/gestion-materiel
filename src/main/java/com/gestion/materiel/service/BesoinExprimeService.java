package com.gestion.materiel.service;

import com.gestion.materiel.Dto.BesoinExprimeDTO;
import com.gestion.materiel.Dto.BesoinExprimeRequest;
import com.gestion.materiel.model.StatutBesoin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BesoinExprimeService {
    // Récupération
    Page<BesoinExprimeDTO> getAllBesoins(Pageable pageable, StatutBesoin statut, Long agentId);
    Optional<BesoinExprimeDTO> getBesoinById(Long id);
    Page<BesoinExprimeDTO> getBesoinsByAgent(Long agentId, Pageable pageable);
    Page<BesoinExprimeDTO> getBesoinsAValider(Pageable pageable);
    Page<BesoinExprimeDTO> getBesoinsAViser(Pageable pageable);
    
    // Création et modification
    BesoinExprimeDTO createBesoin(BesoinExprimeRequest request, String currentUsername);
    BesoinExprimeDTO updateBesoin(Long id, BesoinExprimeRequest request, String currentUsername);
    
    // Workflow
    BesoinExprimeDTO validerBesoin(Long id, String currentUsername);
    BesoinExprimeDTO viserBesoin(Long id, String currentUsername);
    BesoinExprimeDTO accepterBesoin(Long id, String currentUsername);
    BesoinExprimeDTO refuserBesoin(Long id, String motif, String currentUsername);
    BesoinExprimeDTO changeStatut(Long id, StatutBesoin nouveauStatut, String currentUsername);
    
    // Suppression
    void deleteBesoin(Long id, String currentUsername);
}

