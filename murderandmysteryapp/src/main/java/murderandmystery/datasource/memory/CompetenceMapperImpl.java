/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package murderandmystery.datasource.memory;

import core.datasource.ContrainteNotNullPersistenceException;
import core.datasource.EntiteInconnuePersistenceException;
import core.datasource.EntiteTropAnciennePersistenceException;
import core.datasource.PersistenceException;
import core.domain.AuditBase;
import core.domain.Identifiant;
import core.domain.IdentifiantBase;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import murderandmystery.datasource.CompetenceMapper;
import murderandmystery.domain.Competence;
import murderandmystery.domain.CompetenceBase;

/**
 *
 * @author jeanbourquj
 */
public class CompetenceMapperImpl implements CompetenceMapper {

    private final MemoryMapperManagerImpl mapperManager;

    public CompetenceMapperImpl(final MemoryMapperManagerImpl mm) {
        this.mapperManager = mm;
    }

    @Override
    public Competence create(Competence entite) throws PersistenceException {
        if (entite == null) {
            return null;
        }
        AuditBase.Builder auditBuilder = AuditBase.builder()
                .dateCreation(Instant.now());
        if (entite.getAudit() != null) {
            auditBuilder.userCreation(entite.getAudit().getUserCreation());
        }
        CompetenceBase.Builder builder = CompetenceBase.builder()
                .identifiant(new IdentifiantMemory(
                        IdentifiantBase.builder()
                                .version(1L)
                                .build()))
                .audit(new AuditMemory(auditBuilder.build()))
                .nom(entite.getNom())
                .couleur(entite.getCouleur())
                .cooldown(entite.getCooldown())
                .image(entite.getImage())
                .retourTargetedUser(entite.getRetourUserTargeted())
                .retourUser(entite.getRetourUser())
                .hasTarget(entite.getHasTarget());

        Competence nouvelleEntite = builder.build();

        this.mapperManager.getData().getCompetences()
                .put(nouvelleEntite.getIdentifiant(), nouvelleEntite);
        return this.retrieve(nouvelleEntite.getIdentifiant());
    }

    @Override
    public Competence retrieve(Identifiant id) throws PersistenceException {
        if (id == null) {
            return null;
        }
        Competence entite = null;
        entite = this.mapperManager.getData().getCompetences().get(id);
        if (entite != null) {
            entite = deepClone(entite);
        }
        return entite;
    }

    @Override
    public List<Competence> retrieve(String filtre) throws PersistenceException {
        if (filtre == null) {
            return new ArrayList<>();
        }
        Pattern pattern = Pattern.compile(filtre);

        List<Competence> entites = new ArrayList<>();

        for (Competence i : mapperManager.getData()
                .getCompetences().values()) {
            Matcher matcher = pattern.matcher(i.getNom());
            if (matcher.find()) {
                entites.add(deepClone(i));
            }
        }
        return entites;
    }

    @Override
    public void update(Competence entite) throws PersistenceException {
        if (entite == null || entite.getIdentifiant() == null) {
            return;
        }
        Competence e = this.mapperManager.getData()
                .getCompetences().get(entite.getIdentifiant());
        this.checkEntiteInconnue(e, entite);

        CompetenceBase.Builder builder = CompetenceBase.builder()
                .identifiant(entite.getIdentifiant())
                .nom(entite.getNom())
                .couleur(entite.getCouleur())
                .cooldown(entite.getCooldown())
                .image(entite.getImage())
                .retourTargetedUser(entite.getRetourUserTargeted())
                .retourUser(entite.getRetourUser())
                .hasTarget(entite.getHasTarget());

        Competence entiteModifie = builder.build();
        this.checkContainteNomNotNull(entiteModifie);

        if (e.getIdentifiant().getVersion()
                > entite.getIdentifiant().getVersion()) {
            throw new EntiteTropAnciennePersistenceException();
        }
        e.update(entiteModifie);

        if (e.getAudit() instanceof AuditMemory) {
            String user = null;
            if (entite.getAudit() != null) {
                user = entite.getAudit().getUserModification();
            }
            ((AuditMemory) e.getAudit()).updateNow(user);
        }

        if (e.getIdentifiant() instanceof IdentifiantMemory) {
            ((IdentifiantMemory) e.getIdentifiant()).incrementVersion();
        }
    }

    @Override
    public void delete(Competence entite) throws PersistenceException {
       if(entite == null || entite.getIdentifiant() == null){
           return;
       }
       Competence e = this.mapperManager.getData()
                .getCompetences().get(entite.getIdentifiant());
       
       this.checkEntiteInconnue(e, entite);
       
       if (e.getIdentifiant().getVersion()
                > entite.getIdentifiant().getVersion()) {
            throw new EntiteTropAnciennePersistenceException();
        }
       this.mapperManager.getData()
               .getCompetences().remove(entite.getIdentifiant());
    }

    private Competence deepClone(Competence entite) throws PersistenceException {
        CompetenceBase.Builder builder = CompetenceBase.builder()
                .identifiant(IdentifiantBase.builder()
                        .identifiant(entite.getIdentifiant())
                        .build())
                .audit(AuditBase.builder().audit(entite.getAudit()).build())
                .nom(entite.getNom())
                .couleur(entite.getCouleur())
                .cooldown(entite.getCooldown())
                .image(entite.getImage())
                .retourTargetedUser(entite.getRetourUserTargeted())
                .retourUser(entite.getRetourUser())
                .hasTarget(entite.getHasTarget());

        return builder.build();
    }

    private void checkEntiteInconnue(final Competence e, final Competence entite)
            throws EntiteInconnuePersistenceException {
        if (e == null) {
            throw new EntiteInconnuePersistenceException(
                    String.format("Erreur: l'entité est inconnue! (%s)",
                            entite.toString()));
        }
    }

    private void checkContainteNomNotNull(final Competence entite)
            throws ContrainteNotNullPersistenceException {
        if (entite.getNom() == null) {
            throw new ContrainteNotNullPersistenceException(
                    String.format("Erreur: le nom est null! (%s)",
                            entite));
        }
    }
}
