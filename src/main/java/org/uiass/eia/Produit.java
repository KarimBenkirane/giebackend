package org.uiass.eia;

import javax.persistence.*;

@Entity
@Table(name = "Produit")
public class Produit {
    @Id
    @Column(name = "ID_Produit")
    private String id_produit;

    @Column(name = "Description_produit")
    private String description_produit;

    @Column(name = "Prix_unitaire")
    private double prix_unitaire;

    public Produit() {
    }

    public Produit(String id_produit, String description_produit, double prix_unitaire) {
        this.id_produit = id_produit;
        this.description_produit = description_produit;
        this.prix_unitaire = prix_unitaire;
    }

    public String getId_produit() {
        return id_produit;
    }

    public void setId_produit(String id_produit) {
        this.id_produit = id_produit;
    }

    public String getDescription_produit() {
        return description_produit;
    }

    public void setDescription_produit(String description_produit) {
        this.description_produit = description_produit;
    }

    public double getPrix_unitaire() {
        return prix_unitaire;
    }

    public void setPrix_unitaire(double prix_unitaire) {
        this.prix_unitaire = prix_unitaire;
    }
}
