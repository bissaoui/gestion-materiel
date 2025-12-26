package com.gestion.materiel.service.impl;

import com.gestion.materiel.Dto.BesoinExprimeDTO;
import com.gestion.materiel.Dto.BesoinExprimeRequest;
import com.gestion.materiel.exception.NotFoundException;
import com.gestion.materiel.mapper.BesoinExprimeMapper;
import com.gestion.materiel.model.Agent;
import com.gestion.materiel.model.BesoinExprime;
import com.gestion.materiel.model.StatutBesoin;
import com.gestion.materiel.repository.AgentRepository;
import com.gestion.materiel.repository.BesoinExprimeRepository;
import com.gestion.materiel.service.BesoinExprimeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BesoinExprimeServiceImpl implements BesoinExprimeService {
    
    private final BesoinExprimeRepository besoinExprimeRepository;
    private final AgentRepository agentRepository;
    private final BesoinExprimeMapper mapper;
    
    public BesoinExprimeServiceImpl(
            BesoinExprimeRepository besoinExprimeRepository,
            AgentRepository agentRepository,
            BesoinExprimeMapper mapper) {
        this.besoinExprimeRepository = besoinExprimeRepository;
        this.agentRepository = agentRepository;
        this.mapper = mapper;
    }
    
    @Override
    public Page<BesoinExprimeDTO> getAllBesoins(Pageable pageable, StatutBesoin statut, Long agentId) {
        Page<BesoinExprime> besoins;
        
        if (statut != null && agentId != null) {
            besoins = besoinExprimeRepository.findByStatutAndAgentId(statut, agentId, pageable);
        } else if (statut != null) {
            besoins = besoinExprimeRepository.findByStatut(statut, pageable);
        } else if (agentId != null) {
            besoins = besoinExprimeRepository.findByAgentId(agentId, pageable);
        } else {
            besoins = besoinExprimeRepository.findAll(pageable);
        }
        
        return besoins.map(mapper::toDTO);
    }
    
    @Override
    public Optional<BesoinExprimeDTO> getBesoinById(Long id) {
        return besoinExprimeRepository.findById(id)
                .map(mapper::toDTO);
    }
    
    @Override
    public Page<BesoinExprimeDTO> getBesoinsByAgent(Long agentId, Pageable pageable) {
        return besoinExprimeRepository.findByAgentId(agentId, pageable)
                .map(mapper::toDTO);
    }
    
    @Override
    public Page<BesoinExprimeDTO> getBesoinsAValider(Pageable pageable) {
        // This will be filtered by the current user's hierarchy in the controller
        return besoinExprimeRepository.findByStatut(StatutBesoin.CRÉÉ, pageable)
                .map(mapper::toDTO);
    }
    
    @Override
    public Page<BesoinExprimeDTO> getBesoinsAViser(Pageable pageable) {
        return besoinExprimeRepository.findByStatut(StatutBesoin.VALIDATION, pageable)
                .map(mapper::toDTO);
    }
    
    @Override
    @Transactional
    public BesoinExprimeDTO createBesoin(BesoinExprimeRequest request, String currentUsername) {
        Agent currentAgent = agentRepository.findAgentByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("Agent non trouvé avec le username: " + currentUsername));
        
        BesoinExprime besoin = mapper.toEntity(request);
        besoin.setAgent(currentAgent);
        besoin.setStatut(StatutBesoin.CRÉÉ);
        
        BesoinExprime saved = besoinExprimeRepository.save(besoin);
        return mapper.toDTO(saved);
    }
    
    @Override
    @Transactional
    public BesoinExprimeDTO updateBesoin(Long id, BesoinExprimeRequest request, String currentUsername) {
        BesoinExprime besoin = besoinExprimeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("BesoinExprime", id));
        
        Agent currentAgent = agentRepository.findAgentByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("Agent non trouvé avec le username: " + currentUsername));
        
        // Vérifier que l'agent est le créateur et que le statut est CRÉÉ
        if (!besoin.getAgent().getId().equals(currentAgent.getId())) {
            throw new RuntimeException("Vous n'êtes pas autorisé à modifier ce besoin");
        }
        
        if (besoin.getStatut() != StatutBesoin.CRÉÉ) {
            throw new RuntimeException("Seuls les besoins avec le statut CRÉÉ peuvent être modifiés");
        }
        
        mapper.updateEntityFromRequest(besoin, request);
        BesoinExprime updated = besoinExprimeRepository.save(besoin);
        return mapper.toDTO(updated);
    }
    
    @Override
    @Transactional
    public BesoinExprimeDTO validerBesoin(Long id, String currentUsername) {
        BesoinExprime besoin = besoinExprimeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("BesoinExprime", id));
        
        Agent currentAgent = agentRepository.findAgentByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("Agent non trouvé avec le username: " + currentUsername));
        
        // Vérifier le statut
        if (besoin.getStatut() != StatutBesoin.CRÉÉ) {
            throw new RuntimeException("Seuls les besoins avec le statut CRÉÉ peuvent être validés");
        }
        
        // Vérifier les permissions hiérarchiques
        if (!canValidate(besoin.getAgent(), currentAgent)) {
            throw new RuntimeException("Vous n'êtes pas autorisé à valider ce besoin");
        }
        
        besoin.setStatut(StatutBesoin.VALIDATION);
        besoin.setDateValidation(LocalDateTime.now());
        besoin.setValidateur(currentAgent);
        
        BesoinExprime updated = besoinExprimeRepository.save(besoin);
        return mapper.toDTO(updated);
    }
    
    @Override
    @Transactional
    public BesoinExprimeDTO viserBesoin(Long id, String currentUsername) {
        BesoinExprime besoin = besoinExprimeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("BesoinExprime", id));
        
        Agent currentAgent = agentRepository.findAgentByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("Agent non trouvé avec le username: " + currentUsername));
        
        // Vérifier le statut
        if (besoin.getStatut() != StatutBesoin.VALIDATION) {
            throw new RuntimeException("Seuls les besoins avec le statut VALIDATION peuvent être visés. Statut actuel: " + besoin.getStatut());
        }
        
        // Vérifier que l'agent est directeur DAF
        if (!isDirecteurDAF(currentAgent)) {
            String posteInfo = currentAgent.getPoste() != null ? currentAgent.getPoste() : "N/A";
            String directionInfo = currentAgent.getDirection() != null && currentAgent.getDirection().getLibelle() != null 
                    ? currentAgent.getDirection().getLibelle() : "N/A";
            throw new RuntimeException("Seul le Directeur DAF peut viser les besoins. Votre poste: " + posteInfo + ", Direction: " + directionInfo);
        }
        
        besoin.setStatut(StatutBesoin.VISA);
        besoin.setDateVisa(LocalDateTime.now());
        besoin.setViseur(currentAgent);
        
        BesoinExprime updated = besoinExprimeRepository.save(besoin);
        return mapper.toDTO(updated);
    }
    
    @Override
    @Transactional
    public BesoinExprimeDTO accepterBesoin(Long id, String currentUsername) {
        BesoinExprime besoin = besoinExprimeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("BesoinExprime", id));
        
        Agent currentAgent = agentRepository.findAgentByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("Agent non trouvé avec le username: " + currentUsername));
        
        // Vérifier le statut
        if (besoin.getStatut() != StatutBesoin.VISA) {
            throw new RuntimeException("Seuls les besoins avec le statut VISA peuvent être acceptés");
        }
        
        // Vérifier que l'agent est ADMIN
        if (currentAgent.getRole() != com.gestion.materiel.model.Role.ADMIN) {
            throw new RuntimeException("Seul un administrateur peut accepter les besoins");
        }
        
        besoin.setStatut(StatutBesoin.ACCEPTÉ);
        besoin.setDateDecision(LocalDateTime.now());
        besoin.setDecideur(currentAgent);
        
        BesoinExprime updated = besoinExprimeRepository.save(besoin);
        return mapper.toDTO(updated);
    }
    
    @Override
    @Transactional
    public BesoinExprimeDTO refuserBesoin(Long id, String motif, String currentUsername) {
        BesoinExprime besoin = besoinExprimeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("BesoinExprime", id));
        
        Agent currentAgent = agentRepository.findAgentByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("Agent non trouvé avec le username: " + currentUsername));
        
        // Vérifier le statut
        if (besoin.getStatut() != StatutBesoin.VISA) {
            throw new RuntimeException("Seuls les besoins avec le statut VISA peuvent être refusés");
        }
        
        // Vérifier que l'agent est ADMIN
        if (currentAgent.getRole() != com.gestion.materiel.model.Role.ADMIN) {
            throw new RuntimeException("Seul un administrateur peut refuser les besoins");
        }
        
        besoin.setStatut(StatutBesoin.REFUSÉ);
        besoin.setDateDecision(LocalDateTime.now());
        besoin.setDecideur(currentAgent);
        if (motif != null && !motif.trim().isEmpty()) {
            besoin.setObservation(besoin.getObservation() != null 
                    ? besoin.getObservation() + "\nMotif de refus: " + motif 
                    : "Motif de refus: " + motif);
        }
        
        BesoinExprime updated = besoinExprimeRepository.save(besoin);
        return mapper.toDTO(updated);
    }
    
    @Override
    @Transactional
    public BesoinExprimeDTO changeStatut(Long id, StatutBesoin nouveauStatut, String currentUsername) {
        BesoinExprime besoin = besoinExprimeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("BesoinExprime", id));
        
        Agent currentAgent = agentRepository.findAgentByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("Agent non trouvé avec le username: " + currentUsername));
        
        // Validation des transitions de statut
        validateStatutTransition(besoin.getStatut(), nouveauStatut, currentAgent);
        
        besoin.setStatut(nouveauStatut);
        
        // Mettre à jour les dates selon le nouveau statut
        LocalDateTime now = LocalDateTime.now();
        switch (nouveauStatut) {
            case CRÉÉ:
                // Pas de changement de date pour CRÉÉ
                break;
            case VALIDATION:
                besoin.setDateValidation(now);
                besoin.setValidateur(currentAgent);
                break;
            case VISA:
                besoin.setDateVisa(now);
                besoin.setViseur(currentAgent);
                break;
            case ACCEPTÉ:
            case REFUSÉ:
                besoin.setDateDecision(now);
                besoin.setDecideur(currentAgent);
                break;
        }
        
        BesoinExprime updated = besoinExprimeRepository.save(besoin);
        return mapper.toDTO(updated);
    }
    
    @Override
    @Transactional
    public void deleteBesoin(Long id, String currentUsername) {
        BesoinExprime besoin = besoinExprimeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("BesoinExprime", id));
        
        Agent currentAgent = agentRepository.findAgentByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("Agent non trouvé avec le username: " + currentUsername));
        
        // Vérifier que l'agent est le créateur et que le statut est CRÉÉ
        if (!besoin.getAgent().getId().equals(currentAgent.getId())) {
            throw new RuntimeException("Vous n'êtes pas autorisé à supprimer ce besoin");
        }
        
        if (besoin.getStatut() != StatutBesoin.CRÉÉ) {
            throw new RuntimeException("Seuls les besoins avec le statut CRÉÉ peuvent être supprimés");
        }
        
        besoinExprimeRepository.deleteById(id);
    }
    
    // Méthodes utilitaires
    
    /**
     * Vérifie si un agent peut valider un besoin selon la hiérarchie
     */
    private boolean canValidate(Agent agentBesoin, Agent validateur) {
        // Admin peut tout valider
        if (validateur.getRole() == com.gestion.materiel.model.Role.ADMIN) {
            return true;
        }
        
        // Vérifier la hiérarchie selon le poste du validateur
        String posteValidateur = validateur.getPoste() != null ? validateur.getPoste().toLowerCase() : "";
        
        // Chef de service peut valider les besoins des agents de son service
        if (posteValidateur.contains("chef de service") || posteValidateur.contains("chef service")) {
            return agentBesoin.getService() != null 
                    && validateur.getService() != null
                    && agentBesoin.getService().getId().equals(validateur.getService().getId());
        }
        
        // Chef de département peut valider les besoins des agents et chefs de service de son département
        if (posteValidateur.contains("chef de département") || posteValidateur.contains("chef département")) {
            return agentBesoin.getDepartement() != null 
                    && validateur.getDepartement() != null
                    && agentBesoin.getDepartement().getId().equals(validateur.getDepartement().getId());
        }
        
        // Directeur peut valider les besoins des agents, chefs de service et chefs de département de sa direction
        if (posteValidateur.contains("directeur")) {
            return agentBesoin.getDirection() != null 
                    && validateur.getDirection() != null
                    && agentBesoin.getDirection().getId().equals(validateur.getDirection().getId());
        }
        
        return false;
    }
    
    /**
     * Vérifie si un agent est directeur DAF
     * Le Directeur DAF est identifié par :
     * - Poste contenant "directeur"
     * - Direction contenant "daf", "administratif", "financier", etc.
     */
    private boolean isDirecteurDAF(Agent agent) {
        if (agent.getPoste() == null) {
            return false;
        }
        
        String poste = agent.getPoste().toLowerCase();
        boolean isDirecteur = poste.contains("directeur");
        
        if (!isDirecteur) {
            return false;
        }
        
        // Vérifier aussi la direction si disponible
        if (agent.getDirection() != null && agent.getDirection().getLibelle() != null) {
            String directionLibelle = agent.getDirection().getLibelle().toLowerCase();
            return directionLibelle.contains("daf") || 
                   (directionLibelle.contains("administratif") && directionLibelle.contains("financier")) ||
                   (directionLibelle.contains("administrative") && directionLibelle.contains("financière"));
        }
        
        // Fallback : vérifier dans le poste (pour compatibilité)
        return poste.contains("daf") || 
               poste.contains("affaires financières") ||
               (poste.contains("administratif") && poste.contains("financier"));
    }
    
    /**
     * Valide une transition de statut
     */
    private void validateStatutTransition(StatutBesoin ancienStatut, StatutBesoin nouveauStatut, Agent agent) {
        // Transitions autorisées
        switch (ancienStatut) {
            case CRÉÉ:
                if (nouveauStatut != StatutBesoin.VALIDATION) {
                    throw new RuntimeException("Transition invalide: CRÉÉ → " + nouveauStatut);
                }
                break;
            case VALIDATION:
                if (nouveauStatut != StatutBesoin.VISA) {
                    throw new RuntimeException("Transition invalide: VALIDATION → " + nouveauStatut);
                }
                if (!isDirecteurDAF(agent)) {
                    throw new RuntimeException("Seul le Directeur DAF peut viser les besoins");
                }
                break;
            case VISA:
                if (nouveauStatut != StatutBesoin.ACCEPTÉ && nouveauStatut != StatutBesoin.REFUSÉ) {
                    throw new RuntimeException("Transition invalide: VISA → " + nouveauStatut);
                }
                if (agent.getRole() != com.gestion.materiel.model.Role.ADMIN) {
                    throw new RuntimeException("Seul un administrateur peut accepter ou refuser les besoins");
                }
                break;
            case ACCEPTÉ:
            case REFUSÉ:
                throw new RuntimeException("Les besoins " + ancienStatut + " ne peuvent plus être modifiés");
        }
    }
    
    /**
     * Filtre les besoins à valider selon la hiérarchie de l'agent
     */
    public List<BesoinExprime> filterBesoinsAValider(Agent agent) {
        String poste = agent.getPoste() != null ? agent.getPoste().toLowerCase() : "";
        
        if (poste.contains("chef de service") || poste.contains("chef service")) {
            if (agent.getService() != null) {
                return besoinExprimeRepository.findByServiceIdAndStatut(agent.getService().getId(), StatutBesoin.CRÉÉ);
            }
        } else if (poste.contains("chef de département") || poste.contains("chef département")) {
            if (agent.getDepartement() != null) {
                return besoinExprimeRepository.findByDepartementIdAndStatut(agent.getDepartement().getId(), StatutBesoin.CRÉÉ);
            }
        } else if (poste.contains("directeur")) {
            if (agent.getDirection() != null) {
                return besoinExprimeRepository.findByDirectionIdAndStatut(agent.getDirection().getId(), StatutBesoin.CRÉÉ);
            }
        }
        
        // Si admin, retourner tous les besoins CRÉÉ
        if (agent.getRole() == com.gestion.materiel.model.Role.ADMIN) {
            return besoinExprimeRepository.findByStatut(StatutBesoin.CRÉÉ);
        }
        
        return List.of();
    }
}

