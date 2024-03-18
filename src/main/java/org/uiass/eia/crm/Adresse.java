package org.uiass.eia.crm;

import javax.persistence.*;

@Entity
@Table(name="Adresse")
public class Adresse {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;
    @Column(name="rue")
    private String rue;
    @Column(name="numeroRue")
    private int numeroRue;
    @Column(name="quartier")
    private String quartier;
    @Column(name="ville")
    private String ville;
    @Column(name="pays")
    private String pays;

    public Adresse() {
    }

    public Adresse(String rue, int numeroRue, String quartier, String ville, String pays) {
        this.rue = rue;
        this.numeroRue = numeroRue;
        this.quartier = quartier;
        this.ville = ville;
        this.pays = pays;
    }

    public String getRue() {
        return rue;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}

