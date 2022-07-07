/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package murderandmystery.datasource.db;

import core.datasource.PersistenceException;
import java.sql.SQLException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 *
 * @author jeanbourquj
 * @param <T>
 * @param <R>
 */
 
 public interface CheckedFunction<T, R> extends BiConsumer<T,R>{

    @Override
    default void accept(final T t, final R u){
        try{
            acceptThrows(t, u);
        }catch(SQLException ex){
            throw new RuntimeException(ex);
        }catch(PersistenceException ex){
            throw new RuntimeException(ex);
        }
    }
    
    void acceptThrows(T t, R u) throws SQLException, PersistenceException;
 }