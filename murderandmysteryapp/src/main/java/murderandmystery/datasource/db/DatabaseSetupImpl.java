/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package murderandmystery.datasource.db;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import core.datasource.DatabaseSetup;
import core.datasource.PersistenceException;
import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jeanbourquj
 */
public class DatabaseSetupImpl implements DatabaseSetup {

    private final DbMapperManagerImpl mapperManager;
    private final AdminMapperImpl adminMapper;
    private static final Logger LOG = Logger.getLogger(
            DatabaseSetupImpl.class.getName());

    public DatabaseSetupImpl(final DbMapperManagerImpl mm) {
        this.mapperManager = mm;
        this.adminMapper = new AdminMapperImpl();
    }

    public AdminMapperImpl getAdminMapper() {
        if (adminMapper != null) {
            return adminMapper;
        } else {
            return new AdminMapperImpl();
        }
    }

    @Override
    public void createTables() throws PersistenceException {

        try (Statement requete = this.mapperManager.createStatement();) {
            requete.addBatch(SQL.COMPETENCES.CREATE_TABLE);
            requete.addBatch(SQL.CLASSES.CREATE_TABLE);
            requete.addBatch(SQL.OBJETS.CREATE_TABLE);
            requete.addBatch(SQL.PERSONNAGES.CREATE_TABLE);
            requete.addBatch(SQL.SALLE_FICTIVES.CREATE_TABLE);

            requete.addBatch(SQL.COMPETENCES_CLASSES.CREATE_TABLE);
            requete.addBatch(SQL.INVENTAIRE_PERSONNAGES.CREATE_TABLE);
            requete.addBatch(SQL.OBJET_SALLE_LINK.CREATE_TABLE);

            requete.addBatch(SQL.COMPETENCES.ALTER_TABLE);
            requete.addBatch(SQL.CLASSES.ALTER_TABLE);
            requete.addBatch(SQL.OBJETS.ALTER_TABLE);
            requete.addBatch(SQL.PERSONNAGES.ALTER_TABLE);
            requete.addBatch(SQL.SALLE_FICTIVES.ALTER_TABLE);

            requete.addBatch(SQL.COMPETENCES_CLASSES.ALTER_TABLE);
            requete.addBatch(SQL.INVENTAIRE_PERSONNAGES.ALTER_TABLE);
            requete.addBatch(SQL.OBJET_SALLE_LINK.ALTER_TABLE);

            requete.executeBatch();

        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, null, ex);
            throw new PersistenceException(ex);
        }

    }

    @Override
    public void dropTables() throws PersistenceException {
        try (Statement requete = this.mapperManager.createStatement();) {
            requete.addBatch(SQL.COMPETENCES_CLASSES.DROP_TABLE);
            requete.addBatch(SQL.INVENTAIRE_PERSONNAGES.DROP_TABLE);
            requete.addBatch(SQL.OBJET_SALLE_LINK.DROP_TABLE);
            requete.addBatch(SQL.COMPETENCES.DROP_TABLE);
            requete.addBatch(SQL.CLASSES.DROP_TABLE);
            requete.addBatch(SQL.OBJETS.DROP_TABLE);
            requete.addBatch(SQL.PERSONNAGES.DROP_TABLE);
            requete.addBatch(SQL.SALLE_FICTIVES.DROP_TABLE);

            requete.executeBatch();

        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, null, ex);
            throw new PersistenceException(ex);
        }
    }

    @Override
    public void insertData() throws PersistenceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public class AdminMapperImpl {

        public void createAdmin() throws PersistenceException {
            try (Statement requete = mapperManager.createStatement();) {
                requete.addBatch(SQL.ADMINS.CREATE_TABLE);

                requete.addBatch(SQL.ADMINS.ALTER_TABLE);

                requete.executeBatch();

            } catch (SQLException ex) {
                LOG.log(Level.SEVERE, null, ex);
                throw new PersistenceException(ex);
            }
        }

        public void deleteAdmin() throws PersistenceException {
            try (Statement requete = mapperManager.createStatement();) {
                requete.addBatch(SQL.ADMINS.DROP_TABLE);

                requete.executeBatch();

            } catch (SQLException ex) {
                LOG.log(Level.SEVERE, null, ex);
                throw new PersistenceException(ex);
            }
        }

        public HashCode retrieveAdmin(String code) throws SQLException, PersistenceException {
            HashCode codeDB = null;
            try (PreparedStatement ps
                    = mapperManager
                            .createPreparedStatement(
                                    SQL.ADMINS.SELECT_ADMIN)) {
                        ps.setBytes(1, Hashing.sha256().hashString(code, StandardCharsets.UTF_8).asBytes());
                        ResultSet rs = ps.executeQuery();
                        byte[] codeHashed = null;

                        while (rs.next()) {
                            codeHashed = rs.getBytes(SQL.ADMINS.ATTRIBUTS.CODE);
                            if (codeHashed != null) {
                                codeDB = HashCode.fromBytes(codeHashed);
                            }
                        }
                    }
                    return codeDB;
        }
    }
}
