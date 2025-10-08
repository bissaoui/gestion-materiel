package com.gestion.materiel.service.impl;

import com.gestion.materiel.Dto.MaterielDto;
import com.gestion.materiel.model.Materiel;
import com.gestion.materiel.model.TypeMateriel;
import com.gestion.materiel.model.Marque;
import com.gestion.materiel.model.Modele;
import com.gestion.materiel.model.Agent;
import com.gestion.materiel.model.Marcher;
import com.gestion.materiel.repository.MaterielRepository;
import com.gestion.materiel.repository.TypeMaterielRepository;
import com.gestion.materiel.repository.MarqueRepository;
import com.gestion.materiel.repository.ModeleRepository;
import com.gestion.materiel.repository.AgentRepository;
import com.gestion.materiel.repository.MarcherRepository;
import com.gestion.materiel.service.MaterielService;
import com.gestion.materiel.exception.DuplicateNumeroSerieException;
import com.gestion.materiel.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDateTime;

@Service
public class MaterielServiceImpl implements MaterielService {
    @Autowired
    private MaterielRepository repository;
    @Autowired
    private TypeMaterielRepository typeMaterielRepository;
    @Autowired
    private MarqueRepository marqueRepository;
    @Autowired
    private ModeleRepository modeleRepository;
    @Autowired
    private AgentRepository agentRepository;
    @Autowired
    private MarcherRepository marcherRepository;

    @Override
    public MaterielDto save(MaterielDto dto) {
        System.out.println("[DEBUG] MaterielServiceImpl.save - typeMaterielId=" + dto.getTypeMaterielId() + ", marqueId=" + dto.getMarqueId() + ", modeleId=" + dto.getModeleId() + ", agentId=" + dto.getAgentId());
        if (repository.existsByNumeroSerie(dto.getNumeroSerie())) {
            throw new DuplicateNumeroSerieException(dto.getNumeroSerie());
        }
        TypeMateriel typeMateriel = typeMaterielRepository.findById(dto.getTypeMaterielId())
                .orElseThrow(() -> new NotFoundException("TypeMateriel", dto.getTypeMaterielId()));
        Marque marque = marqueRepository.findById(dto.getMarqueId())
                .orElseThrow(() -> new NotFoundException("Marque", dto.getMarqueId()));
        Modele modele = modeleRepository.findById(dto.getModeleId())
                .orElseThrow(() -> new NotFoundException("Modele", dto.getModeleId()));
        Agent agent = null;
        if (dto.getAgentId() != null) {
            agent = agentRepository.findById(dto.getAgentId())
                .orElseThrow(() -> new NotFoundException("Agent", dto.getAgentId()));
        }
        Marcher marcher = null;
        if (dto.getMarcherId() != null) {
            marcher = marcherRepository.findById(dto.getMarcherId())
                .orElseThrow(() -> new NotFoundException("Marcher", dto.getMarcherId()));
        }
        Materiel entity = new Materiel();
        entity.setNumeroSerie(dto.getNumeroSerie());
        entity.setTypeMateriel(typeMateriel);
        entity.setMarque(marque);
        entity.setModele(modele);
        entity.setAgent(agent);
        entity.setMarcher(marcher);
        entity = repository.save(entity);
        dto.setId(entity.getId());
        return dto;
    }

    @Override
    public List<MaterielDto> findAll() {
        return repository.findAll().stream().map(entity -> {
            MaterielDto dto = new MaterielDto();
            dto.setId(entity.getId());
            dto.setDateAffectation(entity.getDateAffectation());
            dto.setNumeroSerie(entity.getNumeroSerie());
            dto.setTypeMaterielId(entity.getTypeMateriel() != null ? entity.getTypeMateriel().getId() : null);
            dto.setMarqueId(entity.getMarque() != null ? entity.getMarque().getId() : null);
            dto.setModeleId(entity.getModele() != null ? entity.getModele().getId() : null);
            dto.setAgentId(entity.getAgent() != null ? entity.getAgent().getId() : null);
            dto.setMarcherId(entity.getMarcher() != null ? entity.getMarcher().getId() : null);
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public MaterielDto findById(Long id) {
        Materiel entity = repository.findById(id).orElse(null);
        if (entity == null) return null;
        MaterielDto dto = new MaterielDto();
        dto.setId(entity.getId());
        dto.setDateAffectation(entity.getDateAffectation());
        dto.setNumeroSerie(entity.getNumeroSerie());
        dto.setTypeMaterielId(entity.getTypeMateriel() != null ? entity.getTypeMateriel().getId() : null);
        dto.setMarqueId(entity.getMarque() != null ? entity.getMarque().getId() : null);
        dto.setModeleId(entity.getModele() != null ? entity.getModele().getId() : null);
        dto.setAgentId(entity.getAgent() != null ? entity.getAgent().getId() : null);
        dto.setMarcherId(entity.getMarcher() != null ? entity.getMarcher().getId() : null);
        return dto;
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public MaterielDto affecterMaterielAAgent(Long materielId, Long agentId) {
        Materiel materiel = repository.findById(materielId)
            .orElseThrow(() -> new NotFoundException("Materiel", materielId));
        Agent agent = agentRepository.findById(agentId)
            .orElseThrow(() -> new NotFoundException("Agent", agentId));
        materiel.setAgent(agent);
        materiel.setDateAffectation(LocalDateTime.now());
        materiel = repository.save(materiel);
        MaterielDto dto = new MaterielDto();
        dto.setId(materiel.getId());
        dto.setNumeroSerie(materiel.getNumeroSerie());
        dto.setTypeMaterielId(materiel.getTypeMateriel() != null ? materiel.getTypeMateriel().getId() : null);
        dto.setMarqueId(materiel.getMarque() != null ? materiel.getMarque().getId() : null);
        dto.setModeleId(materiel.getModele() != null ? materiel.getModele().getId() : null);
        dto.setAgentId(materiel.getAgent() != null ? materiel.getAgent().getId() : null);
        dto.setMarcherId(materiel.getMarcher() != null ? materiel.getMarcher().getId() : null);
        return dto;
    }

    @Override
    public MaterielDto desaffecterMateriel(Long materielId) {
        Materiel materiel = repository.findById(materielId)
            .orElseThrow(() -> new NotFoundException("Materiel", materielId));
        materiel.setAgent(null);
        materiel.setDateAffectation(null);
        materiel = repository.save(materiel);
        MaterielDto dto = new MaterielDto();
        dto.setId(materiel.getId());
        
        dto.setNumeroSerie(materiel.getNumeroSerie());
        dto.setTypeMaterielId(materiel.getTypeMateriel() != null ? materiel.getTypeMateriel().getId() : null);
        dto.setMarqueId(materiel.getMarque() != null ? materiel.getMarque().getId() : null);
        dto.setModeleId(materiel.getModele() != null ? materiel.getModele().getId() : null);
        dto.setAgentId(null);
        dto.setMarcherId(materiel.getMarcher() != null ? materiel.getMarcher().getId() : null);
        return dto;
    }

    @Override
    public MaterielDto update(Long id, MaterielDto dto) {
        Materiel existing = repository.findById(id)
            .orElseThrow(() -> new NotFoundException("Materiel", id));
        // Check if numeroSerie is being changed and if the new one already exists
        if (!existing.getNumeroSerie().equals(dto.getNumeroSerie()) && repository.existsByNumeroSerie(dto.getNumeroSerie())) {
            throw new DuplicateNumeroSerieException(dto.getNumeroSerie());
        }
        TypeMateriel typeMateriel = typeMaterielRepository.findById(dto.getTypeMaterielId())
                .orElseThrow(() -> new NotFoundException("TypeMateriel", dto.getTypeMaterielId()));
        Marque marque = marqueRepository.findById(dto.getMarqueId())
                .orElseThrow(() -> new NotFoundException("Marque", dto.getMarqueId()));
        Modele modele = modeleRepository.findById(dto.getModeleId())
                .orElseThrow(() -> new NotFoundException("Modele", dto.getModeleId()));
        Agent agent = null;
        if (dto.getAgentId() != null) {
            agent = agentRepository.findById(dto.getAgentId())
                .orElseThrow(() -> new NotFoundException("Agent", dto.getAgentId()));
        }
        Marcher marcher = null;
        if (dto.getMarcherId() != null) {
            marcher = marcherRepository.findById(dto.getMarcherId())
                .orElseThrow(() -> new NotFoundException("Marcher", dto.getMarcherId()));
        }
        existing.setNumeroSerie(dto.getNumeroSerie());
        existing.setTypeMateriel(typeMateriel);
        existing.setMarque(marque);
        existing.setModele(modele);
        existing.setAgent(agent);
        existing.setMarcher(marcher);
        existing = repository.save(existing);
        dto.setId(existing.getId());
        return dto;
    }
} 