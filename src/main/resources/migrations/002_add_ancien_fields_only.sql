-- Migration: Ajouter uniquement les champs pour l'ancien matériel
-- Date: 2024
-- Compatible MySQL/phpMyAdmin
-- 
-- Utilisez ce script si la colonne 'motif' existe déjà
-- (si vous avez eu l'erreur #1060 - Nom du champ 'motif' déjà utilisé)

-- Ajouter les nouvelles colonnes pour l'ancien matériel
-- Si une colonne existe déjà, vous aurez une erreur mais vous pouvez l'ignorer
ALTER TABLE besoins_exprimes 
ADD COLUMN date_affectation_ancien DATE,
ADD COLUMN numero_serie_ancien VARCHAR(100),
ADD COLUMN marque_ancien VARCHAR(100),
ADD COLUMN modele_ancien VARCHAR(100);
