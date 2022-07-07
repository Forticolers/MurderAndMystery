package core.datasource;


/**
 *
 * @author julien jeanbourquin (julien.jeanbourquin AT hotmail.com)
 */
public interface DatabaseSetup {

    /**
     *
     * @throws PersistenceException
     */
    void createTables() throws PersistenceException;

    /**
     *
     * @throws PersistenceException
     */
    void dropTables() throws PersistenceException;

    /**
     *
     * @throws PersistenceException
     */
    void insertData() throws PersistenceException;
}
