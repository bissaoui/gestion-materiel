package com.gestion.materiel.service;

import com.gestion.materiel.Dto.MarcherDto;
import java.util.List;

public interface MarcherService {
    MarcherDto save(MarcherDto dto);
    MarcherDto update(Long id, MarcherDto dto);
    List<MarcherDto> findAll();
    MarcherDto findById(Long id);
    void delete(Long id);
}



