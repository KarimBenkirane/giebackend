package org.uiass.eia.crm;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name="Adresse")
public class Adresse {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="adresse_id")
    private int id;
    @Column(name="rue")
    private String rue;
    @Column(name="numeroRue")
    private int numeroRue;
    @Column(name="quartier")
    private String quartier;
    @Column(name="codePostal")
    private int codePostal;
    @Column(name="ville")
    private String ville;
    @Column(name="pays")
    private String pays;

    public Adresse() {
    }

    public Adresse(String rue, int numeroRue, String quartier, int codePostal, String ville, String pays) {
        this.rue = rue;
        this.numeroRue = numeroRue;
        this.quartier = quartier;
        this.codePostal = codePostal;
        this.ville = ville;
        this.pays = pays;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRue() {
        return rue;
    }

    public void setRue(String rue) {
        this.rue = rue;
    }

    public int getNumeroRue() {
        return numeroRue;
    }

    public void setNumeroRue(int numeroRue) {
        this.numeroRue = numeroRue;
    }

    public String getQuartier() {
        return quartier;
    }

    public void setQuartier(String quartier) {
        this.quartier = quartier;
    }

    public int getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(int codePostal) {
        this.codePostal = codePostal;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Adresse adresse = (Adresse) o;
        return numeroRue == adresse.numeroRue && codePostal == adresse.codePostal && Objects.equals(rue, adresse.rue) && Objects.equals(quartier, adresse.quartier) && Objects.equals(ville, adresse.ville) && Objects.equals(pays, adresse.pays);
    }

}

