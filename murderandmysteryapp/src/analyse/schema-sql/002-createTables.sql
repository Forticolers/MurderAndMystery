/*
    murderandmystery - Script de création du schéma
    
    julien jeanbourquin (julien.jeanbourquin AT hotmail.com)

    La base de données murderandmysteryDB doit contenir l'extension "uuid-ossp" 
    pour la génération des uuid avec la fonction uuid_generate_v4().

    CREATE EXTENSION "uuid-ossp";

    select uuid_generate_v4();
*/
-- Création de tables


CREATE TABLE IF NOT EXISTS objets (
	uuid VARCHAR, -- aid
	version INTEGER DEFAULT 1,
	nom TEXT NOT NULL,
	description TEXT,
	image BYTEA,    
	ramassable BOOLEAN NOT NULL,    
    
	date_creation TIMESTAMP DEFAULT now(),
	user_creation TEXT,
	date_modification TIMESTAMP,
	user_modification TEXT,
	date_suppression TIMESTAMP,
	user_suppression TEXT,
               
	CONSTRAINT pk_objet
		PRIMARY KEY (uuid)
)
;

CREATE TABLE IF NOT EXISTS salle_fictives (
	uuid VARCHAR, -- aid
	version INTEGER DEFAULT 1,
	nom TEXT NOT NULL,
	image BYTEA, 
    
	date_creation TIMESTAMP DEFAULT now(),
	user_creation TEXT,
	date_modification TIMESTAMP,
	user_modification TEXT,
	date_suppression TIMESTAMP,
	user_suppression TEXT,
               
	CONSTRAINT pk_salle_fictives
		PRIMARY KEY (uuid)
)
;

CREATE TABLE IF NOT EXISTS personnages (
	uuid VARCHAR, -- aid
	version INTEGER DEFAULT 1,
	classe_uuid VARCHAR,
	nom TEXT NOT NULL,
	image BYTEA,    
	code_connexion TEXT NOT NULL,   
    
	date_creation TIMESTAMP DEFAULT now(),
	user_creation TEXT,
	date_modification TIMESTAMP,
	user_modification TEXT,
	date_suppression TIMESTAMP,
	user_suppression TEXT,
               
	CONSTRAINT pk_personnages
		PRIMARY KEY (uuid)
)
;

CREATE TABLE IF NOT EXISTS classes (
	uuid VARCHAR, -- aid
	version INTEGER DEFAULT 1,
	nom TEXT NOT NULL,
	description TEXT,    
    
	date_creation TIMESTAMP DEFAULT now(),
	user_creation TEXT,
	date_modification TIMESTAMP,
	user_modification TEXT,
	date_suppression TIMESTAMP,
	user_suppression TEXT,
               
	CONSTRAINT pk_classes
		PRIMARY KEY (uuid)
)
;

CREATE TABLE IF NOT EXISTS competences (
	uuid VARCHAR, -- aid
	version INTEGER DEFAULT 1,
	nom TEXT NOT NULL,
	cooldown INTEGER,
	couleur TEXT,
	retour_user TEXT,
	retour_target TEXT,
	hasTarget BOOL NOT NULL,  
	image BYTEA,   
    
	date_creation TIMESTAMP DEFAULT now(),
	user_creation TEXT,
	date_modification TIMESTAMP,
	user_modification TEXT,
	date_suppression TIMESTAMP,
	user_suppression TEXT,
               
	CONSTRAINT pk_competences
		PRIMARY KEY (uuid)
)
;

CREATE TABLE IF NOT EXISTS inventaire_personnage (
	personnage_uuid VARCHAR,
	objet_uuid VARCHAR,
	
	CONSTRAINT pk_inventaire_personnage
		PRIMARY KEY (personnage_uuid, objet_uuid)
)
;

CREATE TABLE IF NOT EXISTS objet_salle_link (
	salle_fictive_uuid VARCHAR,
	objet_uuid VARCHAR,
	posX INTEGER,
	posY INTEGER,
	
	CONSTRAINT pk_objet_salle_link
		PRIMARY KEY (salle_fictive_uuid, objet_uuid, posX, posY)
)
;
CREATE TABLE IF NOT EXISTS competence_classe (
	competence_uuid VARCHAR,
	classe_uuid VARCHAR,
	couleur_override TEXT,
	
	CONSTRAINT pk_competence_classe
		PRIMARY KEY (competence_uuid, classe_uuid, couleur_override)
)
;

CREATE TABLE IF NOT EXISTS admins (
	code BYTEA, 
	version INTEGER DEFAULT 1,
	CONSTRAINT pk_admin
		PRIMARY KEY (code)
)
;
ALTER TABLE IF EXISTS objets
	DROP CONSTRAINT IF EXISTS u1_objets_nom,
	ADD CONSTRAINT u1_objets_nom
		UNIQUE (nom, description)
;

ALTER TABLE IF EXISTS salle_fictives
	DROP CONSTRAINT IF EXISTS u1_salle_fictives_nom,
	ADD CONSTRAINT u1_salle_fictives_nom
		UNIQUE (nom)
;

ALTER TABLE IF EXISTS personnages
	DROP CONSTRAINT IF EXISTS u1_personnages_nom,
	ADD CONSTRAINT u1_personnages_nom
		UNIQUE (nom),
	DROP CONSTRAINT IF EXISTS fk1_classe_personnage,
	ADD CONSTRAINT fk1_classe_personnage
		FOREIGN KEY (classe_uuid)
		REFERENCES classes(uuid)
;

ALTER TABLE IF EXISTS classes
	DROP CONSTRAINT IF EXISTS u1_classes_nom,
	ADD CONSTRAINT u1_classes_nom
		UNIQUE (nom)
;

ALTER TABLE IF EXISTS competences
	DROP CONSTRAINT IF EXISTS u1_competences_nom,
	ADD CONSTRAINT u1_competences_nom
		UNIQUE (nom, couleur)
;

ALTER TABLE IF EXISTS inventaire_personnage
	DROP CONSTRAINT IF EXISTS fk1_inventaire_personnage,
	ADD CONSTRAINT fk1_inventaire_personnage
		FOREIGN KEY (personnage_uuid)
		REFERENCES personnages (uuid),
	DROP CONSTRAINT IF EXISTS fk2_inventaire_objet,
	ADD CONSTRAINT fk2_inventaire_objet
		FOREIGN KEY (objet_uuid)
		REFERENCES objets (uuid)
;

ALTER TABLE IF EXISTS objet_salle_link
	DROP CONSTRAINT IF EXISTS fk1_objet_salle_link_salle_fictive,
	ADD CONSTRAINT fk1_objet_salle_link_salle_fictive
		FOREIGN KEY (salle_fictive_uuid)
		REFERENCES salle_fictive (uuid),
	DROP CONSTRAINT IF EXISTS fk2_objet_salle_link_objet,
	ADD CONSTRAINT fk2_objet_salle_link_objet
		FOREIGN KEY (objet_uuid)
		REFERENCES objets (uuid),
	DROP CONSTRAINT IF EXISTS u1_link_objet,
	ADD CONSTRAINT u1_link_objet
		UNIQUE (posX, posY, salle_fictive_uuid, objet_uuid)
;

ALTER TABLE IF EXISTS competence_classe
	DROP CONSTRAINT IF EXISTS fk1_competence_classe_competence,
	ADD CONSTRAINT fk1_competence_classe_competence
		FOREIGN KEY (competence_uuid)
		REFERENCES competences (uuid),
	DROP CONSTRAINT IF EXISTS fk2_competence_classe_classe,
	ADD CONSTRAINT fk2_competence_classe_classe
		FOREIGN KEY (classe_uuid)
		REFERENCES classes (uuid)
;

ALTER TABLE IF EXISTS admins
	DROP CONSTRAINT IF EXISTS u1_admin,
	ADD CONSTRAINT u1_admin
		UNIQUE (code)
		;