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
import murderandmystery.datasource.ClasseMapper;
import murderandmystery.domain.Classe;
import murderandmystery.domain.ClasseBase;
import murderandmystery.domain.Competence;

/**
 *
 * @author jeanbourquj
 */
public class ClasseMapperImpl
        extends EntiteMapperImpl<Classe>
        implements ClasseMapper {

    public ClasseMapperImpl(final DbMapperManagerImpl mm) {
        super(mm,
                SQL.CLASSES.SELECT_BY_FILTRE,
                SQL.CLASSES.DELETE_BY_UUID);
    }

    @Override
    protected void createEntite(
            final Identifiant id,
            final Classe entite)
            throws SQLException, PersistenceException {
        try (PreparedStatement ps
                = this.getMapperManager()
                        .createPreparedStatement(
                                SQL.CLASSES.INSERT)) {
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

                    if (entite.getCompetences().size() > 0) {
                        for (Competence c : entite.getCompetences()) {
                            createCompetenceLink(c, entite.getIdentifiant());
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

    public void deleteCompetenceClasse(Classe entite, Competence entiteCompetence) throws SQLException, PersistenceException {
        try (PreparedStatement ps = this.getMapperManager()
                .createPreparedStatement(SQL.COMPETENCES_CLASSES.DELETE_BY_UUID)) {
            ps.setString(1, entiteCompetence.getIdentifiant().getUUID());
            ps.setString(2, entite.getIdentifiant().getUUID());

            int row = ps.executeUpdate();

            if (row == 0) {
                throw new EntiteTropAnciennePersistenceException(
                        entite.toString());
            }
        }
    }

    @Override
    public void delete(Classe entite) throws PersistenceException {
        if (entite == null) {
            return;
        }
        if (entite.getIdentifiant() == null) {
            return;
        }
        try {
            Classe entiteTrouvee
                    = this.retrieveEntite(entite.getIdentifiant());
            if (entiteTrouvee == null) {
                throw new EntiteInconnuePersistenceException(
                        String.format("Erreur: l'entitée (%s) "
                                + "n'est pas connue!", entite.toString()));
            }
            for (Competence c : entiteTrouvee.getCompetences()) {
                deleteCompetenceClasse(entite, c);
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

    private void createCompetenceLink(Competence entite, Identifiant idClasse)
            throws SQLException, PersistenceException {
        try (PreparedStatement ps
                = this.getMapperManager()
                        .createPreparedStatement(
                                SQL.COMPETENCES_CLASSES.INSERT)) {
                    ps.setString(1, entite.getIdentifiant().getUUID());
                    ps.setString(2, idClasse.getUUID());

                    if (entite.getCouleurOverride() != null) {
                        ps.setString(3, entite.getCouleurOverride());
                    } else {
                        ps.setNull(3, Types.VARCHAR);
                    }
                    ps.executeUpdate();
                }
    }

    @Override
    protected Classe retrieveEntite(Identifiant identifiant)
            throws SQLException, PersistenceException {
        Classe entite = null;
        try (PreparedStatement ps
                = this.getMapperManager()
                        .createPreparedStatement(
                                SQL.CLASSES.SELECT_BY_UUID)) {
                    ps.setString(1, identifiant.getUUID());
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        entite = readEntite(rs);
                    }
                }
                return entite;
    }

    @Override
    public List<Classe> retrieveEntites(String filtre)
            throws PersistenceException, SQLException {
        List<Classe> entites = new ArrayList<>();
        try (PreparedStatement ps
                = this.getMapperManager()
                        .createPreparedStatement(
                                SQL.CLASSES.SELECT_BY_FILTRE)) {
                    ps.setString(1, filtre);
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        Classe entite = readEntite(rs);
                        if (entite != null) {
                            entites.add(entite);
                        }
                    }
                }
                return entites;
    }

    @Override
    protected void updateEntite(Classe entite)
            throws SQLException, PersistenceException {
        try (PreparedStatement ps
                = this.getMapperManager().createPreparedStatement(
                        SQL.CLASSES.UPDATE)) {
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

            if (entite.getCompetences().size() > 0) {
                updateCompetencesLinks(entite.getIdentifiant(), entite);
            }
        }
    }

    private void updateCompetencesLinks(Identifiant idClasse, Classe classe) throws PersistenceException, SQLException {
        for (Competence c : classe.getCompetences()) {
            try (PreparedStatement ps
                    = this.getMapperManager()
                            .createPreparedStatement(
                                    SQL.COMPETENCES_CLASSES.UPDATE)) {

                        if (c != null) {
                            ps.setString(1, idClasse.getUUID());
                            ps.setString(2, c.getIdentifiant().getUUID());
                            ps.setString(3, c.getCouleurOverride());

                        } else {
                            ps.setNull(1, Types.VARCHAR);
                            ps.setNull(2, Types.VARCHAR);
                            ps.setNull(3, Types.VARCHAR);

                        }
                        ps.setString(4, idClasse.getUUID());
                        ps.setString(5, c.getIdentifiant().getUUID());
                        ps.setLong(6,
                                classe.getIdentifiant().getVersion());
                    }
        }
    }

    @Override
    protected Classe readEntite(ResultSet rs)
            throws SQLException, PersistenceException {
        Identifiant identifiant = readIdentifiant(rs);
        Audit audit = readAudit(rs);

        String nom = rs.getString(
                SQL.CLASSES.ATTRIBUTS.NOM);
        String description = rs.getString(
                SQL.CLASSES.ATTRIBUTS.DESCRIPTION);

        List<Competence> competences = readCompetences(identifiant);
        Classe entite = ClasseBase.builder()
                .identifiant(identifiant)
                .audit(audit)
                .nom(nom)
                .description(description)
                .competences(competences)
                .build();
        return entite;
    }

    private List<Competence> readCompetences(Identifiant id)
            throws SQLException, PersistenceException {
        Competence c = null;
        List<Competence> competences = new ArrayList<>();
        try (PreparedStatement ps
                = this.getMapperManager()
                        .createPreparedStatement(
                                SQL.COMPETENCES_CLASSES.SELECT_BY_CLASSE_UUID)) {
                    ps.setString(1, id.getUUID());
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        Identifiant idCompetence
                                = IdentifiantBase
                                        .builder()
                                        .uuid(rs.getString(
                                                SQL.COMPETENCES_CLASSES.ATTRIBUTS.COMPETENCE_UUID)).build();

                        c = this.getMapperManager().getCompetenceMapper().retrieve(idCompetence);
                        String couleur_override = rs.getString(SQL.COMPETENCES_CLASSES.ATTRIBUTS.COULEUR_OVERRIDE);
                        if (couleur_override != null) {
                            c.setCouleurOverride(couleur_override);
                        }
                        if (c != null) {
                            competences.add(c);
                        }
                    }
                }
                return competences;
    }
}
