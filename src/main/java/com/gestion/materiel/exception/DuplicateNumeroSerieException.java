package com.gestion.materiel.exception;

public class DuplicateNumeroSerieException extends RuntimeException {
    public DuplicateNumeroSerieException(String numeroSerie) {
        super("Le numéro de série '" + numeroSerie + "' existe déjà.");
    }
} 