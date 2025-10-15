package com.gestion.materiel.service;

import com.gestion.materiel.Dto.PrestataireDto;
import java.util.List;

public interface PrestataireService {
    PrestataireDto save(PrestataireDto dto);
    PrestataireDto update(Long id, PrestataireDto dto);
    List<PrestataireDto> findAll();
    PrestataireDto findById(Long id);
    void delete(Long id);
}
