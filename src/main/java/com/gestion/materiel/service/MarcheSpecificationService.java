package com.gestion.materiel.service;

import com.gestion.materiel.Dto.MarcheSpecificationDTO;
import com.gestion.materiel.Dto.MarcheSpecificationRequest;

import java.util.List;

public interface MarcheSpecificationService {
    
    List<MarcheSpecificationDTO> getSpecificationsByMarcheId(Long marcheId);
    
    void saveSpecifications(Long marcheId, List<MarcheSpecificationRequest> specifications);
    
    void updateSpecifications(Long marcheId, List<MarcheSpecificationRequest> specifications);
    
    void deleteSpecificationsByMarcheId(Long marcheId);
}
