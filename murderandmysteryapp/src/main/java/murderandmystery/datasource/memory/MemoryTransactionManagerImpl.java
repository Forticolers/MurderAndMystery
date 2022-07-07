/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package murderandmystery.datasource.memory;

import core.datasource.PersistenceException;
import core.datasource.TransactionManager;
import java.util.logging.Logger;
import murderandmystery.datasource.MapperManager;
import murderandmystery.domain.DemoData;

/**
 *
 * @author jeanbourquj
 */
public class MemoryTransactionManagerImpl
        implements TransactionManager<TransactionManager.Operation<MapperManager>> {

    private static MemoryTransactionManagerImpl transactionManager;
    private static final Logger LOG
            = Logger.getLogger(MemoryTransactionManagerImpl.class.getName());

    public static TransactionManager getInstance(final DemoData data) {
        if (transactionManager == null) {
            transactionManager = new MemoryTransactionManagerImpl(data);
        }
        return transactionManager;
    }
    private final DemoData data;

    private MemoryTransactionManagerImpl(final DemoData data) {
        this.data = data;
    }

    @Override
    public Object executeTransaction(final Operation<MapperManager> operation)
            throws PersistenceException {

        return operation.execute(
                new MemoryMapperManagerImpl(data));
    }

}
