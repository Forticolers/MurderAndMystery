package core.datasource;


/**
 *
 * @author julien jeanbourquin (julien.jeanbourquin AT hotmail.com)
 */
public interface TransactionManager<O> {

    Object executeTransaction(O operation) throws PersistenceException;

    public interface Operation<M> {

        Object execute(M mm) throws PersistenceException;
    }

}
