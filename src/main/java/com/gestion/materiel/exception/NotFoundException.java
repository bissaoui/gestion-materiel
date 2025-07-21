package com.gestion.materiel.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String entity, Long id) {
        super(entity + " avec l'id " + id + " non trouvé.");
    }
} 