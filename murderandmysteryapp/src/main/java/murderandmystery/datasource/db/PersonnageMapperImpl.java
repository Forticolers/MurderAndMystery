/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package murderandmystery.datasource.db;

import core.datasource.EntiteInconnuePersistenceException;
import core.datasource.EntiteTropAnciennePersistenceException;
import core.datasource.EntiteUtiliseePersistenceException;
import core.datasource.PersistenceException;
import core.datasource.db.SQL_ERREUR_CODES;
import core.domain.Audit;
import core.domain.Identifiant;
import core.domain.IdentifiantBase;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import murderandmystery.datasource.PersonnageMapper;
import murderandmystery.domain.Classe;
import murderandmystery.domain.Objet;
import murderandmystery.domain.Personnage;
import murderandmystery.domain.PersonnageBase;
import org.apache.commons.lang3.ArrayUtils;

/**
 *
 * @author jeanbourquj
 */
public class PersonnageMapperImpl
        extends EntiteMapperImpl<Personnage>
        implements PersonnageMapper {

    public PersonnageMapperImpl(final DbMapperManagerImpl mm) {
        super(mm,
                SQL.PERSONNAGES.SELECT_BY_FILTRE,
                SQL.PERSONNAGES.DELETE_BY_UUID);
    }

    @Override
    protected void createEntite(Identifiant id, Personnage entite) throws SQLException, PersistenceException {

        try (PreparedStatement ps
                = this.getMapperManager()
                        .createPreparedStatement(
                                SQL.OBJETS.INSERT)) {
                    ps.setString(1,
                            id.getUUID());
                    if (entite.getNom() != null) {
                        ps.setString(2, entite.getNom());
                    } else {
                        ps.setNull(2, Types.VARCHAR);
                    }
                    if (entite.getClasse() != null) {
                        ps.setString(3, entite.getClasse().getIdentifiant().getUUID());
                    } else {
                        ps.setNull(3, Types.VARCHAR);
                    }
                    if (entite.getCodeConnexion() != null) {
                        ps.setString(4, entite.getCodeConnexion());
                    } else {
                        ps.setNull(4, Types.VARCHAR);
                    }
                    if (entite.getImage() != null) {
                        ps.setBytes(5, entite.getImage());
                    } else {
                        ps.setNull(5, Types.BINARY);
                    }
                    if (entite.getInventaire().size() > 0) {
                        for (Objet i : entite.getInventaire()) {
                            createInventaire(i, entite.getIdentifiant());
                        }
                    }
                    if (entite.getAudit() != null
                            && entite.getAudit().getUserCreation() != null) {
                        ps.setString(6, entite.getAudit().getUserCreation());
                    } else {
                        ps.setNull(6, Types.VARCHAR);
                    }
                    ps.executeUpdate();
                }

    }

    public void deleteObjetInventaire(Personnage entite, Objet entiteObjet) throws SQLException, PersistenceException {
        try (PreparedStatement ps = this.getMapperManager()
                .createPreparedStatement(SQL.INVENTAIRE_PERSONNAGES.DELETE_BY_UUID)) {
            ps.setString(1, entite.getIdentifiant().getUUID());
            ps.setString(2, entiteObjet.getIdentifiant().getUUID());
            int row = ps.executeUpdate();

            if (row == 0) {
                throw new EntiteTropAnciennePersistenceException(
                        entite.toString());
            }
        }
    }

    @Override
    public void delete(Personnage entite) throws PersistenceException {

        if (entite == null) {
            return;
        }
        if (entite.getIdentifiant() == null) {
            return;
        }
        try {
            Personnage entiteTrouvee
                    = this.retrieveEntite(entite.getIdentifiant());
            if (entiteTrouvee == null) {
                throw new EntiteInconnuePersistenceException(
                        String.format("Erreur: l'entitée (%s) "
                                + "n'est pas connue!", entite.toString()));
            }
            for (Objet o : entiteTrouvee.getInventaire()) {
                deleteObjetInventaire(entite, o);
            }
        } catch (SQLException ex) {
            if (ex.getSQLState().equals(
                    SQL_ERREUR_CODES.POSTGRESQL.CONSTRAINT_FOREIGN_KEY_VIOLATION)) {
                throw new EntiteUtiliseePersistenceException(ex);
            }
            throw new PersistenceException(ex);
        }
        super.delete(entite);
    }

    private void createInventaire(Objet entite, Identifiant idPersonnage) throws SQLException, PersistenceException {
        try (PreparedStatement ps
                = this.getMapperManager()
                        .createPreparedStatement(
                                SQL.INVENTAIRE_PERSONNAGES.INSERT)) {
                    ps.setString(1, idPersonnage.getUUID());
                    ps.setString(2, entite.getIdentifiant().getUUID());

                    ps.executeUpdate();
                }
    }

    @Override
    protected Personnage retrieveEntite(Identifiant identifiant) throws SQLException, PersistenceException {
        Personnage entite = null;
        try (PreparedStatement ps
                = this.getMapperManager()
                        .createPreparedStatement(
                                SQL.PERSONNAGES.SELECT_BY_UUID)) {
                    ps.setString(1, identifiant.getUUID());
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        entite = readEntite(rs);
                    }
                }
                return entite;
    }

    @Override
    public Personnage retrieveEntiteCode(String codeConnexion) throws SQLException, PersistenceException {
        Personnage entite = null;
        try (PreparedStatement ps
                = this.getMapperManager()
                        .createPreparedStatement(
                                SQL.PERSONNAGES.SELECT_BY_CODE_CONNEXION)) {
                    ps.setString(1, codeConnexion);
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        entite = readEntite(rs);
                    }
                }
                return entite;
    }

    @Override
    protected List<Personnage> retrieveEntites(String filtre) throws PersistenceException, SQLException {
        List<Personnage> entites = new ArrayList<>();
        try (PreparedStatement ps
                = this.getMapperManager()
                        .createPreparedStatement(
                                SQL.PERSONNAGES.SELECT_BY_FILTRE)) {
                    ps.setString(1, filtre);
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        Personnage entite = readEntite(rs);
                        if (entite != null) {
                            entites.add(entite);
                        }
                    }
                }
                return entites;
    }

    @Override
    protected void updateEntite(Personnage entite) throws SQLException, PersistenceException {
        try (PreparedStatement ps
                = this.getMapperManager().createPreparedStatement(
                        SQL.PERSONNAGES.UPDATE)) {
            if (entite.getNom() != null) {
                ps.setString(1, entite.getNom());
            } else {
                ps.setNull(1, Types.VARCHAR);
            }
            if (entite.getImage() != null) {
                ps.setBytes(2, entite.getImage());
            } else {
                ps.setNull(2, Types.BINARY);
            }
            if (entite.getClasse() != null) {
                ps.setString(3, entite.getClasse().getIdentifiant().getUUID());
            } else {
                ps.setNull(3, Types.VARCHAR);
            }
            if (entite.getCodeConnexion() != null) {
                ps.setString(4, entite.getCodeConnexion());
            } else {
                ps.setNull(4, Types.VARCHAR);
            }
            if (entite.getAudit() != null
                    && entite.getAudit().getUserModification() != null) {
                ps.setString(5,
                        entite.getAudit().getUserCreation());
            } else {
                ps.setNull(5,
                        Types.VARCHAR);
            }

            ps.setString(6,
                    entite.getIdentifiant().getUUID());
            ps.setLong(7,
                    entite.getIdentifiant().getVersion());

            int row = ps.executeUpdate();

            if (row == 0) {
                throw new EntiteTropAnciennePersistenceException(
                        entite.toString());
            }

            if (entite.getInventaire().size() > 0) {
                updateInventaire(entite.getIdentifiant(), entite);
            }
        }
    }

    private void updateInventaire(Identifiant id, Personnage p) throws PersistenceException, SQLException {
        for (Objet o : p.getInventaire()) {
            try (PreparedStatement ps
                    = this.getMapperManager()
                            .createPreparedStatement(
                                    SQL.INVENTAIRE_PERSONNAGES.UPDATE)) {
                        ps.setString(1, id.getUUID());
                        if (o != null) {
                            ps.setString(1, id.getUUID());
                            ps.setString(2, o.getIdentifiant().getUUID());

                        } else {
                            ps.setNull(1, Types.VARCHAR);
                            ps.setNull(2, Types.VARCHAR);
                        }
                        ps.setString(3, id.getUUID());
                        ps.setString(4, o.getIdentifiant().getUUID());
                        ps.setLong(5,
                                p.getIdentifiant().getVersion());
                        int row = ps.executeUpdate();

                        if (row == 0) {
                            throw new EntiteTropAnciennePersistenceException(
                                    p.toString());
                        }
                    }
        }
    }

    protected Identifiant readIdentifiantClasse(final ResultSet rs)
            throws SQLException {
        String uuid = rs.getString(SQL.PERSONNAGES.ATTRIBUTS.CLASSE_UUID);
        Long version = rs.getLong(SQL.ENTITES.ATTRIBUTS.VERSION);

        return IdentifiantBase.builder()
                .uuid(uuid)
                .version(version)
                .build();
    }

    @Override
    protected Personnage readEntite(ResultSet rs) throws SQLException, PersistenceException {
        Identifiant identifiant = readIdentifiant(rs);
        Audit audit = readAudit(rs);
        Identifiant idClasse = readIdentifiantClasse(rs);
        Classe c = this.getMapperManager().getClasseMapper().retrieve(idClasse);
        String nom = rs.getString(SQL.PERSONNAGES.ATTRIBUTS.NOM);
        String codeConnexion = rs.getString(SQL.PERSONNAGES.ATTRIBUTS.CODE_CONNEXION);
        byte[] image = rs.getBytes(SQL.PERSONNAGES.ATTRIBUTS.IMAGE);

        List<Objet> inventaire = readInventaire(identifiant);

        Personnage entite = PersonnageBase.builder()
                .identifiant(identifiant)
                .audit(audit)
                .nom(nom)
                .classe(c)
                .codeConnexion(codeConnexion)
                .image(image)
                .inventaire(inventaire)
                .build();
        return entite;
    }

    private List<Objet> readInventaire(Identifiant id)
            throws SQLException, PersistenceException {
        Objet o = null;
        List<Objet> inventaire = new ArrayList<>();
        try (PreparedStatement ps
                = this.getMapperManager()
                        .createPreparedStatement(
                                SQL.INVENTAIRE_PERSONNAGES.SELECT_BY_PERSONNAGE_UUID)) {
                    ps.setString(1, id.getUUID());
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        Identifiant idObjet
                                = IdentifiantBase
                                        .builder()
                                        .uuid(rs.getString(SQL.INVENTAIRE_PERSONNAGES.ATTRIBUTS.OBJET_UUID))
                                        .build();
                        o = this.getMapperManager().getObjetMapper().retrieve(idObjet);
                        if (o != null) {
                            inventaire.add(o);
                        }
                    }
                }
                return inventaire;
    }
}
