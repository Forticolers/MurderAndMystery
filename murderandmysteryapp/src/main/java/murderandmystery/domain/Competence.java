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
public interface Competence extends Entite<Competence> {

    String getNom();

    void setNom(String sNom);

    byte[] getImage();

    void setImage(byte[] bImage);

    String getEncodedImage(String encoding, String dataValue) throws UnsupportedEncodingException;
    String getCouleur();

    void setCouleur(String sCouleur);
    
    String getCouleurOverride();

    void setCouleurOverride(String sCouleur);

    String getRetourUser();

    void setRetourUser(String sRetourUser);

    boolean getHasTarget();

    void setHasTarget(boolean bHasTarget);

    String getRetourUserTargeted();

    void setRetourUserTargeted(String sRetourUserTargeted);

    Integer getCooldown();

    void setCooldown(Integer iCooldown);
}
