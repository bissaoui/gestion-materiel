package com.gestion.materiel.controller;

import com.gestion.materiel.Dto.MarcheSpecificationDTO;
import com.gestion.materiel.service.MarcheSpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/marches")
public class MarcheSpecificationController {
    
    @Autowired
    private MarcheSpecificationService specificationService;
    
    @GetMapping("/{marcheId}/specifications")
    public ResponseEntity<List<MarcheSpecificationDTO>> getSpecifications(@PathVariable Long marcheId) {
        try {
            List<MarcheSpecificationDTO> specifications = specificationService.getSpecificationsByMarcheId(marcheId);
            return ResponseEntity.ok(specifications);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
