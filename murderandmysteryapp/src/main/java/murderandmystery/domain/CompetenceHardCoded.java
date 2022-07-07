/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package murderandmystery.domain;

import core.domain.EntiteBase;
import core.domain.IdentifiantBase;

/**
 *
 * @author jeanbourquj
 */
public class CompetenceHardCoded {

    private Competence teleportation;
    private Competence volAlaTire;
    private Competence deposerObjet;
    
    public CompetenceHardCoded(){
        this.teleportation = CompetenceBase.builder().nom("Téléportation").build();
                      //todo faire toute les compétences dans la base de données pour les récupérer ici                              
    }
   
    
}
