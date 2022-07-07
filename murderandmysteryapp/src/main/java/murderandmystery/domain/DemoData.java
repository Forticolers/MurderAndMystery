/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package murderandmystery.domain;

import core.domain.Identifiant;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 *
 * @author jeanbourquj
 */
public class DemoData {

    private final Map<Identifiant, Classe> classes;
    private final Map<Identifiant, Personnage> personnages;
    private final Map<Identifiant, Competence> competences;
    private final Map<Identifiant, SalleFictive> salleFictives;
    private final Map<Identifiant, Objet> objets;

    private static final Logger LOG
            = Logger.getLogger(DemoData.class.getName());

    public DemoData() {
        this.classes = new HashMap<>();
        this.personnages = new HashMap<>();
        this.competences = new HashMap<>();
        this.salleFictives = new HashMap<>();
        this.objets = new HashMap<>();
    }

    public void initialisation() {
        initialisation(0L);
    }

    public void initialisation(final Long version) {
        initialisation(version, false);
    }

    public void initialisation(final Long version,
            final boolean createAudit) {
        this.classes.clear();
        this.personnages.clear();
        this.competences.clear();
        this.objets.clear();
        this.salleFictives.clear();

        this.initClasses(version, createAudit);
        this.initCompetences(version, createAudit);
        this.initObjets(version, createAudit);
        this.initPersonnages(version, createAudit);
        this.initSalleFictives(version, createAudit);
    }

    public Map<Identifiant, Classe> getClasses() {
        return this.classes;
    }

    public Map<Identifiant, Personnage> getPersonnages() {
        return this.personnages;
    }

    public Map<Identifiant, Competence> getCompetences() {
        return this.competences;
    }

    public Map<Identifiant, Objet> getObjets() {
        return this.objets;
    }

    public Map<Identifiant, SalleFictive> getSalleFictives() {
        return this.salleFictives;
    }

    public static class CLASSES {

        public enum FIELDS {
            UUID,
            NOM,
            DESCRIPTION
        }

    }

    public static class COMPETENCES {

    }

    public static class PERSONNAGES {

    }

    public static class OBJETS {

    }

    public static class SALLES_FICTIVES {

    }

    private void initClasses(final Long version, final boolean createAudit) {

    }

    private void initCompetences(final Long version, final boolean createAudit) {

    }

    private void initPersonnages(final Long version, final boolean createAudit) {

    }

    private void initObjets(final Long version, final boolean createAudit) {

    }

    private void initSalleFictives(final Long version, final boolean createAudit) {

    }
}
