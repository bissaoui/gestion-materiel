# Gestion de Matériel – Backend

## Présentation Générale
Ce projet est le backend d'une application de gestion de matériel informatique, développé en **Java** avec **Spring Boot**. Il permet de gérer les utilisateurs (agents), les équipements (matériels), les affectations, et propose une API REST sécurisée par JWT.

---

## Architecture & Technologies
- **Langage** : Java 17+
- **Framework** : Spring Boot
- **ORM** : Hibernate (JPA)
- **Base de données** : PostgreSQL ou MySQL
- **Sécurité** : Spring Security + JWT
- **Gestion des dépendances** : Maven
- **Documentation API** : Swagger

---

## Structure du projet (arborescence)

```
gestion-materiel/
├── Dockerfile
├── mvnw / mvnw.cmd
├── pom.xml
├── README.md
├── run.bat / run.sh
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/gestion/materiel/
│   │   │       ├── config/           # Configurations (Swagger, sécurité, etc.)
│   │   │       ├── controller/       # Contrôleurs REST (endpoints)
│   │   │       ├── Dto/              # Objets de transfert de données
│   │   │       ├── exception/        # Gestion des exceptions personnalisées
│   │   │       ├── model/            # Entités JPA (Agent, Materiel, etc.)
│   │   │       ├── repository/       # Interfaces JPA pour accès BDD
│   │   │       ├── security/         # JWT, filtres, configuration Spring Security
│   │   │       └── service/          # Logique métier (services + implémentations)
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── static/
│   │       └── templates/
│   └── test/
│       └── java/com/gestion/materiel/
│           └── GestionMaterielApplicationTests.java
└── target/
```

---

## Tableau complet des endpoints REST

| Méthode | Chemin | Description |
|---------|--------|-------------|
| POST    | /auth/login | Authentification, retourne un JWT |
| POST    | /auth/register | Création d’un agent |
| GET     | /api/agents | Liste tous les agents |
| GET     | /api/agents/{id} | Détail d’un agent par ID |
| GET     | /api/agents/cin/{cin} | Détail d’un agent par CIN |
| POST    | /api/agents | Créer un agent |
| PUT     | /api/agents/{id} | Modifier un agent |
| DELETE  | /api/agents/{id} | Supprimer un agent |
| GET     | /api/materiels | Liste tous les matériels |
| GET     | /api/materiels/{id} | Détail d’un matériel |
| POST    | /api/materiels | Créer un matériel |
| PUT     | /api/materiels/{id} | Modifier un matériel |
| DELETE  | /api/materiels/{id} | Supprimer un matériel |
| PUT     | /api/materiels/{materielId}/affecter/{agentId} | Affecter à un agent |
| PUT     | /api/materiels/{materielId}/desaffecter | Désaffecter |
| GET     | /api/articles | Liste tous les articles |
| GET     | /api/articles/{id} | Détail d’un article |
| POST    | /api/articles | Créer un article |
| PUT     | /api/articles/{id} | Modifier un article |
| PUT     | /api/articles/qte/{id} | Modifier uniquement la quantité |
| DELETE  | /api/articles/{id} | Supprimer un article |
| GET     | /api/demandes | Liste toutes les demandes |
| GET     | /api/demandes/{id} | Détail d’une demande |
| POST    | /api/demandes | Créer une demande |
| PUT     | /api/demandes/{id} | Modifier une demande |
| PUT     | /api/demandes/{id}/validate | Valider une demande |
| DELETE  | /api/demandes/{id} | Supprimer une demande |
| GET     | /api/ligne-demandes | Liste toutes les lignes de demande |
| GET     | /api/ligne-demandes/{id} | Détail d’une ligne de demande |
| POST    | /api/ligne-demandes | Créer une ligne de demande |
| POST    | /api/ligne-demandes/bulk | Créer plusieurs lignes de demande |
| PUT     | /api/ligne-demandes/{id} | Modifier une ligne de demande |
| DELETE  | /api/ligne-demandes/{id} | Supprimer une ligne de demande |
| GET     | /api/directions | Liste toutes les directions |
| GET     | /api/directions/{id} | Détail d’une direction |
| POST    | /api/directions | Créer une direction |
| PUT     | /api/directions/{id} | Modifier une direction |
| DELETE  | /api/directions/{id} | Supprimer une direction |
| GET     | /api/departements | Liste tous les départements |
| GET     | /api/departements/{id} | Détail d’un département |
| POST    | /api/departements | Créer un département |
| PUT     | /api/departements/{id} | Modifier un département |
| DELETE  | /api/departements/{id} | Supprimer un département |
| GET     | /api/services | Liste tous les services |
| GET     | /api/services/{id} | Détail d’un service |
| POST    | /api/services | Créer un service |
| PUT     | /api/services/{id} | Modifier un service |
| DELETE  | /api/services/{id} | Supprimer un service |
| GET     | /api/types | Liste tous les types |
| GET     | /api/types/{id} | Détail d’un type |
| POST    | /api/types | Créer un type |
| DELETE  | /api/types/{id} | Supprimer un type |
| GET     | /api/marques | Liste toutes les marques |
| GET     | /api/marques/{id} | Détail d’une marque |
| GET     | /api/marques/by-type/{typeId} | Marques par type de matériel |
| POST    | /api/marques | Créer une marque |
| PUT     | /api/marques/{id} | Modifier une marque |
| DELETE  | /api/marques/{id} | Supprimer une marque |
| GET     | /api/modeles | Liste tous les modèles |
| GET     | /api/modeles/{id} | Détail d’un modèle |
| GET     | /api/modeles/by-marque/{marqueId} | Modèles par marque |
| GET     | /api/modeles/by-marque-and-type | Modèles par marque et type |
| POST    | /api/modeles | Créer un modèle |
| PUT     | /api/modeles/{id} | Modifier un modèle |
| DELETE  | /api/modeles/{id} | Supprimer un modèle |
| GET     | /admin/users | Liste tous les utilisateurs |
| POST    | /admin/create | Créer un utilisateur |

---

## Entités principales et relations

### Agent
- **Champs** : id, CIN, username, password, nom, poste, role (ADMIN/USER)
- **Relations** :
  - Plusieurs agents peuvent appartenir à une direction, un département, un service

### Materiel
- **Champs** : id, numeroSerie, dateAffectation
- **Relations** :
  - typeMateriel (ManyToOne)
  - marque (ManyToOne)
  - modele (ManyToOne)
  - agent (ManyToOne, optionnel)

### TypeMateriel
- **Champs** : id, nom
- **Relations** :
  - Plusieurs types peuvent être associés à plusieurs marques (ManyToMany)

### Marque
- **Champs** : id, nom
- **Relations** :
  - Plusieurs marques peuvent être associées à plusieurs types (ManyToMany)

### Modele
- **Champs** : id, nom
- **Relations** :
  - marque (ManyToOne)
  - typeMateriel (ManyToOne)

### Article
- **Champs** : id, code, designation, unite, qte

### Demande
- **Champs** : id, date, validation
- **Relations** :
  - agent (ManyToOne)
  - lignes (OneToMany vers LigneDemande)

### LigneDemande
- **Champs** : id, quantite, observation
- **Relations** :
  - article (ManyToOne)
  - demande (ManyToOne)

### Direction
- **Champs** : id, libelle, abreviation
- **Relations** :
  - agents (OneToMany)
  - departements (OneToMany)

### Departement
- **Champs** : id, libelle, abreviation
- **Relations** :
  - direction (ManyToOne)
  - agents (OneToMany)
  - services (OneToMany)

### Service
- **Champs** : id, libelle, abreviation
- **Relations** :
  - departement (ManyToOne)
  - agents (OneToMany)

---

## Fonctionnalités Principales
### 1. Authentification & Sécurité
- Authentification via JWT (login, register)
- Rôles : `ADMIN`, `USER`
- Filtres de sécurité (JWTFilter)
- Accès restreint selon le rôle

### 2. Gestion des Agents
- CRUD complet sur les agents
- Recherche par ID ou CIN
- Chiffrement des mots de passe

### 3. Gestion du Matériel
- CRUD complet sur les matériels
- Affectation/désaffectation à un agent
- Vérification d’unicité du numéro de série
- Historique des affectations (date, agent)
- Mise à jour sécurisée (PUT `/api/materiels/{id}`)

### 4. Gestion des Entités Annexes
- Types, Marques, Modèles, Départements, Directions, Services
- CRUD pour chaque entité

### 5. Gestion des Demandes
- Création, validation, suppression de demandes de matériel

---

## Endpoints REST Principaux
| Méthode | Endpoint                        | Description                        | Accès         |
|---------|----------------------------------|------------------------------------|---------------|
| POST    | /auth/login                     | Authentification, obtention JWT    | Public        |
| POST    | /auth/register                  | Création d’un agent                | Admin         |
| GET     | /api/agents                     | Liste des agents                   | Admin         |
| PUT     | /api/agents/{id}                | Modifier un agent                  | Admin         |
| GET     | /api/materiels                  | Liste des matériels                | Authentifié   |
| POST    | /api/materiels                  | Ajouter un matériel                | Admin         |
| PUT     | /api/materiels/{id}             | Modifier un matériel               | Admin         |
| DELETE  | /api/materiels/{id}             | Supprimer un matériel              | Admin         |
| PUT     | /api/materiels/{id}/affecter/{agentId} | Affecter à un agent        | Admin         |
| PUT     | /api/materiels/{id}/desaffecter | Désaffecter du matériel            | Admin         |

---

## Exemple de Flux : Mise à jour d’un Matériel
1. **Requête PUT** sur `/api/materiels/{id}` avec un objet JSON contenant tous les champs du matériel à mettre à jour.
2. Le backend vérifie l’existence du matériel, l’unicité du numéro de série, et met à jour les champs.
3. Réponse : l’objet mis à jour ou une erreur (ex : numéro de série déjà existant).

---

## Authentification JWT
- À l’authentification (`/auth/login`), un token JWT est retourné.
- Ce token doit être envoyé dans l’en-tête `Authorization: Bearer <token>` pour toutes les requêtes protégées.
- Les rôles sont vérifiés à chaque requête.

---

## Démarrage du Projet
### Prérequis
- Java 17+
- Maven
- Base de données PostgreSQL ou MySQL (configurable dans `application.properties`)

### Lancer en local
#### Windows (PowerShell)
```powershell
$env:JAVA_HOME="C:\Program Files\Java\jdk-17"
./mvnw.cmd spring-boot:run
```
#### Linux/Mac
```sh
export JAVA_HOME=/path/to/your/jdk
./mvnw spring-boot:run
```

### Accès API & Documentation
- API : http://localhost:8080/
- Swagger UI : http://localhost:8080/swagger-ui.html

---

## Contribution & Support
Pour toute question ou contribution, ouvrez une issue ou une pull request sur le dépôt.

---

## Auteur
Ce projet a été créé et développé par **YASSINE BISSAOUI**.
