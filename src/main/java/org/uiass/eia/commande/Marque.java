package org.uiass.eia.commande;

import jakarta.persistence.*;

@Entity(name="Marque")
@Table(name="Marque")
public class Marque {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_marque")
    private int idMarque;

    @Column(name="nom_marque")
    private String nomMarque;

    public Marque(String nomMarque) {
        this.idMarque = idMarque;
        this.nomMarque = nomMarque;
    }

    public Marque() {

    }

    // Getters and Setters
    public int getIdMarque() {
        return idMarque;
    }

    public void setIdMarque(int idMarque) {
        this.idMarque = idMarque;
    }

    public String getNomMarque() {
        return nomMarque;
    }

    public void setNomMarque(String nomMarque) {
        this.nomMarque = nomMarque;
    }

    // toString method
    @Override
    public String toString() {
        return "Marque{" +
                "idMarque='" + idMarque + '\'' +
                ", nomMarque='" + nomMarque + '\'' +
                '}';
    }
}

