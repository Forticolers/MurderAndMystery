/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package murderandmystery.domain;

import core.domain.Audit;
import core.domain.EntiteBase;
import core.domain.Identifiant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author jeanbourquj
 */
public class ClasseBase extends EntiteBase<Classe> implements Classe {

    private String nom;
    private String description;
    private List<Competence> competences;

    private ClasseBase(final Builder b) {
        super(b.identifiant, b.audit);
        this.nom = b.nom;
        this.description = b.description;
        this.competences = b.competences;
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
    public String getDescription() {
        return this.description;
    }

    @Override
    public void setDescription(String sDescription) {
        this.description = sDescription;
    }

    @Override
    public List<Competence> getCompetences() {
        return this.competences;
    }

    @Override
    public void addCompetence(Competence cCompetence) {
        if (!this.competences.isEmpty()) {
            for (Competence c : this.competences) {
                if (c.equals(cCompetence)) {
                    return;
                }
            }
        }
        this.competences.add(cCompetence);
    }

    @Override
    public void removeCompetence(Competence cCompetence) {
        if (!this.competences.isEmpty()) {
            for (Competence c : this.competences) {
                if (c.equals(cCompetence)) {
                    this.competences.remove(c);
                }
            }
        }
    }

    @Override
    public void setCompetences(List<Competence> cCompetences) {
        this.competences = new ArrayList<Competence>();
        for (Competence c : cCompetences) {
            this.competences.add(c);
        }
    }

    @Override
    public void update(Classe entite) {
     
        if (entite == null) {
            return;
        }
        
                
        this.nom = entite.getNom();
        this.description = entite.getDescription();
        this.setCompetences(entite.getCompetences());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.nom);
        hash = 89 * hash + Objects.hashCode(this.description);
        hash = 89 * hash + Objects.hashCode(this.competences);
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
        final ClasseBase other = (ClasseBase) obj;
        if (!Objects.equals(this.nom, other.nom)) {
            return false;
        }
        if (!Objects.equals(this.description, other.description)) {
            return false;
        }
        if (!Objects.equals(this.competences, other.competences)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ClasseBase{nom=").append(nom);
        sb.append(", description=").append(description);
        sb.append(", competences=").append(competences);
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
        private List<Competence> competences;

        protected Builder() {

        }

        public Classe build() {
            return new ClasseBase(this);
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

        public Builder competences(final List<Competence> cCompetences) {
            this.competences = cCompetences;
            return this;
        }

        public Builder classe(final Classe cClasse) {
            if (cClasse != null) {

                this.identifiant = cClasse.getIdentifiant();
                this.audit = cClasse.getAudit();
                this.nom = cClasse.getNom();
                this.description = cClasse.getDescription();
                this.competences = cClasse.getCompetences();
            }
            return this;
        }
    }
}
