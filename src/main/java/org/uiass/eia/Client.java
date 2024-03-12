package org.uiass.eia;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Client")
public class Client {
    @Id
    private String numero_TVA;
    @Column(name="Adresse")
    private String adresse;
    @Column(name="Telephone")
    private String telephone;
    @Column(name="Email")
    private String email;
    @Column(name="Site_Web")
    private String site_web;
    @Column(name="Code_Postal")
    private int code_postal;


    public Client(){

    }

    public Client(String numero_TVA, String adresse, String telephone, String email, String site_web, int code_postal) {
        this.numero_TVA = numero_TVA;
        this.adresse = adresse;
        this.telephone = telephone;
        this.email = email;
        this.site_web = site_web;
        this.code_postal = code_postal;
    }

    public String getNumero_TVA() {
        return numero_TVA;
    }

    public void setNumero_TVA(String numero_TVA) {
        this.numero_TVA = numero_TVA;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
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

    public String getSite_web() {
        return site_web;
    }

    public void setSite_web(String site_web) {
        this.site_web = site_web;
    }

    public int getCode_postal() {
        return code_postal;
    }

    public void setCode_postal(int code_postal) {
        this.code_postal = code_postal;
    }


}
