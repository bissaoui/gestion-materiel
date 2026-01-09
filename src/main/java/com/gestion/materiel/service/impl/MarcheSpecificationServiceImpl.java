package com.gestion.materiel.service.impl;

import com.gestion.materiel.Dto.MarcheSpecificationDTO;
import com.gestion.materiel.Dto.MarcheSpecificationRequest;
import com.gestion.materiel.mapper.MarcheSpecificationMapper;
import com.gestion.materiel.model.Marche;
import com.gestion.materiel.model.MarcheSpecification;
import com.gestion.materiel.model.TypeMateriel;
import com.gestion.materiel.repository.MarcheRepository;
import com.gestion.materiel.repository.MarcheSpecificationRepository;
import com.gestion.materiel.repository.TypeMaterielRepository;
import com.gestion.materiel.service.MarcheSpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MarcheSpecificationServiceImpl implements MarcheSpecificationService {
    
    @Autowired
    private MarcheSpecificationRepository specificationRepository;
    
    @Autowired
    private MarcheRepository marcheRepository;
    
    @Autowired
    private TypeMaterielRepository typeMaterielRepository;
    
    @Autowired
    private MarcheSpecificationMapper mapper;
    
    @Override
    public List<MarcheSpecificationDTO> getSpecificationsByMarcheId(Long marcheId) {
        List<MarcheSpecification> specifications = specificationRepository.findByMarcheId(marcheId);
        return specifications.stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public void saveSpecifications(Long marcheId, List<MarcheSpecificationRequest> specifications) {
        if (specifications == null || specifications.isEmpty()) {
            return;
        }
        
        Marche marche = marcheRepository.findById(marcheId)
                .orElseThrow(() -> new IllegalArgumentException("Marché non trouvé avec l'ID: " + marcheId));
        
        for (MarcheSpecificationRequest request : specifications) {
            // Validation
            if (request.getTypeMaterielId() == null || request.getQuantite() == null || request.getQuantite() <= 0) {
                throw new IllegalArgumentException("Type de matériel et quantité (positive) sont requis");
            }
            
            // Vérifier que le type n'existe pas déjà pour ce marché
            if (specificationRepository.existsByMarcheIdAndTypeMaterielId(marcheId, request.getTypeMaterielId())) {
                throw new IllegalArgumentException("Ce type de matériel est déjà spécifié pour ce marché");
            }
            
            TypeMateriel typeMateriel = typeMaterielRepository.findById(request.getTypeMaterielId())
                    .orElseThrow(() -> new IllegalArgumentException("Type de matériel non trouvé avec l'ID: " + request.getTypeMaterielId()));
            
            MarcheSpecification specification = mapper.toEntity(request, marche, typeMateriel);
            specificationRepository.save(specification);
        }
    }
    
    @Override
    @Transactional
    public void updateSpecifications(Long marcheId, List<MarcheSpecificationRequest> specifications) {
        // Supprimer les anciennes spécifications
        deleteSpecificationsByMarcheId(marcheId);
        
        // Créer les nouvelles spécifications
        if (specifications != null && !specifications.isEmpty()) {
            saveSpecifications(marcheId, specifications);
        }
    }
    
    @Override
    @Transactional
    public void deleteSpecificationsByMarcheId(Long marcheId) {
        specificationRepository.deleteByMarcheId(marcheId);
    }
}
