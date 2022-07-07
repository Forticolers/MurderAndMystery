/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package murderandmystery.domain;

import core.domain.Entite;
import java.io.UnsupportedEncodingException;

/**
 *
 * @author jeanbourquj
 */
public interface Objet extends Entite<Objet> {

    String getNom();

    void setNom(String sNom);

    byte[] getImage();

    void setImage(byte[] bImage);
    
    String getEncodedImage(String encoding, String dataValue) throws UnsupportedEncodingException;

    String getDescription();

    void setDescription(String sDescription);

    boolean isRamassable();

    void setIsRamassable(boolean bIsRamassable);
}
