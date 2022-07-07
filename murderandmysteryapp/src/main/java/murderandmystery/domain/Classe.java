/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package murderandmystery.domain;

import core.domain.Entite;
import java.util.List;

/**
 *
 * @author jeanbourquj
 */
public interface Classe extends Entite<Classe> {

    String getNom();

    void setNom(String sNom);

    String getDescription();

    void setDescription(String sDescription);

    List<Competence> getCompetences();

    void addCompetence(Competence cCompetence);

    void removeCompetence(Competence cCompetence);

    void setCompetences(List<Competence> cCompetences);
}
