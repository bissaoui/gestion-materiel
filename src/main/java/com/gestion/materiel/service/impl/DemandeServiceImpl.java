package com.gestion.materiel.service.impl;

import com.gestion.materiel.model.Demande;
import com.gestion.materiel.repository.DemandeRepository;
import com.gestion.materiel.service.DemandeService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class DemandeServiceImpl implements DemandeService {
    private final DemandeRepository demandeRepository;

    public DemandeServiceImpl(DemandeRepository demandeRepository) {
        this.demandeRepository = demandeRepository;
    }

    @Override
    public List<Demande> getAllDemandes() {
        return demandeRepository.findAll();
    }

    @Override
    public Optional<Demande> getDemandeById(Long id) {
        return demandeRepository.findById(id);
    }

    @Override
    public Demande saveDemande(Demande demande) {
        return demandeRepository.save(demande);
    }

    @Override
    public void deleteDemande(Long id) {
        demandeRepository.deleteById(id);
    }
}
