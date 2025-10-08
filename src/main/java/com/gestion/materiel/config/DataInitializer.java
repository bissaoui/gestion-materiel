package com.gestion.materiel.config;

import com.gestion.materiel.model.*;
import com.gestion.materiel.repository.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final AgentRepository agentRepository;
    private final DirectionRepository directionRepository;
    private final DepartementRepository departementRepository;
    private final ServiceRepository serviceRepository;
    private final PasswordEncoder passwordEncoder; // optionnel si tu veux encoder les mots de passe

    @PostConstruct
    public void init() {
        if (agentRepository.count() == 0) {

            // === Directions ===
            Direction dir1 = new Direction();
            dir1.setLibelle("direction administrative et financière");
            dir1.setAbreviation("DAF");
            dir1.setAgents(new ArrayList<>());
            directionRepository.save(dir1);

            Direction dir2 = new Direction();
            dir2.setLibelle("direction de developpement des zones oasiennes");
            dir2.setAbreviation("DDZO");
            dir2.setAgents(new ArrayList<>());
            directionRepository.save(dir2);

            // === Départements ===
            Departement dep1 = new Departement();
            dep1.setLibelle("Département Finances et Système d’Information");
            dep1.setAbreviation("DFSI");
            dep1.setDirection(dir1);
            dep1.setAgents(new ArrayList<>());
            departementRepository.save(dep1);

            Departement dep2 = new Departement();
            dep2.setLibelle("Département Administratif");
            dep2.setAbreviation("DA");
            dep2.setDirection(dir1);
            dep2.setAgents(new ArrayList<>());
            departementRepository.save(dep2);

            // === Services ===
            Service serv1 = new Service();
            serv1.setLibelle("Service organisation et systèmes d'informations");
            serv1.setAbreviation("SOSI");
            serv1.setDepartement(dep1);
            serv1.setAgents(new ArrayList<>());
            serviceRepository.save(serv1);

            Service serv2 = new Service();
            serv2.setLibelle("service de gestion de ressources humaines");
            serv2.setAbreviation("SGRH");
            serv2.setDepartement(dep2);
            serv2.setAgents(new ArrayList<>());
            serviceRepository.save(serv2);

            // === Agents ===
            Agent admin = new Agent();
            admin.setCIN("AA123456");
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setNom("Administrateur");
            admin.setPoste("Chef de service");
            admin.setRole(Role.ADMIN);
            admin.setDirection(dir1);
            admin.setDepartement(dep1);
            admin.setService(serv1);
            agentRepository.save(admin);

            Agent user = new Agent();
            user.setCIN("BB654321");
            user.setUsername("yassine");
            user.setPassword(passwordEncoder.encode("yassine123"));
            user.setNom("Yassine Bissaoui");
            user.setPoste("Développeur");
            user.setRole(Role.USER);
            user.setDirection(dir1);
            user.setDepartement(dep1);
            user.setService(serv1);
            agentRepository.save(user);

            System.out.println("✅ Données de test initialisées !");
        }
    }
}
