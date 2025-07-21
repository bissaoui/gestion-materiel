package com.gestion.materiel.service.impl;

import com.gestion.materiel.Dto.TypeMaterielDto;
import com.gestion.materiel.model.TypeMateriel;
import com.gestion.materiel.repository.TypeMaterielRepository;
import com.gestion.materiel.service.TypeMaterielService;
import com.gestion.materiel.exception.DuplicateNomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TypeMaterielServiceImpl implements TypeMaterielService {
    @Autowired
    private TypeMaterielRepository repository;

    @Override
    public TypeMaterielDto save(TypeMaterielDto dto) {
        if (repository.existsByNomIgnoreCase(dto.getNom()) && (dto.getId() == null || !repository.findById(dto.getId()).map(t -> t.getNom().equalsIgnoreCase(dto.getNom())).orElse(false))) {
            throw new DuplicateNomException("TypeMateriel", dto.getNom());
        }
        TypeMateriel entity = new TypeMateriel();
        entity.setNom(dto.getNom());
        entity = repository.save(entity);
        dto.setId(entity.getId());
        return dto;
    }

    @Override
    public List<TypeMaterielDto> findAll() {
        return repository.findAll().stream().map(entity -> {
            TypeMaterielDto dto = new TypeMaterielDto();
            dto.setId(entity.getId());
            dto.setNom(entity.getNom());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public TypeMaterielDto findById(Long id) {
        TypeMateriel entity = repository.findById(id).orElse(null);
        if (entity == null) return null;
        TypeMaterielDto dto = new TypeMaterielDto();
        dto.setId(entity.getId());
        dto.setNom(entity.getNom());
        return dto;
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
} 