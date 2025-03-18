package com.gestion.materiel.service.impl;

import com.gestion.materiel.model.Departement;
import com.gestion.materiel.repository.DepartementRepository;
import com.gestion.materiel.service.DepartementService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class DepartementServiceImpl implements DepartementService {
    private final DepartementRepository departementRepository;

    public DepartementServiceImpl(DepartementRepository departementRepository) {
        this.departementRepository = departementRepository;
    }

    @Override
    public List<Departement> getAllDepartements() {
        return departementRepository.findAll();
    }

    @Override
    public Optional<Departement> getDepartementById(Long id) {
        return departementRepository.findById(id);
    }

    @Override
    public Departement saveDepartement(Departement departement) {
        return departementRepository.save(departement);
    }

    @Override
    public void deleteDepartement(Long id) {
        departementRepository.deleteById(id);
    }
}
