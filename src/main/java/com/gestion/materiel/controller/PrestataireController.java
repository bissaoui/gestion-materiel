package com.gestion.materiel.controller;

import com.gestion.materiel.Dto.PrestataireDto;
import com.gestion.materiel.service.PrestataireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prestataires")
@CrossOrigin(origins = "*")
public class PrestataireController {

    @Autowired
    private PrestataireService prestataireService;

    @PostMapping
    public ResponseEntity<PrestataireDto> createPrestataire(@RequestBody PrestataireDto prestataireDto) {
        PrestataireDto createdPrestataire = prestataireService.save(prestataireDto);
        return new ResponseEntity<>(createdPrestataire, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PrestataireDto>> getAllPrestataires() {
        List<PrestataireDto> prestataires = prestataireService.findAll();
        return new ResponseEntity<>(prestataires, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrestataireDto> getPrestataireById(@PathVariable Long id) {
        PrestataireDto prestataire = prestataireService.findById(id);
        return new ResponseEntity<>(prestataire, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PrestataireDto> updatePrestataire(@PathVariable Long id, @RequestBody PrestataireDto prestataireDto) {
        PrestataireDto updatedPrestataire = prestataireService.update(id, prestataireDto);
        return new ResponseEntity<>(updatedPrestataire, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrestataire(@PathVariable Long id) {
        prestataireService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
