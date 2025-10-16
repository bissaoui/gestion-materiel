package com.gestion.materiel.service.impl;

import com.gestion.materiel.Dto.MarcheDto;
import com.gestion.materiel.model.Marche;
import com.gestion.materiel.model.Materiel;
import com.gestion.materiel.model.Prestataire;
import com.gestion.materiel.model.TypeMarche;
import com.gestion.materiel.repository.MarcheRepository;
import com.gestion.materiel.repository.MaterielRepository;
import com.gestion.materiel.repository.PrestataireRepository;
import com.gestion.materiel.service.MarcheService;
import com.gestion.materiel.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MarcheServiceImpl implements MarcheService {

    @Autowired
    private MarcheRepository marcherRepository;

    @Autowired
    private MaterielRepository materielRepository;

    @Autowired
    private PrestataireRepository prestataireRepository;

    @Override
    public MarcheDto save(MarcheDto dto) {
        Marche entity = new Marche();
        entity.setName(dto.getName());
        entity.setDate(dto.getDate());
        entity.setDateOrdreService(dto.getDateOrdreService());
        entity.setDelaiExecution(dto.getDelaiExecution());
        entity.setDateReceptionProvisoire(dto.getDateReceptionProvisoire());
        entity.setDateReceptionDefinitive(dto.getDateReceptionDefinitive());
        entity.setHasRetenueGarantie(dto.getHasRetenueGarantie());
        
        if (dto.getTypeMarche() != null) {
            entity.setTypeMarche(TypeMarche.valueOf(dto.getTypeMarche()));
        }
        
        if (dto.getPrestataireId() != null) {
            Prestataire prestataire = prestataireRepository.findById(dto.getPrestataireId())
                    .orElseThrow(() -> new NotFoundException("Prestataire", dto.getPrestataireId()));
            entity.setPrestataire(prestataire);
        }
        
        if (dto.getMaterielIds() != null && !dto.getMaterielIds().isEmpty()) {
            Set<Materiel> materiels = dto.getMaterielIds().stream()
                .map(id -> materielRepository.findById(id).orElseThrow(() -> new NotFoundException("Materiel", id)))
                .collect(Collectors.toSet());
            entity.setMateriels(materiels);
            // maintain owning side
            for (Materiel m : materiels) {
                m.setMarche(entity);
            }
        }
        entity = marcherRepository.save(entity);
        dto.setId(entity.getId());
        return dto;
    }

    @Override
    public MarcheDto update(Long id, MarcheDto dto) {
        Marche entity = marcherRepository.findById(id).orElseThrow(() -> new NotFoundException("Marche", id));
        entity.setName(dto.getName());
        entity.setDate(dto.getDate());
        entity.setDateOrdreService(dto.getDateOrdreService());
        entity.setDelaiExecution(dto.getDelaiExecution());
        entity.setDateReceptionProvisoire(dto.getDateReceptionProvisoire());
        entity.setDateReceptionDefinitive(dto.getDateReceptionDefinitive());
        entity.setHasRetenueGarantie(dto.getHasRetenueGarantie());
        
        if (dto.getTypeMarche() != null) {
            entity.setTypeMarche(TypeMarche.valueOf(dto.getTypeMarche()));
        }
        
        if (dto.getPrestataireId() != null) {
            Prestataire prestataire = prestataireRepository.findById(dto.getPrestataireId())
                    .orElseThrow(() -> new NotFoundException("Prestataire", dto.getPrestataireId()));
            entity.setPrestataire(prestataire);
        }
        
        if (dto.getMaterielIds() != null) {
            Set<Materiel> materiels = dto.getMaterielIds().stream()
                .map(mid -> materielRepository.findById(mid).orElseThrow(() -> new NotFoundException("Materiel", mid)))
                .collect(Collectors.toSet());
            entity.setMateriels(materiels);
            for (Materiel m : materiels) {
                m.setMarche(entity);
            }
        }
        entity = marcherRepository.save(entity);
        return toDto(entity);
    }

    @Override
    public List<MarcheDto> findAll() {
        return marcherRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public MarcheDto findById(Long id) {
        Marche entity = marcherRepository.findById(id).orElseThrow(() -> new NotFoundException("Marche", id));
        return toDto(entity);
    }

    @Override
    public void delete(Long id) {
        marcherRepository.deleteById(id);
    }

    private MarcheDto toDto(Marche entity) {
        MarcheDto dto = new MarcheDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDate(entity.getDate());
        dto.setDateOrdreService(entity.getDateOrdreService());
        dto.setDelaiExecution(entity.getDelaiExecution());
        dto.setDateReceptionProvisoire(entity.getDateReceptionProvisoire());
        dto.setDateReceptionDefinitive(entity.getDateReceptionDefinitive());
        dto.setHasRetenueGarantie(entity.getHasRetenueGarantie());
        
        if (entity.getTypeMarche() != null) {
            dto.setTypeMarche(entity.getTypeMarche().name());
        }
        
        if (entity.getPrestataire() != null) {
            dto.setPrestataireId(entity.getPrestataire().getId());
        }
        
        if (entity.getMateriels() != null) {
            dto.setMaterielIds(entity.getMateriels().stream().map(Materiel::getId).collect(Collectors.toSet()));
        }
        return dto;
    }
}





