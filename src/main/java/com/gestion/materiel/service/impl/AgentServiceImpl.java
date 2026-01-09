package com.gestion.materiel.service.impl;

import com.gestion.materiel.Dto.AgentDto;
import com.gestion.materiel.model.Agent;
import com.gestion.materiel.model.Direction;
import com.gestion.materiel.model.Departement;
import com.gestion.materiel.model.Service;
import com.gestion.materiel.repository.AgentRepository;
import com.gestion.materiel.repository.DirectionRepository;
import com.gestion.materiel.repository.DepartementRepository;
import com.gestion.materiel.repository.ServiceRepository;
import com.gestion.materiel.repository.MaterielRepository;
import com.gestion.materiel.repository.BesoinExprimeRepository;
import com.gestion.materiel.service.AgentService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class AgentServiceImpl implements AgentService, UserDetailsService {
    private final AgentRepository agentRepository;
    private final DirectionRepository directionRepository;
    private final DepartementRepository departementRepository;
    private final ServiceRepository serviceRepository;
    private final MaterielRepository materielRepository;
    private final BesoinExprimeRepository besoinExprimeRepository;

    public AgentServiceImpl(AgentRepository agentRepository, DirectionRepository directionRepository, DepartementRepository departementRepository, ServiceRepository serviceRepository, MaterielRepository materielRepository, BesoinExprimeRepository besoinExprimeRepository) {
        this.agentRepository = agentRepository;
        this.directionRepository = directionRepository;
        this.departementRepository = departementRepository;
        this.serviceRepository = serviceRepository;
        this.materielRepository = materielRepository;
        this.besoinExprimeRepository = besoinExprimeRepository;
    }

    @Override
    public List<Agent> getAllAgents() {
        return agentRepository.findAll();
    }

    @Override
    public Optional<Agent> getAgentById(Long id) {
        return agentRepository.findById(id);
    }

    @Override
    public void deleteAgent(Long id) {
        // Vérifier si l'agent existe
        Agent agent = agentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Agent introuvable avec l'ID: " + id));
        
        // Vérifier les contraintes avant suppression
        StringBuilder errorMessage = new StringBuilder();
        boolean hasConstraints = false;
        
        // Vérifier les matériels affectés
        long materielsCount = materielRepository.countByAgentId(id);
        if (materielsCount > 0) {
            errorMessage.append(materielsCount).append(" matériel(s) affecté(s)");
            hasConstraints = true;
        }
        
        // Vérifier les besoins exprimés (créateur)
        long besoinsCount = besoinExprimeRepository.countByAgentId(id);
        if (besoinsCount > 0) {
            if (hasConstraints) errorMessage.append(", ");
            errorMessage.append(besoinsCount).append(" besoin(s) exprimé(s)");
            hasConstraints = true;
        }
        
        // Vérifier les validations
        long validationsCount = besoinExprimeRepository.countByValidateurId(id);
        if (validationsCount > 0) {
            if (hasConstraints) errorMessage.append(", ");
            errorMessage.append(validationsCount).append(" validation(s)");
            hasConstraints = true;
        }
        
        // Vérifier les visas
        long visasCount = besoinExprimeRepository.countByViseurId(id);
        if (visasCount > 0) {
            if (hasConstraints) errorMessage.append(", ");
            errorMessage.append(visasCount).append(" visa(s)");
            hasConstraints = true;
        }
        
        // Vérifier les décisions
        long decisionsCount = besoinExprimeRepository.countByDecideurId(id);
        if (decisionsCount > 0) {
            if (hasConstraints) errorMessage.append(", ");
            errorMessage.append(decisionsCount).append(" décision(s)");
            hasConstraints = true;
        }
        
        // Si des contraintes existent, lancer une exception avec le message détaillé
        if (hasConstraints) {
            throw new IllegalStateException("Impossible de supprimer cet agent. Il est utilisé dans : " + errorMessage.toString() + ".");
        }
        
        // Si aucune contrainte, procéder à la suppression
        agentRepository.deleteById(id);
    }

    @Override
    public Optional<Agent> findAgentByCIN(String cin) {
        return agentRepository.findAgentByCIN(cin);
    }

    @Override
    public Agent updateAgent(Long id, AgentDto agentDto) {
        Agent agent = agentRepository.findById(id).orElseThrow(() -> new RuntimeException("Agent not found"));
        
        if (agentDto.getNom() != null && !agentDto.getNom().trim().isEmpty()) {
            agent.setNom(agentDto.getNom().trim());
        }
        if (agentDto.getPrenom() != null && !agentDto.getPrenom().trim().isEmpty()) {
            agent.setPrenom(agentDto.getPrenom().trim());
        }
        if (agentDto.getCin() != null && !agentDto.getCin().trim().isEmpty()) {
            agent.setCIN(agentDto.getCin().trim());
        }
        if (agentDto.getEmail() != null && !agentDto.getEmail().trim().isEmpty()) {
            agent.setEmail(agentDto.getEmail().trim());
        } else if (agentDto.getEmail() != null && agentDto.getEmail().trim().isEmpty()) {
            agent.setEmail(null);
        }
        if (agentDto.getMatricule() != null && !agentDto.getMatricule().trim().isEmpty()) {
            agent.setMatricule(agentDto.getMatricule().trim());
        } else if (agentDto.getMatricule() != null && agentDto.getMatricule().trim().isEmpty()) {
            agent.setMatricule(null);
        }
        if (agentDto.getDateNaissance() != null) {
            agent.setDateNaissance(agentDto.getDateNaissance());
        }
        
        // Gérer le sexe avec validation
        if (agentDto.getSexe() != null && !agentDto.getSexe().trim().isEmpty()) {
            try {
                agent.setSexe(com.gestion.materiel.model.Sexe.valueOf(agentDto.getSexe().trim().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Sexe invalide: " + agentDto.getSexe() + ". Valeurs acceptées: MASCULIN, FEMININ");
            }
        } else if (agentDto.getSexe() != null && agentDto.getSexe().trim().isEmpty()) {
            agent.setSexe(null);
        }
        
        if (agentDto.getPassword() != null && !agentDto.getPassword().trim().isEmpty()) {
            // Encoder le mot de passe avec BCrypt si il est modifié
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            agent.setPassword(passwordEncoder.encode(agentDto.getPassword()));
        }
        
        if (agentDto.getPoste() != null && !agentDto.getPoste().trim().isEmpty()) {
            agent.setPoste(agentDto.getPoste().trim());
        } else if (agentDto.getPoste() != null && agentDto.getPoste().trim().isEmpty()) {
            agent.setPoste(null);
        }
        
        // Gérer le rôle avec validation
        if (agentDto.getRole() != null && !agentDto.getRole().trim().isEmpty()) {
            try {
                agent.setRole(com.gestion.materiel.model.Role.valueOf(agentDto.getRole().trim().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Rôle invalide: " + agentDto.getRole() + ". Valeurs acceptées: ADMIN, USER");
            }
        }
        
        if (agentDto.getDirectionId() != null) {
            Direction direction = directionRepository.findById(agentDto.getDirectionId()).orElse(null);
            agent.setDirection(direction);
        } else {
            agent.setDirection(null);
        }
        if (agentDto.getDepartementId() != null) {
            Departement departement = departementRepository.findById(agentDto.getDepartementId()).orElse(null);
            agent.setDepartement(departement);
        } else {
            agent.setDepartement(null);
        }
        if (agentDto.getServiceId() != null) {
            Service service = serviceRepository.findById(agentDto.getServiceId()).orElse(null);
            agent.setService(service);
        } else {
            agent.setService(null);
        }
        
        return agentRepository.save(agent);
    }



    @Override
    public Agent saveAgent(AgentDto agentDto) {
        // Validation des champs requis
        if (agentDto.getNom() == null || agentDto.getNom().trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom est requis");
        }
        if (agentDto.getPrenom() == null || agentDto.getPrenom().trim().isEmpty()) {
            throw new IllegalArgumentException("Le prénom est requis");
        }
        if (agentDto.getCin() == null || agentDto.getCin().trim().isEmpty()) {
            throw new IllegalArgumentException("Le CIN est requis");
        }
        if (agentDto.getPassword() == null || agentDto.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Le mot de passe est requis");
        }
        
        Agent agent = new Agent();
        agent.setNom(agentDto.getNom().trim());
        agent.setPrenom(agentDto.getPrenom().trim());
        agent.setCIN(agentDto.getCin().trim());
        
        // Gérer les champs optionnels
        if (agentDto.getEmail() != null && !agentDto.getEmail().trim().isEmpty()) {
            agent.setEmail(agentDto.getEmail().trim());
        }
        if (agentDto.getMatricule() != null && !agentDto.getMatricule().trim().isEmpty()) {
            agent.setMatricule(agentDto.getMatricule().trim());
        }
        agent.setDateNaissance(agentDto.getDateNaissance());
        
        // Gérer le sexe (peut être null ou vide)
        if (agentDto.getSexe() != null && !agentDto.getSexe().trim().isEmpty()) {
            try {
                agent.setSexe(com.gestion.materiel.model.Sexe.valueOf(agentDto.getSexe().trim().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Sexe invalide: " + agentDto.getSexe() + ". Valeurs acceptées: MASCULIN, FEMININ");
            }
        }
        
        // Encoder le mot de passe avec BCrypt
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        agent.setPassword(passwordEncoder.encode(agentDto.getPassword()));
        
        // Gérer le poste (peut être null)
        if (agentDto.getPoste() != null && !agentDto.getPoste().trim().isEmpty()) {
            agent.setPoste(agentDto.getPoste().trim());
        }
        
        // Gérer le rôle (par défaut USER si non fourni)
        if (agentDto.getRole() != null && !agentDto.getRole().trim().isEmpty()) {
            try {
                agent.setRole(com.gestion.materiel.model.Role.valueOf(agentDto.getRole().trim().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Rôle invalide: " + agentDto.getRole() + ". Valeurs acceptées: ADMIN, USER");
            }
        } else {
            agent.setRole(com.gestion.materiel.model.Role.USER);
        }
        
        // Gérer les relations
        if (agentDto.getDirectionId() != null) {
            Direction direction = directionRepository.findById(agentDto.getDirectionId()).orElse(null);
            agent.setDirection(direction);
        }
        if (agentDto.getDepartementId() != null) {
            Departement departement = departementRepository.findById(agentDto.getDepartementId()).orElse(null);
            agent.setDepartement(departement);
        }
        if (agentDto.getServiceId() != null) {
            Service service = serviceRepository.findById(agentDto.getServiceId()).orElse(null);
            agent.setService(service);
        }
        
        return agentRepository.save(agent);
    }

    @Override
    public UserDetails loadUserByUsername(String cin) throws UsernameNotFoundException {
        Agent agent = agentRepository.findAgentByCIN(cin)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return org.springframework.security.core.userdetails.User.builder()
                .username(agent.getCIN())
                .password(agent.getPassword())
                .authorities("ROLE_" + agent.getRole())  // Ensure roles are prefixed with "ROLE_"
                .build();
    }
}
