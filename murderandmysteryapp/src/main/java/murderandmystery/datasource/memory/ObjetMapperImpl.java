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
import murderandmystery.datasource.ObjetMapper;
import murderandmystery.domain.Objet;
import murderandmystery.domain.ObjetBase;
import murderandmystery.domain.Personnage;

/**
 *
 * @author jeanbourquj
 */
public class ObjetMapperImpl implements ObjetMapper {

    private final MemoryMapperManagerImpl mapperManager;

    ObjetMapperImpl(final MemoryMapperManagerImpl mm) {
        this.mapperManager = mm;
    }

    @Override
    public Objet create(Objet entite) throws PersistenceException {
        if (entite == null) {
            return null;
        }
        AuditBase.Builder auditBuilder = AuditBase.builder()
                .dateCreation(Instant.now());
        if (entite.getAudit() != null) {
            auditBuilder.userCreation(entite.getAudit().getUserCreation());
        }

        ObjetBase.Builder builder = ObjetBase.builder()
                .identifiant(new IdentifiantMemory(
                        IdentifiantBase.builder()
                                .version(1L)
                                .build()))
                .audit(new AuditMemory(auditBuilder.build()))
                .nom(entite.getNom())
                .description(entite.getDescription())
                .image(entite.getImage())
                .isRamassable(entite.isRamassable());
        Objet nouvelleEntite = builder.build();

        this.mapperManager.getData().getObjets()
                .put(nouvelleEntite.getIdentifiant(), nouvelleEntite);
        return this.retrieve(nouvelleEntite.getIdentifiant());
    }

    @Override
    public Objet retrieve(Identifiant id) throws PersistenceException {
        if (id == null) {
            return null;
        }
        Objet entite = null;

        entite = this.mapperManager.getData().getObjets().get(id);
        if (entite != null) {
            entite = deepClone(entite);
        }
        return entite;
    }

    @Override
    public List<Objet> retrieve(String filtre) throws PersistenceException {
        if (filtre == null) {
            return new ArrayList<>();
        }
        Pattern pattern = Pattern.compile(filtre);

        List<Objet> entites = new ArrayList<>();

        for (Objet i : mapperManager.getData()
                .getObjets().values()) {
            Matcher matcher = pattern.matcher(i.getNom());
            if (matcher.find()) {
                entites.add(deepClone(i));
            }
        }
        return entites;
    }

    @Override
    public void update(Objet entite) throws PersistenceException {
        if (entite == null || entite.getIdentifiant() == null) {
            return;
        }

        Objet e = this.mapperManager.getData()
                .getObjets().get(entite.getIdentifiant());
        this.checkEntiteInconnue(e, entite);

        ObjetBase.Builder builder = ObjetBase.builder()
                .identifiant(entite.getIdentifiant())
                .nom(entite.getNom())
                .description(entite.getDescription())
                .image(entite.getImage())
                .isRamassable(entite.isRamassable());
        Objet entiteModifie = builder.build();

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
    public void delete(Objet entite) throws PersistenceException {
        if (entite == null || entite.getIdentifiant() == null) {
            return;
        }
        Objet e = this.mapperManager.getData()
                .getObjets().get(entite.getIdentifiant());
        this.checkEntiteInconnue(e, entite);
        if (e.getIdentifiant().getVersion()
                > entite.getIdentifiant().getVersion()) {
            throw new EntiteTropAnciennePersistenceException();
        }

        this.mapperManager.getData()
                .getObjets().remove(entite.getIdentifiant());
    }

    private Objet deepClone(Objet entite) throws PersistenceException {
        ObjetBase.Builder builder = ObjetBase.builder()
                .identifiant(IdentifiantBase.builder()
                        .identifiant(entite.getIdentifiant()).build())
                .audit(AuditBase.builder().audit(entite.getAudit()).build())
                .nom(entite.getNom())
                .description(entite.getDescription())
                .image(entite.getImage())
                .isRamassable(entite.isRamassable());

        return builder.build();
    }

    private void checkEntiteInconnue(final Objet e, final Objet entite)
            throws EntiteInconnuePersistenceException {
        if (e == null) {
            throw new EntiteInconnuePersistenceException(
                    String.format("Erreur: l'entité est inconnue! (%s)",
                            entite.toString()));
        }
    }

    private void checkContainteNomNotNull(final Objet entite)
            throws ContrainteNotNullPersistenceException {
        if (entite.getNom() == null) {
            throw new ContrainteNotNullPersistenceException(
                    String.format("Erreur: le nom est null! (%s)",
                            entite));
        }
    }
}
