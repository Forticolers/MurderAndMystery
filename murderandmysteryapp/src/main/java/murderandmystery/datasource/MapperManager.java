/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package murderandmystery.datasource;

import core.datasource.DatabaseSetup;

/**
 *
 * @author jeanbourquj
 */
public interface MapperManager {
    
     SalleFictiveMapper getSalleFictive();
     DatabaseSetup getDatabaseSetup();
     ObjetMapper getObjetMapper();
     PersonnageMapper getPersonnageMapper();
     CompetenceMapper getCompetenceMapper();
     ClasseMapper getClasseMapper();
}
