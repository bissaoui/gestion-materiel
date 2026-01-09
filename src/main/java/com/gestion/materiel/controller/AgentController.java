package com.gestion.materiel.controller;


import com.gestion.materiel.Dto.AgentDto;
import com.gestion.materiel.model.Agent;
import com.gestion.materiel.service.AgentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.SQLGrammarException;

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
    public ResponseEntity<?> createAgent(@RequestBody AgentDto agentDto) {
        try {
            Agent newAgent = agentService.saveAgent(agentDto);
            return ResponseEntity.ok(new AgentDto(newAgent));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            String errorMsg = e.getMessage();
            if (errorMsg != null && errorMsg.contains("Duplicate entry")) {
                if (errorMsg.contains("CIN") || errorMsg.contains("cin")) {
                    return ResponseEntity.status(409).body("Un agent avec ce CIN existe déjà");
                } else if (errorMsg.contains("email")) {
                    return ResponseEntity.status(409).body("Un agent avec cet email existe déjà");
                } else if (errorMsg.contains("matricule")) {
                    return ResponseEntity.status(409).body("Un agent avec ce matricule existe déjà");
                }
            }
            return ResponseEntity.status(400).body("Erreur de contrainte de base de données: " + (e.getCause() != null ? e.getCause().getMessage() : e.getMessage()));
        } catch (org.hibernate.exception.ConstraintViolationException e) {
            return ResponseEntity.status(400).body("Erreur de contrainte: " + (e.getCause() != null ? e.getCause().getMessage() : e.getMessage()));
        } catch (org.hibernate.exception.SQLGrammarException e) {
            return ResponseEntity.status(500).body("Erreur SQL: " + (e.getCause() != null ? e.getCause().getMessage() : e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace(); // Log pour debugging
            String errorMsg = "Erreur lors de la création de l'agent: " + e.getMessage();
            if (e.getCause() != null) {
                errorMsg += " (Cause: " + e.getCause().getMessage() + ")";
            }
            return ResponseEntity.status(500).body(errorMsg);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAgent(@PathVariable Long id, @RequestBody AgentDto agentDto) {
        if (!agentService.getAgentById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        try {
            Agent updatedAgent = agentService.updateAgent(id, agentDto);
            return ResponseEntity.ok(new AgentDto(updatedAgent));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur lors de la mise à jour de l'agent: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAgent(@PathVariable Long id) {
        if (!agentService.getAgentById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        try {
            agentService.deleteAgent(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalStateException e) {
            // Agent is being used - return conflict with detailed message
            return ResponseEntity.status(409).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            // Agent not found or invalid
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            // Database constraint violation
            String errorMsg = "Impossible de supprimer cet agent. Il est utilisé dans d'autres enregistrements.";
            if (e.getCause() != null && e.getCause().getMessage() != null) {
                String causeMsg = e.getCause().getMessage();
                if (causeMsg.contains("foreign key") || causeMsg.contains("constraint")) {
                    errorMsg = "Impossible de supprimer cet agent. Il est lié à d'autres données (matériels, besoins exprimés, etc.).";
                }
            }
            return ResponseEntity.status(409).body(errorMsg);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erreur lors de la suppression de l'agent: " + e.getMessage());
        }
    }
}
