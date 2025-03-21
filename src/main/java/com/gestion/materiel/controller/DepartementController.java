
package com.gestion.materiel.controller;

import com.gestion.materiel.Dto.DepartementDto;
import com.gestion.materiel.model.Departement;
import com.gestion.materiel.service.DepartementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

        import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/departements")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class DepartementController {
    private final DepartementService departementService;

    public DepartementController(DepartementService departementService) {
        this.departementService = departementService;
    }

    @GetMapping
    public List<DepartementDto> getAllDepartements() {
        List<Departement> departements = departementService.getAllDepartements();
        return departements.stream().map(DepartementDto::new).collect(Collectors.toList());

    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartementDto> getDepartementById(@PathVariable Long id) {
        Optional<Departement> departement = departementService.getDepartementById(id);

        return departement.map(dep -> ResponseEntity.ok(new DepartementDto(dep)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PostMapping
    public ResponseEntity<DepartementDto> createDepartement(@RequestBody Departement departement) {
        Departement newDepartement = departementService.saveDepartement(departement);
        return ResponseEntity.ok( new DepartementDto(newDepartement));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartementDto> updateDepartement(@PathVariable Long id, @RequestBody Departement departement) {
        if (!departementService.getDepartementById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        departement.setId(id);
        Departement updatedDepartement = departementService.saveDepartement(departement);
        return ResponseEntity.ok(new DepartementDto(updatedDepartement));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartement(@PathVariable Long id) {
        if (!departementService.getDepartementById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        departementService.deleteDepartement(id);
        return ResponseEntity.noContent().build();
    }
}
