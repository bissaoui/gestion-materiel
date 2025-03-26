package com.gestion.materiel.controller;

import com.gestion.materiel.Dto.AgentDto;
import com.gestion.materiel.Dto.DemandeDto;
import com.gestion.materiel.Dto.DepartementDto;
import com.gestion.materiel.model.Demande;
import com.gestion.materiel.model.Departement;
import com.gestion.materiel.service.DemandeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/demandes")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class DemandeController {
    private final DemandeService demandeService;

    public DemandeController(DemandeService demandeService) {
        this.demandeService = demandeService;
    }

    @GetMapping
    public List<DemandeDto> getAllDemandes() {
        List<Demande> demandes = demandeService.getAllDemandes();
        return demandes.stream().map(DemandeDto::new).collect(Collectors.toList());


    }


    @GetMapping("/{id}")
    public ResponseEntity<DemandeDto> getDemandeById(@PathVariable Long id) {
        Optional<Demande> demande = demandeService.getDemandeById(id);
        return demande.map(dep -> ResponseEntity.ok(new DemandeDto(dep)))
                .orElseGet(() -> ResponseEntity.notFound().build());
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
    @PutMapping("/{id}/validate")
    public ResponseEntity<DemandeDto> validateDemande(@PathVariable Long id) {
        Optional<Demande> demandeOptional = demandeService.getDemandeById(id);

        if (!demandeOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Demande demande = demandeOptional.get();
        demande.setValidation(true); // Mettre à jour uniquement la validation à true
        Demande updatedDemande = demandeService.saveDemande(demande);

        return demandeOptional.map(dep -> ResponseEntity.ok(new DemandeDto(dep)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
