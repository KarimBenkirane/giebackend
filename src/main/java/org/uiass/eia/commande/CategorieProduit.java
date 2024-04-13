package org.uiass.eia.commande;

import javax.persistence.*;

@Entity
@Table(name="CategorieProduit")
public class CategorieProduit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_cat_prod")
    private int idCatProd;

    @Column(name="nom_categorie")
    private String nomCategorie;

    public CategorieProduit() {
    }

    public CategorieProduit(String nomCategorie) {
        this.nomCategorie = nomCategorie;
    }

    // Getters and Setters
    public int getIdCatProd() {
        return idCatProd;
    }

    public void setIdCatProd(int idCatProd) {
        this.idCatProd = idCatProd;
    }

    public String getNomCategorie() {
        return nomCategorie;
    }

    public void setNomCategorie(String nomCategorie) {
        this.nomCategorie = nomCategorie;
    }

    // toString method
    @Override
    public String toString() {
        return "CategorieProduit{" +
                "idCatProd=" + idCatProd +
                ", nomCategorie='" + nomCategorie + '\'' +
                '}';
    }
}