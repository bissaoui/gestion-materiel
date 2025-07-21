package com.gestion.materiel.controller;

import com.gestion.materiel.Dto.ModeleDto;
import com.gestion.materiel.service.ModeleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/modeles")
public class ModeleController {
    @Autowired
    private ModeleService modeleService;

    @GetMapping
    public List<ModeleDto> getAll() { return modeleService.findAll(); }

    @GetMapping("/{id}")
    public ModeleDto getById(@PathVariable Long id) { return modeleService.findById(id); }

    @GetMapping("/by-marque/{marqueId}")
    public List<ModeleDto> getByMarque(@PathVariable Long marqueId) { return modeleService.findByMarqueId(marqueId); }

    @GetMapping("/by-marque-and-type")
    public List<ModeleDto> getModelesByMarqueAndType(
        @RequestParam(required = false) Long marqueId,
        @RequestParam(required = false) Long typeId
    ) {
        if (marqueId != null && typeId != null) {
            return modeleService.findByMarqueAndType(marqueId, typeId);
        } else if (typeId != null) {
            return modeleService.findByType(typeId);
        } else if (marqueId != null) {
            return modeleService.findByMarque(marqueId);
        } else {
            return modeleService.findAll();
        }
    }

    @PostMapping
    public ModeleDto create(@RequestBody ModeleDto dto) { return modeleService.save(dto); }

    @PutMapping("/{id}")
    public ModeleDto update(@PathVariable Long id, @RequestBody ModeleDto dto) { dto.setId(id); return modeleService.save(dto); }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { modeleService.delete(id); }
} 