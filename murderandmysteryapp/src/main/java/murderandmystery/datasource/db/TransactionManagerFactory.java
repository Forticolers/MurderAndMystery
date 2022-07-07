/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package murderandmystery.datasource.db;

import core.datasource.TransactionManager;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 *
 * @author jeanbourquj
 */
public final class TransactionManagerFactory {

    private TransactionManagerFactory() {
    }

    public static TransactionManager getInstance() throws NamingException {
        InitialContext initCtx = new InitialContext();
        DataSource datasource = (DataSource) initCtx
                .lookup("java:/comp/env/jdbc/db");

        return DbTransactionManagerImpl.getInstance(datasource);

    }

}