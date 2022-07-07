/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package murderandmystery.domain;

import core.domain.Entite;
import java.awt.Point;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

/**
 *
 * @author jeanbourquj
 */
public interface SalleFictive extends Entite<SalleFictive> {

    String getNom();

    void setNom(String sNom);

    byte[] getImage();

    void setImage(byte[] bImage);

    HashMap<Point, Objet> getObjets();

    HashMap<Point, Objet> getObjet(Point pPosition);
    
    Objet getObjetOfPoint(Point pPosition);
    
    HashMap<Point, Objet> getObjet(Objet pObjet);

    void setObjets(HashMap<Point, Objet> objets);

    void addObjet(Point pPosition, Objet oObjet);

    void removeObjet(Point pPosition);

    void removeObjet(Objet oObjet);
    
    String getEncodedImage(String encoding, String dataValue) throws UnsupportedEncodingException;
}
