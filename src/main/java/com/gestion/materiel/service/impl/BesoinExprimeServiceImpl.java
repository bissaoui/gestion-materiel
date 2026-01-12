package com.gestion.materiel.service.impl;

import com.gestion.materiel.Dto.BesoinExprimeDTO;
import com.gestion.materiel.Dto.BesoinExprimeRequest;
import com.gestion.materiel.exception.NotFoundException;
import com.gestion.materiel.mapper.BesoinExprimeMapper;
import com.gestion.materiel.model.Agent;
import com.gestion.materiel.model.BesoinExprime;
import com.gestion.materiel.model.Role;
import com.gestion.materiel.model.StatutBesoin;
import com.gestion.materiel.repository.AgentRepository;
import com.gestion.materiel.repository.BesoinExprimeRepository;
import com.gestion.materiel.service.BesoinExprimeService;
import com.gestion.materiel.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BesoinExprimeServiceImpl implements BesoinExprimeService {
    
    private static final Logger logger = LoggerFactory.getLogger(BesoinExprimeServiceImpl.class);
    
    private final BesoinExprimeRepository besoinExprimeRepository;
    private final AgentRepository agentRepository;
    private final BesoinExprimeMapper mapper;
    private final EmailService emailService;
    
    public BesoinExprimeServiceImpl(
            BesoinExprimeRepository besoinExprimeRepository,
            AgentRepository agentRepository,
            BesoinExprimeMapper mapper,
            EmailService emailService) {
        this.besoinExprimeRepository = besoinExprimeRepository;
        this.agentRepository = agentRepository;
        this.mapper = mapper;
        this.emailService = emailService;
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
    
    /**
     * Récupère les besoins à valider filtrés par la hiérarchie de l'agent connecté
     */
    public Page<BesoinExprimeDTO> getBesoinsAValiderByHierarchy(String currentCin, Pageable pageable) {
        Agent currentAgent = agentRepository.findAgentByCIN(currentCin)
                .orElseThrow(() -> new RuntimeException("Agent non trouvé avec le CIN: " + currentCin));
        
        logger.info("Recherche des besoins à valider pour l'agent {} {} (ID: {}, Poste: {}, Service: {}, Département: {}, Direction: {})", 
                currentAgent.getNom(), currentAgent.getPrenom(), currentAgent.getId(), 
                currentAgent.getPoste(),
                currentAgent.getService() != null ? currentAgent.getService().getId() : "NULL",
                currentAgent.getDepartement() != null ? currentAgent.getDepartement().getId() : "NULL",
                currentAgent.getDirection() != null ? currentAgent.getDirection().getId() : "NULL");
        
        // Admin peut voir tous les besoins CRÉÉ
        if (currentAgent.getRole() == Role.ADMIN) {
            logger.info("Agent est ADMIN, retour de tous les besoins CRÉÉ");
            return besoinExprimeRepository.findByStatut(StatutBesoin.CRÉÉ, pageable)
                    .map(mapper::toDTO);
        }
        
        String poste = currentAgent.getPoste() != null ? currentAgent.getPoste().toLowerCase() : "";
        Page<BesoinExprime> besoins;
        
        // Chef de service peut voir les besoins des agents de son service
        if (poste.contains("chef de service") || poste.contains("chef service")) {
            if (currentAgent.getService() != null) {
                Long serviceId = currentAgent.getService().getId();
                logger.info("Agent est chef de service, recherche des besoins pour le service ID: {}", serviceId);
                besoins = besoinExprimeRepository.findBesoinsAValiderByService(serviceId, pageable);
                logger.info("Besoins trouvés pour le service {}: {}", serviceId, besoins.getTotalElements());
            } else {
                logger.warn("Agent est chef de service mais n'a pas de service assigné");
                besoins = new PageImpl<>(List.of(), pageable, 0);
            }
        }
        // Chef de département peut voir les besoins des agents de son département
        else if (poste.contains("chef de département") || poste.contains("chef département")) {
            if (currentAgent.getDepartement() != null) {
                Long departementId = currentAgent.getDepartement().getId();
                logger.info("Agent est chef de département, recherche des besoins pour le département ID: {}", departementId);
                besoins = besoinExprimeRepository.findBesoinsAValiderByDepartement(departementId, pageable);
                logger.info("Besoins trouvés pour le département {}: {}", departementId, besoins.getTotalElements());
            } else {
                logger.warn("Agent est chef de département mais n'a pas de département assigné");
                besoins = new PageImpl<>(List.of(), pageable, 0);
            }
        }
        // Directeur peut voir les besoins des agents de sa direction
        else if (poste.contains("directeur")) {
            if (currentAgent.getDirection() != null) {
                Long directionId = currentAgent.getDirection().getId();
                logger.info("Agent est directeur, recherche des besoins pour la direction ID: {}", directionId);
                besoins = besoinExprimeRepository.findBesoinsAValiderByDirection(directionId, pageable);
                logger.info("Besoins trouvés pour la direction {}: {}", directionId, besoins.getTotalElements());
            } else {
                logger.warn("Agent est directeur mais n'a pas de direction assignée");
                besoins = new PageImpl<>(List.of(), pageable, 0);
            }
        }
        // Autres rôles ne peuvent pas voir de besoins à valider
        else {
            logger.warn("Agent avec poste '{}' n'est pas un supérieur hiérarchique reconnu", currentAgent.getPoste());
            besoins = new PageImpl<>(List.of(), pageable, 0);
        }
        
        return besoins.map(mapper::toDTO);
    }
    
    @Override
    public Page<BesoinExprimeDTO> getBesoinsAViser(Pageable pageable) {
        return besoinExprimeRepository.findByStatut(StatutBesoin.VALIDATION, pageable)
                .map(mapper::toDTO);
    }
    
    @Override
    @Transactional
    public BesoinExprimeDTO createBesoin(BesoinExprimeRequest request, String currentCin) {
        Agent currentAgent = agentRepository.findAgentByCIN(currentCin)
                .orElseThrow(() -> new RuntimeException("Agent non trouvé avec le CIN: " + currentCin));
        
        logger.info("=== CRÉATION D'UN BESOIN ===");
        logger.info("Agent créateur: {} {} (ID: {}, CIN: {}, Email: {})", 
                currentAgent.getNom(), currentAgent.getPrenom(), currentAgent.getId(), currentCin,
                currentAgent.getEmail() != null ? currentAgent.getEmail() : "NON CONFIGURÉ");
        logger.info("Service: {}, Département: {}, Direction: {}", 
                currentAgent.getService() != null ? currentAgent.getService().getId() : "NULL",
                currentAgent.getDepartement() != null ? currentAgent.getDepartement().getId() : "NULL",
                currentAgent.getDirection() != null ? currentAgent.getDirection().getId() : "NULL");
        
        BesoinExprime besoin = mapper.toEntity(request);
        besoin.setAgent(currentAgent);
        besoin.setStatut(StatutBesoin.CRÉÉ);
        
        BesoinExprime saved = besoinExprimeRepository.save(besoin);
        logger.info("Besoin créé avec succès (ID: {})", saved.getId());
        
        // Envoyer les emails de manière asynchrone (ne bloque pas la création)
        try {
            // Trouver le supérieur hiérarchique
            logger.info("Recherche du supérieur hiérarchique...");
            Optional<Agent> superior = findSuperiorAgent(currentAgent);
            
            // Envoyer email au supérieur pour validation (seulement s'il a un email)
            if (superior.isPresent()) {
                Agent superiorAgent = superior.get();
                logger.info("✅ Supérieur hiérarchique trouvé: {} {} (ID: {}, Poste: {}, Email: {})", 
                        superiorAgent.getNom(), superiorAgent.getPrenom(), superiorAgent.getId(),
                        superiorAgent.getPoste() != null ? superiorAgent.getPoste() : "NON DÉFINI",
                        superiorAgent.getEmail() != null ? superiorAgent.getEmail() : "NON CONFIGURÉ");
                
                if (superiorAgent.getEmail() != null && !superiorAgent.getEmail().trim().isEmpty()) {
                    try {
                        logger.info("Tentative d'envoi d'email de validation à {} ({})...", 
                                superiorAgent.getEmail(), superiorAgent.getNom() + " " + superiorAgent.getPrenom());
                        emailService.sendValidationEmail(superiorAgent, saved);
                        logger.info("✅✅✅ Email de validation envoyé avec succès au supérieur {} ({})", 
                                superiorAgent.getNom() + " " + superiorAgent.getPrenom(), superiorAgent.getEmail());
                    } catch (Exception e) {
                        logger.error("❌❌❌ Erreur lors de l'envoi de l'email de validation au supérieur (ID: {}, Email: {}): {}", 
                                superiorAgent.getId(), superiorAgent.getEmail(), e.getMessage(), e);
                        logger.error("Stack trace:", e);
                        // Ne pas bloquer la création si l'email échoue
                    }
                } else {
                    logger.warn("⚠️⚠️⚠️ Le supérieur hiérarchique {} {} (ID: {}) n'a pas d'email configuré. Email non envoyé.", 
                            superiorAgent.getNom(), superiorAgent.getPrenom(), superiorAgent.getId());
                }
            } else {
                logger.warn("⚠️⚠️⚠️ Aucun supérieur hiérarchique trouvé pour l'agent {} {} (ID: {})", 
                        currentAgent.getNom(), currentAgent.getPrenom(), currentAgent.getId());
            }
            
            // Envoyer email de confirmation à l'agent créateur (seulement s'il a un email)
            if (currentAgent.getEmail() != null && !currentAgent.getEmail().trim().isEmpty()) {
                try {
                    logger.info("Tentative d'envoi d'email de confirmation au créateur {} ({})...", 
                            currentAgent.getEmail(), currentAgent.getNom() + " " + currentAgent.getPrenom());
                    emailService.sendConfirmationEmail(currentAgent, saved);
                    logger.info("✅✅✅ Email de confirmation envoyé avec succès au créateur {} ({})", 
                            currentAgent.getNom() + " " + currentAgent.getPrenom(), currentAgent.getEmail());
                } catch (Exception e) {
                    logger.error("❌❌❌ Erreur lors de l'envoi de l'email de confirmation à l'agent créateur (ID: {}, Email: {}): {}", 
                            currentAgent.getId(), currentAgent.getEmail(), e.getMessage(), e);
                    logger.error("Stack trace:", e);
                    // Ne pas bloquer la création si l'email échoue
                }
            } else {
                logger.warn("⚠️⚠️⚠️ L'agent créateur {} {} (ID: {}) n'a pas d'email configuré. Email de confirmation non envoyé.", 
                        currentAgent.getNom(), currentAgent.getPrenom(), currentAgent.getId());
            }
        } catch (Exception e) {
            logger.error("❌❌❌ Erreur lors de l'envoi des emails pour le besoin créé (ID: {}): {}", 
                    saved.getId(), e.getMessage(), e);
            logger.error("Stack trace:", e);
            // Ne pas bloquer la création si l'envoi d'email échoue
        }
        
        logger.info("=== FIN DE CRÉATION DU BESOIN ===");
        return mapper.toDTO(saved);
    }
    
    @Override
    @Transactional
    public BesoinExprimeDTO updateBesoin(Long id, BesoinExprimeRequest request, String currentCin) {
        BesoinExprime besoin = besoinExprimeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("BesoinExprime", id));
        
        Agent currentAgent = agentRepository.findAgentByCIN(currentCin)
                .orElseThrow(() -> new RuntimeException("Agent non trouvé avec le CIN: " + currentCin));
        
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
    public BesoinExprimeDTO validerBesoin(Long id, String currentCin) {
        BesoinExprime besoin = besoinExprimeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("BesoinExprime", id));
        
        Agent currentAgent = agentRepository.findAgentByCIN(currentCin)
                .orElseThrow(() -> new RuntimeException("Agent non trouvé avec le CIN: " + currentCin));
        
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
    public BesoinExprimeDTO viserBesoin(Long id, String currentCin) {
        BesoinExprime besoin = besoinExprimeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("BesoinExprime", id));
        
        Agent currentAgent = agentRepository.findAgentByCIN(currentCin)
                .orElseThrow(() -> new RuntimeException("Agent non trouvé avec le CIN: " + currentCin));
        
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
    public BesoinExprimeDTO accepterBesoin(Long id, String currentCin) {
        BesoinExprime besoin = besoinExprimeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("BesoinExprime", id));
        
        Agent currentAgent = agentRepository.findAgentByCIN(currentCin)
                .orElseThrow(() -> new RuntimeException("Agent non trouvé avec le CIN: " + currentCin));
        
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
    public BesoinExprimeDTO refuserBesoin(Long id, String motif, String currentCin) {
        BesoinExprime besoin = besoinExprimeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("BesoinExprime", id));
        
        Agent currentAgent = agentRepository.findAgentByCIN(currentCin)
                .orElseThrow(() -> new RuntimeException("Agent non trouvé avec le CIN: " + currentCin));
        
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
    public BesoinExprimeDTO changeStatut(Long id, StatutBesoin nouveauStatut, String currentCin) {
        BesoinExprime besoin = besoinExprimeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("BesoinExprime", id));
        
        Agent currentAgent = agentRepository.findAgentByCIN(currentCin)
                .orElseThrow(() -> new RuntimeException("Agent non trouvé avec le CIN: " + currentCin));
        
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
    public void deleteBesoin(Long id, String currentCin) {
        BesoinExprime besoin = besoinExprimeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("BesoinExprime", id));
        
        Agent currentAgent = agentRepository.findAgentByCIN(currentCin)
                .orElseThrow(() -> new RuntimeException("Agent non trouvé avec le CIN: " + currentCin));
        
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
     * Trouve le supérieur hiérarchique d'un agent selon la hiérarchie
     * Logique : chef de service → chef de département → directeur → ADMIN
     */
    private Optional<Agent> findSuperiorAgent(Agent agent) {
        logger.info("🔍 Recherche du supérieur hiérarchique pour l'agent {} {} (ID: {})", 
                agent.getNom(), agent.getPrenom(), agent.getId());
        
        // 1. Si l'agent a un service, chercher un chef de service dans le même service
        if (agent.getService() != null) {
            Long serviceId = agent.getService().getId();
            logger.info("  → Agent a un service (ID: {}), recherche d'un chef de service...", serviceId);
            List<Agent> chefsServiceRaw = agentRepository.findChefServiceByServiceId(serviceId);
            logger.info("  → Nombre de chefs de service trouvés: {}", chefsServiceRaw.size());
            
            // Exclure l'agent créateur lui-même et l'administrateur par défaut
            final int totalChefs = chefsServiceRaw.size();
            List<Agent> chefsService = chefsServiceRaw.stream()
                    .filter(chef -> !chef.getId().equals(agent.getId())) // Exclure l'agent créateur
                    .filter(chef -> chef.getRole() != Role.ADMIN || totalChefs == 1) // Préférer non-ADMIN sauf si c'est le seul
                    .collect(java.util.stream.Collectors.toList());
            
            if (!chefsService.isEmpty()) {
                // Préférer un chef qui a un email
                Optional<Agent> chefAvecEmail = chefsService.stream()
                        .filter(chef -> chef.getEmail() != null && !chef.getEmail().trim().isEmpty())
                        .findFirst();
                
                Agent chef = chefAvecEmail.orElse(chefsService.get(0));
                logger.info("  ✅ Chef de service trouvé: {} {} (ID: {}, Poste: {}, Email: {})", 
                        chef.getNom(), chef.getPrenom(), chef.getId(), 
                        chef.getPoste() != null ? chef.getPoste() : "NON DÉFINI",
                        chef.getEmail() != null ? chef.getEmail() : "NON CONFIGURÉ");
                // Retourner le chef même s'il n'a pas d'email (pour qu'il puisse voir les besoins)
                return Optional.of(chef);
            } else {
                logger.warn("  ⚠️ Aucun chef de service trouvé pour le service ID: {} (après filtrage)", serviceId);
            }
        } else {
            logger.warn("  ⚠️ Agent n'a pas de service assigné");
        }
        
        // 2. Si l'agent a un département, chercher un chef de département dans le même département
        if (agent.getDepartement() != null) {
            Long departementId = agent.getDepartement().getId();
            logger.info("  → Agent a un département (ID: {}), recherche d'un chef de département...", departementId);
            List<Agent> chefsDepartementRaw = agentRepository.findChefDepartementByDepartementId(departementId);
            logger.info("  → Nombre de chefs de département trouvés: {}", chefsDepartementRaw.size());
            
            // Exclure l'agent créateur lui-même
            List<Agent> chefsDepartement = chefsDepartementRaw.stream()
                    .filter(chef -> !chef.getId().equals(agent.getId()))
                    .collect(java.util.stream.Collectors.toList());
            
            if (!chefsDepartement.isEmpty()) {
                // Préférer un chef qui a un email
                Optional<Agent> chefAvecEmail = chefsDepartement.stream()
                        .filter(chef -> chef.getEmail() != null && !chef.getEmail().trim().isEmpty())
                        .findFirst();
                
                Agent chef = chefAvecEmail.orElse(chefsDepartement.get(0));
                logger.info("  ✅ Chef de département trouvé: {} {} (ID: {}, Poste: {}, Email: {})", 
                        chef.getNom(), chef.getPrenom(), chef.getId(),
                        chef.getPoste() != null ? chef.getPoste() : "NON DÉFINI",
                        chef.getEmail() != null ? chef.getEmail() : "NON CONFIGURÉ");
                return Optional.of(chef);
            } else {
                logger.warn("  ⚠️ Aucun chef de département trouvé pour le département ID: {} (après filtrage)", departementId);
            }
        } else {
            logger.warn("  ⚠️ Agent n'a pas de département assigné");
        }
        
        // 3. Si l'agent a une direction, chercher un directeur dans la même direction
        if (agent.getDirection() != null) {
            Long directionId = agent.getDirection().getId();
            logger.info("  → Agent a une direction (ID: {}), recherche d'un directeur...", directionId);
            List<Agent> directeursRaw = agentRepository.findDirecteurByDirectionId(directionId);
            logger.info("  → Nombre de directeurs trouvés: {}", directeursRaw.size());
            
            // Exclure l'agent créateur lui-même
            List<Agent> directeurs = directeursRaw.stream()
                    .filter(dir -> !dir.getId().equals(agent.getId()))
                    .collect(java.util.stream.Collectors.toList());
            
            if (!directeurs.isEmpty()) {
                // Préférer un directeur qui a un email
                Optional<Agent> directeurAvecEmail = directeurs.stream()
                        .filter(dir -> dir.getEmail() != null && !dir.getEmail().trim().isEmpty())
                        .findFirst();
                
                Agent directeur = directeurAvecEmail.orElse(directeurs.get(0));
                logger.info("  ✅ Directeur trouvé: {} {} (ID: {}, Poste: {}, Email: {})", 
                        directeur.getNom(), directeur.getPrenom(), directeur.getId(),
                        directeur.getPoste() != null ? directeur.getPoste() : "NON DÉFINI",
                        directeur.getEmail() != null ? directeur.getEmail() : "NON CONFIGURÉ");
                return Optional.of(directeur);
            } else {
                logger.warn("  ⚠️ Aucun directeur trouvé pour la direction ID: {} (après filtrage)", directionId);
            }
        } else {
            logger.warn("  ⚠️ Agent n'a pas de direction assignée");
        }
        
        // 4. En dernier recours, chercher un ADMIN
        logger.info("  → Recherche d'un ADMIN comme supérieur de dernier recours...");
        List<Agent> admins = agentRepository.findByRole(Role.ADMIN);
        logger.info("  → Nombre d'ADMIN trouvés: {}", admins.size());
        if (!admins.isEmpty()) {
            // Chercher un ADMIN (avec ou sans email)
            Agent admin = admins.get(0);
            logger.info("  ✅ ADMIN trouvé: {} {} (ID: {}, Email: {})", 
                    admin.getNom(), admin.getPrenom(), admin.getId(), 
                    admin.getEmail() != null ? admin.getEmail() : "NON CONFIGURÉ");
            return Optional.of(admin);
        }
        
        logger.warn("  ❌ Aucun supérieur hiérarchique trouvé pour l'agent {} {} (ID: {})", 
                agent.getNom(), agent.getPrenom(), agent.getId());
        return Optional.empty();
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

