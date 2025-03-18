package com.gestion.materiel.service.impl;

import com.gestion.materiel.model.Direction;
import com.gestion.materiel.repository.DirectionRepository;
import com.gestion.materiel.service.DirectionService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class DirectionServiceImpl implements DirectionService {
    private final DirectionRepository directionRepository;

    public DirectionServiceImpl(DirectionRepository directionRepository) {
        this.directionRepository = directionRepository;
    }

    @Override
    public List<Direction> getAllDirections() {
        return directionRepository.findAll();
    }

    @Override
    public Optional<Direction> getDirectionById(Long id) {
        return directionRepository.findById(id);
    }

    @Override
    public Direction saveDirection(Direction direction) {
        return directionRepository.save(direction);
    }

    @Override
    public void deleteDirection(Long id) {
        directionRepository.deleteById(id);
    }
}
