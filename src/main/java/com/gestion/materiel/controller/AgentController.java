package com.gestion.materiel.controller;


import com.gestion.materiel.Dto.AgentDto;
import com.gestion.materiel.Dto.DepartementDto;
import com.gestion.materiel.model.Agent;
import com.gestion.materiel.model.Departement;
import com.gestion.materiel.service.AgentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/agents")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})

public class AgentController {
    private final AgentService agentService;

    public AgentController(AgentService agentService) {
        this.agentService = agentService;
    }

    @GetMapping
    public List<AgentDto> getAllAgents() {
        List<Agent> agents = agentService.getAllAgents();
        return agents.stream().map(AgentDto::new).collect(Collectors.toList());

    }

    @GetMapping("/{id}")
    public ResponseEntity<Agent> getAgentById(@PathVariable Long id) {
        Optional<Agent> agent = agentService.getAgentById(id);
        return agent.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @GetMapping("/cin/{cin}")
    public ResponseEntity<AgentDto> getAgentById(@PathVariable String cin) {
        Optional<Agent> agent = agentService.findAgentByCIN(cin);
        return agent.map(ag -> ResponseEntity.ok(new AgentDto(ag)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Agent> createAgent(@RequestBody Agent agent) {
        Agent newAgent = agentService.saveAgent(agent);
        return ResponseEntity.ok(newAgent);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Agent> updateAgent(@PathVariable Long id, @RequestBody Agent agent) {
        if (!agentService.getAgentById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        agent.setId(id);
        Agent updatedAgent = agentService.saveAgent(agent);
        return ResponseEntity.ok(updatedAgent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAgent(@PathVariable Long id) {
        if (!agentService.getAgentById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        agentService.deleteAgent(id);
        return ResponseEntity.noContent().build();
    }
}
