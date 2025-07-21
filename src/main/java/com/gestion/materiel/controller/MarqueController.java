package com.gestion.materiel.controller;

import com.gestion.materiel.Dto.MarqueDto;
import com.gestion.materiel.service.MarqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/marques")
public class MarqueController {
    @Autowired
    private MarqueService marqueService;

    @GetMapping
    public List<MarqueDto> getAll() { return marqueService.findAll(); }

    @GetMapping("/{id}")
    public MarqueDto getById(@PathVariable Long id) { return marqueService.findById(id); }

    @GetMapping("/by-type/{typeId}")
    public List<MarqueDto> getByType(@PathVariable Long typeId) { return marqueService.findByTypeMaterielId(typeId); }

    @PostMapping
    public MarqueDto create(@RequestBody MarqueDto dto) { return marqueService.save(dto); }

    @PutMapping("/{id}")
    public MarqueDto update(@PathVariable Long id, @RequestBody MarqueDto dto) { dto.setId(id); return marqueService.save(dto); }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { marqueService.delete(id); }
} 