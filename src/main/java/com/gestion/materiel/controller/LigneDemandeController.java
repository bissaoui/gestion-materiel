package com.gestion.materiel.controller;

import com.gestion.materiel.model.LigneDemande;
import com.gestion.materiel.service.LigneDemandeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ligne-demandes")
@CrossOrigin("*") // Permettre l'accès depuis d'autres domaines
public class LigneDemandeController {
    private final LigneDemandeService ligneDemandeService;

    public LigneDemandeController(LigneDemandeService ligneDemandeService) {
        this.ligneDemandeService = ligneDemandeService;
    }

    @GetMapping
    public List<LigneDemande> getAllLigneDemandes() {
        return ligneDemandeService.getAllLigneDemandes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LigneDemande> getLigneDemandeById(@PathVariable Long id) {
        Optional<LigneDemande> ligneDemande = ligneDemandeService.getLigneDemandeById(id);
        return ligneDemande.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<LigneDemande> createLigneDemande(@RequestBody LigneDemande ligneDemande) {
        LigneDemande newLigneDemande = ligneDemandeService.saveLigneDemande(ligneDemande);
        return ResponseEntity.ok(newLigneDemande);
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<LigneDemande>> createLignesDemande(@RequestBody List<LigneDemande> ligneDemandes) {
        List<LigneDemande> newLigneDemandes = ligneDemandeService.saveLignesDemande(ligneDemandes);
        return ResponseEntity.ok(newLigneDemandes);
    }
    @PutMapping("/{id}")
    public ResponseEntity<LigneDemande> updateLigneDemande(@PathVariable Long id, @RequestBody LigneDemande ligneDemande) {
        if (!ligneDemandeService.getLigneDemandeById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        ligneDemande.setId(id);
        LigneDemande updatedLigneDemande = ligneDemandeService.saveLigneDemande(ligneDemande);
        return ResponseEntity.ok(updatedLigneDemande);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLigneDemande(@PathVariable Long id) {
        if (!ligneDemandeService.getLigneDemandeById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        ligneDemandeService.deleteLigneDemande(id);
        return ResponseEntity.noContent().build();
    }
}
