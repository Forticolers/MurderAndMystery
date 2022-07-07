/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package murderandmystery.datasource;

import core.datasource.Mapper;
import core.datasource.PersistenceException;
import java.sql.SQLException;
import murderandmystery.domain.Personnage;

/**
 *
 * @author jeanbourquj
 */
public interface PersonnageMapper extends Mapper<Personnage>{
    Personnage retrieveEntiteCode(String codeConnexion) throws SQLException, PersistenceException;
}
