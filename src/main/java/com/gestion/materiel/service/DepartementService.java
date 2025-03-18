package com.gestion.materiel.service;

import com.gestion.materiel.model.Departement;
import java.util.List;
import java.util.Optional;

public interface DepartementService {
    List<Departement> getAllDepartements();
    Optional<Departement> getDepartementById(Long id);
    Departement saveDepartement(Departement departement);
    void deleteDepartement(Long id);
}
