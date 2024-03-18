package org.uiass.eia.crm;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.io.Serializable;

@Entity
@DiscriminatorValue("Particulier")
public class Particulier extends Contact implements Serializable {
    @Column(name="nom")
    private String nom;
    @Column(name="prenom")
    private String prenom;

    public Particulier() {

    }

    public Particulier(String telephone, String email, String fax, Adresse adresse, String nom, String prenom) {
        super(telephone, email, fax, adresse);
        this.nom = nom;
        this.prenom = prenom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
}
