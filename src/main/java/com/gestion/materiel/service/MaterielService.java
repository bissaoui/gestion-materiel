package com.gestion.materiel.service;

import com.gestion.materiel.Dto.MaterielDto;
import java.util.List;
import java.time.LocalDateTime;

public interface MaterielService {
    MaterielDto save(MaterielDto dto);
    List<MaterielDto> findAll();
    MaterielDto findById(Long id);
    void delete(Long id);
    MaterielDto affecterMaterielAAgent(Long materielId, Long agentId, LocalDateTime dateAffectation);
    MaterielDto desaffecterMateriel(Long materielId);
    MaterielDto update(Long id, MaterielDto dto);
} 