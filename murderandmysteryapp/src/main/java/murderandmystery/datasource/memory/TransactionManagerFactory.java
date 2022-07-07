/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package murderandmystery.datasource.memory;

import core.datasource.TransactionManager;
import javax.naming.NamingException;
import murderandmystery.domain.DemoData;

/**
 *
 * @author jeanbourquj
 */
public final class TransactionManagerFactory {

    private TransactionManagerFactory() {
    }

    public static TransactionManager getInstance() throws NamingException {

        DemoData data = new DemoData();
        data.initialisation(1L, true);

        return MemoryTransactionManagerImpl.getInstance(data);

    }

}
