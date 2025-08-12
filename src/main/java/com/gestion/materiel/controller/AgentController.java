package com.gestion.materiel.controller;


import com.gestion.materiel.Dto.AgentDto;
import com.gestion.materiel.model.Agent;
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
    public ResponseEntity<AgentDto> getAgentById(@PathVariable Long id) {
        Optional<Agent> agent = agentService.getAgentById(id);
        return agent.map(dep -> ResponseEntity.ok(new AgentDto(dep)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @GetMapping("/cin/{cin}")
    public ResponseEntity<AgentDto> getAgentById(@PathVariable String cin) {
        Optional<Agent> agent = agentService.findAgentByCIN(cin);
        return agent.map(ag -> ResponseEntity.ok(new AgentDto(ag)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AgentDto> createAgent(@RequestBody AgentDto agentDto) {
        Agent newAgent = agentService.saveAgent(agentDto);
        return ResponseEntity.ok(new AgentDto(newAgent));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AgentDto> updateAgent(@PathVariable Long id, @RequestBody AgentDto agentDto) {
        if (!agentService.getAgentById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Agent updatedAgent = agentService.updateAgent(id, agentDto);
        return ResponseEntity.ok(new AgentDto(updatedAgent));
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
