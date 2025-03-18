
package com.gestion.materiel.controller;

import com.gestion.materiel.model.Direction;
import com.gestion.materiel.service.DirectionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

        import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/directions")
@CrossOrigin("*")
public class DirectionController {
    private final DirectionService directionService;

    public DirectionController(DirectionService directionService) {
        this.directionService = directionService;
    }

    @GetMapping
    public List<Direction> getAllDirections() {
        return directionService.getAllDirections();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Direction> getDirectionById(@PathVariable Long id) {
        Optional<Direction> direction = directionService.getDirectionById(id);
        return direction.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Direction> createDirection(@RequestBody Direction direction) {
        Direction newDirection = directionService.saveDirection(direction);
        return ResponseEntity.ok(newDirection);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Direction> updateDirection(@PathVariable Long id, @RequestBody Direction direction) {
        System.out.println("Received PUT request for ID: " + id);
        System.out.println("Request Body: " + direction);

        if (!directionService.getDirectionById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        direction.setId(id);
        Direction updatedDirection = directionService.saveDirection(direction);
        return ResponseEntity.ok(updatedDirection);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDirection(@PathVariable Long id) {
        if (!directionService.getDirectionById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        directionService.deleteDirection(id);
        return ResponseEntity.noContent().build();
    }
}
