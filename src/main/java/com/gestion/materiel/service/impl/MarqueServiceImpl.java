package com.gestion.materiel.service.impl;

import com.gestion.materiel.Dto.MarqueDto;
import com.gestion.materiel.model.Marque;
import com.gestion.materiel.model.TypeMateriel;
import com.gestion.materiel.repository.MarqueRepository;
import com.gestion.materiel.repository.TypeMaterielRepository;
import com.gestion.materiel.service.MarqueService;
import com.gestion.materiel.exception.DuplicateNomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.Optional;

@Service
public class MarqueServiceImpl implements MarqueService {
    @Autowired
    private MarqueRepository repository;
    @Autowired
    private TypeMaterielRepository typeMaterielRepository;

    @Override
    public MarqueDto save(MarqueDto dto) {
        System.out.println("[DEBUG] MarqueServiceImpl.save - nom=" + dto.getNom() + ", typeMaterielIds=" + dto.getTypeMaterielIds());
        Set<TypeMateriel> types = new HashSet<>();

        if (dto.getTypeMaterielIds() != null) {
            for (Long typeId : dto.getTypeMaterielIds()) {
                System.out.println("[DEBUG] Recherche TypeMateriel id=" + typeId);
                typeMaterielRepository.findById(typeId).ifPresentOrElse(
                    t -> {
                        types.add(t);
                        System.out.println("[DEBUG] TypeMateriel trouvé : " + t.getId() + " - " + t.getNom());
                    },
                    () -> System.out.println("[DEBUG] TypeMateriel NON trouvé pour id=" + typeId)
                );
            }
        }
        Optional<Marque> existingMarqueOpt = repository.findAll().stream()
            .filter(m -> m.getNom().equalsIgnoreCase(dto.getNom()))
            .findFirst();
        if (existingMarqueOpt.isPresent()) {
            Marque existingMarque = existingMarqueOpt.get();
            System.out.println("[DEBUG] Marque EXISTANTE trouvée : id=" + existingMarque.getId() + ", nom=" + existingMarque.getNom());
            boolean added = false;
            for (TypeMateriel t : types) {
                if (!existingMarque.getTypes().contains(t)) {
                    existingMarque.getTypes().add(t);
                    System.out.println("[DEBUG] Ajout du type id=" + t.getId() + " à la marque existante");
                    added = true;
                } else {
                    System.out.println("[DEBUG] Type id=" + t.getId() + " déjà lié à la marque");
                }
            }
            if (added) {
                repository.save(existingMarque);
                System.out.println("[DEBUG] Marque existante mise à jour avec nouveaux types");
            }
            MarqueDto result = new MarqueDto();
            result.setId(existingMarque.getId());
            result.setNom(existingMarque.getNom());
            Set<Long> typeIds = existingMarque.getTypes().stream().map(TypeMateriel::getId).collect(Collectors.toSet());
            result.setTypeMaterielIds(typeIds);
            System.out.println("[DEBUG] Retour DTO marque existante : " + result.getId() + ", " + result.getNom() + ", types=" + result.getTypeMaterielIds());
            return result;
        }
        if (repository.existsByNomIgnoreCase(dto.getNom())) {
            System.out.println("[DEBUG] Doublon détecté pour nom=" + dto.getNom());
            throw new DuplicateNomException("Marque", dto.getNom());
        }
        Marque entity = new Marque();
        entity.setNom(dto.getNom());
        entity.setTypes(types);
        entity = repository.save(entity);
        dto.setId(entity.getId());
        System.out.println("[DEBUG] Nouvelle marque créée : id=" + entity.getId() + ", nom=" + entity.getNom() + ", types=" + types.size());
        return dto;
    }

    @Override
    public List<MarqueDto> findAll() {
        return repository.findAll().stream().map(entity -> {
            MarqueDto dto = new MarqueDto();
            dto.setId(entity.getId());
            dto.setNom(entity.getNom());
            Set<Long> typeIds = new HashSet<>();
            if (entity.getTypes() != null) {
                for (TypeMateriel t : entity.getTypes()) {
                    typeIds.add(t.getId());
                }
            }
            dto.setTypeMaterielIds(typeIds);
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public MarqueDto findById(Long id) {
        Marque entity = repository.findById(id).orElse(null);
        if (entity == null) return null;
        MarqueDto dto = new MarqueDto();
        dto.setId(entity.getId());
        dto.setNom(entity.getNom());
        Set<Long> typeIds = new HashSet<>();
        if (entity.getTypes() != null) {
            for (TypeMateriel t : entity.getTypes()) {
                typeIds.add(t.getId());
            }
        }
        dto.setTypeMaterielIds(typeIds);
        return dto;
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<MarqueDto> findByTypeMaterielId(Long typeMaterielId) {
        return repository.findAll().stream()
            .filter(entity -> entity.getTypes() != null && entity.getTypes().stream().anyMatch(t -> t.getId().equals(typeMaterielId)))
            .map(entity -> {
                MarqueDto dto = new MarqueDto();
                dto.setId(entity.getId());
                dto.setNom(entity.getNom());
                Set<Long> typeIds = new HashSet<>();
                if (entity.getTypes() != null) {
                    for (TypeMateriel t : entity.getTypes()) {
                        typeIds.add(t.getId());
                    }
                }
                dto.setTypeMaterielIds(typeIds);
                return dto;
            }).collect(Collectors.toList());
    }
} 