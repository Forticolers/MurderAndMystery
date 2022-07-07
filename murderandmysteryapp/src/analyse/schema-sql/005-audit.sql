/*
    murderandmystery - Champs d'audit
    
    Jeanbourquin Julien (jeanbourquj@rpn.ch)

*/
CREATE OR REPLACE FUNCTION uniqueRelations_trigger_function()
RETURNS TRIGGER AS $uniqueRelationDelimiter$
BEGIN
	if exists (
		select *
		from relations
		where personne1_uuid = new.personne2_uuid and personne2_uuid = new.personne1_uuid
	)
	then
		return null;
	end if;
	return new;
END;
$uniqueRelationDelimiter$ language 'plpgsql';	


CREATE TRIGGER uniqueRelations_trigger
BEFORE INSERT OR UPDATE ON relations
FOR EACH ROW EXECUTE FUNCTION uniqueRelations_trigger_function();

CREATE OR REPLACE FUNCTION relations_audit_trigger_function()
RETURNS TRIGGER AS $trigger_relations_func$
BEGIN
	IF (TG_OP = 'INSERT') THEN
		NEW.ajDate := current_timestamp;
		NEW.ajUser := current_user;
		NEW.moDate := NULL;
		NEW.moUser := NULL;
	ELSIF (TG_OP = 'UPDATE') THEN
		NEW.ajDate := OLD.ajDate;
		NEW.ajUser := OLD.ajUser;
		NEW.moDate := current_timestamp;
		NEW.moUser := current_user;

	END IF;
	RETURN NEW;
	
END;
$trigger_relations_func$ language 'plpgsql';

CREATE OR REPLACE FUNCTION nouvelleRencontreAddNewRelation()
RETURNS TRIGGER AS $trigger_newRencontre$
BEGIN
	IF not exists(
		SELECT *
		from relations
		where (personne1_uuid = new.personne2_uuid and personne2_uuid = new.personne1_uuid) 
		OR (personne1_uuid = new.personne1_uuid and personne2_uuid = new.personne2_uuid) 
	)
	then
		INSERT INTO relations (uuid, personne1_uuid, personne2_uuid, type_relation)
		VALUES ((select uuid_generate_v4()), new.personne1_uuid, new.personne2_uuid, 'CONNAISSANCE');
	end if;
	return new;
END;
$trigger_newRencontre$ language 'plpgsql';

CREATE TRIGGER newRencontre_trigger
BEFORE INSERT ON rencontres
FOR EACH ROW EXECUTE FUNCTION nouvelleRencontreAddNewRelation();

ALTER TABLE personne

	ADD COLUMN	ajUser TEXT DEFAULT current_user,
	ADD COLUMN	ajDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP, 
	ADD COLUMN	moUser TEXT DEFAULT NULL, 
	ADD COLUMN	moDate TIMESTAMP DEFAULT NULL 

;

CREATE TRIGGER relations_trigger_personne
BEFORE INSERT OR UPDATE ON personne
FOR EACH ROW EXECUTE FUNCTION relations_audit_trigger_function();

ALTER TABLE relations

	ADD COLUMN	ajUser TEXT DEFAULT current_user,
	ADD COLUMN	ajDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP, 
	ADD COLUMN	moUser TEXT DEFAULT NULL, 
	ADD COLUMN	moDate TIMESTAMP DEFAULT NULL 

;

CREATE TRIGGER relations_trigger_relations
BEFORE INSERT OR UPDATE ON relations
FOR EACH ROW EXECUTE FUNCTION relations_audit_trigger_function();

ALTER TABLE rencontres 

	ADD COLUMN	ajUser TEXT DEFAULT current_user,
	ADD COLUMN	ajDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP, 
	ADD COLUMN	moUser TEXT DEFAULT NULL, 
	ADD COLUMN	moDate TIMESTAMP DEFAULT NULL 

;

CREATE TRIGGER relations_trigger_rencontres
BEFORE INSERT OR UPDATE ON rencontres
FOR EACH ROW EXECUTE FUNCTION relations_audit_trigger_function();