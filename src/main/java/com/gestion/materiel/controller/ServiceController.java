package com.gestion.materiel.controller;

import com.gestion.materiel.Dto.DepartementDto;
import com.gestion.materiel.Dto.ServiceDto;
import com.gestion.materiel.model.Departement;
import com.gestion.materiel.model.Service;
import com.gestion.materiel.service.ServiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

        import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/services")
@CrossOrigin("*")
public class ServiceController {
    private final ServiceService serviceService;

    public ServiceController(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    @GetMapping
    public List<ServiceDto> getAllServices() {
        List<Service> services = serviceService.getAllServices();
        return services.stream().map(ServiceDto::new).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Service> getServiceById(@PathVariable Long id) {
        Optional<Service> service = serviceService.getServiceById(id);
        return service.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PostMapping
    public ResponseEntity<Service> createService(@RequestBody Service service) {
        Service newService = serviceService.saveService(service);
        return ResponseEntity.ok(newService);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceDto> updateDepartement(@PathVariable Long id, @RequestBody Service service) {
        if (!serviceService.getServiceById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        service.setId(id);
        Service updatedService = serviceService.saveService(service);
        return ResponseEntity.ok(new ServiceDto(updatedService));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable Long id) {
        if (!serviceService.getServiceById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        serviceService.deleteService(id);
        return ResponseEntity.noContent().build();
    }
}
