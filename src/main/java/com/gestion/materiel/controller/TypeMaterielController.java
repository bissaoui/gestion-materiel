package com.gestion.materiel.controller;

import com.gestion.materiel.Dto.TypeMaterielDto;
import com.gestion.materiel.service.TypeMaterielService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/types")
public class TypeMaterielController {
    @Autowired
    private TypeMaterielService typeMaterielService;

    @GetMapping
    public List<TypeMaterielDto> getAll() { return typeMaterielService.findAll(); }

    @GetMapping("/{id}")
    public TypeMaterielDto getById(@PathVariable Long id) { return typeMaterielService.findById(id); }

    @PostMapping
    public TypeMaterielDto create(@RequestBody TypeMaterielDto dto) { return typeMaterielService.save(dto); }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { typeMaterielService.delete(id); }
} 