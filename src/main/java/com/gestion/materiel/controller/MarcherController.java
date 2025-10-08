package com.gestion.materiel.controller;

import com.gestion.materiel.Dto.MarcherDto;
import com.gestion.materiel.service.MarcherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/marchers")
public class MarcherController {
    @Autowired
    private MarcherService marcherService;

    @GetMapping
    public List<MarcherDto> getAll() { return marcherService.findAll(); }

    @GetMapping("/{id}")
    public MarcherDto getById(@PathVariable Long id) { return marcherService.findById(id); }

    @PostMapping
    public MarcherDto create(@RequestBody MarcherDto dto) { return marcherService.save(dto); }

    @PutMapping("/{id}")
    public MarcherDto update(@PathVariable Long id, @RequestBody MarcherDto dto) { return marcherService.update(id, dto); }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { marcherService.delete(id); }
}



