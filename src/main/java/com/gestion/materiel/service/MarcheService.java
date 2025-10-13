package com.gestion.materiel.service;

import com.gestion.materiel.Dto.MarcheDto;
import java.util.List;

public interface MarcheService {
    MarcheDto save(MarcheDto dto);
    MarcheDto update(Long id, MarcheDto dto);
    List<MarcheDto> findAll();
    MarcheDto findById(Long id);
    void delete(Long id);
}


