package com.gestion.materiel.controller;


import com.gestion.materiel.model.Agent;
import com.gestion.materiel.repository.AgentRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final AgentRepository agentRepository;

    public AdminController(AgentRepository agentRepository) {
        this.agentRepository = agentRepository;
    }

    @GetMapping("/users")
    public List<Agent> getAllUsers() {
        return agentRepository.findAll();
    }

    @PostMapping("/create")
    public String createUser(@RequestBody Agent user) {
        agentRepository.save(user);
        return "User created!";
    }
}
