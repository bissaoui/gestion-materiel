package com.gestion.materiel.service;

import com.gestion.materiel.model.Demande;
import java.util.List;
import java.util.Optional;

public interface DemandeService {
    List<Demande> getAllDemandes();
    Optional<Demande> getDemandeById(Long id);
    Demande saveDemande(Demande demande);
    void deleteDemande(Long id);
}
