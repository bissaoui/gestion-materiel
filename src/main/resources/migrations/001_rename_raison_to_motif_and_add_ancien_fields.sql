-- Migration: Renommer raison en motif et ajouter les champs pour l'ancien matériel
-- Date: 2024
-- Compatible MySQL/phpMyAdmin
-- 
-- INSTRUCTIONS:
-- 1. Si la colonne 'motif' existe déjà (erreur #1060), exécutez SEULEMENT la partie "Étape 2"
-- 2. Si la colonne 'raison' existe encore, exécutez d'abord "Étape 1" puis "Étape 2"
-- 3. Pour vérifier quelles colonnes existent, utilisez: DESCRIBE besoins_exprimes;

-- Étape 1: Renommer la colonne raison en motif (UNIQUEMENT si raison existe encore)
-- Si vous avez l'erreur "Nom du champ 'motif' déjà utilisé", SAUTEZ cette étape
ALTER TABLE besoins_exprimes 
CHANGE COLUMN raison motif VARCHAR(1000) NOT NULL;

ALTER TABLE besoins_exprimes 
DROP COLUMN raison;

-- Étape 2: Ajouter les nouvelles colonnes pour l'ancien matériel
-- Exécutez cette partie même si certaines colonnes existent déjà (vous aurez une erreur si elles existent, mais c'est sans danger)
ALTER TABLE besoins_exprimes 
ADD COLUMN date_affectation_ancien DATE,
ADD COLUMN numero_serie_ancien VARCHAR(100),
ADD COLUMN marque_ancien VARCHAR(100),
ADD COLUMN modele_ancien VARCHAR(100);
