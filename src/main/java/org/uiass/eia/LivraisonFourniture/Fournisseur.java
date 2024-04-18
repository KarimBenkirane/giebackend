package org.uiass.eia.LivraisonFourniture;

import org.uiass.eia.crm.Adresse;
import org.uiass.eia.crm.Contact;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.io.Serializable;

@Entity
@DiscriminatorValue("Fournisseur")
public class Fournisseur extends Contact implements Serializable {
    private String name;

    public Fournisseur(String telephone, String email, String fax, Adresse adresse, String name){
        super(telephone, email, fax, adresse);
        this.name=name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Fournisseur{" +
                "name='" + name + '\'' +
                '}';
    }
}
