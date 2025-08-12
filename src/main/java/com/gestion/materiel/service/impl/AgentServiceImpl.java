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

    public AgentServiceImpl(AgentRepository agentRepository, DirectionRepository directionRepository, DepartementRepository departementRepository, ServiceRepository serviceRepository) {
        this.agentRepository = agentRepository;
        this.directionRepository = directionRepository;
        this.departementRepository = departementRepository;
        this.serviceRepository = serviceRepository;
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
        agentRepository.deleteById(id);
    }

    @Override
    public Optional<Agent> findAgentByCIN(String cin) {
        return agentRepository.findAgentByCIN(cin);
    }
    @Override
    public Optional<Agent> findAgentByUsername(String username) {
        return agentRepository.findAgentByUsername(username);
    }

    @Override
    public Agent updateAgent(Long id, AgentDto agentDto) {
        Agent agent = agentRepository.findById(id).orElseThrow(() -> new RuntimeException("Agent not found"));
        if (agentDto.getNom() != null) agent.setNom(agentDto.getNom());
        if (agentDto.getCin() != null) agent.setCIN(agentDto.getCin());
        if (agentDto.getUsername() != null) agent.setUsername(agentDto.getUsername());
        if (agentDto.getPassword() != null && !agentDto.getPassword().trim().isEmpty()) {
            // Encoder le mot de passe avec BCrypt si il est modifié
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            agent.setPassword(passwordEncoder.encode(agentDto.getPassword()));
        }
        if (agentDto.getPoste() != null) agent.setPoste(agentDto.getPoste());
        if (agentDto.getRole() != null) agent.setRole(com.gestion.materiel.model.Role.valueOf(agentDto.getRole()));
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
    public Agent saveAgent(AgentDto agentDto) {
        Agent agent = new Agent();
        agent.setNom(agentDto.getNom());
        agent.setCIN(agentDto.getCin());
        agent.setUsername(agentDto.getUsername());
        
        // Encoder le mot de passe avec BCrypt
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        agent.setPassword(passwordEncoder.encode(agentDto.getPassword()));
        
        agent.setPoste(agentDto.getPoste());
        if (agentDto.getRole() != null) {
            agent.setRole(com.gestion.materiel.model.Role.valueOf(agentDto.getRole()));
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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Agent agent = agentRepository.findAgentByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return org.springframework.security.core.userdetails.User.builder()
                .username(agent.getUsername())
                .password(agent.getPassword())
                .authorities("ROLE_" + agent.getRole())  // Ensure roles are prefixed with "ROLE_"
                .build();
    }
}
