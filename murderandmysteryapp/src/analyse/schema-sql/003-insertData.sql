/*
    murderandmystery - Script d'insertion des données de demonstration
    
    julien jeanbourquin (julien.jeanbourquin AT hotmail.com)

    La base de données murderandmysteryDB doit contenir l'extension "uuid-ossp"  et "pgcrypto"
    pour la génération des uuid avec la fonction uuid_generate_v4().

	CREATE EXTENSION pgcrypto;
    CREATE EXTENSION "uuid-ossp";

    select uuid_generate_v4();
*/
 \lo_import './aubergines.jpg' 'aubergines'
\set OID :LASTOID
INSERT INTO objets (uuid, nom, description,image, ramassable)
 VALUES (uuid_generate_v4(), 'clé', 'une clé pour la porte B', lo_get(:OID)::bytea, true),
					(uuid_generate_v4(), 'clé', 'une clé pour la porte A', lo_get(:OID)::bytea, true)
 ;
 \lo_import './aubergines.jpg' 'aubergines'
\set OID :LASTOID
 INSERT INTO salle_fictives (uuid, nom, image)
 VALUES (uuid_generate_v4(), 'salle_magique', lo_get(:OID)::bytea),
				(uuid_generate_v4(), 'salle_magique2', lo_get(:OID)::bytea)
 ;
 
 INSERT INTO classes (uuid, nom, description)
 VALUES (uuid_generate_v4(), 'Paladin', 'Un paladin avec des pouvoir de défense'),
				(uuid_generate_v4(), 'Sorcière', 'Une sorcière')
				;
\lo_import './aubergines.jpg' 'aubergines'
\set OID :LASTOID
INSERT INTO personnages (uuid, nom, classe_uuid,code_connexion, image)
VALUES (uuid_generate_v4(), 'Grand paladin', (select uuid from classes where nom = 'Paladin'),'123456',NULL),
(uuid_generate_v4(), 'Grande sorcière', (select uuid from classes where nom = 'Sorcière'),'12345',lo_get(:OID)::bytea)
;
\lo_import './aubergines.jpg' 'aubergines'
\set OID :LASTOID
INSERT INTO competences (uuid, nom, cooldown, couleur, retour_user, retour_target, hasTarget, image)
VALUES (uuid_generate_v4(), 'Potion', 10, '#fff', 'Utilisation dune potion', null, FALSE, lo_get(:OID)::bytea),
(uuid_generate_v4(), 'Potion2', 10, '#fff', 'Utilisation dune potion', null, FALSE, lo_get(:OID)::bytea),
(uuid_generate_v4(), 'Potion3', 10, '#fff', 'Utilisation dune potion', null, FALSE, lo_get(:OID)::bytea),
(uuid_generate_v4(), 'Potion4', 10, '#fff', 'Utilisation dune potion', null, FALSE, lo_get(:OID)::bytea)
;

INSERT INTO inventaire_personnage (personnage_uuid, objet_uuid)
VALUES ((select uuid from personnages where nom = 'Grand paladin'), (select uuid from objets where nom = 'clé' and description ~* 'B$' ))
;

INSERT INTO objet_salle_link (salle_fictive_uuid, objet_uuid, posX, posY)
VALUES ((select uuid from salle_fictives where nom = 'salle_magique'), (select uuid from objets where nom = 'clé' and description ~* 'B$' ), 10, 10)
;

INSERT INTO competence_classe (competence_uuid, classe_uuid, couleur_override)
VALUES ((select uuid from competences where nom = 'Potion'),(select uuid from classes where nom = 'Sorcière'),'null'),
	((select uuid from competences where nom = 'Potion2'),(select uuid from classes where nom = 'Sorcière'),'null'),
	((select uuid from competences where nom = 'Potion3'),(select uuid from classes where nom = 'Sorcière'),'#aaa'),
	((select uuid from competences where nom = 'Potion4'),(select uuid from classes where nom = 'Paladin'),'null')

;
INSERT INTO admins (code)
VALUES ((SELECT digest('TEST', 'sha256')));
;

