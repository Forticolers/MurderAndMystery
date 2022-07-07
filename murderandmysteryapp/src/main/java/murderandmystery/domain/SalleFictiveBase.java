/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package murderandmystery.domain;

import core.domain.Audit;
import core.domain.EntiteBase;
import core.domain.Identifiant;
import java.awt.Point;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Objects;
import murderandmystery.datasource.db.CheckedFunction;

/**
 *
 * @author jeanbourquj
 */
public class SalleFictiveBase extends EntiteBase<SalleFictive> implements SalleFictive {

    private String nom;
    private byte[] image;
    private HashMap<Point, Objet> objets;

    private SalleFictiveBase(final Builder b) {
        super(b.identifiant, b.audit);
        this.nom = b.nom;
        this.image = b.image;
        this.objets = b.objets;
    }

    @Override
    public void update(SalleFictive entite) {
        if (entite == null) {
            return;
        }

        this.nom = entite.getNom();
        this.image = entite.getImage();
        this.objets = entite.getObjets();
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
    public HashMap<Point, Objet> getObjets() {
        return this.objets;
    }

    @Override
    public Objet getObjetOfPoint(Point pPosition) {
        return this.objets.get(pPosition);
    }

    @Override
    public HashMap<Point, Objet> getObjet(Point pPosition) {
        HashMap<Point, Objet> entites = new HashMap<>();
        if (objets.containsKey(pPosition)) {
            entites.put(pPosition, this.objets.get(pPosition));
        }
        return entites;
    }

    @Override
    public HashMap<Point, Objet> getObjet(Objet pObjet) {
        HashMap<Point, Objet> entites = new HashMap<>();
        if (this.objets.containsValue(pObjet)) {
            CheckedFunction<Point, Objet> throwing = (key, value) -> {
                if (value.equals(pObjet)) {
                    entites.put(key, value);
                }
            };
            this.objets.forEach(throwing);
        }

        return entites;
    }

    @Override
    public void setObjets(HashMap<Point, Objet> objets) {
        this.objets = new HashMap<>();
        objets.forEach((key, value) -> {
            this.objets.put(key, value);
        });
    }

    @Override
    public void addObjet(Point pPosition, Objet oObjet) {
        if (this.objets.get(pPosition) == null) {
            this.objets.put(pPosition, oObjet);
        }
    }

    @Override
    public void removeObjet(Point pPosition) {
        if (!this.objets.isEmpty()) {
            this.objets.remove(pPosition);
        }
    }

    @Override
    public void removeObjet(Objet oObjet) {
        if (!this.objets.isEmpty()) {
            this.objets.forEach((key, value) -> {
                if (value.equals(oObjet)) {
                    this.objets.remove(key, value);
                }
            });
        }
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.nom);
        hash = 29 * hash + Objects.hashCode(this.image);
        hash = 29 * hash + Objects.hashCode(this.objets);
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
        final SalleFictiveBase other = (SalleFictiveBase) obj;
        if (!Objects.equals(this.nom, other.nom)) {
            return false;
        }
        if (!Objects.equals(this.image, other.image)) {
            return false;
        }
        if (!Objects.equals(this.objets, other.objets)) {
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
        sb.append("SalleFictiveBase{nom=").append(nom);
        sb.append(", image=").append(image);
        sb.append(", objets=").append(objets);
        sb.append('}');
        return sb.toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Identifiant identifiant;
        private Audit audit;
        private String nom;
        private byte[] image;
        private HashMap<Point, Objet> objets;

        protected Builder() {

        }

        public SalleFictive build() {
            return new SalleFictiveBase(this);
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

        public Builder objets(final HashMap<Point, Objet> oObjets) {
            this.objets = oObjets;
            return this;
        }

        public Builder salleFictive(final SalleFictive sSalleFictive) {
            if (sSalleFictive != null) {
                this.identifiant = sSalleFictive.getIdentifiant();
                this.audit = sSalleFictive.getAudit();
                this.nom = sSalleFictive.getNom();
                this.image = sSalleFictive.getImage();
                this.objets = sSalleFictive.getObjets();
            }
            return this;
        }
    }
}
