/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package murderandmystery.domain;

import core.domain.Audit;
import core.domain.EntiteBase;
import core.domain.Identifiant;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author jeanbourquj
 */
public class PersonnageBase extends EntiteBase<Personnage> implements Personnage {

    private String nom;
    private byte[] image;
    private String codeConnexion;
    private Classe classe;
    private List<Objet> inventaire;
    private final Integer INVENTAIRE_SIZE = 6;

    private PersonnageBase(final Builder b) {
        super(b.identifiant, b.audit);
        this.nom = b.nom;
        this.codeConnexion = b.codeConnexion;
        this.classe = b.classe;
        this.image = b.image;
        this.inventaire = b.inventaire;
    }

    @Override
    public void update(Personnage entite) {
        if (entite == null) {
            return;
        }

        this.nom = entite.getNom();
        this.codeConnexion = entite.getCodeConnexion();
        this.classe = entite.getClasse();
        this.image = entite.getImage();
        this.inventaire = entite.getInventaire();
    }

    @Override
    public String getNom() {
        return this.nom;
    }

    @Override
    public void setNom(String sNom) {
        this.nom = sNom;
    }

    @Override
    public byte[] getImage() {
        return this.image;
    }

    @Override
    public void setImage(byte[] bImage) {
        this.image = bImage;
    }

    @Override
    public String getCodeConnexion() {
        return this.codeConnexion;
    }

    @Override
    public void setCodeConnexion(String sCodeConnexion) {
        this.codeConnexion = sCodeConnexion;
    }

    @Override
    public Classe getClasse() {
        return this.classe;
    }

    @Override
    public void setClasse(Classe cClasse) {
        this.classe = cClasse;
    }

    @Override
    public List<Objet> getInventaire() {
        return this.inventaire;
    }

    @Override
    public void setInventaire(List<Objet> lObjets) {
        this.inventaire = new ArrayList<>();
        if (lObjets.size() <= INVENTAIRE_SIZE) {
            for (Objet o : lObjets) {
                this.inventaire.add(o);
            }
        }
    }

    @Override
    public void addObjetInventaire(Objet oObjet) {
        if (this.inventaire.size() <= INVENTAIRE_SIZE) {
            this.inventaire.add(oObjet);
        }
    }

    @Override
    public void removeObjetInventaire(Objet oObjet) {
        for (Objet o : this.inventaire) {
            if (o.equals(oObjet)) {
                this.inventaire.remove(o);
            }
        }
    }

    @Override
    public void removeObjetInventaire(Integer index) {
        if (this.inventaire.isEmpty()) {
            this.inventaire.remove(index.intValue());
        }
    }

    @Override
    public List<Competence> listCompetences() {
        return this.classe.getCompetences();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.nom);
        hash = 31 * hash + Objects.hashCode(this.image);
        hash = 31 * hash + Objects.hashCode(this.codeConnexion);
        hash = 31 * hash + Objects.hashCode(this.classe);
        hash = 31 * hash + Objects.hashCode(this.inventaire);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PersonnageBase other = (PersonnageBase) obj;
        if (!Objects.equals(this.nom, other.nom)) {
            return false;
        }
        if (!Objects.equals(this.codeConnexion, other.codeConnexion)) {
            return false;
        }
        if (!Objects.equals(this.image, other.image)) {
            return false;
        }
        if (!Objects.equals(this.classe, other.classe)) {
            return false;
        }
        if (!Objects.equals(this.inventaire, other.inventaire)) {
            return false;
        }
        return true;
    }

    @Override
    public String getEncodedImage(final String encoding, final String dataValue)
            throws UnsupportedEncodingException {
        byte[] encodedByte = Base64.getEncoder().encode(this.image);
        String returnString = new String(encodedByte, encoding);

        return dataValue + returnString;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PersonnageBase{nom=").append(nom);
        sb.append(", image=").append(image);
        sb.append(", codeConnexion=").append(codeConnexion);
        sb.append(", classe=").append(classe);
        sb.append(", inventaire=").append(inventaire);
        sb.append('}');
        return sb.toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Identifiant identifiant = null;
        private Audit audit = null;
        private String nom;
        private byte[] image;
        private String codeConnexion;
        private Classe classe;
        private List<Objet> inventaire;

        protected Builder() {

        }

        public Personnage build() {
            return new PersonnageBase(this);
        }

        public Builder identifiant(final Identifiant pIdentifiant) {
            this.identifiant = pIdentifiant;
            return this;
        }

        public Builder audit(final Audit pAudit) {
            this.audit = pAudit;
            return this;
        }

        public Builder nom(final String sNom) {
            this.nom = sNom;
            return this;
        }

        public Builder image(final byte[] bImage) {
            this.image = bImage;
            return this;
        }

        public Builder classe(final Classe cClasse) {
            this.classe = cClasse;
            return this;
        }

        public Builder codeConnexion(final String sCodeConnexion) {
            this.codeConnexion = sCodeConnexion;
            return this;
        }

        public Builder inventaire(final List<Objet> lInventaire) {
            this.inventaire = lInventaire;
            return this;
        }

        public Builder personnage(final Personnage pPersonnage) {
            if (pPersonnage != null) {
                this.identifiant = pPersonnage.getIdentifiant();
                this.audit = pPersonnage.getAudit();
                this.nom = pPersonnage.getNom();
                this.codeConnexion = pPersonnage.getCodeConnexion();
                this.classe = pPersonnage.getClasse();
                this.image = pPersonnage.getImage();
                this.inventaire = pPersonnage.getInventaire();

            }
            return this;
        }
    }
}
