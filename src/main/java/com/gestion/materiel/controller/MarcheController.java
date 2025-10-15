package com.gestion.materiel.controller;

import com.gestion.materiel.Dto.MarcheDto;
import com.gestion.materiel.service.MarcheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/marches")
public class MarcheController {
    @Autowired
    private MarcheService marcheService;

    @GetMapping
    public List<MarcheDto> getAll() { return marcheService.findAll(); }

    @GetMapping("/{id}")
    public MarcheDto getById(@PathVariable Long id) { return marcheService.findById(id); }

    @PostMapping
    public MarcheDto create(@RequestBody MarcheDto dto) { return marcheService.save(dto); }

    @PutMapping("/{id}")
    public MarcheDto update(@PathVariable Long id, @RequestBody MarcheDto dto) { return marcheService.update(id, dto); }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { marcheService.delete(id); }
}





