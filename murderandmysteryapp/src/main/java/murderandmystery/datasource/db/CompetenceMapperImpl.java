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
import murderandmystery.datasource.CompetenceMapper;
import murderandmystery.domain.Competence;
import murderandmystery.domain.CompetenceBase;

/**
 *
 * @author jeanbourquj
 */
public class CompetenceMapperImpl
        extends EntiteMapperImpl<Competence>
        implements CompetenceMapper {

    public CompetenceMapperImpl(final DbMapperManagerImpl mm) {
        super(mm,
                SQL.COMPETENCES.SELECT_BY_FILTRE,
                SQL.COMPETENCES.DELETE_BY_UUID);
    }

    @Override
    protected void createEntite(Identifiant id, Competence entite) throws SQLException, PersistenceException {
        try (PreparedStatement ps
                = this.getMapperManager()
                        .createPreparedStatement(
                                SQL.COMPETENCES.INSERT)) {

                    ps.setString(1,
                            id.getUUID());
                    if (entite.getNom() != null) {
                        ps.setString(2, entite.getNom());
                    } else {
                        ps.setNull(2, Types.VARCHAR);
                    }

                    if (entite.getCooldown() != null) {
                        ps.setInt(3, entite.getCooldown());
                    } else {
                        ps.setNull(3, Types.INTEGER);
                    }

                    if (entite.getCouleur() != null) {
                        ps.setString(4, entite.getCouleur());
                    } else {
                        ps.setNull(4, Types.VARCHAR);
                    }
                    if (entite.getRetourUser() != null) {
                        ps.setString(5, entite.getRetourUser());
                    } else {
                        ps.setNull(5, Types.VARCHAR);
                    }
                    if (entite.getRetourUserTargeted() != null) {
                        ps.setString(6, entite.getRetourUserTargeted());
                    } else {
                        ps.setNull(6, Types.VARCHAR);
                    }

                    ps.setBoolean(7, entite.getHasTarget());

                    if (entite.getImage() != null) {
                        ps.setBytes(8, entite.getImage());
                    } else {
                        ps.setNull(8, Types.BINARY);
                    }

                    if (entite.getAudit() != null
                            && entite.getAudit().getUserCreation() != null) {
                        ps.setString(9, entite.getAudit().getUserCreation());
                    } else {
                        ps.setNull(9, Types.VARCHAR);
                    }
                    ps.executeUpdate();
                }
    }

    @Override
    protected Competence retrieveEntite(Identifiant identifiant) throws SQLException, PersistenceException {
        Competence entite = null;
        try (PreparedStatement ps
                = this.getMapperManager()
                        .createPreparedStatement(
                                SQL.COMPETENCES.SELECT_BY_UUID)) {
                    ps.setString(1, identifiant.getUUID());
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        entite = readEntite(rs);
                    }
                }
                return entite;
    }

    @Override
    public List<Competence> retrieveEntites(String filtre) throws PersistenceException, SQLException {
        List<Competence> entites = new ArrayList<>();
        try (PreparedStatement ps
                = this.getMapperManager()
                        .createPreparedStatement(
                                SQL.COMPETENCES.SELECT_BY_FILTRE)) {
                    ps.setString(1, filtre);
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        Competence entite = readEntite(rs);
                        if (entite != null) {
                            entites.add(entite);
                        }
                    }
                }
                return entites;
    }

    @Override
    protected void updateEntite(Competence entite) throws SQLException, PersistenceException {
        try (PreparedStatement ps
                = this.getMapperManager().createPreparedStatement(
                        SQL.COMPETENCES.UPDATE)) {

            if (entite.getNom() != null) {
                ps.setString(1, entite.getNom());
            } else {
                ps.setNull(1, Types.VARCHAR);
            }
            if (entite.getCooldown() != null) {
                ps.setInt(2, entite.getCooldown());
            } else {
                ps.setNull(2, Types.INTEGER);
            }

            if (entite.getCouleur() != null) {
                ps.setString(3, entite.getCouleur());
            } else {
                ps.setNull(3, Types.VARCHAR);
            }
            if (entite.getRetourUser() != null) {
                ps.setString(4, entite.getRetourUser());
            } else {
                ps.setNull(4, Types.VARCHAR);
            }
            if (entite.getRetourUserTargeted() != null) {
                ps.setString(5, entite.getRetourUserTargeted());
            } else {
                ps.setNull(5, Types.VARCHAR);
            }
            
            ps.setBoolean(7, entite.getHasTarget());
            if (entite.getImage() != null) {
                ps.setBytes(8, entite.getImage());
            } else {
                ps.setNull(8, Types.BINARY);
            }

            ps.setString(9,
                    entite.getIdentifiant().getUUID());
            ps.setLong(10,
                    entite.getIdentifiant().getVersion());

            int row = ps.executeUpdate();

            if (row == 0) {
                throw new EntiteTropAnciennePersistenceException(
                        entite.toString());
            }
        }
    }

    @Override
    protected Competence readEntite(ResultSet rs) throws SQLException, PersistenceException {
        Identifiant id = readIdentifiant(rs);
        Audit audit = readAudit(rs);

        String nom = rs.getString(SQL.COMPETENCES.ATTRIBUTS.NOM);
        Integer cooldown = rs.getInt(SQL.COMPETENCES.ATTRIBUTS.COOLDOWN);
        String couleur = rs.getString(SQL.COMPETENCES.ATTRIBUTS.COULEUR);
        String retour_user = rs.getString(SQL.COMPETENCES.ATTRIBUTS.RETOUR_USER);
        String retour_target = rs.getString(SQL.COMPETENCES.ATTRIBUTS.RETOUR_TARGET);
        boolean hasTarget = rs.getBoolean(SQL.COMPETENCES.ATTRIBUTS.HAS_TARGET);
        byte[] image = rs.getBytes(SQL.COMPETENCES.ATTRIBUTS.IMAGE);

        Competence entite = CompetenceBase.builder()
                .identifiant(id)
                .audit(audit)
                .nom(nom)
                .cooldown(cooldown)
                .couleur(couleur)
                .retourUser(retour_user)
                .retourTargetedUser(retour_target)
                .hasTarget(hasTarget)
                .image(image)
                .couleur_override(null)
                .build();

        return entite;
    }

}
