package com.gestion.materiel.mapper;

import com.gestion.materiel.Dto.MarcheSpecificationDTO;
import com.gestion.materiel.Dto.MarcheSpecificationRequest;
import com.gestion.materiel.model.MarcheSpecification;
import com.gestion.materiel.model.Marche;
import com.gestion.materiel.model.TypeMateriel;
import org.springframework.stereotype.Component;

@Component
public class MarcheSpecificationMapper {
    
    public MarcheSpecificationDTO toDTO(MarcheSpecification specification) {
        if (specification == null) {
            return null;
        }
        
        MarcheSpecificationDTO dto = new MarcheSpecificationDTO();
        dto.setId(specification.getId());
        dto.setMarcheId(specification.getMarche() != null ? specification.getMarche().getId() : null);
        dto.setTypeMaterielId(specification.getTypeMateriel() != null ? specification.getTypeMateriel().getId() : null);
        dto.setTypeMaterielNom(specification.getTypeMateriel() != null ? specification.getTypeMateriel().getNom() : null);
        dto.setQuantite(specification.getQuantite());
        
        return dto;
    }
    
    public MarcheSpecification toEntity(MarcheSpecificationRequest request, Marche marche, TypeMateriel typeMateriel) {
        if (request == null) {
            return null;
        }
        
        MarcheSpecification specification = new MarcheSpecification();
        specification.setMarche(marche);
        specification.setTypeMateriel(typeMateriel);
        specification.setQuantite(request.getQuantite());
        
        return specification;
    }
}
