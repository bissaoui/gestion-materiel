package com.gestion.materiel.service.impl;

import com.gestion.materiel.model.Agent;
import com.gestion.materiel.repository.AgentRepository;
import com.gestion.materiel.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final AgentRepository agentRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(AgentRepository agentRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.agentRepository = agentRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public String register(Agent agent) {
        // Check if the username already exists
        if (agentRepository.findAgentByCIN(agent.getCIN()).isPresent()) {
            throw new RuntimeException("Username already taken!");
        }

        agent.setPassword(passwordEncoder.encode(agent.getPassword()));
        agentRepository.save(agent);
        return "User registered successfully!";
    }

    public String login(String username, String password) {
        Agent agent = agentRepository.findAgentByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(password, agent.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return jwtUtil.generateToken(agent.getCIN(), String.valueOf(agent.getRole()),agent.getUsername());
    }


    public Optional<Agent> findUserByName(String username) {
        return agentRepository.findAgentByUsername(username);
    }
}