package com.gestion.materiel.exception;

public class DuplicateNomException extends RuntimeException {
    public DuplicateNomException(String entity, String nom) {
        super(entity + " avec le nom '" + nom + "' existe déjà.");
    }
} 