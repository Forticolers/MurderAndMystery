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
import murderandmystery.datasource.ClasseMapper;
import murderandmystery.domain.Classe;
import murderandmystery.domain.ClasseBase;

/**
 *
 * @author jeanbourquj
 */
public class ClasseMapperImpl implements ClasseMapper {

    private final MemoryMapperManagerImpl mapperManager;

    ClasseMapperImpl(final MemoryMapperManagerImpl mm){
        this.mapperManager = mm;
    }

    @Override
    public Classe create(Classe entite) throws PersistenceException {
        if (entite == null) {
            return null;
        }
        AuditBase.Builder auditBuilder = AuditBase.builder()
                .dateCreation(Instant.now());
        if (entite.getAudit() != null) {
            auditBuilder.userCreation(entite.getAudit().getUserCreation());
        }
        ClasseBase.Builder builder = ClasseBase.builder()
                .identifiant(new IdentifiantMemory(
                        IdentifiantBase.builder()
                                .version(1L)
                                .build()))
                .audit(new AuditMemory(auditBuilder.build()))
                .nom(entite.getNom())
                .description(entite.getDescription())
                .competences(entite.getCompetences());
        Classe nouvelleEntite = builder.build();

        this.mapperManager.getData().getClasses()
                .put(nouvelleEntite.getIdentifiant(), nouvelleEntite);
        return this.retrieve(nouvelleEntite.getIdentifiant());

    }

    @Override
    public Classe retrieve(final Identifiant id) throws PersistenceException {
        if (id == null) {
            return null;
        }
        Classe entite = null;
        entite = this.mapperManager.getData().getClasses().get(id);
        if (entite != null) {
            entite = deepClone(entite);
        }
        return entite;
    }

    @Override
    public List<Classe> retrieve(String filtre) throws PersistenceException {
        if (filtre == null) {
            return new ArrayList<>();
        }
        Pattern pattern = Pattern.compile(filtre);

        List<Classe> entites = new ArrayList<>();

        for (Classe i : mapperManager.getData()
                .getClasses().values()) {
            Matcher matcher = pattern.matcher(i.getNom());
            if (matcher.find()) {
                entites.add(deepClone(i));
            }
        }
        return entites;
    }

    @Override
    public void update(Classe entite) throws PersistenceException {
        if (entite == null || entite.getIdentifiant() == null) {
            return;
        }

        Classe e = this.mapperManager.getData()
                .getClasses().get(entite.getIdentifiant());
        checkEntiteInconnue(e, entite);

        ClasseBase.Builder builder = ClasseBase.builder()
                .identifiant(entite.getIdentifiant())
                .nom(entite.getNom())
                .description(entite.getDescription())
                .competences(entite.getCompetences());

        Classe entiteModifie = builder.build();
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
    public void delete(Classe entite) throws PersistenceException {
        if (entite == null || entite.getIdentifiant() == null) {
            return;
        }
        Classe e = this.mapperManager.getData()
                .getClasses().get(entite.getIdentifiant());
        this.checkEntiteInconnue(e, entite);

        if (e.getIdentifiant().getVersion()
                > entite.getIdentifiant().getVersion()) {
            throw new EntiteTropAnciennePersistenceException();
        }
        this.mapperManager.getData()
                .getClasses().remove(entite.getIdentifiant());
    }

    private Classe deepClone(Classe entite) throws PersistenceException {
        ClasseBase.Builder builder = ClasseBase.builder()
                .identifiant(IdentifiantBase.builder()
                        .identifiant(entite.getIdentifiant())
                        .build())
                .audit(AuditBase.builder().audit(entite.getAudit()).build())
                .nom(entite.getNom())
                .description(entite.getDescription())
                .competences(entite.getCompetences());
        return builder.build();
    }

    private void checkEntiteInconnue(final Classe e, final Classe entite)
            throws EntiteInconnuePersistenceException {
        if (e == null) {
            throw new EntiteInconnuePersistenceException(
                    String.format("Erreur: l'entité est inconnue! (%s)",
                            entite.toString()));
        }
    }

    private void checkContainteNomNotNull(final Classe entite)
            throws ContrainteNotNullPersistenceException {
        if (entite.getNom() == null) {
            throw new ContrainteNotNullPersistenceException(
                    String.format("Erreur: le nom est null! (%s)",
                            entite));
        }
    }

}
