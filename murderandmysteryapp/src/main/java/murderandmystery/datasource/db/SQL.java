/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package murderandmystery.datasource.db;

/**
 *
 * @author jeanbourquj
 */
public class SQL {

    public static final class ENTITES {

        public static final class ATTRIBUTS {

            public static final String UUID = "uuid";
            public static final String VERSION = "version";
            public static final String DATE_CREATION = "date_creation";
            public static final String USER_CREATION = "user_creation";
            public static final String DATE_MODIFICATION = "date_modification";
            public static final String USER_MODIFICATION = "user_modification";
            public static final String DATE_SUPPRESSION = "date_suppression";
            public static final String USER_SUPPRESSION = "user_suppression";
        }
    }

    public static final class OBJETS {

        public static final class ATTRIBUTS {

            public static final String NOM = "nom";
            public static final String DESCRIPTION = "description";
            public static final String IMAGE = "image";
            public static final String RAMASSABLE = "ramassable";
        }
        public static final String CREATE_TABLE
                = "CREATE TABLE IF NOT EXISTS objets (\n"
                + "    uuid VARCHAR, -- aid\n"
                + "    version INTEGER DEFAULT 1,\n"
                + "    nom TEXT NOT NULL,\n"
                + "    description TEXT,\n"
                + "    image BYTEA,    \n"
                + "    ramassable BOOLEAN NOT NULL,    \n"
                + "\n"
                + "    date_creation TIMESTAMP DEFAULT now(),\n"
                + "    user_creation TEXT,\n "
                + "    date_modification TIMESTAMP,\n"
                + "    user_modification TEXT,\n "
                + "    date_suppression TIMESTAMP,\n"
                + "    user_suppression TEXT,\n"
                + "\n"
                + "    CONSTRAINT pk_objet\n"
                + "        PRIMARY KEY (uuid)\n"
                + ")";
        public static final String ALTER_TABLE
                = "ALTER TABLE IF EXISTS objets\n"
                + "    DROP CONSTRAINT IF EXISTS u1_objets_nom,\n"
                + "    ADD CONSTRAINT u1_objets_nom\n"
                + "            UNIQUE (nom)";

        public static final String DROP_TABLE
                = "DROP TABLE IF EXISTS objets CASCADE";

        public static final String INSERT
                = "INSERT INTO objets (uuid, nom, description,\n"
                + " image, ramassable, user_creation)\n"
                + "VALUES(?,?,?,?,?,?)";
        public static final String DELETE
                = "DELETE FROM objets\n";

        public static final String DELETE_BY_UUID
                = DELETE
                + "WHERE uuid = ?\n"
                + "      AND version = ?";
        public static final String SELECTION
                = "SELECT o.uuid, o.version,\n"
                + "   o.nom, o.description,\n"
                + "   o.ramassable,\n"
                + "   o.image,\n"
                + " o.date_creation, o.user_creation,\n"
                + " o.date_modification, o.user_modification,\n"
                + " o.date_suppression, o.user_suppression\n";
        public static final String SELECT_BY_UUID
                = SELECTION
                + "FROM objets o\n"
                + "WHERE o.uuid = ?\n";

        public static final String SELECT_BY_FILTRE
                = SELECTION
                + "FROM objets o\n"
                + "WHERE o.nom ~* ?\n";
        public static final String UPDATE
                = "UPDATE objets\n"
                + "SET  nom = ?,\n"
                + "     description = ?,\n"
                + "     image = ?,\n "
                + "     ramassable = ?,\n"
                + "     version = version + 1,\n"
                + "     date_modification = now(),\n"
                + "     user_modification = ?\n"
                + "WHERE uuid = ?"
                + "      AND version = ?";
    }

    public static final class SALLE_FICTIVES {

        public static final class ATTRIBUTS {

            public static final String NOM = "nom";
            public static final String IMAGE = "image";
        }
        public static final String CREATE_TABLE
                = "CREATE TABLE IF NOT EXISTS salle_fictives (\n"
                + "    uuid VARCHAR, -- aid\n"
                + "    version INTEGER DEFAULT 1,\n"
                + "    nom TEXT NOT NULL,\n"
                + "    image BYTEA,    \n"
                + "\n"
                + "    date_creation TIMESTAMP DEFAULT now(),\n"
                + "    user_creation TEXT,\n "
                + "    date_modification TIMESTAMP,\n"
                + "    user_modification TEXT,\n "
                + "    date_suppression TIMESTAMP,\n"
                + "    user_suppression TEXT,\n"
                + "\n"
                + "    CONSTRAINT pk_salle_fictives\n"
                + "        PRIMARY KEY (uuid)\n"
                + ")";
        public static final String ALTER_TABLE
                = "ALTER TABLE IF EXISTS salle_fictives\n"
                + "    DROP CONSTRAINT IF EXISTS u1_salle_fictives_nom,\n"
                + "    ADD CONSTRAINT u1_salle_fictives_nom\n"
                + "            UNIQUE (nom)";

        public static final String DROP_TABLE
                = "DROP TABLE IF EXISTS salle_fictives CASCADE";

        public static final String INSERT
                = "INSERT INTO salle_fictives (uuid, nom, \n"
                + "image, user_creation)\n"
                + "VALUES(?,?,?,?)";
        public static final String DELETE
                = "DELETE FROM salle_fictives\n";

        public static final String DELETE_BY_UUID
                = DELETE
                + "WHERE uuid = ?\n"
                + "      AND version = ?";
        public static final String SELECTION
                = "SELECT s.uuid, s.version,\n"
                + "   s.nom,\n"
                + "   s.image,\n"
                + " s.date_creation, s.user_creation,\n"
                + " s.date_modification, s.user_modification,\n"
                + " s.date_suppression, s.user_suppression\n";
        public static final String SELECT_BY_UUID
                = SELECTION
                + "FROM salle_fictives s\n"
                + "WHERE s.uuid = ?\n";

        public static final String SELECT_BY_FILTRE
                = SELECTION
                + "FROM salle_fictives s\n"
                + "WHERE s.nom ~* ?\n";
        public static final String UPDATE
                = "UPDATE salle_fictives\n"
                + "SET  nom = ?,\n"
                + "     image = ?,\n "
                + "     version = version + 1,\n"
                + "     date_modification = now(),\n"
                + "     user_modification = ?\n"
                + "WHERE uuid = ?"
                + "      AND version = ?";
    }

    public static final class PERSONNAGES {

        public static final class ATTRIBUTS {

            public static final String NOM = "nom";
            public static final String CLASSE_UUID = "classe_uuid";
            public static final String IMAGE = "image";
            public static final String CODE_CONNEXION = "code_connexion";
        }
        public static final String CREATE_TABLE
                = "CREATE TABLE IF NOT EXISTS personnages (\n"
                + "    uuid VARCHAR, -- aid\n"
                + "    version INTEGER DEFAULT 1,\n"
                + "    classe_uuid VARCHAR,\n"
                + "    nom TEXT NOT NULL,\n"
                + "    code_connexion TEXT NOT NULL,\n"
                + "    image BYTEA,    \n"
                + "\n"
                + "    date_creation TIMESTAMP DEFAULT now(),\n"
                + "    user_creation TEXT,\n "
                + "    date_modification TIMESTAMP,\n"
                + "    user_modification TEXT,\n "
                + "    date_suppression TIMESTAMP,\n"
                + "    user_suppression TEXT,\n"
                + "\n"
                + "    CONSTRAINT pk_personnages\n"
                + "        PRIMARY KEY (uuid)\n"
                + ")";
        public static final String ALTER_TABLE
                = "ALTER TABLE IF EXISTS personnages\n"
                + "    DROP CONSTRAINT IF EXISTS u1_personnages_nom,\n"
                + "    ADD CONSTRAINT u1_personnages_nom\n"
                + "            UNIQUE (nom),\n"
                + "    DROP CONSTRAINT IF EXISTS u2_personnages_code_connexion,\n"
                + "    ADD CONSTRAINT u2_personnages_code_connexion\n"
                + "            UNIQUE (code_connexion),\n"
                + "    DROP CONSTRAINT IF EXISTS fk1_classe_personnage,\n"
                + "    ADD CONSTRAINT fk1_classe_personnage\n"
                + "             FOREIGN KEY (classe_uuid)\n"
                + "             REFERENCES classes(uuid)";

        public static final String DROP_TABLE
                = "DROP TABLE IF EXISTS personnages CASCADE";

        public static final String INSERT
                = "INSERT INTO personnages (uuid, nom, classe_uuid \n"
                + ",code_connexion, image, user_creation)\n"
                + "VALUES(?,?,?,?,?,?)";
        public static final String DELETE
                = "DELETE FROM personnages\n";

        public static final String DELETE_BY_UUID
                = DELETE
                + "WHERE uuid = ?\n"
                + "      AND version = ?";
        public static final String SELECTION
                = "SELECT p.uuid, p.version,\n"
                + "   p.nom,\n"
                + "   p.classe_uuid,\n"
                + "   p.code_connexion,\n"
                + "   p.image,\n"
                + " p.date_creation, p.user_creation,\n"
                + " p.date_modification, p.user_modification,\n"
                + " p.date_suppression, p.user_suppression\n";
        public static final String SELECT_BY_UUID
                = SELECTION
                + "FROM personnages p\n"
                + "WHERE p.uuid = ?\n";
        public static final String SELECT_BY_CODE_CONNEXION
                = SELECTION
                + "FROM personnages p\n"
                + "WHERE p.code_connexion = ?\n";
        public static final String SELECT_BY_FILTRE
                = SELECTION
                + "FROM personnages p\n"
                + "WHERE p.nom ~* ?\n";
        public static final String UPDATE
                = "UPDATE personnages\n"
                + "SET  nom = ?,\n"
                + "     image = ?,\n "
                + "     classe_uuid = ?,\n "
                + "     code_connexion = ?,\n "
                + "     version = version + 1,\n"
                + "     date_modification = now(),\n"
                + "     user_modification = ?\n"
                + "WHERE uuid = ?"
                + "      AND version = ?";
    }

    public static final class CLASSES {

        public static final class ATTRIBUTS {

            public static final String NOM = "nom";
            public static final String DESCRIPTION = "description";
        }
        public static final String CREATE_TABLE
                = "CREATE TABLE IF NOT EXISTS classes (\n"
                + "    uuid VARCHAR, -- aid\n"
                + "    version INTEGER DEFAULT 1,\n"
                + "    nom TEXT NOT NULL, \n"
                + "    description TEXT,\n"
                + "\n"
                + "    date_creation TIMESTAMP DEFAULT now(),\n"
                + "    user_creation TEXT,\n "
                + "    date_modification TIMESTAMP,\n"
                + "    user_modification TEXT,\n "
                + "    date_suppression TIMESTAMP,\n"
                + "    user_suppression TEXT,\n"
                + "\n"
                + "    CONSTRAINT pk_classes\n"
                + "        PRIMARY KEY (uuid)\n"
                + ")";
        public static final String ALTER_TABLE
                = "ALTER TABLE IF EXISTS classes\n"
                + "    DROP CONSTRAINT IF EXISTS u1_classes_nom,\n"
                + "    ADD CONSTRAINT u1_classes_nom\n"
                + "            UNIQUE (nom)";

        public static final String DROP_TABLE
                = "DROP TABLE IF EXISTS classes CASCADE";

        public static final String INSERT
                = "INSERT INTO classes (uuid, nom, description \n"
                + ", user_creation)\n"
                + "VALUES(?,?,?,?)";
        public static final String DELETE
                = "DELETE FROM classes\n";

        public static final String DELETE_BY_UUID
                = DELETE
                + "WHERE uuid = ?\n"
                + "      AND version = ?";
        public static final String SELECTION
                = "SELECT c.uuid, c.version,\n"
                + "   c.nom,\n"
                + "   c.description,\n"
                + " c.date_creation, c.user_creation,\n"
                + " c.date_modification, c.user_modification,\n"
                + " c.date_suppression, c.user_suppression\n";
        public static final String SELECT_BY_UUID
                = SELECTION
                + "FROM classes c\n"
                + "WHERE c.uuid = ?\n";

        public static final String SELECT_BY_FILTRE
                = SELECTION
                + "FROM classes c\n"
                + "WHERE c.nom ~* ?\n";
        public static final String UPDATE
                = "UPDATE personnages\n"
                + "SET  nom = ?,\n"
                + "     description = ?,\n "
                + "     version = version + 1,\n"
                + "     date_modification = now(),\n"
                + "     user_modification = ?\n"
                + "WHERE uuid = ?"
                + "      AND version = ?";
    }

    public static final class COMPETENCES {

        public static final class ATTRIBUTS {

            public static final String NOM = "nom";
            public static final String COULEUR = "couleur";
            public static final String IMAGE = "image";
            public static final String RETOUR_USER = "retour_user";
            public static final String RETOUR_TARGET = "retour_target";
            public static final String COOLDOWN = "cooldown";
            public static final String HAS_TARGET = "hasTarget";
        }
        public static final String CREATE_TABLE
                = "CREATE TABLE IF NOT EXISTS competences (\n"
                + "    uuid VARCHAR, -- aid\n"
                + "    version INTEGER DEFAULT 1,\n"
                + "    nom TEXT NOT NULL,\n"
                + "    cooldown INTEGER,\n"
                + "    couleur TEXT,\n"
                + "    retour_user TEXT,\n"
                + "    retour_target TEXT,\n"
                + "    hasTarget BOOL NOT NULL,\n"
                + "    image BYTEA,    \n"
                + "\n"
                + "    date_creation TIMESTAMP DEFAULT now(),\n"
                + "    user_creation TEXT,\n "
                + "    date_modification TIMESTAMP,\n"
                + "    user_modification TEXT,\n "
                + "    date_suppression TIMESTAMP,\n"
                + "    user_suppression TEXT,\n"
                + "\n"
                + "    CONSTRAINT pk_competences\n"
                + "        PRIMARY KEY (uuid)\n"
                + ")";
        public static final String ALTER_TABLE
                = "ALTER TABLE IF EXISTS competences\n"
                + "    DROP CONSTRAINT IF EXISTS u1_competences_nom,\n"
                + "    ADD CONSTRAINT u1_competences_nom\n"
                + "            UNIQUE (nom)";

        public static final String DROP_TABLE
                = "DROP TABLE IF EXISTS competences CASCADE";

        public static final String INSERT
                = "INSERT INTO competences (uuid, nom, cooldown, couleur \n"
                + ", retour_user, retour_target, hasTarget, image, user_creation)\n"
                + "VALUES(?,?,?,?,?,?,?,?,?)";
        public static final String DELETE
                = "DELETE FROM competences\n";

        public static final String DELETE_BY_UUID
                = DELETE
                + "WHERE uuid = ?\n"
                + "      AND version = ?";
        public static final String SELECTION
                = "SELECT c.uuid, c.version,\n"
                + "   c.nom,\n"
                + "   c.cooldown,\n"
                + "   c.couleur,\n"
                + "   c.retour_user,\n"
                + "   c.retour_target,\n"
                + "   c.hasTarget,\n"
                + "   c.image,\n"
                + " c.date_creation, c.user_creation,\n"
                + " c.date_modification, c.user_modification,\n"
                + " c.date_suppression, c.user_suppression\n";
        public static final String SELECT_BY_UUID
                = SELECTION
                + "FROM competences c\n"
                + "WHERE c.uuid = ?\n";

        public static final String SELECT_BY_FILTRE
                = SELECTION
                + "FROM competences c\n"
                + "WHERE c.nom ~* ?\n";
        public static final String UPDATE
                = "UPDATE competences\n"
                + "SET  nom = ?,\n"
                + "     cooldown = ?,\n "
                + "     couleur = ?,\n "
                + "     retour_user = ?,\n "
                + "     retour_target = ?,\n "
                + "     hasTarget = ?,\n "
                + "     image = ?,\n "
                + "     version = version + 1,\n"
                + "     date_modification = now(),\n"
                + "     user_modification = ?\n"
                + "WHERE uuid = ?"
                + "      AND version = ?";
    }

    public static final class INVENTAIRE_PERSONNAGES {

        public static final class ATTRIBUTS {

            public static final String PERSONNAGE_UUID = "personnage_uuid";
            public static final String OBJET_UUID = "objet_uuid";
        }
        public static final String CREATE_TABLE
                = "CREATE TABLE IF NOT EXISTS inventaire_personnage (\n"
                + "    personnage_uuid VARCHAR, \n"
                + "    objet_uuid VARCHAR, \n"
                + "    version INTEGER DEFAULT 1, \n"
                + "\n"
                + "    CONSTRAINT pk_inventaire_personnage\n"
                + "        PRIMARY KEY (personnage_uuid, objet_uuid)\n"
                + ")";
        public static final String ALTER_TABLE
                = "ALTER TABLE IF EXISTS inventaire_personnage\n"
                + "    DROP CONSTRAINT IF EXISTS fk1_inventaire_personnage,\n"
                + "    ADD CONSTRAINT fk1_inventaire_personnage\n"
                + "            FOREIGN KEY (personnage_uuid)\n"
                + "            REFERENCES personnages (uuid),\n"
                + "    DROP CONSTRAINT IF EXISTS fk2_inventaire_objet,\n"
                + "    ADD CONSTRAINT fk2_inventaire_objet\n"
                + "            FOREIGN KEY (objet_uuid)\n"
                + "            REFERENCES objets (uuid)";

        public static final String DROP_TABLE
                = "DROP TABLE IF EXISTS inventaire_personnage CASCADE";

        public static final String INSERT
                = "INSERT INTO inventaire_personnage (personnage_uuid, objet_uuid)\n"
                + "VALUES(?,?)";
        public static final String DELETE
                = "DELETE FROM inventaire_personnage\n";

        public static final String DELETE_BY_UUID
                = DELETE
                + "WHERE personnage_uuid = ?\n"
                + "      AND objet_uuid = ?";
        public static final String SELECTION
                = "SELECT i.personnage_uuid, i.objet_uuid \n";
        public static final String SELECT_BY_PERSONNAGE_UUID
                = SELECTION
                + "FROM inventaire_personnage i\n"
                + "WHERE i.personnage_uuid = ? \n";
        public static final String SELECT_BY_OBJET_UUID
                = SELECTION
                + "FROM inventaire_personnage i\n"
                + "WHERE i.objet_uuid = ? \n";

        public static final String UPDATE
                = "UPDATE inventaire_personnage\n"
                + "SET  personnage_uuid = ?,\n"
                + "     objet_uuid = ?,\n "
                + "     version = version + 1,\n"
                + "WHERE (personnage_uuid = ?"
                + "      AND objet_uuid = ?)"
                + "      AND version = ?";
    }

    public static final class OBJET_SALLE_LINK {

        public static final class ATTRIBUTS {

            public static final String SALLE_FICTIVE_UUID = "salle_fictive_uuid";
            public static final String OBJET_UUID = "objet_uuid";
            public static final String POSX = "posX";
            public static final String POSY = "posY";
        }
        public static final String CREATE_TABLE
                = "CREATE TABLE IF NOT EXISTS objet_salle_link (\n"
                + "    salle_fictive_uuid VARCHAR, \n"
                + "    objet_uuid VARCHAR, \n"
                + "    posX INTEGER, \n"
                + "    posY INTEGER, \n"
                + "\n"
                + "    CONSTRAINT pk_objet_salle_link\n"
                + "        PRIMARY KEY (salle_fictive_uuid, objet_uuid, posX, posY)\n"
                + ")";
        public static final String ALTER_TABLE
                = "ALTER TABLE IF EXISTS objet_salle_link\n"
                + "    DROP CONSTRAINT IF EXISTS fk1_objet_salle_link_salle_fictive,\n"
                + "    ADD CONSTRAINT fk1_objet_salle_link_salle_fictive\n"
                + "            FOREIGN KEY (salle_fictive_uuid)\n"
                + "            REFERENCES salle_fictive (uuid)"
                + "    DROP CONSTRAINT IF EXISTS fk2_objet_salle_link_objet,\n"
                + "    ADD CONSTRAINT fk2_objet_salle_link_objet\n"
                + "            FOREIGN KEY (objet_uuid)\n"
                + "            REFERENCES objets (uuid)"
                + "    DROP CONSTRAINT IF EXISTS u1_link_objet,\n"
                + "    ADD CONSTRAINT u1_link_objet\n"
                + "            UNIQUE (posX, posY, salle_fictive_uuid, objet_uuid)";

        public static final String DROP_TABLE
                = "DROP TABLE IF EXISTS objet_salle_link CASCADE";

        public static final String INSERT
                = "INSERT INTO objet_salle_link (salle_fictive_uuid, objet_uuid, posX, posY)\n"
                + "VALUES(?,?,?,?)";
        public static final String DELETE
                = "DELETE FROM objet_salle_link\n";

        public static final String DELETE_BY_UUID
                = DELETE
                + "WHERE salle_fictive_uuid = ?\n"
                + "      AND objet_uuid = ?";
        public static final String SELECTION
                = "SELECT o.salle_fictive_uuid, o.objet_uuid,\n"
                + "o.posX, o.posY \n";
        public static final String SELECT_BY_SALLE_FICTIVE_UUID
                = SELECTION
                + "FROM objet_salle_link o\n"
                + "WHERE o.salle_fictive_uuid = ?\n";
        public static final String SELECT_BY_OBJET_UUID
                = SELECTION
                + "FROM objet_salle_link o\n"
                + "WHERE o.objet_uuid = ?\n";

        public static final String UPDATE
                = "UPDATE objet_salle_link\n"
                + "SET  salle_fictive_uuid = ?,\n"
                + "     objet_uuid = ?,\n "
                + "     posX = ?,\n "
                + "     posY = ?\n "
                + "WHERE salle_fictive_uuid = ?"
                + "      AND objet_uuid = ?";
    }

    public static final class COMPETENCES_CLASSES {

        public static final class ATTRIBUTS {

            public static final String COMPETENCE_UUID = "competence_uuid";
            public static final String CLASSE_UUID = "classe_uuid";
            public static final String COULEUR_OVERRIDE = "couleur_override";
        }
        public static final String CREATE_TABLE
                = "CREATE TABLE IF NOT EXISTS competence_classe (\n"
                + "    competence_uuid VARCHAR, \n"
                + "    classe_uuid VARCHAR, \n"
                + "    version INTEGER DEFAULT 1, \n"
                + "    couleur_override TEXT, \n"
                + "\n"
                + "    CONSTRAINT pk_competence_classe\n"
                + "        PRIMARY KEY (competence_uuid, classe_uuid)\n"
                + ")";
        public static final String ALTER_TABLE
                = "ALTER TABLE IF EXISTS competence_classe\n"
                + "    DROP CONSTRAINT IF EXISTS fk1_competence_classe_competence,\n"
                + "    ADD CONSTRAINT fk1_competence_classe_competence\n"
                + "            FOREIGN KEY (competence_uuid)\n"
                + "            REFERENCES competences (uuid)"
                + "    DROP CONSTRAINT IF EXISTS fk2_competence_classe_classe,\n"
                + "    ADD CONSTRAINT fk2_competence_classe_classe\n"
                + "            FOREIGN KEY (classe_uuid)\n"
                + "            REFERENCES classes (uuid)";

        public static final String DROP_TABLE
                = "DROP TABLE IF EXISTS competence_classe CASCADE";

        public static final String INSERT
                = "INSERT INTO competence_classe (competence_uuid, classe_uuid, couleur_override)\n"
                + "VALUES(?,?,?)";
        public static final String DELETE
                = "DELETE FROM competence_classe\n";

        public static final String DELETE_BY_UUID
                = DELETE
                + "WHERE competence_uuid = ?\n"
                + "      AND classe_uuid = ?";
        public static final String SELECTION
                = "SELECT o.competence_uuid, o.classe_uuid,\n"
                + "o.couleur_override\n";
        public static final String SELECT_BY_CLASSE_UUID
                = SELECTION
                + "FROM competence_classe o\n"
                + "WHERE o.classe_uuid = ?\n";
        public static final String SELECT_BY_COMPETENCE_UUID
                = SELECTION
                + "FROM competence_classe o\n"
                + "WHERE o.competence_uuid = ?\n";

        public static final String UPDATE
                = "UPDATE competence_classe\n"
                + "SET  classe_uuid = ?,\n"
                + "     competence_uuid = ?,\n "
                + "     couleur_override = ?,\n "
                + "     version = version + 1,\n"
                + "WHERE (classe_uuid = ?"
                + "      AND competence_uuid = ?)"
                + "      AND version = ?";
    }
    
    public static final class ADMINS {

        public static final class ATTRIBUTS {

            public static final String CODE = "code";
        }
        public static final String CREATE_TABLE
                = "CREATE TABLE IF NOT EXISTS admins (\n"
                + "    code BYTEA, \n"
                + "    version INTEGER DEFAULT 1, \n"
                + "\n"
                + "    CONSTRAINT pk_admin\n"
                + "        PRIMARY KEY (code)\n"
                + ")";
        public static final String ALTER_TABLE
                = "ALTER TABLE IF EXISTS admins\n"
                + "    DROP CONSTRAINT IF EXISTS u1_admin,\n"
                + "    ADD CONSTRAINT u1_admin\n"
                + "           UNIQUE (code)\n";

        public static final String DROP_TABLE
                = "DROP TABLE IF EXISTS admins CASCADE";

        public static final String INSERT
                = "INSERT INTO admins (code)\n"
                + "VALUES(?)";
        public static final String DELETE
                = "DELETE FROM admins\n";

        public static final String DELETE_BY_UUID
                = DELETE
                + "WHERE code = ?\n";
        public static final String SELECTION
                = "SELECT o.code\n";
        public static final String SELECT_ADMIN
                = SELECTION
                + "FROM admins o\n"
                + "WHERE o.code = ?\n";
        public static final String SELECT_BY_COMPETENCE_UUID
                = SELECTION
                + "FROM admins o\n"
                + "WHERE o.code = ?\n";

        public static final String UPDATE
                = "UPDATE admins\n"
                + "SET  code = ?,\n"
                + "     version = version + 1,\n"
                + "WHERE code = ?";
    }
}
