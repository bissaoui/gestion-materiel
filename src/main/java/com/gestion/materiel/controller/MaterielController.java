package com.gestion.materiel.controller;

import com.gestion.materiel.Dto.MaterielDto;
import com.gestion.materiel.service.MaterielService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/materiels")
public class MaterielController {
    @Autowired
    private MaterielService materielService;

    @GetMapping
    public List<MaterielDto> getAll() { return materielService.findAll(); }

    @GetMapping("/{id}")
    public MaterielDto getById(@PathVariable Long id) { return materielService.findById(id); }

    @PostMapping
    public MaterielDto create(@RequestBody MaterielDto dto) { return materielService.save(dto); }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { materielService.delete(id); }

    @PutMapping("/{materielId}/affecter/{agentId}")
    public MaterielDto affecter(@PathVariable Long materielId, @PathVariable Long agentId) {
        return materielService.affecterMaterielAAgent(materielId, agentId);
    }

    @PutMapping("/{materielId}/desaffecter")
    public MaterielDto desaffecter(@PathVariable Long materielId) {
        return materielService.desaffecterMateriel(materielId);
    }

    @PutMapping("/{id}")
    public MaterielDto update(@PathVariable Long id, @RequestBody MaterielDto dto) {
        return materielService.update(id, dto);
    }
} 