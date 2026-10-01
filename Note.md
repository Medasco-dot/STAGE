# SIGECO — Notes d'analyse

## 1. À quoi sert l'application ?

Le service Contentieux et Juridique est chargé de la gestion de l'ensemble des litiges impliquant la CARFO : rejets de dossiers de prestations, contestations d'actes de carrière, litiges liés aux marchés publics, ainsi que des affaires à caractère pénal (fraude). Le service assure le suivi de chaque dossier depuis sa réclamation initiale jusqu'à sa clôture, en coordination avec des avocats-conseils externes lorsque nécessaire.

À ce jour, le suivi repose sur Excel et un archivage papier manuel, ce qui pose plusieurs limites :
- Absence de traçabilité fine des étapes de chaque dossier (audiences, décisions successives, relances)
- Difficulté à consulter rapidement l'historique complet d'un dossier ou d'une partie
- Risque de perte d'information (informations dispersées entre les échanges avec les avocats et le suivi interne)
- Absence de vision consolidée du risque financier global pesant sur l'institution

Objectif : une application web pour gérer tout le cycle de vie des dossiers contentieux, de l'ouverture à la clôture.
- Centraliser les dossiers dans une base de données unique et structurée
- Suivre les parties impliquées et leur rôle (demandeur, défendeur)
- Tracer chronologiquement les étapes, audiences et décisions
- Suivre les juristes internes et cabinets d'avocats-conseils externes de chaque dossier
- Suivre les indicateurs financiers (montant réclamé, frais de justice, montant obtenu, risque financier)
- Rechercher et consulter rapidement les dossiers selon différents critères


## 2. Qui l'utilise ? (les acteurs)

- Juriste
- Chef de service
- Direction Générale


## 3. Fonctionnalités par objet

**Dossier**
- Créer, consulter, modifier un dossier contentieux
- Associer un dossier à un type de contentieux et à un assuré
- Faire évoluer un dossier à travers ses étapes, avec historisation des dates (l'étape courante = la dernière étape sans date de fin)
- Consulter l'historique complet des étapes d'un dossier

**Partie**
- Enregistrer les parties liées à un dossier (assuré, ayants droit, partie adverse)
- Attribuer un rôle à chaque partie (demandeur / défendeur)

**Acteurs internes et externes**
- Associer un ou plusieurs juristes internes à un dossier
- Associer un ou plusieurs cabinets d'avocats-conseils à un dossier, avec le nom de l'avocat référent

**Audience et décision**
- Enregistrer chaque audience (date, juridiction, type d'étape : première instance, appel, cassation)
- Compléter la décision après coup (nature, résumé, issue pour la CARFO, montants)

**Document**
- Associer les pièces d'un dossier (requête, pièces justificatives, PV d'audience, décision de justice, etc.)
- Téléverser le fichier réel, avec validation du type MIME et stockage persistant
- Gérer le référentiel des types de document (créer, modifier, supprimer) sans liste figée dans le code

**Suivi financier**
- Enregistrer le montant réclamé, les frais de justice, le montant obtenu
- Consulter une vue consolidée du risque financier, par dossier et globale

**Recherche et reporting**
- Rechercher un dossier par numéro, partie, type de contentieux ou étape
- Générer des statistiques simples (nombre de dossiers par étape, par type)

**Utilisateurs et accès**
- Authentifier un utilisateur (identifiant + mot de passe) avant tout accès
- Un rôle unique par compte parmi : juriste, chef de service, Direction Générale
- Restreindre les actions selon le rôle
- Comptes créés en amont (pas d'auto-inscription, pas de réinitialisation par e-mail)


## 4. Les entités (MLD)

`¹` = clé étrangère (elle contient la clé primaire d'une ligne d'une autre table).

### Entités principales

```
DOSSIER (numero_dossier, date_ouverture, resume_affaire, observation,
         risque_financier, montant_reclame, num_contentieux¹)

ETAPE_DOSSIER (id, etape, date_debut, date_fin, numero_dossier¹)

PARTIE (id, nom, prenom, numero_cnib, statut_matrimonial)

JURISTE (matricule, nom, prenoms, specialite)

CABINET (identifiant_cabinet, nom_cabinet, mail, telephone, adresse)

AUDIENCE_DECISION (num_audience_decision, date, lieu_audience, type_etape,
                   nature_decision, resume_decision, issue_pour_carfo,
                   montant_obtenu, montant_du, frais_justice, numero_dossier¹)

DOCUMENT (id_document, date_ajout, fichier, numero_dossier¹, id_type¹)

UTILISATEUR (id, login, mot_de_passe_hash, role, actif, matricule_juriste¹)
```

### Référentiels (listes modifiables par les utilisateurs)

```
TYPE_DOCUMENT (id_type, libelle)

TYPE_CONTENTIEUX (num_contentieux, libelle)
```

### Tables de jonction (associations n,n)

```
IMPLICATION (numero_dossier¹, id_partie¹, role, lien_parente)
   clé primaire composite : (numero_dossier, id_partie)

DOSSIER_JURISTE (numero_dossier¹, matricule¹)
   clé primaire composite : (numero_dossier, matricule)

DOSSIER_CABINET (numero_dossier¹, identifiant_cabinet¹, nom_avocat_referent)
   clé primaire composite : (numero_dossier, identifiant_cabinet)
```

### Remarques

- **ETAPE_DOSSIER** : pas d'attribut `statut` dans DOSSIER ; l'étape courante est celle dont `date_fin` est vide.
- **AUDIENCE_DECISION** : fusion d'AUDIENCE et DECISION (relation (1,1) des deux côtés). Les champs de la décision sont facultatifs pour pouvoir enregistrer une audience avant son issue.
- **CABINET** : représente un cabinet d'avocats, pas une personne. `nom_avocat_referent` (dans DOSSIER_CABINET) identifie l'avocat qui suit le dossier.
- **UTILISATEUR** : `login` unique ; `role` = JURISTE / CHEF_SERVICE / DIRECTION_GENERALE ; `matricule_juriste` facultatif. Le chef de service n'existe pas dans la table JURISTE, donc il n'y a aucun juriste à relier à son compte : la clé étrangère reste vide.


## 5. Liens entre les objets (cardinalités)

| Association | Cardinalités | Signification |
|---|---|---|
| DOSSIER — ETAPE_DOSSIER | (1,n) — (1,1) | Un dossier traverse au moins une étape ; une étape appartient à un seul dossier |
| DOSSIER — TYPE_CONTENTIEUX | (1,1) — (0,n) | Un dossier relève d'exactement un type ; un type couvre plusieurs dossiers |
| DOSSIER — PARTIE (via IMPLICATION) | (1,n) — (1,n) | Un dossier implique une ou plusieurs parties, chacune avec un rôle propre au dossier |
| DOSSIER — JURISTE | (0,n) — (0,n) | Un dossier peut être suivi par plusieurs juristes ; un juriste peut n'avoir aucun dossier |
| DOSSIER — CABINET | (0,n) — (0,n) | Un dossier peut être défendu par plusieurs cabinets ; un cabinet peut n'avoir aucun dossier |
| DOSSIER — AUDIENCE_DECISION | (0,n) — (1,1) | Un dossier a zéro ou plusieurs audiences ; une audience appartient à un seul dossier |
| DOSSIER — DOCUMENT | (0,n) — (1,1) | Un dossier a zéro ou plusieurs documents ; un document appartient à un seul dossier |
| DOCUMENT — TYPE_DOCUMENT | (1,1) — (0,n) | Un document a exactement un type ; un type peut servir à plusieurs documents |
| UTILISATEUR — JURISTE | (0,1) — (0,1) | Un compte est lié au plus à un juriste ; un juriste a au plus un compte |


## 6. Modèle physique (MPD) — référence

> Ce SQL sert de référence pour comprendre. Plus tard, c'est Spring Boot (JPA) qui créera les tables à partir des classes Java.

```sql
CREATE TABLE TYPE_CONTENTIEUX (
  num_contentieux  INT AUTO_INCREMENT PRIMARY KEY,
  libelle          VARCHAR(100) NOT NULL
);

CREATE TABLE TYPE_DOCUMENT (
  id_type  INT AUTO_INCREMENT PRIMARY KEY,
  libelle  VARCHAR(100) NOT NULL
);

CREATE TABLE DOSSIER (
  numero_dossier   VARCHAR(15)   PRIMARY KEY,
  date_ouverture   DATE          NOT NULL,
  resume_affaire   TEXT,
  observation      TEXT,
  risque_financier DECIMAL(12,2),
  montant_reclame  DECIMAL(12,2),
  num_contentieux  INT NOT NULL,
  FOREIGN KEY (num_contentieux) REFERENCES TYPE_CONTENTIEUX(num_contentieux)
);

CREATE TABLE ETAPE_DOSSIER (
  id              INT AUTO_INCREMENT PRIMARY KEY,
  etape           ENUM('ouvert','en_instruction','juge','en_appel',
                       'en_cassation','cloture','classe_sans_suite') NOT NULL,
  date_debut      DATE NOT NULL,
  date_fin        DATE,
  numero_dossier  VARCHAR(15) NOT NULL,
  FOREIGN KEY (numero_dossier) REFERENCES DOSSIER(numero_dossier)
);

CREATE TABLE PARTIE (
  id                  INT AUTO_INCREMENT PRIMARY KEY,
  nom                 VARCHAR(50) NOT NULL,
  prenom              VARCHAR(50) NOT NULL,
  numero_cnib         VARCHAR(20),
  statut_matrimonial  ENUM('marie','celibataire','divorce','veuf')
);

CREATE TABLE JURISTE (
  matricule   VARCHAR(15) PRIMARY KEY,
  nom         VARCHAR(50) NOT NULL,
  prenoms     VARCHAR(50) NOT NULL,
  specialite  ENUM('pension_retraite','pension_reversement','acte_carriere',
                   'marche_public','penal','polyvalent')
);

CREATE TABLE CABINET (
  identifiant_cabinet  VARCHAR(15)  PRIMARY KEY,
  nom_cabinet          VARCHAR(100) NOT NULL,
  mail                 VARCHAR(100),
  telephone            VARCHAR(20),
  adresse              VARCHAR(100)
);

CREATE TABLE AUDIENCE_DECISION (
  num_audience_decision  INT AUTO_INCREMENT PRIMARY KEY,
  date                   DATE NOT NULL,
  lieu_audience          VARCHAR(100),
  type_etape             ENUM('premiere_instance','appel','cassation') NOT NULL,
  nature_decision        ENUM('jugement','arret','ordonnance'),
  resume_decision        TEXT,
  issue_pour_carfo       ENUM('favorable','defavorable','partiellement_favorable'),
  montant_obtenu         DECIMAL(12,2),
  montant_du             DECIMAL(12,2),
  frais_justice          DECIMAL(12,2),
  numero_dossier         VARCHAR(15) NOT NULL,
  FOREIGN KEY (numero_dossier) REFERENCES DOSSIER(numero_dossier)
);

CREATE TABLE DOCUMENT (
  id_document     INT AUTO_INCREMENT PRIMARY KEY,
  date_ajout      DATE,
  fichier         VARCHAR(255),
  numero_dossier  VARCHAR(15) NOT NULL,
  id_type         INT NOT NULL,
  FOREIGN KEY (numero_dossier) REFERENCES DOSSIER(numero_dossier),
  FOREIGN KEY (id_type) REFERENCES TYPE_DOCUMENT(id_type)
);

CREATE TABLE UTILISATEUR (
  id                 INT AUTO_INCREMENT PRIMARY KEY,
  login              VARCHAR(50)  NOT NULL UNIQUE,
  mot_de_passe_hash  VARCHAR(255) NOT NULL,
  role               ENUM('JURISTE','CHEF_SERVICE','DIRECTION_GENERALE') NOT NULL,
  actif              BOOLEAN NOT NULL DEFAULT TRUE,
  matricule_juriste  VARCHAR(15) UNIQUE,
  FOREIGN KEY (matricule_juriste) REFERENCES JURISTE(matricule)
);

CREATE TABLE IMPLICATION (
  numero_dossier  VARCHAR(15) NOT NULL,
  id_partie       INT NOT NULL,
  role            ENUM('demandeur','defendeur'),
  lien_parente    ENUM('assure','epoux_epouse','enfant','autre_ayant_droit'),
  PRIMARY KEY (numero_dossier, id_partie),
  FOREIGN KEY (numero_dossier) REFERENCES DOSSIER(numero_dossier),
  FOREIGN KEY (id_partie) REFERENCES PARTIE(id)
);

CREATE TABLE DOSSIER_JURISTE (
  numero_dossier  VARCHAR(15) NOT NULL,
  matricule       VARCHAR(15) NOT NULL,
  PRIMARY KEY (numero_dossier, matricule),
  FOREIGN KEY (numero_dossier) REFERENCES DOSSIER(numero_dossier),
  FOREIGN KEY (matricule) REFERENCES JURISTE(matricule)
);

CREATE TABLE DOSSIER_CABINET (
  numero_dossier       VARCHAR(15) NOT NULL,
  identifiant_cabinet  VARCHAR(15) NOT NULL,
  nom_avocat_referent  VARCHAR(100),
  PRIMARY KEY (numero_dossier, identifiant_cabinet),
  FOREIGN KEY (numero_dossier) REFERENCES DOSSIER(numero_dossier),
  FOREIGN KEY (identifiant_cabinet) REFERENCES CABINET(identifiant_cabinet)
);
```


## 7. Questions en cours (à vous de répondre)

### Q1 — Supprimer un type utilisé
Que doit faire l'application si on veut supprimer « Requête » alors que 50 documents l'utilisent ?

Votre première réponse : *« l'application va juste enlever l'id du type correspondant dans la table document »*

À revoir : dans le MPD, `id_type` est `NOT NULL` (un document a **exactement 1** type, cardinalité (1,1)). Peut-on alors « enlever » l'id ? Quelle autre solution ?

Votre nouvelle réponse : l'application nous afficherra message d'erreur impossible de supprimer car la clé étrangère empêche de supprimer un type encore utilisé par des documents


### Q2 — `frais_justice` en double
`frais_justice` est dans DOSSIER **et** dans AUDIENCE_DECISION. Lequel garder, et comment obtenir l'autre ?

Votre réponse : on garde dans audience decision car celle-ci permettrait de mieux calculer la sum pour l'afficher dans dossier 

Quels dossiers voyez-vous dans src/ ?: Nous voyons 
.gitattributes
.gitignore
HELP.md
mvnw
mvnw.cmd
pom.xml
.mvn
src
Ouvrez pom.xml : retrouvez-vous vos 4 dépendances ? Recopiez leurs <artifactId>.
<artifactId>sigeco</artifactId>
<artifactId>spring-boot-starter-data-jpa</artifactId>
<artifactId>spring-boot-starter-webmvc</artifactId>
<artifactId>spring-boot-starter-data-jpa-test</artifactId>
<artifactId>spring-boot-starter-validation-test</artifactId>
<artifactId>spring-boot-starter-webmvc-test</artifactId>
<artifactId>spring-boot-maven-plugin</artifactId>
non il n'y a pas mysql drivers 

