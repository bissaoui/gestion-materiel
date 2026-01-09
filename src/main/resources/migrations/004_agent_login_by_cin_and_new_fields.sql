-- Migration: Modifications Agent - Login par CIN, nouvelles colonnes et suppression username
-- Date: 2024
-- Compatible MySQL/phpMyAdmin
-- 
-- INSTRUCTIONS:
-- 1. Ajouter les nouvelles colonnes
-- 2. Supprimer la colonne username (si elle existe)
-- 3. Vérifier que la colonne CIN existe et est unique

-- Étape 1: Ajouter les nouvelles colonnes pour Agent
ALTER TABLE agent 
ADD COLUMN prenom VARCHAR(255) NOT NULL DEFAULT '',
ADD COLUMN email VARCHAR(255) UNIQUE,
ADD COLUMN matricule VARCHAR(255) UNIQUE,
ADD COLUMN date_naissance DATE,
ADD COLUMN sexe VARCHAR(10);

-- Étape 2: Supprimer la colonne username (si elle existe)
-- Si vous avez une erreur indiquant que la colonne n'existe pas, c'est normal
ALTER TABLE agent 
DROP COLUMN username;

-- Note: La colonne CIN doit déjà exister et être unique
-- Si ce n'est pas le cas, exécutez:
-- ALTER TABLE agent ADD COLUMN CIN VARCHAR(255) UNIQUE NOT NULL;
