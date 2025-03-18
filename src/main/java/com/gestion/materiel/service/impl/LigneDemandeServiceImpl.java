package com.gestion.materiel.service.impl;

import com.gestion.materiel.model.LigneDemande;
import com.gestion.materiel.repository.LigneDemandeRepository;
import com.gestion.materiel.service.LigneDemandeService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class LigneDemandeServiceImpl implements LigneDemandeService {
    private final LigneDemandeRepository ligneDemandeRepository;

    public LigneDemandeServiceImpl(LigneDemandeRepository ligneDemandeRepository) {
        this.ligneDemandeRepository = ligneDemandeRepository;
    }

    @Override
    public List<LigneDemande> getAllLigneDemandes() {
        return ligneDemandeRepository.findAll();
    }

    @Override
    public Optional<LigneDemande> getLigneDemandeById(Long id) {
        return ligneDemandeRepository.findById(id);
    }

    @Override
    public LigneDemande saveLigneDemande(LigneDemande ligneDemande) {
        return ligneDemandeRepository.save(ligneDemande);
    }

    @Override
    public List <LigneDemande> saveLignesDemande(List<LigneDemande> ligneDemandes) {

        return ligneDemandeRepository.saveAll(ligneDemandes);
    }


    @Override
    public void deleteLigneDemande(Long id) {
        ligneDemandeRepository.deleteById(id);
    }
}
