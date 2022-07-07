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
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import murderandmystery.datasource.PersonnageMapper;
import murderandmystery.domain.Personnage;
import murderandmystery.domain.PersonnageBase;

/**
 *
 * @author jeanbourquj
 */
public class PersonnageMapperImpl implements PersonnageMapper {

    private final MemoryMapperManagerImpl mapperManager;

    PersonnageMapperImpl(final MemoryMapperManagerImpl mm) {
        this.mapperManager = mm;
    }

    @Override
    public Personnage create(Personnage entite) throws PersistenceException {
        if (entite == null) {
            return null;
        }
        AuditBase.Builder auditBuilder = AuditBase.builder()
                .dateCreation(Instant.now());
        if (entite.getAudit() != null) {
            auditBuilder.userCreation(entite.getAudit().getUserCreation());
        }

        PersonnageBase.Builder builder = PersonnageBase.builder()
                .identifiant(new IdentifiantMemory(
                        IdentifiantBase.builder()
                                .version(1L)
                                .build()))
                .audit(new AuditMemory(auditBuilder.build()))
                .nom(entite.getNom())
                .codeConnexion(entite.getCodeConnexion())
                .image(entite.getImage())
                .inventaire(entite.getInventaire())
                .classe(entite.getClasse());
        Personnage nouvelleEntite = builder.build();

        this.mapperManager.getData().getPersonnages()
                .put(nouvelleEntite.getIdentifiant(), nouvelleEntite);
        return this.retrieve(nouvelleEntite.getIdentifiant());
    }

    @Override
    public Personnage retrieve(Identifiant id) throws PersistenceException {
        if (id == null) {
            return null;
        }
        Personnage entite = null;

        entite = this.mapperManager.getData().getPersonnages().get(id);
        if (entite != null) {
            entite = deepClone(entite);
        }
        return entite;
    }

    @Override
    public List<Personnage> retrieve(String filtre) throws PersistenceException {
        if (filtre == null) {
            return new ArrayList<>();
        }
        Pattern pattern = Pattern.compile(filtre);

        List<Personnage> entites = new ArrayList<>();

        for (Personnage i : mapperManager.getData()
                .getPersonnages().values()) {
            Matcher matcher = pattern.matcher(i.getNom());
            if (matcher.find()) {
                entites.add(deepClone(i));
            }
        }
        return entites;
    }

    @Override
    public void update(Personnage entite) throws PersistenceException {
        if (entite == null || entite.getIdentifiant() == null) {
            return;
        }

        Personnage e = this.mapperManager.getData()
                .getPersonnages().get(entite.getIdentifiant());
        this.checkEntiteInconnue(e, entite);

        PersonnageBase.Builder builder = PersonnageBase.builder()
                .identifiant(entite.getIdentifiant())
                .nom(entite.getNom())
                .codeConnexion(entite.getCodeConnexion())
                .image(entite.getImage())
                .inventaire(entite.getInventaire())
                .classe(entite.getClasse());

        Personnage entiteModifie = builder.build();

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
    public void delete(Personnage entite) throws PersistenceException {
        if (entite == null || entite.getIdentifiant() == null) {
            return;
        }
        Personnage e = this.mapperManager.getData()
                .getPersonnages().get(entite.getIdentifiant());
        this.checkEntiteInconnue(e, entite);

        if (e.getIdentifiant().getVersion()
                > entite.getIdentifiant().getVersion()) {
            throw new EntiteTropAnciennePersistenceException();
        }

        this.mapperManager.getData()
                .getPersonnages().remove(entite.getIdentifiant());
    }

    private Personnage deepClone(Personnage entite) throws PersistenceException {
        PersonnageBase.Builder builder = PersonnageBase.builder()
                .identifiant(IdentifiantBase.builder()
                        .identifiant(entite.getIdentifiant())
                        .build())
                .audit(AuditBase.builder().audit(entite.getAudit()).build())
                .nom(entite.getNom())
                .codeConnexion(entite.getCodeConnexion())
                .image(entite.getImage())
                .inventaire(entite.getInventaire())
                .classe(entite.getClasse());

        return builder.build();
    }

    private void checkEntiteInconnue(final Personnage e, final Personnage entite)
            throws EntiteInconnuePersistenceException {
        if (e == null) {
            throw new EntiteInconnuePersistenceException(
                    String.format("Erreur: l'entité est inconnue! (%s)",
                            entite.toString()));
        }
    }

    private void checkContainteNomNotNull(final Personnage entite)
            throws ContrainteNotNullPersistenceException {
        if (entite.getNom() == null) {
            throw new ContrainteNotNullPersistenceException(
                    String.format("Erreur: le nom est null! (%s)",
                            entite));
        }
    }

    @Override
    public Personnage retrieveEntiteCode(String codeConnexion) throws SQLException, PersistenceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
