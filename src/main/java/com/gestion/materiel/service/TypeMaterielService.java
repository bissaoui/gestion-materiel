package com.gestion.materiel.service;

import com.gestion.materiel.Dto.TypeMaterielDto;
import java.util.List;

public interface TypeMaterielService {
    TypeMaterielDto save(TypeMaterielDto dto);
    List<TypeMaterielDto> findAll();
    TypeMaterielDto findById(Long id);
    void delete(Long id);
} 