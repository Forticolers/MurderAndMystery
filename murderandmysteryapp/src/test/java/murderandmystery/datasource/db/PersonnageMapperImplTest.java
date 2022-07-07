/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package murderandmystery.datasource.db;

import core.datasource.DatabaseSetup;
import core.datasource.PersistenceException;
import core.datasource.TransactionManager;
import core.datasource.db.DataSourceFactory;
import core.domain.Identifiant;
import core.domain.IdentifiantBase;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import junit.framework.Assert;
import murderandmystery.datasource.MapperManager;
import murderandmystery.domain.DemoData;
import murderandmystery.domain.Personnage;
import murderandmystery.routing.ConnexionPersonnageRoutage;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jeanbourquj
 */
public class PersonnageMapperImplTest {

    protected DemoData demoData;
    protected TransactionManager transactionManager;

    public PersonnageMapperImplTest() throws PersistenceException {
        demoData = new DemoData();
        demoData.initialisation();

        DataSource datasource = DataSourceFactory.getPostgreSQLDataSource(
                "postgres", 5432,
                "murderandmysteryDB",
                "murderandmystery", "murderandmysterypass");
        this.transactionManager = DbTransactionManagerImpl.getInstance(datasource);

        this.transactionManager.executeTransaction(new TransactionManager.Operation<MapperManager>() {
            @Override
            public Object execute(MapperManager mm) throws PersistenceException {
                DatabaseSetup setup = mm.getDatabaseSetup();
                return null;
            }
        });
    }

    @BeforeClass

    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testGetPersonnageCode() throws PersistenceException {
        String codeConnexion = "12345";
        //Identifiant id = IdentifiantBase.builder().uuid(codeConnexion).build();
        Personnage detail = (Personnage) transactionManager
                .executeTransaction(
                        new TransactionManager.Operation<MapperManager>() {
                    @Override
                    public Object execute(final MapperManager mm)
                            throws PersistenceException {

                        try {
                            return mm.getPersonnageMapper().retrieveEntiteCode("123456");
                        } catch (SQLException ex) {
                            Logger.getLogger(PersonnageMapperImplTest.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        return null;

                    }
                });
        //Assert.assertEquals("5e2b2cb5-c88e-4000-8f1f-182306f3eace", detail.getIdentifiant().getUUID());
        Assert.assertNotNull(detail);
    }
}
