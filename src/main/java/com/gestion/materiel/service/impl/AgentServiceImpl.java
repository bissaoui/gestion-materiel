package com.gestion.materiel.service.impl;

import com.gestion.materiel.model.Agent;
import com.gestion.materiel.repository.AgentRepository;
import com.gestion.materiel.service.AgentService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.List;
import java.util.Optional;

@Service
public class AgentServiceImpl implements AgentService, UserDetailsService {
    private final AgentRepository agentRepository;

    public AgentServiceImpl(AgentRepository agentRepository) {
        this.agentRepository = agentRepository;
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
    public Agent saveAgent(Agent agent) {
        return agentRepository.save(agent);
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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Agent agent = agentRepository.findAgentByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return org.springframework.security.core.userdetails.User.builder()
                .username(agent.getUsername())
                .password(agent.getPassword())
                .authorities("ROLE_" + agent.getRole())  // Ensure roles are prefixed with "ROLE_"
                .build();    }
}
