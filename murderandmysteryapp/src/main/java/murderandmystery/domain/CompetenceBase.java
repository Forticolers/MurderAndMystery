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
public class CompetenceBase extends EntiteBase<Competence> implements Competence {

    private String nom;
    private String retourUser;
    private String retourTargetedUser;
    private Integer cooldown;
    private String couleur;
    private String couleur_override;
    private byte[] image;
    private boolean hasTarget;
    private final String HASN_TARGET = "Cette compétence n'a pas de cible";

    private CompetenceBase(final Builder b) {
        super(b.identifiant, b.audit);
        this.nom = b.nom;
        this.retourUser = b.retourUser;
        this.retourTargetedUser = b.retourTargetedUser;
        this.cooldown = b.cooldown;
        this.couleur = b.couleur;
        this.image = b.image;
        this.hasTarget = b.hasTarget;
        this.couleur_override = b.couleur_override;
    }

    @Override
    public void update(Competence entite) {
        if (entite == null) {
            return;
        }

        this.nom = entite.getNom();
        this.retourUser = entite.getRetourUser();
        this.retourTargetedUser = entite.getRetourUserTargeted();
        this.cooldown = entite.getCooldown();
        this.couleur = entite.getCouleur();
        this.image = entite.getImage();
        this.hasTarget = entite.getHasTarget();
        this.couleur_override = entite.getCouleurOverride();
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
    public String getCouleur() {
        return this.couleur;
    }

    @Override
    public void setCouleur(String sCouleur) {
        this.couleur = sCouleur;
    }

    @Override
    public String getCouleurOverride() {
        return this.couleur_override;
    }

    @Override
    public void setCouleurOverride(String sCouleur) {
        this.couleur_override = sCouleur;
    }

    @Override
    public String getRetourUser() {
        return this.retourUser;
    }

    @Override
    public void setRetourUser(String sRetourUser) {
        this.retourUser = sRetourUser;
    }

    @Override
    public boolean getHasTarget() {
        return this.hasTarget;
    }

    @Override
    public void setHasTarget(boolean bHasTarget) {
        this.hasTarget = bHasTarget;
    }

    @Override
    public String getRetourUserTargeted() {
        return (getHasTarget() ? this.retourTargetedUser : HASN_TARGET);
    }

    @Override
    public void setRetourUserTargeted(String sRetourUserTargeted) {
        this.retourTargetedUser = sRetourUserTargeted;
    }

    @Override
    public Integer getCooldown() {
        return this.cooldown;
    }

    @Override
    public void setCooldown(Integer iCooldown) {
        this.cooldown = iCooldown;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.nom);
        hash = 97 * hash + Objects.hashCode(this.retourUser);
        hash = 97 * hash + Objects.hashCode(this.retourTargetedUser);
        hash = 97 * hash + Objects.hashCode(this.cooldown);
        hash = 97 * hash + Objects.hashCode(this.couleur);
        hash = 97 * hash + Objects.hashCode(this.couleur_override);
        hash = 97 * hash + Objects.hashCode(this.image);
        hash = 97 * hash + (this.hasTarget ? 1 : 0);
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
        final CompetenceBase other = (CompetenceBase) obj;
        if (this.hasTarget != other.hasTarget) {
            return false;
        }
        if (!Objects.equals(this.nom, other.nom)) {
            return false;
        }
        if (!Objects.equals(this.retourUser, other.retourUser)) {
            return false;
        }
        if (!Objects.equals(this.retourTargetedUser, other.retourTargetedUser)) {
            return false;
        }
        if (!Objects.equals(this.couleur, other.couleur)) {
            return false;
        }
        if (!Objects.equals(this.couleur_override, other.couleur_override)) {
            return false;
        }
        if (!Objects.equals(this.cooldown, other.cooldown)) {
            return false;
        }
        if (!Objects.equals(this.image, other.image)) {
            return false;
        }
        return true;
    }

     @Override
    public String getEncodedImage(final String encoding, final String dataValue) 
    throws UnsupportedEncodingException{
        byte[] encodedByte = Base64.getEncoder().encode(this.image);
        String returnString = new String(encodedByte, encoding);
        
        return dataValue + returnString;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CompetenceBase{nom=").append(nom);
        sb.append(", retourUser=").append(retourUser);
        sb.append(", retourTargetedUser=").append(retourTargetedUser);
        sb.append(", cooldown=").append(cooldown);
        sb.append(", couleur=").append(couleur);
        sb.append(", couleur_override=").append(couleur_override);
        sb.append(", image=").append(image);
        sb.append(", hasTarget=").append(hasTarget);
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
        private String retourUser;
        private String retourTargetedUser;
        private Integer cooldown;
        private String couleur;
        private String couleur_override;
        private byte[] image;
        private boolean hasTarget;

        protected Builder() {

        }

        public Competence build() {
            return new CompetenceBase(this);
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

        public Builder retourUser(final String sRetourUser) {
            this.retourUser = sRetourUser;
            return this;
        }

        public Builder retourTargetedUser(final String sRetourTargetedUser) {
            this.retourTargetedUser = sRetourTargetedUser;
            return this;
        }

        public Builder cooldown(final Integer iCooldown) {
            this.cooldown = iCooldown;
            return this;
        }

        public Builder couleur(final String sCouleur) {
            this.couleur = sCouleur;
            return this;
        }

        public Builder couleur_override(final String sCouleur) {
            this.couleur_override = sCouleur;
            return this;
        }

        public Builder image(final byte[] bImage) {
            this.image = bImage;
            return this;
        }

        public Builder hasTarget(final boolean bHasTarget) {
            this.hasTarget = bHasTarget;
            return this;
        }

        public Builder competence(final Competence cCompetence) {
            if (cCompetence != null) {
                this.identifiant = cCompetence.getIdentifiant();
                this.audit = cCompetence.getAudit();
                this.nom = cCompetence.getNom();
                this.retourUser = cCompetence.getRetourUser();
                this.retourTargetedUser = cCompetence.getRetourUserTargeted();
                this.cooldown = cCompetence.getCooldown();
                this.couleur = cCompetence.getCouleur();
                this.couleur = cCompetence.getCouleurOverride();
                this.image = cCompetence.getImage();
                this.hasTarget = cCompetence.getHasTarget();
            }
            return this;
        }
    }
}
