package org.uiass.eia.crm;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name="Contact")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="contact_type")
@Table(name="contact")
public abstract class Contact implements Serializable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected int id;
    @Column(name="telephone")
    protected String telephone;
    @Column(name="email")
    protected String email;
    @Column(name="fax")
    protected String fax;
    @OneToOne
    @JoinColumn(name = "id_adresse")
    protected Adresse adresse;

    public Contact() {
    }

    public Contact(String telephone, String email, String fax, Adresse adresse) {
        this.telephone = telephone;
        this.email = email;
        this.fax = fax;
        this.adresse = adresse;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public Adresse getAdresse() {
        return adresse;
    }

    public void setAdresse(Adresse adresse) {
        this.adresse = adresse;
    }
}
