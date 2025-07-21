package com.gestion.materiel.service.impl;

import com.gestion.materiel.Dto.ModeleDto;
import com.gestion.materiel.model.Modele;
import com.gestion.materiel.model.Marque;
import com.gestion.materiel.repository.ModeleRepository;
import com.gestion.materiel.repository.MarqueRepository;
import com.gestion.materiel.repository.TypeMaterielRepository;
import com.gestion.materiel.service.ModeleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ModeleServiceImpl implements ModeleService {
    @Autowired
    private ModeleRepository repository;
    @Autowired
    private MarqueRepository marqueRepository;
    @Autowired
    private TypeMaterielRepository typeMaterielRepository;

    @Override
    public ModeleDto save(ModeleDto dto) {
        System.out.println("[DEBUG] ModeleServiceImpl.save - nom=" + dto.getNom() + ", marqueId=" + dto.getMarqueId());
        if (dto.getMarqueId() == null) {
            throw new IllegalArgumentException("marqueId ne doit pas être null");
        }
        Modele entity = new Modele();
        entity.setNom(dto.getNom());
        if (dto.getMarqueId() != null) {
            marqueRepository.findById(dto.getMarqueId()).ifPresent(entity::setMarque);
        }
        if (dto.getTypeMaterielId() != null) {
            typeMaterielRepository.findById(dto.getTypeMaterielId()).ifPresent(entity::setTypeMateriel);
        }
        entity = repository.save(entity);
        dto.setId(entity.getId());
        return dto;
    }
    

    @Override
    public List<ModeleDto> findAll() {
        return repository.findAll().stream().map(entity -> {
            ModeleDto dto = new ModeleDto();
            dto.setId(entity.getId());
            dto.setNom(entity.getNom());
            dto.setMarqueId(entity.getMarque() != null ? entity.getMarque().getId() : null);
            dto.setTypeMaterielId(entity.getTypeMateriel() != null ? entity.getTypeMateriel().getId() : null);
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public ModeleDto findById(Long id) {
        Modele entity = repository.findById(id).orElse(null);
        if (entity == null) return null;
        ModeleDto dto = new ModeleDto();
        dto.setId(entity.getId());
        dto.setNom(entity.getNom());
        dto.setMarqueId(entity.getMarque() != null ? entity.getMarque().getId() : null);
        dto.setTypeMaterielId(entity.getTypeMateriel() != null ? entity.getTypeMateriel().getId() : null);
        return dto;
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<ModeleDto> findByMarqueId(Long marqueId) {
        return repository.findByMarqueId(marqueId).stream().map(entity -> {
            ModeleDto dto = new ModeleDto();
            dto.setId(entity.getId());
            dto.setNom(entity.getNom());
            dto.setMarqueId(entity.getMarque() != null ? entity.getMarque().getId() : null);
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<ModeleDto> findByMarqueAndType(Long marqueId, Long typeId) {
        return repository.findAll().stream()
            .filter(modele -> modele.getMarque() != null
                && modele.getMarque().getId().equals(marqueId)
                && modele.getTypeMateriel() != null
                && modele.getTypeMateriel().getId().equals(typeId))
            .map(entity -> {
                ModeleDto dto = new ModeleDto();
                dto.setId(entity.getId());
                dto.setNom(entity.getNom());
                dto.setMarqueId(entity.getMarque() != null ? entity.getMarque().getId() : null);
                dto.setTypeMaterielId(entity.getTypeMateriel() != null ? entity.getTypeMateriel().getId() : null);
                return dto;
            })
            .collect(Collectors.toList());
    }

    @Override
    public List<ModeleDto> findByType(Long typeId) {
        return repository.findAll().stream()
            .filter(modele -> modele.getTypeMateriel() != null && modele.getTypeMateriel().getId().equals(typeId))
            .map(entity -> {
                ModeleDto dto = new ModeleDto();
                dto.setId(entity.getId());
                dto.setNom(entity.getNom());
                dto.setMarqueId(entity.getMarque() != null ? entity.getMarque().getId() : null);
                dto.setTypeMaterielId(entity.getTypeMateriel() != null ? entity.getTypeMateriel().getId() : null);
                return dto;
            })
            .collect(Collectors.toList());
    }

    @Override
    public List<ModeleDto> findByMarque(Long marqueId) {
        return repository.findAll().stream()
            .filter(modele -> modele.getMarque() != null && modele.getMarque().getId().equals(marqueId))
            .map(entity -> {
                ModeleDto dto = new ModeleDto();
                dto.setId(entity.getId());
                dto.setNom(entity.getNom());
                dto.setMarqueId(entity.getMarque() != null ? entity.getMarque().getId() : null);
                dto.setTypeMaterielId(entity.getTypeMateriel() != null ? entity.getTypeMateriel().getId() : null);
                return dto;
            })
            .collect(Collectors.toList());
    }
} 