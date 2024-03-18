package org.uiass.eia.crm;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.io.Serializable;

@Entity
@DiscriminatorValue("Entreprise")
public class Entreprise extends Contact implements Serializable {
    @Column(name="raisonSociale")
    private String raisonSociale;
    @Column(name="formeJuridique")
    private String formeJuridique;

    public Entreprise() {

    }

    public Entreprise(String telephone, String email, String fax, Adresse adresse, String raisonSociale, String formeJuridique) {
        super(telephone, email, fax, adresse);
        this.raisonSociale = raisonSociale;
        this.formeJuridique = formeJuridique;
    }

    public String getRaisonSociale() {
        return raisonSociale;
    }

    public void setRaisonSociale(String raisonSociale) {
        this.raisonSociale = raisonSociale;
    }

    public String getFormeJuridique() {
        return formeJuridique;
    }

    public void setFormeJuridique(String formeJuridique) {
        this.formeJuridique = formeJuridique;
    }
}
