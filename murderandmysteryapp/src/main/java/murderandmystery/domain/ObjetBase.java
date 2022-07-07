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
import java.util.Base64;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author jeanbourquj
 */
public class ObjetBase extends EntiteBase<Objet> implements Objet {

    private String nom;
    private String description;
    private byte[] image;
    private boolean isRamassable;

    private ObjetBase(final Builder b) {
        super(b.identifiant, b.audit);
        this.nom = b.nom;
        this.description = b.description;
        this.image = b.image;
        this.isRamassable = b.isRamassable;

    }

    @Override
    public void update(Objet entite) {
        if (entite == null) {
            return;
        }

        this.description = entite.getDescription();
        this.image = entite.getImage();
        this.isRamassable = entite.isRamassable();
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
    public String getDescription() {
        return this.description;
    }

    @Override
    public void setDescription(String sDescription) {
        this.description = sDescription;
    }

    @Override
    public boolean isRamassable() {
        return this.isRamassable;
    }

    @Override
    public void setIsRamassable(boolean bIsRamassable) {
        this.isRamassable = bIsRamassable;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.nom);
        hash = 67 * hash + Objects.hashCode(this.description);
        hash = 67 * hash + Objects.hashCode(this.image);
        hash = 67 * hash + (this.isRamassable ? 1 : 0);
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
        final ObjetBase other = (ObjetBase) obj;
        if (this.isRamassable != other.isRamassable) {
            return false;
        }
        if (!Objects.equals(this.nom, other.nom)) {
            return false;
        }
        if (!Objects.equals(this.description, other.description)) {
            return false;
        }
        if (!Objects.equals(this.image, other.image)) {
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
        sb.append("ObjetBase{nom=").append(nom);
        sb.append(", description=").append(description);
        sb.append(", image=").append(image);
        sb.append(", isRamassable=").append(isRamassable);
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
        private String description;
        private byte[] image;
        private boolean isRamassable;

        protected Builder() {

        }

        public Objet build() {
            return new ObjetBase(this);
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

        public Builder description(final String sDescription) {
            this.description = sDescription;
            return this;
        }

        public Builder image(final byte[] bImage) {
            this.image = bImage;
            return this;
        }

        public Builder isRamassable(final boolean bIsRamassable) {
            this.isRamassable = bIsRamassable;
            return this;
        }

        public Builder objet(final Objet oObjet) {
            if (oObjet != null) {

                this.identifiant = oObjet.getIdentifiant();
                this.audit = oObjet.getAudit();
                this.description = oObjet.getDescription();
                this.image = oObjet.getImage();
                this.isRamassable = oObjet.isRamassable();
            }
            return this;
        }
    }

}
