# Back end gestion-materiel
Back-End de l'Application de Gestion de Matériel
Le back-end de l'application de gestion de matériel est développé avec Spring Boot, un framework Java robuste et flexible, permettant de gérer l'authentification, l'autorisation, la gestion des utilisateurs (agents), ainsi que la manipulation des ressources matérielles.

Architecture et Technologies Utilisées
Langage : Java
Framework : Spring Boot
Base de données : PostgreSQL/MySQL
Sécurité : Spring Security avec JWT
Gestion des dépendances : Maven
ORM (Mapping Objet-Relationnel) : Hibernate
API REST : Spring Web
Modules Principaux
1. Sécurité et Authentification
Utilisation de Spring Security pour la gestion des accès.
Authentification par JWT (JSON Web Token) pour sécuriser les requêtes API.
Gestion des rôles et permissions (ADMIN, AGENT, etc.).
Filtrage des requêtes avec JwtFilter pour valider les tokens.
2. Gestion des Utilisateurs (Agents)
Création, modification et suppression des agents via des endpoints REST.
Chiffrement des mots de passe avec BCryptPasswordEncoder.
Chargement des utilisateurs via UserDetailsService.
3. Gestion du Matériel
Ajout, mise à jour, suppression et récupération des équipements.
Association des équipements à des agents responsables.
Historique des prêts et retours.
4. Gestion des Accès API (CORS & Autorisations)
CORS activé pour permettre les requêtes depuis le front-end.
Sécurisation des endpoints avec des restrictions basées sur les rôles.
Mise en place d'une politique de session stateless pour une API REST sécurisée.
5. Base de Données et ORM
Utilisation d'Hibernate/JPA pour interagir avec la base de données.
Définition d'entités relationnelles pour les agents, matériels, et transactions.
Gestion des requêtes via Spring Data JPA.
Endpoints Principaux
Méthode	Endpoint	Description	Accès
POST	/auth/login	Authentification et génération du JWT	Public
POST	/auth/register	Création d'un nouvel agent	Admin
GET	/api/agents	Liste des agents	Admin
GET	/api/materiels	Liste du matériel disponible	Authentifié
POST	/api/materiels	Ajout d’un matériel	Admin
PUT	/api/materiels/{id}	Mise à jour d’un matériel	Admin
DELETE	/api/materiels/{id}	Suppression d’un matériel	Admin
