package com.gestion.materiel.controller;

import com.gestion.materiel.Dto.BesoinExprimeDTO;
import com.gestion.materiel.Dto.BesoinExprimeRequest;
import com.gestion.materiel.model.StatutBesoin;
import com.gestion.materiel.service.BesoinExprimeService;
import com.gestion.materiel.service.impl.BesoinExprimeServiceImpl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/besoins-exprimes")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class BesoinExprimeController {
    
    private final BesoinExprimeService besoinExprimeService;
    
    public BesoinExprimeController(BesoinExprimeService besoinExprimeService) {
        this.besoinExprimeService = besoinExprimeService;
    }
    
    /**
     * Récupère le CIN de l'utilisateur actuel depuis le contexte de sécurité
     */
    private String getCurrentCin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        throw new RuntimeException("Utilisateur non authentifié");
    }
    
    /**
     * GET /api/besoins-exprimes - Liste paginée avec filtres optionnels
     */
    @GetMapping
    public ResponseEntity<Page<BesoinExprimeDTO>> getAllBesoins(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) StatutBesoin statut,
            @RequestParam(required = false) Long agentId) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BesoinExprimeDTO> besoins = besoinExprimeService.getAllBesoins(pageable, statut, agentId);
        return ResponseEntity.ok(besoins);
    }
    
    /**
     * GET /api/besoins-exprimes/{id} - Détails d'un besoin
     */
    @GetMapping("/{id}")
    public ResponseEntity<BesoinExprimeDTO> getBesoinById(@PathVariable Long id) {
        return besoinExprimeService.getBesoinById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * GET /api/besoins-exprimes/agent/{agentId} - Besoins d'un agent
     */
    @GetMapping("/agent/{agentId}")
    public ResponseEntity<Page<BesoinExprimeDTO>> getBesoinsByAgent(
            @PathVariable Long agentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BesoinExprimeDTO> besoins = besoinExprimeService.getBesoinsByAgent(agentId, pageable);
        return ResponseEntity.ok(besoins);
    }
    
    /**
     * GET /api/besoins-exprimes/a-valider - Besoins à valider (filtrés par hiérarchie)
     */
    @GetMapping("/a-valider")
    public ResponseEntity<Page<BesoinExprimeDTO>> getBesoinsAValider(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            String currentCin = getCurrentCin();
            Pageable pageable = PageRequest.of(page, size);
            // Utiliser la méthode qui filtre par hiérarchie
            if (besoinExprimeService instanceof BesoinExprimeServiceImpl) {
                Page<BesoinExprimeDTO> besoins = ((BesoinExprimeServiceImpl) besoinExprimeService)
                        .getBesoinsAValiderByHierarchy(currentCin, pageable);
                return ResponseEntity.ok(besoins);
            } else {
                // Fallback si le cast échoue
                Page<BesoinExprimeDTO> besoins = besoinExprimeService.getBesoinsAValider(pageable);
                return ResponseEntity.ok(besoins);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * GET /api/besoins-exprimes/a-viser - Besoins à viser (directeur DAF)
     */
    @GetMapping("/a-viser")
    public ResponseEntity<Page<BesoinExprimeDTO>> getBesoinsAViser(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BesoinExprimeDTO> besoins = besoinExprimeService.getBesoinsAViser(pageable);
        return ResponseEntity.ok(besoins);
    }
    
    /**
     * POST /api/besoins-exprimes - Créer un besoin
     */
    @PostMapping
    public ResponseEntity<?> createBesoin(@RequestBody BesoinExprimeRequest request) {
        try {
            String currentCin = getCurrentCin();
            BesoinExprimeDTO besoin = besoinExprimeService.createBesoin(request, currentCin);
            return ResponseEntity.ok(besoin);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            errorResponse.put("error", e.getClass().getSimpleName());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Erreur lors de la création du besoin");
            errorResponse.put("error", e.getClass().getSimpleName());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    /**
     * PUT /api/besoins-exprimes/{id} - Modifier un besoin
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBesoin(
            @PathVariable Long id,
            @RequestBody BesoinExprimeRequest request) {
        try {
            String currentCin = getCurrentCin();
            BesoinExprimeDTO besoin = besoinExprimeService.updateBesoin(id, request, currentCin);
            return ResponseEntity.ok(besoin);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            errorResponse.put("error", e.getClass().getSimpleName());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * PUT /api/besoins-exprimes/{id}/statut - Changer le statut
     */
    @PutMapping("/{id}/statut")
    public ResponseEntity<BesoinExprimeDTO> changeStatut(
            @PathVariable Long id,
            @RequestParam StatutBesoin nouveauStatut) {
        try {
            String currentCin = getCurrentCin();
            BesoinExprimeDTO besoin = besoinExprimeService.changeStatut(id, nouveauStatut, currentCin);
            return ResponseEntity.ok(besoin);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * PUT /api/besoins-exprimes/{id}/valider - Valider un besoin (CRÉÉ → VALIDATION)
     */
    @PutMapping("/{id}/valider")
    public ResponseEntity<?> validerBesoin(@PathVariable Long id) {
        try {
            String currentCin = getCurrentCin();
            BesoinExprimeDTO besoin = besoinExprimeService.validerBesoin(id, currentCin);
            return ResponseEntity.ok(besoin);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            errorResponse.put("error", e.getClass().getSimpleName());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * PUT /api/besoins-exprimes/{id}/viser - Viser un besoin (VALIDATION → VISA)
     */
    @PutMapping("/{id}/viser")
    public ResponseEntity<?> viserBesoin(@PathVariable Long id) {
        try {
            String currentCin = getCurrentCin();
            BesoinExprimeDTO besoin = besoinExprimeService.viserBesoin(id, currentCin);
            return ResponseEntity.ok(besoin);
        } catch (RuntimeException e) {
            // Retourner le message d'erreur dans le body pour faciliter le débogage
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            errorResponse.put("error", e.getClass().getSimpleName());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * PUT /api/besoins-exprimes/{id}/accepter - Accepter un besoin (VISA → ACCEPTÉ)
     */
    @PutMapping("/{id}/accepter")
    public ResponseEntity<?> accepterBesoin(@PathVariable Long id) {
        try {
            String currentCin = getCurrentCin();
            BesoinExprimeDTO besoin = besoinExprimeService.accepterBesoin(id, currentCin);
            return ResponseEntity.ok(besoin);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            errorResponse.put("error", e.getClass().getSimpleName());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * PUT /api/besoins-exprimes/{id}/refuser - Refuser un besoin (VISA → REFUSÉ)
     */
    @PutMapping("/{id}/refuser")
    public ResponseEntity<?> refuserBesoin(
            @PathVariable Long id,
            @RequestParam(required = false) String motif) {
        try {
            String currentCin = getCurrentCin();
            BesoinExprimeDTO besoin = besoinExprimeService.refuserBesoin(id, motif, currentCin);
            return ResponseEntity.ok(besoin);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            errorResponse.put("error", e.getClass().getSimpleName());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * DELETE /api/besoins-exprimes/{id} - Supprimer un besoin
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBesoin(@PathVariable Long id) {
        try {
            String currentCin = getCurrentCin();
            besoinExprimeService.deleteBesoin(id, currentCin);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            errorResponse.put("error", e.getClass().getSimpleName());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}

