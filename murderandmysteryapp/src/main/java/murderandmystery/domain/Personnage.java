/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package murderandmystery.domain;

import core.domain.Entite;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 *
 * @author jeanbourquj
 */
public interface Personnage extends Entite<Personnage>{
    String getNom();

    void setNom(String sNom);

    byte[] getImage();

    void setImage(byte[] bImage);
    String getEncodedImage(String encoding, String dataValue) throws UnsupportedEncodingException;
    String getCodeConnexion();
    
    void setCodeConnexion(String sCodeConnexion);
    
    Classe getClasse();
    
    void setClasse(Classe cClasse);
    
    List<Objet> getInventaire();
    
    void setInventaire(List<Objet> lObjets);
    
    void addObjetInventaire(Objet oObjet);
    
    void removeObjetInventaire(Objet oObjet);
    
    void removeObjetInventaire(Integer index);
    
    List<Competence> listCompetences();
}
