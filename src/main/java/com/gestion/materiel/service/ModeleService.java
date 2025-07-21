package com.gestion.materiel.service;

import com.gestion.materiel.Dto.ModeleDto;
import java.util.List;

public interface ModeleService {
    ModeleDto save(ModeleDto dto);
    List<ModeleDto> findAll();
    ModeleDto findById(Long id);
    void delete(Long id);
    List<ModeleDto> findByMarqueId(Long marqueId);
    List<ModeleDto> findByMarqueAndType(Long marqueId, Long typeId);
    List<ModeleDto> findByType(Long typeId);
    List<ModeleDto> findByMarque(Long marqueId);
} 