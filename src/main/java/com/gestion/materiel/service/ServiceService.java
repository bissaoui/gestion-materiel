package com.gestion.materiel.service;

import com.gestion.materiel.model.Service;
import java.util.List;
import java.util.Optional;

public interface ServiceService {
    List<Service> getAllServices();
    Optional<Service> getServiceById(Long id);
    Service saveService(Service service);
    void deleteService(Long id);
}
