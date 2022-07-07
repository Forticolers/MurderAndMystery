/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package murderandmystery.datasource.db;

import core.datasource.DatabaseSetup;
import core.datasource.PersistenceException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import murderandmystery.datasource.ClasseMapper;
import murderandmystery.datasource.CompetenceMapper;
import murderandmystery.datasource.MapperManager;
import murderandmystery.datasource.ObjetMapper;
import murderandmystery.datasource.PersonnageMapper;
import murderandmystery.datasource.SalleFictiveMapper;

/**
 *
 * @author jeanbourquj
 */
public class DbMapperManagerImpl implements MapperManager {

    private final Connection connection;
    private DatabaseSetup databaseSetup;
    private ClasseMapper classeMapper;
    private CompetenceMapper competenceMapper;
    private PersonnageMapper personnageMapper;
    private ObjetMapper objetMapper;
    private SalleFictiveMapper salleFictiveMapper;

    public DbMapperManagerImpl(final Connection conn) {
        this.connection = conn;
    }

    PreparedStatement createPreparedStatement(final String query)
            throws PersistenceException {
        try {
            return this.connection.prepareStatement(query);
        } catch (SQLException ex) {
            throw new PersistenceException(ex);
        }
    }

    Statement createStatement()
            throws PersistenceException {
        try {
            return this.connection.createStatement();
        } catch (SQLException ex) {
            throw new PersistenceException(ex);
        }
    }

    @Override
    public SalleFictiveMapper getSalleFictive() {
        if (salleFictiveMapper == null) {
            salleFictiveMapper = new SalleFictiveMapperImpl(this);
        }
        return this.salleFictiveMapper;
    }

    @Override
    public DatabaseSetup getDatabaseSetup() {
        if (databaseSetup == null) {
            databaseSetup = new DatabaseSetupImpl(this);
        }
        return this.databaseSetup;
    }

    @Override
    public ObjetMapper getObjetMapper() {
        if (objetMapper == null) {
            objetMapper = new ObjetMapperImpl(this);
        }
        return this.objetMapper;
    }

    @Override
    public PersonnageMapper getPersonnageMapper() {
        if (personnageMapper == null) {
            personnageMapper = new PersonnageMapperImpl(this);
        }
        return this.personnageMapper;
    }

    @Override
    public CompetenceMapper getCompetenceMapper() {
        if (competenceMapper == null) {
            competenceMapper = new CompetenceMapperImpl(this);
        }
        return this.competenceMapper;
    }

    @Override
    public ClasseMapper getClasseMapper() {
        if (classeMapper == null) {
            classeMapper = new ClasseMapperImpl(this);
        }
        return this.classeMapper;
    }

}
