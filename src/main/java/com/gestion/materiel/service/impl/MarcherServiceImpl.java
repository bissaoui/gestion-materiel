package com.gestion.materiel.service.impl;

import com.gestion.materiel.Dto.MarcherDto;
import com.gestion.materiel.model.Marcher;
import com.gestion.materiel.model.Materiel;
import com.gestion.materiel.repository.MarcherRepository;
import com.gestion.materiel.repository.MaterielRepository;
import com.gestion.materiel.service.MarcherService;
import com.gestion.materiel.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MarcherServiceImpl implements MarcherService {

    @Autowired
    private MarcherRepository marcherRepository;

    @Autowired
    private MaterielRepository materielRepository;

    @Override
    public MarcherDto save(MarcherDto dto) {
        Marcher entity = new Marcher();
        entity.setName(dto.getName());
        entity.setDate(dto.getDate());
        if (dto.getMaterielIds() != null && !dto.getMaterielIds().isEmpty()) {
            Set<Materiel> materiels = dto.getMaterielIds().stream()
                .map(id -> materielRepository.findById(id).orElseThrow(() -> new NotFoundException("Materiel", id)))
                .collect(Collectors.toSet());
            entity.setMateriels(materiels);
            // maintain owning side
            for (Materiel m : materiels) {
                m.setMarcher(entity);
            }
        }
        entity = marcherRepository.save(entity);
        dto.setId(entity.getId());
        return dto;
    }

    @Override
    public MarcherDto update(Long id, MarcherDto dto) {
        Marcher entity = marcherRepository.findById(id).orElseThrow(() -> new NotFoundException("Marcher", id));
        entity.setName(dto.getName());
        entity.setDate(dto.getDate());
        if (dto.getMaterielIds() != null) {
            Set<Materiel> materiels = dto.getMaterielIds().stream()
                .map(mid -> materielRepository.findById(mid).orElseThrow(() -> new NotFoundException("Materiel", mid)))
                .collect(Collectors.toSet());
            entity.setMateriels(materiels);
            for (Materiel m : materiels) {
                m.setMarcher(entity);
            }
        }
        entity = marcherRepository.save(entity);
        return toDto(entity);
    }

    @Override
    public List<MarcherDto> findAll() {
        return marcherRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public MarcherDto findById(Long id) {
        Marcher entity = marcherRepository.findById(id).orElseThrow(() -> new NotFoundException("Marcher", id));
        return toDto(entity);
    }

    @Override
    public void delete(Long id) {
        marcherRepository.deleteById(id);
    }

    private MarcherDto toDto(Marcher entity) {
        MarcherDto dto = new MarcherDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDate(entity.getDate());
        if (entity.getMateriels() != null) {
            dto.setMaterielIds(entity.getMateriels().stream().map(Materiel::getId).collect(Collectors.toSet()));
        }
        return dto;
    }
}


