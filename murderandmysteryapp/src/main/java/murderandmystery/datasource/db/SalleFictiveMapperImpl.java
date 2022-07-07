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
import java.awt.Point;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import murderandmystery.datasource.SalleFictiveMapper;
import murderandmystery.domain.Objet;
import murderandmystery.domain.SalleFictive;
import murderandmystery.domain.SalleFictiveBase;

/**
 *
 * @author jeanbourquj
 */
public class SalleFictiveMapperImpl
        extends EntiteMapperImpl<SalleFictive>
        implements SalleFictiveMapper {

    public SalleFictiveMapperImpl(final DbMapperManagerImpl mm) {
        super(mm,
                SQL.SALLE_FICTIVES.SELECT_BY_FILTRE,
                SQL.SALLE_FICTIVES.DELETE_BY_UUID);
    }

    @Override
    protected void createEntite(Identifiant id, SalleFictive entite) throws SQLException, PersistenceException {
        try (PreparedStatement ps
                = this.getMapperManager()
                        .createPreparedStatement(
                                SQL.SALLE_FICTIVES.INSERT)) {
                    ps.setString(1,
                            id.getUUID());
                    if (entite.getNom() != null) {
                        ps.setString(2, entite.getNom());
                    } else {
                        ps.setNull(2, Types.VARCHAR);
                    }
                    if (entite.getImage() != null) {
                        ps.setBytes(3, entite.getImage());
                    } else {
                        ps.setNull(3, Types.BINARY);
                    }
                    if (entite.getObjets().values().size() > 0) {
                        for (Objet i : entite.getObjets().values()) {

                        }
                    }
                    if (entite.getAudit() != null
                            && entite.getAudit().getUserCreation() != null) {
                        ps.setString(4, entite.getAudit().getUserCreation());
                    } else {
                        ps.setNull(4, Types.VARCHAR);
                    }
                    ps.executeUpdate();
                }
    }

    public void deleteObjetSalleFictive(SalleFictive entite, Objet entiteObjet) throws SQLException, PersistenceException {
        try (PreparedStatement ps = this.getMapperManager()
                .createPreparedStatement(SQL.OBJET_SALLE_LINK.DELETE_BY_UUID)) {
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
    public void delete(SalleFictive entite) throws PersistenceException {
        if (entite == null) {
            return;
        }
        if (entite.getIdentifiant() == null) {
            return;
        }
        try {
            SalleFictive entiteTrouvee
                    = this.retrieveEntite(entite.getIdentifiant());
            if (entiteTrouvee == null) {
                throw new EntiteInconnuePersistenceException(
                        String.format("Erreur: l'entitée (%s) "
                                + "n'est pas connue!", entite.toString()));
            }
            for (Objet o : entiteTrouvee.getObjets().values()) {
                deleteObjetSalleFictive(entite, o);
            }
        } catch (SQLException ex) {
            if (ex.getSQLState().equals(
                    SQL_ERREUR_CODES.POSTGRESQL.CONSTRAINT_FOREIGN_KEY_VIOLATION)) {
                throw new EntiteUtiliseePersistenceException(ex);
            }
            throw new PersistenceException(ex);
        }
        super.delete(entite); //To change body of generated methods, choose Tools | Templates.
    }

    private void createLinkMap(Objet entite, Point posObjet, Identifiant idSalleFictive) throws SQLException, PersistenceException {
        try (PreparedStatement ps
                = this.getMapperManager()
                        .createPreparedStatement(
                                SQL.OBJET_SALLE_LINK.INSERT)) {
                    ps.setString(1, idSalleFictive.getUUID());
                    ps.setString(2, entite.getIdentifiant().getUUID());
                    ps.setInt(3, posObjet.x);
                    ps.setInt(4, posObjet.y);

                    ps.executeUpdate();
                }
    }

    @Override
    protected SalleFictive retrieveEntite(Identifiant identifiant) throws SQLException, PersistenceException {
        SalleFictive entite = null;
        try (PreparedStatement ps
                = this.getMapperManager()
                        .createPreparedStatement(
                                SQL.SALLE_FICTIVES.SELECT_BY_UUID)) {
                    ps.setString(1, identifiant.getUUID());
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        entite = readEntite(rs);
                    }
                }
                return entite;
    }

    @Override
    protected List<SalleFictive> retrieveEntites(String filtre) throws PersistenceException, SQLException {
        List<SalleFictive> entites = new ArrayList<>();
        try (PreparedStatement ps
                = this.getMapperManager()
                        .createPreparedStatement(
                                SQL.SALLE_FICTIVES.SELECT_BY_FILTRE)) {
                    ps.setString(1, filtre);
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        SalleFictive entite = readEntite(rs);
                        if (entite != null) {
                            entites.add(entite);
                        }
                    }
                }
                return entites;
    }

    @Override
    protected void updateEntite(SalleFictive entite) throws SQLException, PersistenceException {
        try (PreparedStatement ps
                = this.getMapperManager().createPreparedStatement(
                        SQL.SALLE_FICTIVES.UPDATE)) {
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
            if (entite.getAudit() != null
                    && entite.getAudit().getUserModification() != null) {
                ps.setString(3,
                        entite.getAudit().getUserCreation());
            } else {
                ps.setNull(3,
                        Types.VARCHAR);
            }

            ps.setString(4,
                    entite.getIdentifiant().getUUID());
            ps.setLong(5,
                    entite.getIdentifiant().getVersion());

            int row = ps.executeUpdate();

            if (row == 0) {
                throw new EntiteTropAnciennePersistenceException(
                        entite.toString());
            }

            updateLinkObjet(entite.getIdentifiant(), entite);

        }
    }

    private boolean compareEntite(final Point key, final Objet value, final HashMap<Point, Objet> entite) {
        boolean returnValue = true;
        if (entite.containsKey(key)) {
            if (entite.get(key).equals(value)) {
                returnValue = true;
            } else {
                returnValue = false;
            }
        } else {
            returnValue = false;
        }
        return returnValue;
    }
    boolean dbEquals;

    private void updateLinkObjet(Identifiant id, SalleFictive entite) throws PersistenceException, SQLException {
        HashMap<Point, Objet> dbLinkedObjet = readObjetLink(id);

        CheckedFunction<Point, Objet> compareToDb = (Point key, Objet value) -> {
            dbEquals = compareEntite(key, value, dbLinkedObjet);
            
        };

        CheckedFunction<Point, Objet> deleteAll = (key, value) -> {
            deleteObjetSalleFictive(entite, value);
        };
        CheckedFunction<Point, Objet> creatAll = (key, value) -> {
            createLinkMap(value, key, id);
            //boolean test = value == dbLinkedObjet.get(key);
            /* if (!(dbLinkedObjet.containsKey(key) && value.getIdentifiant().getUUID().equals(dbLinkedObjet.get(key).getIdentifiant().getUUID()))) {

                /* try (final PreparedStatement ps
                        = SalleFictiveMapperImpl.this.getMapperManager().createPreparedStatement(SQL.OBJET_SALLE_LINK.UPDATE)) {

                    ps.setString(1, id.getUUID());
                    ps.setString(2, value.getIdentifiant().getUUID());
                    ps.setInt(3, key.x);
                    ps.setInt(4, key.y);

                    ps.setString(5, id.getUUID());
                    ps.setString(6, value.getIdentifiant().getUUID());
                    int row = ps.executeUpdate();

                    if (row == 0) {
                        throw new EntiteTropAnciennePersistenceException(
                                entite.toString());
                    }
                }
                if (dbLinkedObjet.containsKey(key) && !value.getIdentifiant().getUUID().equals(dbLinkedObjet.get(key).getIdentifiant().getUUID())) {
                    deleteObjetSalleFictive(entite, value);
                    createLinkMap(value, key, id);
                }
                else {
                    createLinkMap(value, key, id);
                }
            }*/
        };
        dbEquals = (entite.getObjets().size() == dbLinkedObjet.size());
        entite.getObjets().forEach(compareToDb);
        if (!dbEquals) {
            dbLinkedObjet.forEach(deleteAll);
            entite.getObjets().forEach(creatAll);
        }

    }

    @Override
    protected SalleFictive readEntite(ResultSet rs) throws SQLException, PersistenceException {
        Identifiant identifiant = readIdentifiant(rs);
        Audit audit = readAudit(rs);
        String nom = rs.getString(SQL.SALLE_FICTIVES.ATTRIBUTS.NOM);
        byte[] image = rs.getBytes(SQL.SALLE_FICTIVES.ATTRIBUTS.IMAGE);

        HashMap<Point, Objet> objetsLink = readObjetLink(identifiant);

        SalleFictive entite = SalleFictiveBase.builder()
                .identifiant(identifiant)
                .audit(audit)
                .nom(nom)
                .image(image)
                .objets(objetsLink)
                .build();

        return entite;
    }

    private HashMap<Point, Objet> readObjetLink(Identifiant id) throws SQLException, PersistenceException {

        Objet o = null;
        HashMap<Point, Objet> objetLinked = new HashMap<>();
        try (PreparedStatement ps
                = this.getMapperManager()
                        .createPreparedStatement(
                                SQL.OBJET_SALLE_LINK.SELECT_BY_SALLE_FICTIVE_UUID)) {
                    ps.setString(1, id.getUUID());
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        Identifiant idObjet
                                = IdentifiantBase
                                        .builder()
                                        .uuid(rs.getString(SQL.OBJET_SALLE_LINK.ATTRIBUTS.OBJET_UUID))
                                        .build();
                        o = this.getMapperManager().getObjetMapper().retrieve(idObjet);
                        Point p = new Point(
                                rs.getInt(SQL.OBJET_SALLE_LINK.ATTRIBUTS.POSX),
                                rs.getInt(SQL.OBJET_SALLE_LINK.ATTRIBUTS.POSY));
                        if (o != null && (p != null)) {
                            objetLinked.put(p, o);
                        }
                    }
                }
                return objetLinked;
    }
}
