package com.gestion.materiel.service;

import com.gestion.materiel.model.Direction;
import java.util.List;
import java.util.Optional;

public interface DirectionService {
    List<Direction> getAllDirections();
    Optional<Direction> getDirectionById(Long id);
    Direction saveDirection(Direction direction);
    void deleteDirection(Long id);
}
