/*
    murderandmystery - requêtes utilisées dans l'interface utilisateur
    
    dominique huguenin (dominique.huguenin AT rpn.ch)

*/

-- Requête permettant de retourner la liste des recettes en fonction de 
-- la valeur saisie dans le champs recherche

/*
SELECT nom, detail
FROM recettes
WHERE to_tsvector('french',nom || '  ' || detail) 
        @@ to_tsquery('french','aubergine')
;

-- requêtes permettant de retourner le détail d'une recette

SELECT nom, detail,preparation, nombre_personnes
FROM recettes
WHERE uuid IN (
        SELECT uuid
        FROM recettes
        WHERe to_tsvector('french',nom || '  ' || detail) @@ to_tsquery('french','aubergine'))
;

SELECT c.ordre, c.quantite, u.code, i.nom, c.commentaire
FROM composants c 
        LEFT OUTER JOIN unites u ON c.unites_uuid = u.uuid
        INNER JOIN ingredients i ON c.ingredients_uuid = i.uuid
WHERE c.recettes_uuid IN (
        SELECT uuid
        FROM recettes
        WHERE to_tsvector('french',nom || '  ' || detail) @@ to_tsquery('french','aubergine'))
ORDER BY ordre        
;

-- Requète permettant de retourner la liste des ingrédients en fonction de 
-- la valeur saisie dans le champs recherche

SELECT nom, detail
FROM ingredients
WHERE to_tsvector('french',nom || '  ' || detail) 
        @@ to_tsquery('french','sauce')
;

-- requêtes permettant de retourner le détail d'un ingédient

SELECT i.nom, i.detail, r.nom recette
FROM ingredients i
    LEFT OUTER JOIN recettes r ON i.recettes_uuid = r.uuid
WHERE to_tsvector('french',i.nom || '  ' || i.detail) 
        @@ to_tsquery('french','sauce')
;

-- Requête permettant de retourner la liste des unités en fonction de la valeur saisie dans le champs recherche

SELECT code
FROM unites
WHERE code ~ '^c.*' 
;        */
