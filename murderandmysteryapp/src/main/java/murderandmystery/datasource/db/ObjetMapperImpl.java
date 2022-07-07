/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package murderandmystery.datasource.db;

import core.datasource.EntiteTropAnciennePersistenceException;
import core.datasource.PersistenceException;
import core.domain.Audit;
import core.domain.Identifiant;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import murderandmystery.datasource.ObjetMapper;
import murderandmystery.domain.Objet;
import murderandmystery.domain.ObjetBase;

/**
 *
 * @author jeanbourquj
 */
public class ObjetMapperImpl
        extends EntiteMapperImpl<Objet>
        implements ObjetMapper {

    public ObjetMapperImpl(final DbMapperManagerImpl mm) {
        super(mm,
                SQL.OBJETS.SELECT_BY_FILTRE,
                SQL.OBJETS.DELETE_BY_UUID);
    }

    @Override
    protected void createEntite(Identifiant id, Objet entite) throws SQLException, PersistenceException {
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
                    if (entite.getDescription() != null) {
                        ps.setString(3, entite.getDescription());
                    } else {
                        ps.setNull(3, Types.VARCHAR);
                    }
                    if (entite.getImage() != null) {
                        ps.setBytes(4, entite.getImage());
                    } else {
                        ps.setNull(4, Types.BINARY);
                    }
                    ps.setBoolean(5, entite.isRamassable());

                    if (entite.getAudit() != null
                            && entite.getAudit().getUserCreation() != null) {
                        ps.setString(6, entite.getAudit().getUserCreation());
                    } else {
                        ps.setNull(6, Types.VARCHAR);
                    }
                    ps.executeUpdate();
                }
    }

    @Override
    protected Objet retrieveEntite(Identifiant identifiant) throws SQLException, PersistenceException {
        Objet entite = null;
        try (PreparedStatement ps
                = this.getMapperManager()
                        .createPreparedStatement(
                                SQL.OBJETS.SELECT_BY_UUID)) {
                    ps.setString(1, identifiant.getUUID());
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        entite = readEntite(rs);
                    }
                }

                return entite;
    }

    @Override
    protected List<Objet> retrieveEntites(String filtre) throws PersistenceException, SQLException {
        List<Objet> entites = new ArrayList<>();
        try (PreparedStatement ps
                = this.getMapperManager()
                        .createPreparedStatement(
                                SQL.OBJETS.SELECT_BY_FILTRE)) {
                    ps.setString(1, filtre);
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        Objet entite = readEntite(rs);
                        if (entite != null) {
                            entites.add(entite);
                        }
                    }
                }
                return entites;
    }

    @Override
    protected void updateEntite(Objet entite) throws SQLException, PersistenceException {
        try (PreparedStatement ps
                = this.getMapperManager().createPreparedStatement(
                        SQL.OBJETS.UPDATE)) {
            if (entite.getNom() != null) {
                ps.setString(1, entite.getNom());
            } else {
                ps.setNull(1, Types.VARCHAR);
            }
            if (entite.getDescription() != null) {
                ps.setString(2, entite.getDescription());
            } else {
                ps.setNull(2, Types.VARCHAR);
            }
            if (entite.getImage() != null) {
                ps.setBytes(3, entite.getImage());
            } else {
                ps.setNull(3, Types.BINARY);
            }
            ps.setBoolean(4, entite.isRamassable());

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
        }
    }

    @Override
    protected Objet readEntite(ResultSet rs) throws SQLException, PersistenceException {
        Identifiant identifiant = readIdentifiant(rs);
        Audit audit = readAudit(rs);

        String nom = rs.getString(
                SQL.OBJETS.ATTRIBUTS.NOM);
        String description = rs.getString(
                SQL.OBJETS.ATTRIBUTS.DESCRIPTION);
        byte[] image = rs.getBytes(SQL.OBJETS.ATTRIBUTS.IMAGE);
        boolean ramassable = rs.getBoolean(SQL.OBJETS.ATTRIBUTS.RAMASSABLE);

        Objet entite = ObjetBase.builder()
                .identifiant(identifiant)
                .audit(audit)
                .nom(nom)
                .description(description)
                .image(image)
                .isRamassable(ramassable)
                .build();

        return entite;
    }

}
