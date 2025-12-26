package com.gestion.materiel.mapper;

import com.gestion.materiel.Dto.BesoinExprimeDTO;
import com.gestion.materiel.Dto.BesoinExprimeRequest;
import com.gestion.materiel.model.Agent;
import com.gestion.materiel.model.BesoinExprime;
import com.gestion.materiel.model.TypeMateriel;
import com.gestion.materiel.repository.AgentRepository;
import com.gestion.materiel.repository.TypeMaterielRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BesoinExprimeMapper {
    
    private final AgentRepository agentRepository;
    private final TypeMaterielRepository typeMaterielRepository;
    
    public BesoinExprimeMapper(AgentRepository agentRepository, TypeMaterielRepository typeMaterielRepository) {
        this.agentRepository = agentRepository;
        this.typeMaterielRepository = typeMaterielRepository;
    }
    
    /**
     * Convert Entity to DTO
     */
    public BesoinExprimeDTO toDTO(BesoinExprime besoin) {
        if (besoin == null) {
            return null;
        }
        return new BesoinExprimeDTO(besoin);
    }
    
    /**
     * Convert Request DTO to Entity
     */
    public BesoinExprime toEntity(BesoinExprimeRequest request) {
        if (request == null) {
            return null;
        }
        
        BesoinExprime besoin = new BesoinExprime();
        besoin.setDateBesoin(request.getDateBesoin());
        besoin.setRaison(request.getRaison());
        besoin.setObservation(request.getObservation());
        besoin.setStatut(com.gestion.materiel.model.StatutBesoin.CRÉÉ);
        
        // Set agent
        if (request.getAgentId() != null) {
            Optional<Agent> agent = agentRepository.findById(request.getAgentId());
            agent.ifPresent(besoin::setAgent);
        }
        
        // Set type materiel
        if (request.getTypeMaterielId() != null) {
            Optional<TypeMateriel> typeMateriel = typeMaterielRepository.findById(request.getTypeMaterielId());
            typeMateriel.ifPresent(besoin::setTypeMateriel);
        }
        
        return besoin;
    }
    
    /**
     * Update Entity from Request DTO
     */
    public void updateEntityFromRequest(BesoinExprime besoin, BesoinExprimeRequest request) {
        if (besoin == null || request == null) {
            return;
        }
        
        besoin.setDateBesoin(request.getDateBesoin());
        besoin.setRaison(request.getRaison());
        besoin.setObservation(request.getObservation());
        
        // Update type materiel if provided
        if (request.getTypeMaterielId() != null) {
            Optional<TypeMateriel> typeMateriel = typeMaterielRepository.findById(request.getTypeMaterielId());
            typeMateriel.ifPresent(besoin::setTypeMateriel);
        }
    }
}

