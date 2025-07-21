package com.gestion.materiel.service;

import com.gestion.materiel.Dto.MarqueDto;
import java.util.List;

public interface MarqueService {
    MarqueDto save(MarqueDto dto);
    List<MarqueDto> findAll();
    MarqueDto findById(Long id);
    void delete(Long id);
    List<MarqueDto> findByTypeMaterielId(Long typeMaterielId);
} 