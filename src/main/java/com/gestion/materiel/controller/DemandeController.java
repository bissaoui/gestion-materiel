package com.gestion.materiel.controller;

import com.gestion.materiel.model.Demande;
import com.gestion.materiel.service.DemandeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/demandes")
@CrossOrigin("*") // Permettre l'accès depuis d'autres domaines
public class DemandeController {
    private final DemandeService demandeService;

    public DemandeController(DemandeService demandeService) {
        this.demandeService = demandeService;
    }

    @GetMapping
    public List<Demande> getAllDemandes() {
        return demandeService.getAllDemandes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Demande> getDemandeById(@PathVariable Long id) {
        Optional<Demande> demande = demandeService.getDemandeById(id);
        return demande.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Demande> createDemande(@RequestBody Demande demande) {
        Demande newDemande = demandeService.saveDemande(demande);
        return ResponseEntity.ok(newDemande);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Demande> updateDemande(@PathVariable Long id, @RequestBody Demande demande) {
        if (!demandeService.getDemandeById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        demande.setId(id);
        Demande updatedDemande = demandeService.saveDemande(demande);
        return ResponseEntity.ok(updatedDemande);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDemande(@PathVariable Long id) {
        if (!demandeService.getDemandeById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        demandeService.deleteDemande(id);
        return ResponseEntity.noContent().build();
    }
}
