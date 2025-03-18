package com.gestion.materiel.service;

import com.gestion.materiel.model.Agent;
import java.util.List;
import java.util.Optional;

public interface AgentService {
    List<Agent> getAllAgents();
    Optional<Agent> getAgentById(Long id);
    Agent saveAgent(Agent agent);
    void deleteAgent(Long id);

    Optional<Agent> findAgentByCIN(String cin);
    Optional<Agent> findAgentByUsername(String username);
}
