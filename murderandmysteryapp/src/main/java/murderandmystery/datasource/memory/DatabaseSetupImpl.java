/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package murderandmystery.datasource.memory;

import core.datasource.DatabaseSetup;
import core.datasource.PersistenceException;

/**
 *
 * @author jeanbourquj
 */
public class DatabaseSetupImpl implements DatabaseSetup{
    
    private final MemoryMapperManagerImpl mapperManager;

    public DatabaseSetupImpl(final MemoryMapperManagerImpl mm) {
        this.mapperManager = mm;
    }
    
    

    @Override
    public void createTables() throws PersistenceException {
        this.mapperManager.getData().getClasses().clear();
        this.mapperManager.getData().getCompetences().clear();
        this.mapperManager.getData().getObjets().clear();
        this.mapperManager.getData().getPersonnages().clear();
        this.mapperManager.getData().getSalleFictives().clear();
    }

    @Override
    public void dropTables() throws PersistenceException {
        this.mapperManager.getData().getClasses().clear();
        this.mapperManager.getData().getCompetences().clear();
        this.mapperManager.getData().getObjets().clear();
        this.mapperManager.getData().getPersonnages().clear();
        this.mapperManager.getData().getSalleFictives().clear();
    }

    @Override
    public void insertData() throws PersistenceException {
        this.mapperManager.getData().initialisation(1L, true);
    }
    
}
