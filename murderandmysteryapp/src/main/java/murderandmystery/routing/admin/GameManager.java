/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package murderandmystery.routing.admin;

import java.util.ArrayList;
import java.util.List;
import murderandmystery.datasource.MapperManager;
import murderandmystery.datasource.db.DbMapperManagerImpl;
import murderandmystery.domain.Personnage;

/**
 *
 * @author jeanbourquj
 */
public class GameManager {
    private List<Personnage> listPersonnages;
    private MapperManager mm;
    
    public GameManager(MapperManager mm){
        this.mm = mm;
        this.listPersonnages = new ArrayList<>();
    }
    public GameManager(DbMapperManagerImpl mm){
        this.mm = mm;
        this.listPersonnages = new ArrayList<>();
    }
    
    public List<Personnage> getPersonnages(){
        return this.listPersonnages;
    }
    
    public void addPlayer(Personnage entite){
        this.listPersonnages.add(entite);
    }
    
    public void removePlayer(Personnage entite){
        this.listPersonnages.remove(entite);
    }
    
}
