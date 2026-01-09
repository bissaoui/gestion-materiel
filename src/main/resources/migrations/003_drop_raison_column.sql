-- Migration: Supprimer la colonne raison (remplacée par motif)
-- Date: 2024
-- Compatible MySQL/phpMyAdmin
-- 
-- Ce script supprime l'ancienne colonne 'raison' qui a été remplacée par 'motif'
-- À exécuter uniquement si la colonne 'raison' existe encore dans la table

-- Supprimer la colonne raison
ALTER TABLE besoins_exprimes 
DROP COLUMN raison;
