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
import murderandmystery.datasource.SalleFictiveMapper;
import murderandmystery.domain.SalleFictive;
import murderandmystery.domain.SalleFictiveBase;

/**
 *
 * @author jeanbourquj
 */
public class SalleFictiveMapperImpl implements SalleFictiveMapper {

    private final MemoryMapperManagerImpl mapperManager;

    SalleFictiveMapperImpl(final MemoryMapperManagerImpl mm) {
        this.mapperManager = mm;
    }

    @Override
    public SalleFictive create(SalleFictive entite) throws PersistenceException {
        if (entite == null) {
            return null;
        }
        AuditBase.Builder auditBuilder = AuditBase.builder()
                .dateCreation(Instant.now());
        if (entite.getAudit() != null) {
            auditBuilder.userCreation(entite.getAudit().getUserCreation());
        }

        SalleFictiveBase.Builder builder = SalleFictiveBase.builder()
                .identifiant(new IdentifiantMemory(
                        IdentifiantBase.builder()
                                .version(1L)
                                .build()))
                .audit(new AuditMemory(auditBuilder.build()))
                .image(entite.getImage())
                .nom(entite.getNom())
                .objets(entite.getObjets());

        SalleFictive nouvelleEntite = builder.build();

        this.mapperManager.getData().getSalleFictives()
                .put(nouvelleEntite.getIdentifiant(), nouvelleEntite);
        return this.retrieve(nouvelleEntite.getIdentifiant());
    }

    @Override
    public SalleFictive retrieve(Identifiant id) throws PersistenceException {
        if (id == null) {
            return null;
        }

        SalleFictive entite = null;

        entite = this.mapperManager.getData().getSalleFictives().get(id);

        if (entite != null) {
            entite = deepClone(entite);
        }
        return entite;
    }

    @Override
    public List<SalleFictive> retrieve(String filtre) throws PersistenceException {
        if (filtre == null) {
            return new ArrayList<>();
        }
        Pattern pattern = Pattern.compile(filtre);

        List<SalleFictive> entites = new ArrayList<>();

        for (SalleFictive i : mapperManager.getData()
                .getSalleFictives().values()) {
            Matcher matcher = pattern.matcher(i.getNom());
            if (matcher.find()) {
                entites.add(deepClone(i));
            }
        }
        return entites;
    }

    @Override
    public void update(SalleFictive entite) throws PersistenceException {
        if (entite == null || entite.getIdentifiant() == null) {
            return;
        }

        SalleFictive e = this.mapperManager.getData().getSalleFictives()
                .get(entite.getIdentifiant());
        this.checkEntiteInconnue(e, entite);

        SalleFictiveBase.Builder builder = SalleFictiveBase.builder()
                .identifiant(entite.getIdentifiant())
                .image(entite.getImage())
                .nom(entite.getNom())
                .objets(entite.getObjets());
        SalleFictive entiteModifie = builder.build();
        this.checkContainteNomNotNull(entite);
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
    public void delete(SalleFictive entite) throws PersistenceException {
        if (entite == null || entite.getIdentifiant() == null) {
            return;
        }
        SalleFictive e = this.mapperManager.getData().getSalleFictives()
                .get(entite.getIdentifiant());
        this.checkEntiteInconnue(e, entite);

        if (e.getIdentifiant().getVersion()
                > entite.getIdentifiant().getVersion()) {
            throw new EntiteTropAnciennePersistenceException();
        }

        this.mapperManager.getData()
                .getSalleFictives().remove(entite.getIdentifiant());

    }

    private SalleFictive deepClone(SalleFictive entite) throws PersistenceException {

        SalleFictiveBase.Builder builder = SalleFictiveBase.builder()
                .identifiant(IdentifiantBase.builder()
                        .identifiant(entite.getIdentifiant())
                        .build())
                .audit(AuditBase.builder().audit(entite.getAudit()).build())
                .image(entite.getImage())
                .nom(entite.getNom())
                .objets(entite.getObjets());

        return builder.build();

    }

    private void checkEntiteInconnue(final SalleFictive e, final SalleFictive entite)
            throws EntiteInconnuePersistenceException {
        if (e == null) {
            throw new EntiteInconnuePersistenceException(
                    String.format("Erreur: l'entité est inconnue! (%s)",
                            entite.toString()));
        }
    }

    private void checkContainteNomNotNull(final SalleFictive entite)
            throws ContrainteNotNullPersistenceException {
        if (entite.getNom() == null) {
            throw new ContrainteNotNullPersistenceException(
                    String.format("Erreur: le nom est null! (%s)",
                            entite));
        }
    }
}
