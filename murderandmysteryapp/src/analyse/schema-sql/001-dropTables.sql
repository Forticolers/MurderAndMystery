/*
    murderandmystery - Script de suppression du schéma
    
    julien jeanbourquin (julien.jeanbourquin AT hotmail.com)
*/

DROP TABLE IF EXISTS inventaire_personnage CASCADE
;

DROP TABLE IF EXISTS objet_salle_link CASCADE
;

DROP TABLE IF EXISTS competence_classe CASCADE
;

DROP TABLE IF EXISTS objets CASCADE
;

DROP TABLE IF EXISTS classes CASCADE
;

DROP TABLE IF EXISTS competences CASCADE
;

DROP TABLE IF EXISTS personnages CASCADE
;

DROP TABLE IF EXISTS salle_fictives CASCADE
;
DROP TABLE IF EXISTS admins CASCADE
;