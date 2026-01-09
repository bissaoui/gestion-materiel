-- Migration: Supprimer définitivement la colonne username de la table agent
-- Date: 2026-01-07
-- Problème: La colonne username existe toujours et cause des erreurs lors de l'insertion
-- Solution: Supprimer la colonne username

-- IMPORTANT: Exécutez ce script dans votre base de données MySQL/phpMyAdmin
-- La colonne username doit être supprimée car elle n'est plus utilisée dans le code

-- Méthode 1: Pour MySQL 8.0.23+ (DROP COLUMN IF EXISTS)
-- ALTER TABLE agent DROP COLUMN IF EXISTS username;

-- Méthode 2: Pour toutes les versions MySQL (méthode recommandée)
-- Vérifier d'abord si la colonne existe, puis la supprimer
SET @dbname = DATABASE();
SET @tablename = "agent";
SET @columnname = "username";

SET @preparedStatement = (SELECT IF(
  (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE
      (table_name = @tablename)
      AND (table_schema = @dbname)
      AND (column_name = @columnname)
  ) > 0,
  CONCAT("ALTER TABLE ", @tablename, " DROP COLUMN ", @columnname),
  "SELECT 'Column username does not exist - nothing to do' as message"
));

PREPARE alterIfExists FROM @preparedStatement;
EXECUTE alterIfExists;
DEALLOCATE PREPARE alterIfExists;

-- Méthode 3: Si les méthodes ci-dessus ne fonctionnent pas, utilisez directement:
-- ALTER TABLE agent DROP COLUMN username;
