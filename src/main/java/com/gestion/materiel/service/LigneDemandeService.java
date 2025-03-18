package com.gestion.materiel.service;

import com.gestion.materiel.model.LigneDemande;
import java.util.List;
import java.util.Optional;

public interface LigneDemandeService {
    List<LigneDemande> getAllLigneDemandes();
    Optional<LigneDemande> getLigneDemandeById(Long id);
    LigneDemande saveLigneDemande(LigneDemande ligneDemande);
    List <LigneDemande>  saveLignesDemande(List<LigneDemande>  ligneDemandes);
    void deleteLigneDemande(Long id);
}
